package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Page(
        PageInfo pageInfo,
        @JsonProperty("media")
        List<Media> mediaList
) {
}
