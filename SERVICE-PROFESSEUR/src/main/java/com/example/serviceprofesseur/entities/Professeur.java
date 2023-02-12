package com.example.serviceprofesseur.entities;

import com.example.serviceprofesseur.modules.Element;
import com.example.serviceprofesseur.modules.Module;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity(name="professeurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Professeur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProf;
    private String nomProf;
    private String prenomProf;
    private Long idDepar;
    private String discipline;
    private String role;

    @Transient
    Collection<Element> elementCollection;


    //en attendant le micro-service inscription pedagogique
//    @Transient
//    Collection<Etudiant> etudiantCollection;


//en attendant le micro-service Emploi de temps
//    private EmploiDeTemps emploiDeTemps;


}
