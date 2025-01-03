package com.example.demo.service.mapper;

import com.example.demo.dto.TitleDto;
import com.example.demo.model.Title;
import org.springframework.stereotype.Component;

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
