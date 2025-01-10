package pjatk.edu.pl.data.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "media_lists")
public class CustomMediaList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;
    String description;

    @ManyToMany
            @JoinTable(name = "media_list_relations")
    Set<Media> mediaList;
}
