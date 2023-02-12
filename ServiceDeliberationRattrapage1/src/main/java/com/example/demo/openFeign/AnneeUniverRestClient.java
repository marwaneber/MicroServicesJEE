package com.example.demo.openFeign;

import com.example.demo.model.AnneeUniversitaire;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name="ANNEE-UNIVERSITAIRE-SERVICE", url = "localhost:8099")
public interface AnneeUniverRestClient {
    @GetMapping(path = "/AUs/{id}")
    Optional<AnneeUniversitaire> getAUById(@PathVariable Long id);
    @GetMapping(path="/AnneeCourante")
    AnneeUniversitaire getAnneeCourante();
    }
