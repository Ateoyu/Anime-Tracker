package pl.edu.pjatk.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Media {
    private Integer id;
    private Integer anilistId;
    private Title title;
    private Integer episodes;
    private Integer averageScore;
    private String coverImage;
    private String bannerImage;
    private String description;
    private Date startDate;
    private Date endDate;
    private Set<Genre> genres;
    private Set<Character> characters;
}
