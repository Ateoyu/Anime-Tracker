package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MediaDto {
    private Integer id;
    private TitleDto title;
    private Integer episodes;
    @JsonProperty("startDate")
    private DateDto startDate;
    @JsonProperty("endDate")
    private DateDto endDate;
    private List<String> genres;
    private Integer averageScore;
    private CharactersConnection characters;
}
