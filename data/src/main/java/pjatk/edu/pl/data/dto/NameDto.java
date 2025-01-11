package pjatk.edu.pl.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NameDto(
        String full,
        @JsonProperty("native")
        String local
) {
}
