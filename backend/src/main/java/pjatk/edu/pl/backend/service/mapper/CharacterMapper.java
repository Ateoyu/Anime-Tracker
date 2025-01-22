package pjatk.edu.pl.backend.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pjatk.edu.pl.data.dto.CharacterDto;
import pjatk.edu.pl.data.model.Character;
import pjatk.edu.pl.data.repository.CharacterRepository;

@Component
@AllArgsConstructor
public class CharacterMapper implements EntityMapper<Character, CharacterDto> {
    private final CharacterRepository characterRepository;
    private final DateMapper dateMapper;
    private final NameMapper nameMapper;

    @Override
    public Character toEntity(CharacterDto dto) {
        return characterRepository.findByAnilistId(dto.id())
                .orElseGet(() -> characterRepository.save(Character.builder()
                        .anilistId(dto.id())
                        .name(nameMapper.toEntity(dto.name()))
                        .gender(dto.gender())
                        .age(dto.age())
                        .dateOfBirth(dateMapper.toEntity(dto.dateOfBirth()))
                        .imageUrl(dto.image().large())
                        .build()));
    }

    @Override
    public CharacterDto toDto(Character entity) {
        return null;
    }
}
