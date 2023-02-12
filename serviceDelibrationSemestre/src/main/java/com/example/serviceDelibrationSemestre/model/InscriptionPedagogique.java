package com.example.serviceDelibrationSemestre.model;

import com.example.serviceDelibrationSemestre.Enumeration.Resultat;
import com.example.serviceDelibrationSemestre.Enumeration.TypeElementPedagogique;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InscriptionPedagogique {
    private Long id;//
    private Long idInscripAdmin;//
    private Long idElmPed;// Un element pedagogique est soit : Module, Semestre ou Etape
    private int anneeuniv;//
    private TypeElementPedagogique typeelp;//
    private float note;//
    private Resultat resultat;//
}
