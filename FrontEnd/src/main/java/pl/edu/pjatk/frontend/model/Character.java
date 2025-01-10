package pl.edu.pjatk.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Character {
    private Integer id;
    private Integer anilistId;
    private Name name;
    private String gender;
    private String age;
    private Date dateOfBirth;
    private String imageUrl;
    private Set<Media> mediaAppearances;
} 