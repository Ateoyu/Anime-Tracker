package pjatk.edu.pl.backend.unitTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjatk.edu.pl.backend.service.mapper.*;
import pjatk.edu.pl.data.dto.*;
import pjatk.edu.pl.data.model.Character;
import pjatk.edu.pl.data.model.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediaMapperTest {
    @Mock private TitleMapper titleMapper;
    @Mock private DateMapper dateMapper;
    @Mock private CharacterMapper characterMapper;
    @Mock private GenreMapper genreMapper;

    private MediaMapper mediaMapper;

    @BeforeEach
    void setUp() {
        mediaMapper = new MediaMapper(titleMapper, dateMapper, characterMapper, genreMapper);
    }

    private static MediaDto getMediaDto(TitleDto titleDto, DateDto startDateDto, DateDto endDateDto) {
        ImageDto coverImageDto = new ImageDto("large-url");
        List<String> genres = List.of("Action", "Adventure");

        CharacterDto characterDto = new CharacterDto(1,
                new NameDto("Full Name", "Local Name"),
                "Male",
                "20",
                new DateDto(2000, 1, 1),
                new ImageDto("char-large-url")
        );

        CharactersConnection charactersConnection = new CharactersConnection(List.of(characterDto));

        return new MediaDto(
                1,
                titleDto,
                12,
                startDateDto,
                endDateDto,
                genres,
                85,
                coverImageDto,
                "banner-url",
                "Description",
                charactersConnection
        );
    }

    @Test
    void toEntity_WithValidDto_Success() {
        // Arrange
        TitleDto titleDto = new TitleDto("English", "Romaji", "Local");
        DateDto startDateDto = new DateDto(2024, 1, 1);
        DateDto endDateDto = new DateDto(2024, 12, 31);
        MediaDto dto = getMediaDto(titleDto, startDateDto, endDateDto);

        // Mock responses
        Title mockTitle = new Title("English", "Romaji", "Local");
        Date mockStartDate = new Date(2024, 1, 1);
        Date mockEndDate = new Date(2024, 12, 31);
        Genre mockGenre = new Genre("Action");
        Character mockCharacter = Character.builder().anilistId(1).build();

        when(titleMapper.toEntity(any())).thenReturn(mockTitle);
        when(dateMapper.toEntity(startDateDto)).thenReturn(mockStartDate);
        when(dateMapper.toEntity(endDateDto)).thenReturn(mockEndDate);
        when(genreMapper.toEntity(any())).thenReturn(mockGenre);
        when(characterMapper.toEntity(any())).thenReturn(mockCharacter);

        // Act
        Media result = mediaMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getAnilistId());
        assertEquals(mockTitle, result.getTitle());
        assertEquals(12, result.getEpisodes());
        assertEquals(85, result.getAverageScore());
        assertEquals("large-url", result.getCoverImage());
        assertEquals("banner-url", result.getBannerImage());
        assertEquals("Description", result.getDescription());
        assertEquals(mockStartDate, result.getStartDate());
        assertEquals(mockEndDate, result.getEndDate());
        assertNotNull(result.getGenres());
        assertNotNull(result.getCharacters());
    }



    @Test
    void toEntity_WithNullDto_ReturnsNull() {
        assertNull(mediaMapper.toEntity(null));
    }

    @Test
    void toEntity_WithNullCollections_Success() {
        // Arrange
        MediaDto dto = new MediaDto(
                1,
                new TitleDto("English", "Romaji", "Local"),
                12,
                new DateDto(2024, 1, 1),
                new DateDto(2024, 12, 31),
                null, // null genres
                85,
                new ImageDto("large-url"),
                "banner-url",
                "Description",
                null // null characters
        );

        when(titleMapper.toEntity(any())).thenReturn(new Title());
        when(dateMapper.toEntity(any())).thenReturn(new Date());

        // Act
        Media result = mediaMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertTrue(result.getGenres() == null || result.getGenres().isEmpty());
        assertTrue(result.getCharacters() == null || result.getCharacters().isEmpty());
    }
}