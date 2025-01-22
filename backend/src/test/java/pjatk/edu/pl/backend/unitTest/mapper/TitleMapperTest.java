package pjatk.edu.pl.backend.unitTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pjatk.edu.pl.backend.service.mapper.TitleMapper;
import pjatk.edu.pl.data.dto.TitleDto;
import pjatk.edu.pl.data.model.Title;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TitleMapperTest {
    private TitleMapper titleMapper;

    @BeforeEach
    void setUp() {
        titleMapper = new TitleMapper();
    }

    @Test
    void toEntity_WithValidDto_Success() {
        TitleDto dto = new TitleDto("Attack on Titan", "Shingeki no Kyojin", "進撃の巨人");
        Title result = titleMapper.toEntity(dto);

        assertEquals("Attack on Titan", result.getEnglish());
        assertEquals("Shingeki no Kyojin", result.getRomaji());
        assertEquals("進撃の巨人", result.getLocal());
    }


    @Test
    void toDto_WithValidEntity_Success() {
        Title entity = new Title("Attack on Titan", "Shingeki no Kyojin", "進撃の巨人");
        TitleDto result = titleMapper.toDto(entity);

        assertEquals("Attack on Titan", result.english());
        assertEquals("Shingeki no Kyojin", result.romaji());
        assertEquals("進撃の巨人", result.local());
    }

}