package com.example.serviceinscriptionadministrative.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AnneeUniversitaire {
    private Long idAU;
    private int nomAnneeUniversitaire;
    private Boolean isCourant;
}
