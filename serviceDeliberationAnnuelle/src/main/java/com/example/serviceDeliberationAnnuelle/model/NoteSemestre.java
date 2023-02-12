package com.example.serviceDeliberationAnnuelle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor @NoArgsConstructor @Data @ToString
public class NoteSemestre {
    private String LibLong;
    private String LibCourt;
    private float noteSemestreF;
    private int anneeS;
}
