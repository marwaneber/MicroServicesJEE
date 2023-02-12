package com.example.serviceDeliberationAnnuelle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Etape {
    private Long idEtape;
    private int LibEtape;
    private Boolean OI;
    private Filiere filiere;
    private Collection<Semestre> semestreCollection;
    float notMin;
}
