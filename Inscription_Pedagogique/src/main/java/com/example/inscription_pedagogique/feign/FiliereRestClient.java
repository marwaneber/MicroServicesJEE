package com.example.inscription_pedagogique.feign;


import com.example.inscription_pedagogique.model.Etape;
import com.example.inscription_pedagogique.model.Filiere;
import com.example.inscription_pedagogique.model.Module;
import com.example.inscription_pedagogique.model.Semestre;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@FeignClient(name = "FILIERE-SERVICE", url = "localhost:8081")
public interface FiliereRestClient {
    @GetMapping(path="/etapes/{id}")
    Etape getEtapeById(@PathVariable Long id);
//    @GetMapping(path="/etapes/{etapeID}/semestres")
//    List<Semestre> getSemestresByEtape(@PathVariable Long etapeID);
    //On va la remplacer par une methode renvoi les semestres d'une etape d'une filiere
    @GetMapping(path = "/semestres/etape/filiere/{idFiliere}/{etape}")//test
    List<Semestre> semestresEtapeFiliere(@PathVariable Long idFiliere, @PathVariable int etape);

    @GetMapping(path="/filiereetape/{id}/{lib}")
    Long findIdetapeByFiliere_IdAndLibEtape(@PathVariable Long id ,@PathVariable int lib);
    @GetMapping(path="/semestreByid/{semID}")
    Semestre getSemestresByid(@PathVariable Long  semID);
    @GetMapping(path="/etapes/{etapeID}/{libc}/semestres")
    Semestre getSemestresByEtapeandlib(@PathVariable Long etapeID , @PathVariable String libc);
    @GetMapping(path="/filiere/{idFil}/etapes")
    Collection<Etape> listeEtapesFiliere(@PathVariable Long idFil);

    @GetMapping(path = "/filieres/{id}")
    Optional<Filiere> getFiliereById(@PathVariable Long id);

    @GetMapping(path = "/etape/filiere/{idFiliere}/{etape}")//tester
    Etape etapeFiliere(@PathVariable Long idFiliere, @PathVariable int etape);









}
