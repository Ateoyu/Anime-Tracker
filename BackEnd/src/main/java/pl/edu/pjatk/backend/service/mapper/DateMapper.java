package pl.edu.pjatk.backend.service.mapper;

import org.springframework.stereotype.Component;
import pl.edu.pjatk.backend.dto.DateDto;
import pl.edu.pjatk.backend.model.Date;

@Component
public class DateMapper implements EntityMapper<Date, DateDto> {

    @Override
    public Date toEntity(DateDto dto) {
        return new Date(dto.year(), dto.month(), dto.day());
    }

    @Override
    public DateDto toDto(Date entity) {
        return new DateDto(entity.getYear(), entity.getMonth(), entity.getDay());
    }
}
