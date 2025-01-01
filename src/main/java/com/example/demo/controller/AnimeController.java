package com.example.demo.controller;

import com.example.demo.client.AnimeClient;
import com.example.demo.dto.MediaDto;
import com.example.demo.model.Media;
import com.example.demo.service.MediaService;
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
    private final MediaService mediaService;

    @GetMapping("/getMedia/id/{mediaId}")
    public ResponseEntity<MediaDto> getMedia(@PathVariable Integer mediaId) {
        return new ResponseEntity<>(animeClient.viewMedia(mediaId), HttpStatus.OK);
    }

    @GetMapping("/getMedia/dateRange/{fromDate}/{toDate}")
    public ResponseEntity<List<MediaDto>> getMediaFromDate(@PathVariable Integer fromDate, @PathVariable Integer toDate) {
        return new ResponseEntity<>(animeClient.getAnimeByDateRange(fromDate, toDate), HttpStatus.OK);
    }

    @GetMapping("/getMedia/averageScoreGreater/{score}")
    public ResponseEntity<List<MediaDto>> getMediaAverageScoreGreater(@PathVariable Integer score) {
        return new ResponseEntity<>(animeClient.animeByAverageScoreGreaterThan(score), HttpStatus.OK);
    }


    @GetMapping("/getMedia/test/{score}")
    public ResponseEntity<List<MediaDto>> getMediaAverageScoreGreaterTest(@PathVariable Integer score) {
        return new ResponseEntity<>(animeClient.animyByAverageScoreAndCharactersTest(score), HttpStatus.OK);
    }

    @GetMapping("/getMedia/properServiceTest/id/{mediaId}")
    public ResponseEntity<Media> getMediaProperServiceTest(@PathVariable Integer mediaId) {
        return new ResponseEntity<>(mediaService.getMediaById(mediaId), HttpStatus.OK);
    }
}
