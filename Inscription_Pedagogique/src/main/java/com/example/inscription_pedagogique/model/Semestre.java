package com.example.inscription_pedagogique.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Semestre {
    private Long idSemestre;
    private String LibLong;
    private String LibCourt;
    private float CofSem;
    private float notMin;
    private Etape etape;
}
