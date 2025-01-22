package pjatk.edu.pl.frontend.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.ui.Model;
import pjatk.edu.pl.backend.service.GenreService;
import pjatk.edu.pl.backend.service.MediaService;
import pjatk.edu.pl.data.model.CustomMediaList;
import pjatk.edu.pl.data.model.Genre;
import pjatk.edu.pl.data.model.Media;
import pjatk.edu.pl.data.model.Title;
import pjatk.edu.pl.frontend.controller.ViewController;
import pjatk.edu.pl.frontend.service.ViewService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ViewControllerTest {

    @Mock
    private MediaService mediaService;

    @Mock
    private GenreService genreService;

    @Mock
    private ViewService viewService;

    @Mock
    private Model model;

    @InjectMocks
    private ViewController controller;

    private CustomMediaList testList;
    private Media testMedia;

    @BeforeEach
    void setUp() {
        testList = new CustomMediaList();
        testList.setId(1);
        testList.setTitle("Test List");
        testList.setDescription("Test Description");
        testList.setMediaList(new HashSet<>());

        testMedia = Media.builder()
                .id(1)
                .title(new Title("Test Anime", "テストアニメ", "Test Anime"))
                .build();
    }

    @Test
    void home_ReturnsHomePage() {
        assertEquals("home", controller.home());
    }

    @Test
    void browse_Success() {
        Page<Media> mediaPage = new PageImpl<>(Collections.singletonList(testMedia));
        List<Genre> genres = Arrays.asList(new Genre("Action"), new Genre("Comedy"));
        List<Integer> years = Arrays.asList(2020, 2021);
        List<Integer> episodes = Arrays.asList(12, 24);

        when(mediaService.getMediaByPage(0, 50)).thenReturn(mediaPage);
        when(genreService.getAllGenres()).thenReturn(genres);
        when(mediaService.getAllAnimeReleaseYear()).thenReturn(years);
        when(mediaService.getAllAnimeEpisodes()).thenReturn(episodes);

        String result = controller.browse(0, model);

        assertEquals("browse", result);
        verify(model).addAttribute("mediaList", mediaPage.getContent());
        verify(model).addAttribute("genres", genres);
        verify(model).addAttribute("years", years);
        verify(model).addAttribute("episodes", episodes);
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("hasNext", false);
    }

    @Test
    void anime_Success() {
        when(mediaService.getMediaById(1)).thenReturn(testMedia);
        when(viewService.getAllMediaLists()).thenReturn(Collections.singletonList(testList));

        String result = controller.anime(1, model);

        assertEquals("mediaView", result);
        verify(model).addAttribute("media", testMedia);
        verify(model).addAttribute("allMediaLists", Collections.singletonList(testList));
    }

    @Test
    void addAnimeToAnimeList_Success() {
        String result = controller.addAnimeToAnimeList(1, 1);

        assertEquals("redirect:/anime/1", result);
        verify(viewService).addMediaToList(1, 1);
    }

    @Test
    void animeLists_Success() {
        when(viewService.getAllMediaLists()).thenReturn(Collections.singletonList(testList));

        String result = controller.animeLists(model);

        assertEquals("animeLists", result);
        verify(model).addAttribute(eq("mediaList"), any(CustomMediaList.class));
        verify(model).addAttribute("allMediaLists", Collections.singletonList(testList));
    }

    @Test
    void animeListsSubmit_Success() {
        String result = controller.animeListsSubmit(testList);

        assertEquals("redirect:/animeLists", result);
        verify(viewService).createMediaList(testList);
    }

    @Test
    void deleteAnimeList_Success() {
        String result = controller.deleteAnimeList(1);

        assertEquals("redirect:/animeLists", result);
        verify(viewService).deleteMediaList(1);
    }

    @Test
    void viewSpecificAnimeList_Success() {
        when(viewService.getMediaList(1)).thenReturn(testList);

        String result = controller.viewSpecificAnimeList(1, model);

        assertEquals("animeListView", result);
        verify(model).addAttribute("animeList", testList);
    }

    @Test
    void deleteSpecificAnimeList_Success() {
        String result = controller.deleteSpecificAnimeList(1, 2);

        assertEquals("redirect:/animeLists/1", result);
        verify(viewService).deleteMediaFromMediaList(1, 2);
    }
}