package com.example.demo.service;

import com.example.demo.client.AnimeClient;
import com.example.demo.dto.DateDto;
import com.example.demo.dto.MediaDto;
import com.example.demo.model.Date;
import com.example.demo.model.Genre;
import com.example.demo.model.Media;
import com.example.demo.model.Title;
import com.example.demo.repository.AnimeRepository;
import com.example.demo.repository.GenreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class MediaService {
    private final AnimeClient animeClient;
    private final AnimeRepository animeRepository;
    private final GenreRepository genreRepository;

    public Media getMediaById(int id) {
        Optional<Media> media = animeRepository.findById(id);
        if (media.isPresent()) {
            log.info("Found media with id {} in local database", id);
            return media.get();
        }

        MediaDto mediaDto = animeClient.viewMedia(id);
        return saveMediaFromDto(mediaDto);
    }

    //todo: handle mapping and saving the characters to the DB.
    private Media saveMediaFromDto(MediaDto mediaDto) {
        Media media = Media.builder()
                .id(mediaDto.id())
                .title(mapTitle(mediaDto))
                .episodes(mediaDto.episodes())
                .averageScore(mediaDto.averageScore())
                .startDate(mapDate(mediaDto.startDate()))
                .endDate(mapDate(mediaDto.endDate()))
                .genres(mapGenres(mediaDto))
                .build();

        return animeRepository.save(media);
    }

    private Set<Genre> mapGenres(MediaDto mediaDto) {
        return mediaDto.genres().stream()
                .map(this::getOrCreateGenre)
                .collect(Collectors.toSet());
    }

    private Genre getOrCreateGenre(String genreName) {
        return genreRepository.findByName(genreName)
                .orElseGet(() -> genreRepository.save(Genre.builder().name(genreName).build())) ;
    }


    private Date mapDate(DateDto dateDto) {
        Date date = new Date();
        date.setYear(dateDto.year());
        date.setMonth(dateDto.month());
        date.setDay(dateDto.day());
        return date;
    }

    private Title mapTitle(MediaDto mediaDto) {
        Title title = new Title();
        title.setEnglish(mediaDto.title().english());
        title.setRomaji(mediaDto.title().romaji());
        title.setLocal(mediaDto.title().local());
        return title;
    }
}
