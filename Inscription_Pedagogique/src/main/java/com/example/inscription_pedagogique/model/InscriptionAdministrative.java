package com.example.inscription_pedagogique.model;


import com.example.inscription_pedagogique.Enumeration.EtatIns;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

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
//    private EtudiantInscAdmin etudiantInscAdmin;

}
