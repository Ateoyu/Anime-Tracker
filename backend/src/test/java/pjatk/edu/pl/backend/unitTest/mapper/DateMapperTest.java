package pjatk.edu.pl.backend.unitTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pjatk.edu.pl.backend.service.mapper.DateMapper;
import pjatk.edu.pl.data.dto.DateDto;
import pjatk.edu.pl.data.model.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateMapperTest {
    private DateMapper dateMapper;

    @BeforeEach
    void setUp() {
        dateMapper = new DateMapper();
    }

    @Test
    void toEntity_WithValidDto_Success() {
        DateDto dto = new DateDto(2024, 3, 15);
        Date result = dateMapper.toEntity(dto);

        assertEquals(2024, result.getYear());
        assertEquals(3, result.getMonth());
        assertEquals(15, result.getDay());
    }


    @Test
    void toDto_WithValidEntity_Success() {
        Date entity = new Date(2024, 3, 15);
        DateDto result = dateMapper.toDto(entity);

        assertEquals(2024, result.year());
        assertEquals(3, result.month());
        assertEquals(15, result.day());
    }
}