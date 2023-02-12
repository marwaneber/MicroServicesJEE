package com.example.serviceprofesseur.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Element {
    private Long idElement;
    private String LibLong;
    private String LibCourt;
    private double coef; //coefficient element
    private double NoteMin;

    private Long idProf;

    private Module module;

}
