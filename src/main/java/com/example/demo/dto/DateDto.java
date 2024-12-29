package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DateDto {
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("month")
    private Integer month;
    @JsonProperty("day")
    private Integer day;
}
