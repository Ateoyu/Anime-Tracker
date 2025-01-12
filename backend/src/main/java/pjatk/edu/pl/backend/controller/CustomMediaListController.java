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

    @PostMapping("/customMediaList/addTo/{id}")
    public ResponseEntity<Void> addToMediaList(@PathVariable int id, @RequestParam int mediaId) {
        mediaListService.addMediaToList(id, mediaId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/customMediaList/deleteFrom/{id}")
    public ResponseEntity<Void> deleteFromMediaList(@PathVariable int id, @RequestParam Integer mediaId) {
        mediaListService.deleteMediaFromList(id, mediaId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/customMediaList/delete/{listId}")
    public ResponseEntity<Void> deleteList(@PathVariable int listId) {
        mediaListService.deleteMediaList(listId);
        return ResponseEntity.ok().build();
    }
}
