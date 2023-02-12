package com.example.inscription_pedagogique.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collection;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Module {
    private Long idModule;
    private String LibLong;
    private String LibCourt;
    private double NoteMin;
    private int coef;
    private Long idFiliere;
    private Long idProf;
    private Long idSemestre;


}
