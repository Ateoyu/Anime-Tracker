package com.example.demo.service.mapper;

import com.example.demo.dto.NameDto;
import com.example.demo.model.Name;
import org.springframework.stereotype.Component;

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
