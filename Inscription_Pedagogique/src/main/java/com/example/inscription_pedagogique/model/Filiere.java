package com.example.inscription_pedagogique.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Filiere {
    private  Long id;

    private  String LibLong;
    private String LibCourt;
    private String LibArLong;
    private String LibArCourt;

    private int nbrEtape;//Le max des etapes
    private int nbrSemestre;
    private int nbrAnneeDiplomantes;
    private String responsableFiliere;

    private Long idDepart;
    private Collection<Etape> etapeCollection;

}
