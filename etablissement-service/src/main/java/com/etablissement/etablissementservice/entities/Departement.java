package com.etablissement.etablissementservice.entities;

import com.etablissement.etablissementservice.model.Filiere;
import com.etablissement.etablissementservice.model.Professeur;
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
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String LibLong;
    private String LibCourt;
    private String LibArLong;
    private String LibArCourt;
    private String chefDepartement;
    @Transient
    private Collection<Filiere> filiereLists;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "IdEtab")
    private Etablissement etablissement;

    @Transient
    private Collection<Professeur> professeurCollection;
}
