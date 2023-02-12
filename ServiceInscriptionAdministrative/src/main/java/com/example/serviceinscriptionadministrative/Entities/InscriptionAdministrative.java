package com.example.serviceinscriptionadministrative.Entities;

import com.example.serviceinscriptionadministrative.Enums.EtatIns;
import com.example.serviceinscriptionadministrative.Model.Etape;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
@Entity(name="InscriptionAdministrative")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InscriptionAdministrative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInsAdmin;
    private Long idFiliere;
    private int AnneeUniv;
    private Long idEtape;
    @Enumerated(EnumType.STRING)
    private EtatIns EtatIns;
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private EtudiantInscAdmin etudiantInscAdmin;
}
