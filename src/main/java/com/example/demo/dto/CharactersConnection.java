package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CharactersConnection(
        @JsonProperty("nodes")
        List<CharacterDto> list
) {
}
