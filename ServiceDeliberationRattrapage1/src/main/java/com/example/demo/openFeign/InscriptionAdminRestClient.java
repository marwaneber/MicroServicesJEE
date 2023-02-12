package com.example.demo.openFeign;

import com.example.demo.model.Etudiant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "INSCRIPTIONADMINISTRATIVE-SERVICE", url = "localhost:8883")
public interface InscriptionAdminRestClient {
    @GetMapping(path = "/etudiantsInscAdminByCne/{cne}")
    Etudiant findEtudiantByCne(@PathVariable String cne);
    @GetMapping(path="/etudiantsInscAdmin/{idEtudiantInscAdmin}")
    Optional<Etudiant> etudiantInscAdminById(@PathVariable Long idEtudiantInscAdmin);
    @GetMapping(path = "/etudiantParInscriptionAdmin/{idInscAdm}")
    Etudiant etudiantParInscriptionAdmin(@PathVariable Long idInscAdm);
}
