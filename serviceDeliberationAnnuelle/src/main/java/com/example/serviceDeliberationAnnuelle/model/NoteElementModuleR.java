package com.example.serviceDeliberationAnnuelle.model;

import com.example.serviceDeliberationAnnuelle.Enumeration.Resultat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
