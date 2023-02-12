package com.example.servicedeliberationordinaire.Entities;

import com.example.servicedeliberationordinaire.Enumeration.Resultat;
import com.example.servicedeliberationordinaire.Model.Element;
import com.example.servicedeliberationordinaire.Model.Etudiant;
import com.example.servicedeliberationordinaire.Model.Module;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@AllArgsConstructor @NoArgsConstructor @Data @ToString
public class NoteElementModule {
    @Id @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;//
    private double noteTP;
    private double noteControl;
    private double noteExam;
    private double noteElement;
    private Resultat validete;
    private Long idEtudiant;//
    private Long idElement;//
    private Long idModule;//
    private int anneeU;//


    @Transient
    private Module module;
    @Transient
    private Element element;
    @Transient
    private Etudiant etudiant;

    @Column(unique = true)
    private String ladiff;//


}
