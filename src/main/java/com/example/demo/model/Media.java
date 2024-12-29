package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Media {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "titleId", referencedColumnName = "id")
    private Title title;

    private Integer episodes;

//    @Embedded
//    private Date startDate;
//    @Embedded
//    private Date endDate;
//
//    @ManyToMany
//    @JoinColumn(name = "genreId", referencedColumnName = "id")
//    private List<Genre> genres;
}
