package pjatk.edu.pl.backend.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import pjatk.edu.pl.backend.client.MediaClient;
import pjatk.edu.pl.backend.controller.MediaController;
import pjatk.edu.pl.backend.service.MediaService;
import pjatk.edu.pl.data.dto.MediaDto;
import pjatk.edu.pl.data.dto.TitleDto;
import pjatk.edu.pl.data.model.Media;
import pjatk.edu.pl.data.model.Title;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediaControllerTest {

    @Mock
    private MediaClient mediaClient;

    @Mock
    private MediaService mediaService;

    @InjectMocks
    private MediaController mediaController;

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
                .genres(new HashSet<>())
                .characters(new HashSet<>())
                .build();

        // Setup test MediaDto
        testMediaDto = new MediaDto(
                1,
                new TitleDto("Test Anime", "テストアニメ", "Test Anime"),
                12,
                null, // DateDto
                null, // DateDto
                Collections.emptyList(),
                80,
                null, // ImageDto
                "banner.jpg",
                "Test description",
                null  // CharactersConnection
        );
    }

    @Test
    void getMediaById_Success() {
        when(mediaService.getMediaById(1)).thenReturn(testMedia);

        var response = mediaController.getMediaById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMedia, response.getBody());
        verify(mediaService).getMediaById(1);
    }

    @Test
    void getMediaFromDate_Success() {
        List<Media> mediaList = Collections.singletonList(testMedia);
        when(mediaService.getMediaByDateRange(20200101, 20200201))
                .thenReturn(mediaList);

        var response = mediaController.getMediaFromDate(20200101, 20200201);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mediaList, response.getBody());
        verify(mediaService).getMediaByDateRange(20200101, 20200201);
    }

    @Test
    void getMediaAverageScoreGreater_Success() {
        List<MediaDto> mediaDtoList = Collections.singletonList(testMediaDto);
        when(mediaClient.animeByAverageScoreGreaterThan(80))
                .thenReturn(mediaDtoList);

        var response = mediaController.getMediaAverageScoreGreater(80);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mediaDtoList, response.getBody());
        verify(mediaClient).animeByAverageScoreGreaterThan(80);
    }

    @Test
    void getMediaAverageScoreGreaterTest_Success() {
        List<MediaDto> mediaDtoList = Collections.singletonList(testMediaDto);
        when(mediaClient.animeByAverageScoreGreaterThan(80))
                .thenReturn(mediaDtoList);

        var response = mediaController.getMediaAverageScoreGreaterTest(80);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mediaDtoList, response.getBody());
        verify(mediaClient).animeByAverageScoreGreaterThan(80);
    }

    @Test
    void getMediaFromDate_WithEmptyResult() {
        when(mediaService.getMediaByDateRange(20200101, 20200201))
                .thenReturn(Collections.emptyList());

        var response = mediaController.getMediaFromDate(20200101, 20200201);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(mediaService).getMediaByDateRange(20200101, 20200201);
    }

    @Test
    void getMediaAverageScoreGreater_WithEmptyResult() {
        when(mediaClient.animeByAverageScoreGreaterThan(80))
                .thenReturn(Collections.emptyList());

        var response = mediaController.getMediaAverageScoreGreater(80);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(mediaClient).animeByAverageScoreGreaterThan(80);
    }

    @Test
    void getMediaById_WithNullResult() {
        when(mediaService.getMediaById(1)).thenReturn(null);

        var response = mediaController.getMediaById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(mediaService).getMediaById(1);
    }
}