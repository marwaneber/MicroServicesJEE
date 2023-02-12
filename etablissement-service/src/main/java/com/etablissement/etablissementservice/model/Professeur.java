package com.etablissement.etablissementservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Professeur {
    private Long idProf;
    private String nomProf;
    private String prenomProf;
    private Long idDepar;
}
