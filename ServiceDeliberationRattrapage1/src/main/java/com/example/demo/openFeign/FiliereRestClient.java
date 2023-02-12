package com.example.demo.openFeign;



import com.example.demo.model.Filiere;
import com.example.demo.model.Semestre;
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
    @GetMapping(path="/semestreByid/{semID}")
    Semestre getSemestresByid(@PathVariable Long  semID);
}
