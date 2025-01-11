package pjatk.edu.pl.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PageDto(
        PageInfoDto pageInfo,
        @JsonProperty("media")
        List<MediaDto> mediaList
) {
}
