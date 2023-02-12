package com.example.demo.entities;

import com.example.demo.Enumeration.Resultat;
import com.example.demo.model.Element;
import com.example.demo.model.Etudiant;
import com.example.demo.model.Module;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class NoteElementR {
    @Id @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;//
    private double noteCotrole;//
    private double noteTp;//
    private double noteExamR;//
    private double noteElementR;//
    private double noteElementFinale;//
    private Resultat validite;//
    private Long idElement;//
    private Long idModule;//
    private int anneeU;//
    private Long idEtudiant;//


    @Column(unique = true)
    private String ladiff;//

    @Transient
    private Etudiant etudiant;
    @Transient
    private Element element;
    @Transient
    private Module module;






}
