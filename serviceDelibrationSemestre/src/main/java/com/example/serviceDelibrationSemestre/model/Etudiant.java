package com.example.serviceDelibrationSemestre.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Etudiant {
    private Long id;
    private String name;
    private String lastname;
    private String CNE;
    private Long idFiliere;

}
