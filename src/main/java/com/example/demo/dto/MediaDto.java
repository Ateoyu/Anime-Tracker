package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record MediaDto(
        Integer id,
        TitleDto title,
        Integer episodes,
        DateDto startDate,
        DateDto endDate,
        List<String> genres,
        Integer averageScore,
        @JsonProperty("coverImage")
        ImageDto image,
        CharactersConnection characters
) {
}


