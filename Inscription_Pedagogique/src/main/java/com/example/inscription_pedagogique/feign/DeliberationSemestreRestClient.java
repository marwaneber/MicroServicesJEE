package com.example.inscription_pedagogique.feign;

import com.example.inscription_pedagogique.entities.InscriptionPedagogique;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SERVICE-DELIBERATION-SEMESTRE", url = "localhost:8084")
public interface DeliberationSemestreRestClient {
    @GetMapping(path = "/sauvegarderNoteModule/{idEtudiant}/{idModule}")
    InscriptionPedagogique sauvegarderNoteModule(@PathVariable Long idEtudiant, @PathVariable Long idModule);
    @GetMapping(path = "/sauvegarderResultatSemestreEtudiant/{idSemestre}/{idEtudiant}")
    InscriptionPedagogique sauvegarderResultatSemestreEtudiant(@PathVariable Long idSemestre, @PathVariable Long idEtudiant);
}
