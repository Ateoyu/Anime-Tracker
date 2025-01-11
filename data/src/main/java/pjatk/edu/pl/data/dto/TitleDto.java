package pjatk.edu.pl.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TitleDto(
        String english,
        String romaji,
        @JsonProperty("native")
        String local
) {
}