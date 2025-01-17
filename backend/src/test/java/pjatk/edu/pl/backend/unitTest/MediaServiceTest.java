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
import pjatk.edu.pl.backend.service.mapper.EntityMapper;
import pjatk.edu.pl.data.dto.*;
import pjatk.edu.pl.data.exception.MediaClientException;
import pjatk.edu.pl.data.exception.MediaNotFoundException;
import pjatk.edu.pl.data.exception.MediaObjectIncomplete;
import pjatk.edu.pl.data.model.Date;
import pjatk.edu.pl.data.model.Genre;
import pjatk.edu.pl.data.model.Media;
import pjatk.edu.pl.data.model.Title;
import pjatk.edu.pl.data.repository.MediaRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private EntityMapper<Media, MediaDto> mediaMapper;

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
    void getMediaByDateRange_WhenFromDateIsNull_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> mediaService.getMediaByDateRange(null, 20200201));

        verify(mediaClient, never()).getAnimeByDateRange(any(), any());
        verify(mediaRepository, never()).findByDateRange(anyInt(), anyInt());
        verify(mediaRepository, never()).save(any(Media.class));
    }

    @Test
    void getMediaByDateRange_WhenToDateIsNull_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> mediaService.getMediaByDateRange(20200101, null));

        verify(mediaClient, never()).getAnimeByDateRange(any(), any());
        verify(mediaRepository, never()).findByDateRange(anyInt(), anyInt());
        verify(mediaRepository, never()).save(any(Media.class));
    }

    @Test
    void getMediaByDateRange_WhenFromDateIsGreaterThanToDate_ThrowsException() {
        assertThrows(InputMismatchException.class, () -> mediaService.getMediaByDateRange(20200201, 20200101));

        verify(mediaClient, never()).getAnimeByDateRange(any(), any());
        verify(mediaRepository, never()).findByDateRange(anyInt(), anyInt());
        verify(mediaRepository, never()).save(any(Media.class));
    }
    @Test
    void getMediaByDateRange_WhenAnilistApiFails_ThrowsException() {
        when(mediaClient.getAnimeByDateRange(20200101, 20200201)).thenThrow(new MediaClientException("API Error"));

        MediaClientException exception = assertThrows(MediaClientException.class,() -> mediaService.getMediaByDateRange(20200101, 20200201));

        assertTrue(exception.getMessage().contains("API Error"));

        verify(mediaClient).getAnimeByDateRange(20200101, 20200201);
        verify(mediaRepository, never()).save(any(Media.class));
        verify(mediaRepository, never()).findByDateRange(anyInt(), anyInt());
    }

    @Test
    void getMediaByDateRange_WhenMediaDoesNotExistLocally_SavesButLocalRepositoryReturnsEmptyList() {
        List<MediaDto> apiMediaDtos = Collections.singletonList(testMediaDto);

        when(mediaClient.getAnimeByDateRange(20200101, 20200201)).thenReturn(apiMediaDtos);
        when(mediaRepository.findByAnilistId(testMediaDto.id())).thenReturn(Optional.empty());
        when(mediaMapper.toEntity(testMediaDto)).thenReturn(testMedia);
        when(mediaRepository.save(testMedia)).thenReturn(testMedia);
        when(mediaRepository.findByDateRange(20200101, 20200201)).thenReturn(Collections.emptyList());

        assertThrows(MediaNotFoundException.class, () -> mediaService.getMediaByDateRange(20200101, 20200201));

        verify(mediaClient).getAnimeByDateRange(20200101, 20200201);
        verify(mediaRepository).findByAnilistId(testMediaDto.id());
        verify(mediaMapper).toEntity(testMediaDto);
        verify(mediaRepository).save(testMedia);
        verify(mediaRepository).findByDateRange(20200101, 20200201);
    }

    @Test
    void getMediaByDateRange_WhenMediaDoesNotExistLocally_SavesAndReturnsMediaList() {
        List<MediaDto> apiMediaDtos = Collections.singletonList(testMediaDto);
        List<Media> expectedMedia = Collections.singletonList(testMedia);

        when(mediaClient.getAnimeByDateRange(20200101, 20200201)).thenReturn(apiMediaDtos);
        when(mediaRepository.findByAnilistId(testMediaDto.id())).thenReturn(Optional.empty());
        when(mediaMapper.toEntity(testMediaDto)).thenReturn(testMedia);
        when(mediaRepository.save(testMedia)).thenReturn(testMedia);
        when(mediaRepository.findByDateRange(20200101, 20200201)).thenReturn(expectedMedia);

        List<Media> result = mediaService.getMediaByDateRange(20200101, 20200201);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testMedia, result.getFirst());

        verify(mediaClient).getAnimeByDateRange(20200101, 20200201);
        verify(mediaRepository).findByAnilistId(testMediaDto.id());
        verify(mediaMapper).toEntity(testMediaDto);
        verify(mediaRepository).save(testMedia);
        verify(mediaRepository).findByDateRange(20200101, 20200201);
    }

    @Test
    void getMediaByDateRange_WhenMediaAlreadyExistsLocally_ReturnsMediaList() {
        List<MediaDto> dtos = Collections.singletonList(testMediaDto);
        List<Media> media = Collections.singletonList(testMedia);

        when(mediaClient.getAnimeByDateRange(20200101, 20200201)).thenReturn(dtos);
        when(mediaRepository.findByAnilistId(dtos.getFirst().id())).thenReturn(Optional.of(media.getFirst()));
        when(mediaRepository.findByDateRange(20200101, 20200201)).thenReturn(media);

        List<Media> result = mediaService.getMediaByDateRange(20200101, 20200201);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(2021, result.getFirst().getStartDate().getYear());
    }

    @Test
    void getMediaByPage_whenPageIsNull_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> mediaService.getMediaByPage(null, 1));
        verify(mediaRepository, never()).findAll(any(PageRequest.class));
    }

    @Test
    void getMediaByPage_whenSizeIsNull_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> mediaService.getMediaByPage(1, null));
        verify(mediaRepository, never()).findAll(any(PageRequest.class));
    }

    @Test
    void getMediaByPage_whenPageIsEmpty_ThrowsException() {
        when(mediaRepository.findAll(PageRequest.of(0, 50))).thenReturn(Page.empty());
        assertThrows(MediaNotFoundException.class, () -> mediaService.getMediaByPage(0, 50));
        verify(mediaRepository).findAll(PageRequest.of(0, 50));
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

    @Test
    void getAllAnimeReleaseYear_WhenReleaseYearListIsEmpty_ThrowsException() {
        when(mediaRepository.getAllMediaYears()).thenReturn(Collections.emptyList());
        assertThrows(MediaNotFoundException.class, () -> mediaService.getAllAnimeReleaseYear());

        verify(mediaRepository).getAllMediaYears();
    }

    @Test
    void getAllAnimeReleaseYear_Success() {
        when(mediaRepository.getAllMediaYears()).thenReturn(Stream.of(2018,2019,2020).collect(Collectors.toList()));

        List<Integer> allAnimeReleaseYear = mediaService.getAllAnimeReleaseYear();

        assertEquals(3, allAnimeReleaseYear.size());
        assertEquals(2018, allAnimeReleaseYear.get(0));
        assertEquals(2019, allAnimeReleaseYear.get(1));
        assertEquals(2020, allAnimeReleaseYear.get(2));

        verify(mediaRepository).getAllMediaYears();
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
        assertTrue(result.getContent().getFirst().getGenres().stream()
                .anyMatch(genre -> genre.getName().equals("Action")));
    }
}