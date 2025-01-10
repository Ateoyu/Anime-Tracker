package pl.edu.pjatk.frontend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Title {
    private Integer id;
    private String english;
    private String romaji;
    private String local;

    public Title(String english, String romaji, String local) {
        this.english = english;
        this.romaji = romaji;
        this.local = local;
    }
} 