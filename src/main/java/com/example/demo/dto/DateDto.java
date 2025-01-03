package com.example.demo.dto;

//@Data
//public class DateDto {
//    @JsonProperty("year")
//    private Integer year;
//    @JsonProperty("month")
//    private Integer month;
//    @JsonProperty("day")
//    private Integer day;
//}

public record DateDto (
        Integer year,
        Integer month,
        Integer day
) {}
