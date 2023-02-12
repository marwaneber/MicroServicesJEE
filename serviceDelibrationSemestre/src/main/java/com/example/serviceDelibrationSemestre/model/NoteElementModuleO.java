package com.example.serviceDelibrationSemestre.model;

import com.example.serviceDelibrationSemestre.Enumeration.Resultat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor @NoArgsConstructor @Data @ToString
public class NoteElementModuleO {
    private Long id;
    private double noteTP;
    private double noteControl;
    private double noteExam;
    private double noteElement;
    private Resultat validete;
    private Long idEtudiant;
    private Long idElement;
    private Long idModule;
    private int anneeU;
    private String ladiff;
}
