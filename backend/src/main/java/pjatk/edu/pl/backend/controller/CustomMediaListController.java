package pjatk.edu.pl.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pjatk.edu.pl.backend.service.CustomMediaListService;
import pjatk.edu.pl.data.model.CustomMediaList;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomMediaListController {
    private final CustomMediaListService mediaListService;

    @PostMapping("/customMediaList/create")
    public ResponseEntity<Void> createMediaList(@RequestBody CustomMediaList mediaList) {
        mediaListService.createMediaList(mediaList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customMediaList/getMediaList/{id}")
    public ResponseEntity<CustomMediaList> getMediaList(@PathVariable int id) {
        return ResponseEntity.ok(mediaListService.getMediaList(id));
    }

    @GetMapping("/customMediaList/getAll")
    public ResponseEntity<List<CustomMediaList>> getAllMediaList() {
        return ResponseEntity.ok(mediaListService.getAllMediaLists());
    }
}
