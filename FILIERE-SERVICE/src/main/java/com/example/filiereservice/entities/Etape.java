package com.example.filiereservice.entities;

import com.example.filiereservice.Modules.InscriptionAdministrative;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Etape {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEtape;
    private int LibEtape;
    private Boolean OI; //OI: Ouverte à l'Inscription ou non.
    float notMin;
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private Filiere filiere;

    @OneToMany(mappedBy = "etape")
    private Collection<Semestre> semestreCollection;

    @Transient
    private Collection<InscriptionAdministrative> inscriptionAdministratives;

}
