package com.example.inscription_pedagogique.model;

import com.example.inscription_pedagogique.Enumeration.Resultat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor @NoArgsConstructor @Data @ToString
public class NoteElementModuleR {
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
    private String ladiff;//

}
