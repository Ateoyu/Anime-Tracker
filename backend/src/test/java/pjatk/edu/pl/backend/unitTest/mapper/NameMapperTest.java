package pjatk.edu.pl.backend.unitTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pjatk.edu.pl.backend.service.mapper.NameMapper;
import pjatk.edu.pl.data.dto.NameDto;
import pjatk.edu.pl.data.model.Name;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NameMapperTest {
    private NameMapper nameMapper;

    @BeforeEach
    void setUp() {
        nameMapper = new NameMapper();
    }

    @Test
    void toEntity_WithValidDto_Success() {
        NameDto dto = new NameDto("John Doe", "ジョン・ドウ");
        Name result = nameMapper.toEntity(dto);

        assertEquals("John Doe", result.getFull());
        assertEquals("ジョン・ドウ", result.getLocal());
    }


    @Test
    void toDto_WithValidEntity_Success() {
        Name entity = new Name("John Doe", "ジョン・ドウ");
        NameDto result = nameMapper.toDto(entity);

        assertEquals("John Doe", result.full());
        assertEquals("ジョン・ドウ", result.local());
    }

}