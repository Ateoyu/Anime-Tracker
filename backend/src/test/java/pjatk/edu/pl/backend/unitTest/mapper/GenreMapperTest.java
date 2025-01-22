package pjatk.edu.pl.backend.unitTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjatk.edu.pl.backend.service.mapper.GenreMapper;
import pjatk.edu.pl.data.model.Genre;
import pjatk.edu.pl.data.repository.GenreRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreMapperTest {

    @Mock
    private GenreRepository genreRepository;

    private GenreMapper genreMapper;

    @BeforeEach
    void setUp() {
        genreMapper = new GenreMapper(genreRepository);
    }

    @Test
    void toEntity_WhenGenreDoesNotExist_ShouldCreateNew() {
        // Arrange
        String genreName = "Action";
        Genre newGenre = new Genre(genreName);

        when(genreRepository.findByName(genreName)).thenReturn(Optional.empty());
        when(genreRepository.save(any(Genre.class))).thenReturn(newGenre);

        // Act
        Genre result = genreMapper.toEntity(genreName);

        // Assert
        assertEquals(genreName, result.getName());
    }

    @Test
    void toEntity_WhenGenreExists_ShouldReturnExisting() {
        // Arrange
        String genreName = "Action";
        Genre existingGenre = new Genre(genreName);

        when(genreRepository.findByName(genreName)).thenReturn(Optional.of(existingGenre));

        // Act
        Genre result = genreMapper.toEntity(genreName);

        // Assert
        assertEquals(existingGenre, result);
    }
}