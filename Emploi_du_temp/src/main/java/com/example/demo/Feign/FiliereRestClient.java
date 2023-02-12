package com.example.demo.Feign;


import com.example.demo.Model.Filiere;
import com.example.demo.Model.Semestre;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "FILIERE-SERVICE", url = "localhost:8081")

public interface FiliereRestClient {

    @GetMapping(path = "/filieres/{id}") //tester
    public Optional<Filiere> getFiliereById(@PathVariable Long id);
    @GetMapping(path="/semestreByid/{semID}")
    Semestre getSemestresByid(@PathVariable Long  semID);

}
