package com.example.inscription_pedagogique.entities;

import com.example.inscription_pedagogique.Enumeration.Resultat;
import com.example.inscription_pedagogique.Enumeration.TypeElementPedagogique;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class  InscriptionPedagogique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//
    private Long idInscripAdmin;//
    private Long idElmPed;// Un element pedagogique est soit : Module, Semestre ou Etape
    private int anneeuniv;//

    @Enumerated
    private TypeElementPedagogique typeelp;//
    private float note;//
    private Resultat resultat;//




}
