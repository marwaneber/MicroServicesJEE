package com.example.filiereservice.Modules;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.example.filiereservice.Enums.EtatIns;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InscriptionAdministrative {
    private Long idInsAdmin;
    private Long idFiliere;
    private int AnneeUniv;
    private Long idEtape;
    private EtatIns EtatIns;
    private Long idEtudiantInscAdmin;
}
