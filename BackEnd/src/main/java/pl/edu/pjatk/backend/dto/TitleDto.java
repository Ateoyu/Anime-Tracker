package pl.edu.pjatk.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TitleDto(
        String english,
        String romaji,
        @JsonProperty("native")
        String local
) {
}