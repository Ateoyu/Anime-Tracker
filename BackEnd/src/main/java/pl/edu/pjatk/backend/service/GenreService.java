package pl.edu.pjatk.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.backend.model.Genre;
import pl.edu.pjatk.backend.repository.GenreRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
}
