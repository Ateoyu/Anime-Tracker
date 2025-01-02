package com.example.demo.service;

import com.example.demo.client.AnimeClient;
import com.example.demo.dto.CharacterDto;
import com.example.demo.dto.DateDto;
import com.example.demo.dto.MediaDto;
import com.example.demo.dto.NameDto;
import com.example.demo.model.Character;
import com.example.demo.model.*;
import com.example.demo.repository.AnimeRepository;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.GenreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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
    private final CharacterRepository characterRepository;

    @Transactional
    public Media getMediaById(Integer anilistId) {
        Optional<Media> media = animeRepository.findByAnilistId(anilistId);
        
        if (media.isPresent()) {
            log.info("Found media with AniList id {} in local database", anilistId);
            return media.get();
        }

        MediaDto mediaDto = animeClient.viewMedia(anilistId);
        return saveMediaFromDto(mediaDto);
    }

    //todo: figure out why i get a ConcurrentModificationException when trying to add more than just 1 anime into the DB.
    private Media saveMediaFromDto(MediaDto mediaDto) {
        Media media = Media.builder()
                .anilistId(mediaDto.id())
                .title(mapTitle(mediaDto))
                .episodes(mediaDto.episodes())
                .averageScore(mediaDto.averageScore())
                .startDate(mapDate(mediaDto.startDate()))
                .endDate(mapDate(mediaDto.endDate()))
                .genres(mapGenres(mediaDto))
                .characters(mapCharacters(mediaDto))
                .build();

        return animeRepository.save(media);
    }

    private Set<Character> mapCharacters(MediaDto mediaDto) {
        return mediaDto.characters().list().stream()
                .map(this::getOrCreateCharacter)
                .collect(Collectors.toSet());
    }

    private Character getOrCreateCharacter(CharacterDto characterDto) {
        return characterRepository.findByAnilistId(characterDto.id())
                .orElseGet(() -> {
                    Character character = Character.builder()
                            .anilistId(characterDto.id())
                            .name(mapName(characterDto.name()))
                            .gender(characterDto.gender())
                            .age(characterDto.age())
                            .dateOfBirth(mapDate(characterDto.dateOfBirth()))
                            .imageUrl(characterDto.image().large())
                            .mediaAppearances(new HashSet<>())
                            .build();

                    return characterRepository.save(character);
                });
    }

    private Name mapName(NameDto name) {
        return new Name(name.full(), name.local());
    }

    private Set<Genre> mapGenres(MediaDto mediaDto) {
        return mediaDto.genres().stream()
                .map(this::getOrCreateGenre)
                .collect(Collectors.toSet());
    }

    private Genre getOrCreateGenre(String genreName) {
        return genreRepository.findByName(genreName)
                .orElseGet(() -> genreRepository.save(Genre.builder().name(genreName).build()));
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
