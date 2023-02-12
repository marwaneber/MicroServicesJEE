package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class NoteElementO {
    private Long id;
    private double noteTP;
    private double noteControl;
    private double noteExam;
    private double noteElement;
    private String validete;
    private String cne;
    private Long idElement;
    private Long idModule;
    private String nomElement;
    private int anneeU;

    private Module module;
    private Element element;
    private Etudiant etudiant;

    private String ladiff;
}
