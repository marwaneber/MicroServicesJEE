package com.example.servicedeliberationordinaire.OpenFeign;



import com.example.servicedeliberationordinaire.Model.Filiere;
import com.example.servicedeliberationordinaire.Model.Semestre;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

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
    @GetMapping(path="/semestreByid/{semID}")
    Semestre getSemestresByid(@PathVariable Long  semID);
}
