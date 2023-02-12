package com.example.serviceDeliberationAnnuelle.feign;


import com.example.serviceDeliberationAnnuelle.model.Etape;
import com.example.serviceDeliberationAnnuelle.model.Filiere;
import com.example.serviceDeliberationAnnuelle.model.Semestre;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@FeignClient(name = "FILIERE-SERVICE", url = "localhost:8081")//, url = "localhost:8081"
public interface FiliereRestClient {
    @GetMapping(path="/etapes/{id}")
    Etape getEtapeById(@PathVariable Long id);

    @GetMapping(path="/etapes/{etapeID}/semestres")
    List<Semestre> getSemestresByEtape(@PathVariable Long etapeID);

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

    @GetMapping(path="/filiereBySemestre/{idSemestre}")
    Filiere filiereBySemestre(@PathVariable Long idSemestre);
    //Liste des semestres d'une etape d'un filiere
    @GetMapping(path="/semestres/etape/{ide}/filiere/{idf}") //tester
    List<Semestre> listeSemestresEtapeFiliere(@PathVariable Long ide , @PathVariable Long idf);

    @GetMapping(path = "/semestres/etape/filiere/{idFiliere}/{etape}")//test
    List<Semestre> semestresEtapeFiliere(@PathVariable Long idFiliere, @PathVariable int etape);









}
