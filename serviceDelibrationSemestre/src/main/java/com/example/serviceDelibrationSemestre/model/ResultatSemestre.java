package com.example.serviceDelibrationSemestre.model;

import com.example.serviceDelibrationSemestre.Enumeration.Resultat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @ToString @Data
public class ResultatSemestre {
    private int annee;
    private String Filiere;
    private String Responsable;
    private String EtudiantCne;
    private String EtudiantNom;
    private String EtudiantPrenom;
    private String SemestreLibLong;
    private String SemestreLibCourt;
    private double moyenneSemestre;
    private Resultat resultat;//

    private List<NoteModule> noteModules;

}
