package com.etablissement.etablissementservice.entities;

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

public class Etablissement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEtab;
    private String LibLong;
    private String LibCourt;
    private String LibArLong;
    private String LibArCourt;

    @OneToMany(mappedBy = "etablissement")
    private Collection<Departement> departementList;

}
