package com.example.demo.controller;

import com.example.demo.client.AnimeClient;
import com.example.demo.dto.Media;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class AnimeController {

    private final AnimeClient animeClient;

    @GetMapping("/getMedia/{mediaId}")
    public ResponseEntity<Media> getMedia(@PathVariable Integer mediaId) {
        return new ResponseEntity<>(animeClient.viewMedia(mediaId), HttpStatus.OK);
    }
}
