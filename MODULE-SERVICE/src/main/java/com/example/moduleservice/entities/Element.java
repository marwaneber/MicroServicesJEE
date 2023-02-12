package com.example.moduleservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Collection;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Element {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idElement;
    private String LibLong;
    private String LibCourt;
    private double coefElem; //coefficient element
    private double NoteMin;
    private Long idProf;
    private float coefTP;
    private float coefControl;
    private float coefExam;

    @JsonIgnore
    @ManyToOne
    private Module module;

}
