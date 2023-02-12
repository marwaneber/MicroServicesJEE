package com.example.serviceDelibrationSemestre.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor @NoArgsConstructor @Data @ToString
public class NoteModule {
    private String LibLong;
    private String LibCourt;
    private float noteModuleF;
    private int anneeM;
}
