package com.example.serviceinscriptionadministrative.Model;

import com.example.serviceinscriptionadministrative.Enums.Gender;
import com.example.serviceinscriptionadministrative.Enums.Mention;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Etudiant implements Serializable {
    private Long idStudent;
    private String CNE;
    private String CIN_Passeport;
    private String firstname;
    private String firstnameAr;
    private String lastname;
    private String lastnameAr;
    private String email;
    private String telephone;
    private String photo;
    private String nationalite;
    private Date dateNaissance;
    private String lieuNaissance;
    private String lieuNaissanceAr;
    private String ville;
    private String province;
    private int anneeBac;
    private String serieBac;
    private Mention mention;
    private String lycee;
    private String lieuBac;
    private String academie;
    private boolean TEM_Fonc;
    private boolean handicap;
    private boolean TEM_JourMois;
    private Date dateInscription;
    private String etablissement;
    private Long idFiliere1;
    private Long idFiliere2;
    private Long idFiliere3;
    private Gender sexe;
}
