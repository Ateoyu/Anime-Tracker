package pjatk.edu.pl.frontend.controller;

import pjatk.edu.pl.data.model.Media;
import pjatk.edu.pl.backend.service.GenreService;
import pjatk.edu.pl.backend.service.MediaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@AllArgsConstructor
public class ViewController {
    private final MediaService mediaService;
    private final GenreService genreService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

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

    @GetMapping("/anime/{id}")
    public String anime(@PathVariable int id, Model model) {
        model.addAttribute("media", mediaService.getMediaById(id));
        return "mediaView";
    }

}
