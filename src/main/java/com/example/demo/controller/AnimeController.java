package com.example.demo.controller;

import com.example.demo.client.AnimeClient;
import com.example.demo.dto.Media;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@AllArgsConstructor
public class AnimeController {

    private final AnimeClient animeClient;

    @GetMapping("/getMedia/{mediaId}")
    public ResponseEntity<Media> getMedia(@PathVariable Integer mediaId) {
        return new ResponseEntity<>(animeClient.viewMedia(mediaId), HttpStatus.OK);
    }

    @GetMapping("/getMedia/{fromDate}/{toDate}")
    public ResponseEntity<List<Media>> getMediaFromDate(@PathVariable Integer fromDate, @PathVariable Integer toDate) {
        return new ResponseEntity<>(animeClient.getAnimeByDateRange(fromDate, toDate), HttpStatus.OK);
    }
}
