package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "characters")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "anilist_id", unique = true)
    private Integer anilistId;

    @Embedded
    private Name name;

    private String gender;
    private String age;

    @Embedded
    private Date dateOfBirth;

    private String imageUrl;

    @ManyToMany(mappedBy = "characters")
    private Set<Media> mediaAppearances;
} 