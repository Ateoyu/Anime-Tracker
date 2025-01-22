package pjatk.edu.pl.backend.unitTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjatk.edu.pl.backend.service.mapper.CharacterMapper;
import pjatk.edu.pl.backend.service.mapper.DateMapper;
import pjatk.edu.pl.backend.service.mapper.NameMapper;
import pjatk.edu.pl.data.dto.CharacterDto;
import pjatk.edu.pl.data.dto.DateDto;
import pjatk.edu.pl.data.dto.ImageDto;
import pjatk.edu.pl.data.dto.NameDto;
import pjatk.edu.pl.data.model.Character;
import pjatk.edu.pl.data.model.Date;
import pjatk.edu.pl.data.model.Name;
import pjatk.edu.pl.data.repository.CharacterRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterMapperTest {

    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private DateMapper dateMapper;
    @Mock
    private NameMapper nameMapper;

    private CharacterMapper characterMapper;

    @BeforeEach
    void setUp() {
        characterMapper = new CharacterMapper(characterRepository, dateMapper, nameMapper);
    }

    @Test
    void toEntity_WhenCharacterDoesNotExist_ShouldCreateNew() {
        // Arrange
        int anilistId = 1;
        NameDto nameDto = new NameDto("Full Name", "Local Name");
        DateDto dateDto = new DateDto(2000, 1, 1);
        ImageDto imageDto = new ImageDto("large-url");

        CharacterDto dto = new CharacterDto(
                anilistId,
                nameDto,
                "Male",
                "20",
                dateDto,
                imageDto
        );

        Name mappedName = new Name("Full Name", "Local Name");
        Date mappedDate = new Date(2000, 1, 1);

        when(characterRepository.findByAnilistId(anilistId)).thenReturn(Optional.empty());
        when(nameMapper.toEntity(nameDto)).thenReturn(mappedName);
        when(dateMapper.toEntity(dateDto)).thenReturn(mappedDate);
        when(characterRepository.save(any(Character.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Character result = characterMapper.toEntity(dto);

        // Assert
        assertEquals(anilistId, result.getAnilistId());
        assertEquals(mappedName, result.getName());
        assertEquals("Male", result.getGender());
        assertEquals("20", result.getAge());
        assertEquals(mappedDate, result.getDateOfBirth());
        assertEquals("large-url", result.getImageUrl());
    }

    @Test
    void toEntity_WhenCharacterExists_ShouldReturnExisting() {
        // Arrange
        int anilistId = 1;
        Character existingCharacter = Character.builder()
                .anilistId(anilistId)
                .name(new Name("Existing Name", "Existing Local"))
                .gender("Female")
                .build();

        CharacterDto dto = new CharacterDto(
                anilistId,
                new NameDto("New Name", "New Local"),
                "Male",
                "20",
                new DateDto(2000, 1, 1),
                new ImageDto("large-url")
        );

        when(characterRepository.findByAnilistId(anilistId)).thenReturn(Optional.of(existingCharacter));

        // Act
        Character result = characterMapper.toEntity(dto);

        // Assert
        assertEquals(existingCharacter, result);
    }
}