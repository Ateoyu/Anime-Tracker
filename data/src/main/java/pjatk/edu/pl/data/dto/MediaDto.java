package pjatk.edu.pl.data.dto;

import java.util.List;


public record MediaDto(
        Integer id,
        TitleDto title,
        Integer episodes,
        DateDto startDate,
        DateDto endDate,
        List<String> genres,
        Integer averageScore,
        ImageDto coverImage,
        String bannerImage,
        String description,
        CharactersConnection characters
) {
}


