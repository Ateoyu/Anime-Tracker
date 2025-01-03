package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "mediaAppearances")
@ToString(exclude = "mediaAppearances")
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
    @AttributeOverrides({
            @AttributeOverride(name = "year", column = @Column(name = "dob_year")),
            @AttributeOverride(name = "month", column = @Column(name = "dob_month")),
            @AttributeOverride(name = "day", column = @Column(name = "dob_day"))
    })
    private Date dateOfBirth;

    private String imageUrl;

    @ManyToMany(mappedBy = "characters")
    private Set<Media> mediaAppearances;
} 