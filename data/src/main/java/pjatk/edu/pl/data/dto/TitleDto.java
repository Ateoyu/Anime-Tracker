package pjatk.edu.pl.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_DEFAULT)
//public class TitleDto {
//
//    private String english;
//    private String romaji;
//    @JsonProperty("native")
//    private String local;
//}

public record TitleDto(
        String english,
        String romaji,
        @JsonProperty("native")
        String local
) {
}