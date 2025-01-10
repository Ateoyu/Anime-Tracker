package pl.edu.pjatk.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record CharacterDto(
        int id,
        NameDto name,
        String gender,
        String age,
        DateDto dateOfBirth,
        ImageDto image
) {}