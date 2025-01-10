package pl.edu.pjatk.frontend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class Genre {
    private Integer id;
    private String name;
    private Set<Media> media;

    public Genre(String genreName) {
        this.name = genreName;
    }
} 