package com.etablissement.etablissementservice.feign;


import com.etablissement.etablissementservice.model.Filiere;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "FILIERE-SERVICE")

public interface FiliereRestClient {
    @GetMapping(path = "/filieres")
    PagedModel<Filiere> pageFilieres();

    @GetMapping(path = "/filieres/{id}")
    Filiere getFiliereById(@PathVariable Long id);


    @PostMapping(path = "/filieres/addfiliere")
    Filiere addfiliere(@RequestBody  Filiere filiere);
    @GetMapping(path="/departement/{departID}")
    List<Filiere> listeFiliereDepartement(@PathVariable Long departID);
}
