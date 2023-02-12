package com.example.serviceinscriptionenligne.entities;

import com.example.serviceinscriptionenligne.Enums.Gender;
import com.example.serviceinscriptionenligne.Enums.Mention;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


@Entity(name="EtudiantInscriptionEnLigne")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@Table(name="EtudiantInscriptionEnLigne")
public class Student implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStudent;
    @Column(unique = true)
    private String CNE;
    @Column(unique = true)
    private String CIN_Passeport;
    @Size(min = 3, max = 50)
    private String firstname;
    private String firstnameAr;
    private String lastname;
    private String lastnameAr;

    @NotBlank
    @Email(message = "Please enter a valid e-mail address")
    private String email;
    private String telephone;
    private String photo;


    private String nationalite;

    @Temporal(TemporalType.DATE)
    private Date dateNaissance;

    private String lieuNaissance;
    private String lieuNaissanceAr;
    private String ville;
    private String province;
    private int anneeBac;
    private String serieBac;
    @Enumerated(EnumType.STRING)
    private Mention mention;
    private String lycee;
    private String lieuBac;
    private String academie;

    private boolean TEM_Fonc;
    private boolean handicap;
    private boolean TEM_JourMois;
    @Temporal(TemporalType.DATE)
    private Date dateInscription;
    private String etablissement;
    private Long idFiliere1;
    private Long idFiliere2;
    private Long idFiliere3;

    @Enumerated(EnumType.STRING)
    private Gender sexe;
}
