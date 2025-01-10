package pl.edu.pjatk.backend.service.mapper;

import org.springframework.stereotype.Component;
import pl.edu.pjatk.backend.dto.NameDto;
import pl.edu.pjatk.backend.model.Name;

@Component
public class NameMapper implements EntityMapper<Name, NameDto> {

    @Override
    public Name toEntity(NameDto dto) {
        return new Name(dto.full(), dto.local());
    }

    @Override
    public NameDto toDto(Name entity) {
        return new NameDto(entity.getFull(), entity.getLocal());
    }
}
