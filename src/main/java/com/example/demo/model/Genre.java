package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Genre {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
}
