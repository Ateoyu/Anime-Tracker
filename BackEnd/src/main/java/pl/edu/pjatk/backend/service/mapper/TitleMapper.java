package pl.edu.pjatk.backend.service.mapper;

import org.springframework.stereotype.Component;
import pl.edu.pjatk.backend.dto.TitleDto;
import pl.edu.pjatk.backend.model.Title;

@Component
public class TitleMapper implements EntityMapper<Title, TitleDto> {

    @Override
    public Title toEntity(TitleDto dto) {
        return new Title(dto.english(), dto.romaji(), dto.local());
    }

    @Override
    public TitleDto toDto(Title entity) {
        return new TitleDto(entity.getEnglish(), entity.getRomaji(), entity.getLocal());
    }
}
