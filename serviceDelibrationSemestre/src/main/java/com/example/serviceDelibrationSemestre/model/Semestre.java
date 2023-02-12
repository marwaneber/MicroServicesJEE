package com.example.serviceDelibrationSemestre.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
}
