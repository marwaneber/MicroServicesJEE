package com.example.serviceinscriptionadministrative.Entities;


import com.example.serviceinscriptionadministrative.Enums.Gender;
import com.example.serviceinscriptionadministrative.Enums.Mention;
import com.example.serviceinscriptionadministrative.Model.Module;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EtudiantInscAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Informations personnelles
    @Column(unique = true)
    private String CNE;
    private String CIN_Passeport;
    private String name;
    private String nameAr;
    private String lastname;
    private String lastnameAr;

    private String email;
    private String telephone;
    private String photo;
    @Enumerated(EnumType.STRING)
    private Gender sexe;
    private String nationalité;

    //Informations sur l'origine
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    private String lieuNaissance;
    private String lieuNaissanceAr;
    private String ville;
    private String province;

    //Informations sur le bac
    private int anneeBac;
    private String serieBac;
    @Enumerated(EnumType.STRING)
    private Mention mention;
    private String lycee;
    private String lieuBac;
    private String academie;

    private boolean TEM_Fonc;
    private boolean handicap;
    private boolean TEM_Bourse;

    private boolean TEM_JourMois;

    //Informations sur l'inscription administrative
    @Temporal(TemporalType.DATE)
    private Date dateInscriptionEnligne;
    private Long id_inscription_enligne;

    private Long idFiliere;

    private int anneePremInscr;
//    private boolean nouveauEtudiant=true;

    @OneToMany(mappedBy = "etudiantInscAdmin")
    private Collection<InscriptionAdministrative> inscriptionAdministratives;
}
