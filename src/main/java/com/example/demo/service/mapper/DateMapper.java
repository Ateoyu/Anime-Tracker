package com.example.demo.service.mapper;

import com.example.demo.dto.DateDto;
import com.example.demo.model.Date;
import org.springframework.stereotype.Component;

@Component
public class DateMapper implements EntityMapper<Date, DateDto>  {

    @Override
    public Date toEntity(DateDto dto) {
        return new Date(dto.year(), dto.month(), dto.day());
    }

    @Override
    public DateDto toDto(Date entity) {
        return new DateDto(entity.getYear(), entity.getMonth(), entity.getDay());
    }
}
