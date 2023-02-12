package com.example.serviceDelibrationSemestre.model;

import com.example.serviceDelibrationSemestre.Enumeration.EtatIns;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InscriptionAdministrative {
    private Long idInsAdmin;
    private Long idFiliere;
    private int AnneeUniv;
    private Long idEtape;
    @Enumerated(EnumType.STRING)
    private EtatIns EtatIns;
    private Etudiant etudiantInscAdmin;
}
