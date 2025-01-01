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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "title_id")
    private Title title;
    private Integer episodes;
    private Integer averageScore;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "year", column = @Column(name = "start_year")),
        @AttributeOverride(name = "month", column = @Column(name = "start_month")),
        @AttributeOverride(name = "day", column = @Column(name = "start_day"))
    })
    private Date startDate;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "year", column = @Column(name = "end_year")),
        @AttributeOverride(name = "month", column = @Column(name = "end_month")),
        @AttributeOverride(name = "day", column = @Column(name = "end_day"))
    })
    private Date endDate;

    @ManyToMany
    @JoinTable(
        name = "media_genres",
        joinColumns = @JoinColumn(name = "media_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @ManyToMany
    @JoinTable(
        name = "media_characters",
        joinColumns = @JoinColumn(name = "media_id"),
        inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    private Set<Character> characters;


}
