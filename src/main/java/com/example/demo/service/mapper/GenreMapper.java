package com.example.demo.service.mapper;

import com.example.demo.model.Genre;
import com.example.demo.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GenreMapper implements EntityMapper<Genre, String> {
    private final GenreRepository genreRepository;

    @Override
    public Genre toEntity(String genreName) {
        return genreRepository.findByName(genreName).orElseGet(() -> genreRepository.save(new Genre(genreName)));
    }

    @Override
    public String toDto(Genre entity) {
        return List.of().toString();
    }
}
