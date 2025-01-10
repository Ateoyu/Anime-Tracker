package pl.edu.pjatk.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NameDto(
        String full,
        @JsonProperty("native")
        String local
) {
}
