package pjatk.edu.pl.backend.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pjatk.edu.pl.backend.client.MediaClient;
import pjatk.edu.pl.backend.service.MediaService;
import pjatk.edu.pl.backend.service.mapper.MediaMapper;
import pjatk.edu.pl.data.dto.*;
import pjatk.edu.pl.data.exception.MediaClientException;
import pjatk.edu.pl.data.exception.MediaObjectIncomplete;
import pjatk.edu.pl.data.model.Date;
import pjatk.edu.pl.data.model.Genre;
import pjatk.edu.pl.data.model.Media;
import pjatk.edu.pl.data.model.Title;
import pjatk.edu.pl.data.repository.MediaRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MediaServiceTest {

    @Mock
    private MediaClient mediaClient;

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private MediaMapper mediaMapper;

    @InjectMocks
    private MediaService mediaService;

    private Media testMedia;
    private MediaDto testMediaDto;

    @BeforeEach
    void setUp() {
        // Setup test Media entity
        testMedia = Media.builder()
                .id(1)
                .anilistId(1)
                .title(new Title("Test Anime", "テストアニメ", "Test Anime"))
                .episodes(12)
                .averageScore(80)
                .coverImage("cover.jpg")
                .bannerImage("banner.jpg")
                .description("Test description")
                .startDate(new Date(2021, 1, 1))
                .endDate(new Date(2021, 3, 31))
                .genres(new HashSet<>(Collections.singletonList(new Genre("Action"))))
                .characters(new HashSet<>())
                .build();

        // Setup test MediaDto
        testMediaDto = new MediaDto(
                1,
                new TitleDto("Test Anime", "テストアニメ", "Test Anime"),
                12,
                new DateDto(2021, 1, 1),
                new DateDto(2021, 3, 31),
                List.of("Action"),
                80,
                new ImageDto("cover.jpg"),
                "banner.jpg",
                "Test description",
                null
        );
    }

    @Test
    void getMediaById_WhenMediaExists_ReturnsMedia() {
        when(mediaRepository.findByAnilistId(1)).thenReturn(Optional.of(testMedia));

        Media result = mediaService.getMediaById(1);

        assertNotNull(result);
        assertEquals(1, result.getAnilistId());
        assertEquals("Test Anime", result.getTitle().getEnglish());
        verify(mediaClient, never()).viewMedia(any());
    }

    @Test
    void getMediaById_WhenMediaDoesNotExistLocally_FetchesAndSavesMedia() {
        when(mediaRepository.findByAnilistId(1)).thenReturn(Optional.empty());
        when(mediaClient.viewMedia(1)).thenReturn(testMediaDto);
        when(mediaMapper.toEntity(testMediaDto)).thenReturn(testMedia);
        when(mediaRepository.save(testMedia)).thenReturn(testMedia);

        Media result = mediaService.getMediaById(1);

        assertNotNull(result);
        assertEquals(1, result.getAnilistId());
        assertEquals(12, result.getEpisodes());
        assertEquals(80, result.getAverageScore());
        verify(mediaClient).viewMedia(1);
        verify(mediaRepository).save(any(Media.class));
    }

    @Test
    void getMediaById_WhenAniListIdIsNull_ThrowsException() {
        assertThrows(MediaObjectIncomplete.class, () -> mediaService.getMediaById(null));
    }

    @Test
    void getMediaById_WhenMediaDoesNotExistLocally_AndDoesNotExistFromAnilist_ThrowsException() {
        when(mediaRepository.findByAnilistId(1)).thenReturn(Optional.empty());
        when(mediaClient.viewMedia(1)).thenReturn(null);

        assertThrows(MediaClientException.class, () -> mediaService.getMediaById(1));

        verify(mediaRepository).findByAnilistId(1);
        verify(mediaClient).viewMedia(1);
    }

    @Test
    void getMediaById_WhenMediaDoesNotExistLocally_FetchesButDoesNotMapToEntity_ThrowsException() {
        when(mediaRepository.findByAnilistId(1)).thenReturn(Optional.empty());
        when(mediaClient.viewMedia(1)).thenReturn(testMediaDto);
        when(mediaMapper.toEntity(testMediaDto)).thenReturn(null);

        assertThrows(MediaObjectIncomplete.class, () -> mediaService.getMediaById(1));

        verify(mediaRepository).findByAnilistId(1);
        verify(mediaClient).viewMedia(1);
        verify(mediaMapper).toEntity(testMediaDto);
    }

    @Test
    void getMediaByDateRange_Success() {
        List<MediaDto> dtos = Collections.singletonList(testMediaDto);
        List<Media> media = Collections.singletonList(testMedia);

        when(mediaClient.getAnimeByDateRange(2021, 2021)).thenReturn(dtos);
        when(mediaRepository.findByDateRange(2021, 2021)).thenReturn(media);
        when(mediaMapper.toEntity(testMediaDto)).thenReturn(testMedia);
        when(mediaRepository.findByAnilistId(1)).thenReturn(Optional.empty());

        List<Media> result = mediaService.getMediaByDateRange(2021, 2021);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(2021, result.get(0).getStartDate().getYear());
    }

    @Test
    void getFilteredMedia_WithGenreAndYearFilter() {
        MediaFilterDto filters = new MediaFilterDto();
        filters.setGenreFilter("Action");
        filters.setYearFilter("2021");
        filters.setEpisodeFilter("12");
        filters.setPage(0);

        testMedia.setGenres(Set.of(new Genre("Action")));

        Page<Media> page = new PageImpl<>(Collections.singletonList(testMedia));
        when(mediaRepository.findAll(any(), any(PageRequest.class))).thenReturn(page);

        Page<Media> result = mediaService.getFilteredMedia(filters);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getGenres().stream()
                .anyMatch(genre -> genre.getName().equals("Action")));
    }

    @Test
    void getMediaByPage_Success() {
        Page<Media> page = new PageImpl<>(Collections.singletonList(testMedia));
        when(mediaRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Media> result = mediaService.getMediaByPage(0, 20);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getMediaByPage_InvalidParameters_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> mediaService.getMediaByPage(-1, 20));
        assertThrows(IllegalArgumentException.class, () -> mediaService.getMediaByPage(0, 51));
        assertThrows(IllegalArgumentException.class, () -> mediaService.getMediaByPage(0, 0));
    }
}