package com.example.inscription_pedagogique.feign;

import com.example.inscription_pedagogique.model.NoteElementModuleO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SERVICE-DELIBERATION-ORDINAIRE", url = "localhost:8003")
public interface DeliberationOrdinaireRestClient {
    @GetMapping(path = "/deliberationordin/{idEtudiant}/{idElement}")
    NoteElementModuleO noteElementModule(@PathVariable Long idEtudiant, @PathVariable Long idElement);
}
