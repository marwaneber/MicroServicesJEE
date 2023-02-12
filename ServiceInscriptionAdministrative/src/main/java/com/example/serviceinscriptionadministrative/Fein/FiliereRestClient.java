package com.example.serviceinscriptionadministrative.Fein;


import com.example.serviceinscriptionadministrative.Model.Etape;
import com.example.serviceinscriptionadministrative.Model.Filiere;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "FILIERE-SERVICE", url = "localhost:8081")

public interface FiliereRestClient {
    @GetMapping(path = "/filieres")
    PagedModel<Filiere> pageFilieres();

    @GetMapping(path = "/filieres/{id}")
    Filiere getFiliereById(@PathVariable Long id);

    @PostMapping(path = "/filieres/addfiliere")
    Filiere addfiliere(@RequestBody  Filiere filiere);

    @GetMapping(path="/departement/{departID}")
    List<Filiere> listeFiliereDepartement(@PathVariable Long departID);

    @GetMapping(path="/etapes/{id}")
    Etape etapeById(@PathVariable Long id);

    @GetMapping(path="/etapeParNum/{num}")
    Etape etapeByNb(@PathVariable int num);

    @GetMapping(path = "/etape/filiere/{idFiliere}/{etape}")
    Etape etapeFiliere(@PathVariable Long idFiliere, @PathVariable int etape);
}
