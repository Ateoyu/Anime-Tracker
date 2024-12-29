package com.example.demo.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Name {
    private String full;
    private String local;
}
