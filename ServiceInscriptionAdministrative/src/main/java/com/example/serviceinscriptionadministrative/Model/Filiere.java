package com.example.serviceinscriptionadministrative.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Filiere {
    private  Long id;
    private  String LibLong;
    private String LibCourt;
    private String LibArLong;
    private String LibArCourt;
    private int nbrAnnees;
    private int nbrSemestre;
    private int nbrAnneeDiplomantes;
    private String responsableFiliere;
    private Long idDepart;



}
