package com.example.serviceDeliberationAnnuelle.model;

import com.example.serviceDeliberationAnnuelle.Enumeration.Resultat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @ToString @Data
public class ResultatEtape {
    private int annee;
    private String Filiere;
    private String Responsable;
    private String EtudiantCne;
    private String EtudiantNom;
    private String EtudiantPrenom;
    private int EtapeLib;
    private double moyenneEtape;
    private Resultat resultat;//

    private List<NoteSemestre> noteSemestres;

}
