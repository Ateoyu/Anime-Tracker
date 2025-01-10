package pl.edu.pjatk.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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