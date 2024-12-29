package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import com.example.demo.model.Media;
import java.util.Set;

@Entity
@Data
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Media> media;
}
