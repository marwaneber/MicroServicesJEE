package com.example.inscription_pedagogique.feign;

import com.example.inscription_pedagogique.model.NoteElementModuleO;
import com.example.inscription_pedagogique.model.NoteElementModuleR;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SERVICE-DELIBERATION-RATTRAPAGE", url = "localhost:8083")
public interface DeliberationRattRestClient {
    @GetMapping(path = "/deliberationRatt/{idEtudiant}/{idElement}")
    NoteElementModuleR noteElementModule(@PathVariable Long idEtudiant, @PathVariable Long idElement);


}
