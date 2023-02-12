package com.example.inscription_pedagogique.feign;

import com.example.inscription_pedagogique.entities.InscriptionPedagogique;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SERVICE-DELIBERATION-ANNUELLE", url = "localhost:8085")
public interface DeliberationAnnuelleRestClient {
    @GetMapping(path = "/sauvegarderResultatEtapeEtudiant/{idEtape}/{idEtudiant}")
    InscriptionPedagogique sauvegarderResultatEtapeEtudiant(@PathVariable Long idEtape, @PathVariable Long idEtudiant);

}
