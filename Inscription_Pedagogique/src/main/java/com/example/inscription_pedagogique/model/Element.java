package com.example.inscription_pedagogique.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor @NoArgsConstructor @Data @ToString
public class Element {
    private Long idElement;
    private String LibLong; //nomElement
    private String LibCourt;
    private double coefElem; //coefficient element
    private double coefTP;
    private double coefControl;
    private double coefExam;
    private double NoteMin;
    private Long idProf;
    private Module module;

}
