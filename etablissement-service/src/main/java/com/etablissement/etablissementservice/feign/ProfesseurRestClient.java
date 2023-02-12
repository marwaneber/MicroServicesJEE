package com.etablissement.etablissementservice.feign;

import com.etablissement.etablissementservice.model.Professeur;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "SERVICE-PROFESSEUR")
public interface ProfesseurRestClient {
    @GetMapping(path = "/professeurs/departement/{idDepart}")
    List<Professeur> professeursDepart(@PathVariable Long idDepart);
}
