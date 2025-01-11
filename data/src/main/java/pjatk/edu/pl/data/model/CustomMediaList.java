package pjatk.edu.pl.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "media_lists")
public class CustomMediaList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;

    @ManyToMany
    @JoinTable(
        name = "media_list_relations",
        joinColumns = @JoinColumn(name = "media_list_id"),
        inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    @JsonIgnoreProperties({"characters", "genres"})
    private Set<Media> mediaList;
}
