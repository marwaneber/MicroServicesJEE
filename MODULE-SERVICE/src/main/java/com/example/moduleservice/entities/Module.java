package com.example.moduleservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity(name="modules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Module {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idModule;
    private String LibLong;
    private String LibCourt;
    private double NoteMin;
    private int coef;
    private Long idFiliere;
    private int nbrElement=0;
    private Long idProf; //Le responsable de module
    //Remarque : Lors de la création d'un module il y aura la création automatique d'un element portant le meme nom que le module
    // Cet element cree par défaut sera supprimer si on ajoute au minimum un element, tout en incrementant le nbrElement
    // De meme lors de la suppression d'un element si le nombre d'elemnt devient 0 on cree l'element par defaut

    private Long idSemestre;


    @OneToMany(mappedBy = "module", fetch = FetchType.EAGER)
    private Collection<Element> elements;

//    private Long idProf;
    //En attendant le micro service inscription pedagogique
//    @Transient
//    private Collection<Etudiant> etudiantCollection;
}

