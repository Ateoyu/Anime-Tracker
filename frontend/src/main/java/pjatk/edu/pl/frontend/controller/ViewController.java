package pjatk.edu.pl.frontend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pjatk.edu.pl.backend.service.GenreService;
import pjatk.edu.pl.backend.service.MediaService;
import pjatk.edu.pl.data.model.CustomMediaList;
import pjatk.edu.pl.data.model.Media;
import pjatk.edu.pl.frontend.service.ViewService;

@Slf4j
@Controller
@AllArgsConstructor
public class ViewController {
    private final MediaService mediaService;
    private final GenreService genreService;
    private final ViewService viewService;

    @GetMapping("/")
    public String home() {
        return "home";
    }


    //    Browse page displaying all anime in a paginated form
    @GetMapping("/browse")
    public String browse(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Media> mediaList = mediaService.getMediaByPage(page, 50);
        model.addAttribute("mediaList", mediaList.getContent());
        model.addAttribute("genres", genreService.getAllGenres());
        model.addAttribute("years", mediaService.getAllAnimeReleaseYear());
        model.addAttribute("episodes", mediaService.getAllAnimeEpisodes());
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNext", mediaList.hasNext());
        return "browse";
    }

    @GetMapping("/browse/next")
    public String browseNext(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Media> mediaList = mediaService.getMediaByPage(page, 50);
        model.addAttribute("mediaList", mediaList.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("hasNext", mediaList.hasNext());
        return "fragments/animeGrid :: animeGrid";
    }

    //    Page displaying specific anime
    @GetMapping("/anime/{id}")
    public String anime(@PathVariable int id, Model model) {
        model.addAttribute("media", mediaService.getMediaById(id));
        model.addAttribute("allMediaLists", viewService.getAllMediaLists());
        return "mediaView";
    }

    @PostMapping("/anime/{id}")
    public String addAnimeToAnimeList(@PathVariable int id, @RequestParam int mediaListId) {
        viewService.addMediaToList(mediaListId, id);
        return "redirect:/anime/" + id;
    }

    //    page displaying all the anime lists that have been created and optionally creating a new list
    @GetMapping("/animeLists")
    public String animeLists(Model model) {
        model.addAttribute("mediaList", new CustomMediaList());
        model.addAttribute("allMediaLists", viewService.getAllMediaLists());
        return "animeLists";
    }

    @PostMapping("/animeLists")
    public String animeListsSubmit(@ModelAttribute("mediaList") CustomMediaList mediaList) {
        viewService.createMediaList(mediaList);
        return "redirect:/animeLists";
    }

    //    Page displaying details of the specific anime list,
    //    showing the anime added to it, and allowing for deleting stuff from it.
    @GetMapping("/animeLists/{id}")
    public String viewSpecificAnimeList(@PathVariable int id, Model model) {
        model.addAttribute("animeList", viewService.getMediaList(id));
        return "animeListView";
    }

    @PostMapping("/animeLists/{id}")
    public String deleteSpecificAnimeList(@PathVariable int id, @RequestParam int mediaId) {
        viewService.deleteMediaFromMediaList(id, mediaId);
        return "redirect:/animeLists/" + id;
    }

}
