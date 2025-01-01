package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "characters")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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