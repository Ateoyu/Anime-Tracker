package pl.edu.pjatk.backend.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.pjatk.backend.model.Genre;
import pl.edu.pjatk.backend.repository.GenreRepository;

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
