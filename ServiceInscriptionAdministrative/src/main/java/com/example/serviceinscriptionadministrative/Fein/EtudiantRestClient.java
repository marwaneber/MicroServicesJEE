package com.example.serviceinscriptionadministrative.Fein;

import com.example.serviceinscriptionadministrative.Model.Etudiant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name="INSCRIPTION-ENLIGNE-SERVICE", url = "localhost:8087")
public interface EtudiantRestClient {
    @GetMapping(path = "/etudiants")
    PagedModel<Etudiant> pageStudents();

    @GetMapping(path = "/etudiants/{id}")
    Optional<Etudiant> getStudent(@PathVariable Long id);
//    @GetMapping(path = "/etudiants/{date}")
//    List<Etudiant> findByDateInscription_Year(@PathVariable int year);

    }
