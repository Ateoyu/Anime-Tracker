package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record CharacterDto(
        int id,
        NameDto name,
        String gender,
        String age,
        DateDto dateOfBirth,
        ImageDto image
) {}
