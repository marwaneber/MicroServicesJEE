package com.example.filiereservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity @Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Filiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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



//    @OneToMany(mappedBy = "filiere")
//    private Collection<Semestre> semestreCollection;

    @OneToMany(mappedBy = "filiere")
    private Collection<Etape> etapeCollection;

    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private TypeFormation typeFormation;

}
