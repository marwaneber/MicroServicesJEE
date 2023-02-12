package com.example.serviceDelibrationSemestre.feign;



import com.example.serviceDelibrationSemestre.Enumeration.EtatIns;
import com.example.serviceDelibrationSemestre.model.Etudiant;
import com.example.serviceDelibrationSemestre.model.InscriptionAdministrative;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "INSCRIPTIONADMINISTRATIVE-SERVICE", url = "localhost:8883")//, url = "localhost:8883"
public interface InscriptionAdminRestClient {
    @GetMapping(path = "/getIdEtape/{id}")
    Long GetIdEtapeFromIdInscAdmin(@PathVariable Long id);
    @GetMapping(path = "/InscriptionsAdministrative/{id}")
    InscriptionAdministrative getInscriptionsAdministrativeByid(@PathVariable(name = "id") Long id) ;
    @GetMapping(path = "/InscriptionsAdministrative")
    List<InscriptionAdministrative> listIns();
    @GetMapping(path = "/getIdE/{id}")
    Long getIde(@PathVariable Long id);
    @GetMapping(path = "/InscriptionsAdministrativeEtudiantAnnee/{id_etudiant}/{annee}")
    InscriptionAdministrative getInsByIdEtudiantAnnee(@PathVariable(name = "id_etudiant")Long idEtudiant,@PathVariable(name = "annee") int anneeUniv);
    @GetMapping(path = "/InsAdminByAnnee/{annee}")
    List<InscriptionAdministrative> getInsByAnne(@PathVariable int annee);
    @GetMapping(path = "/etudiantsInscAdmin/{cne}")
    Etudiant findEtudiantByCne(@PathVariable String cne);
    @GetMapping(path="/etudiantsInscAdmin/{idEtudiantInscAdmin}")
    Optional<Etudiant> etudiantInscAdminById(@PathVariable Long idEtudiantInscAdmin);

    @GetMapping(path="/inscriptionsAdministrativeEncous/{annee}")
    List<InscriptionAdministrative> inscriptionsAdministrativeEncous(@PathVariable int annee);

    @GetMapping(path = "/etudiantParInscriptionAdmin/{idInscAdm}")
    Etudiant etudiantParInscriptionAdmin(@PathVariable Long idInscAdm);

    @GetMapping(path="/inscriptionAdminEtudiantAnneE/{idEtudiant}/{annee}/{etat}")
    InscriptionAdministrative inscriptionAdminEtudiantAnneE(@PathVariable Long idEtudiant, @PathVariable int annee, @PathVariable EtatIns etat);





}
