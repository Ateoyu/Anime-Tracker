package com.example.demo.dto;

import java.util.List;


public record MediaDto(
        Integer id,
        TitleDto title,
        Integer episodes,
        DateDto startDate,
        DateDto endDate,
        List<String> genres,
        Integer averageScore,
        CharactersConnection characters
) {
}


