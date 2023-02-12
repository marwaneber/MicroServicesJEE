package com.example.filiereservice.Feign;

import com.example.filiereservice.Modules.Departement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
@FeignClient(name = "Etablissement-service", url = "localhost:8086")
public interface DepartementRestClient {
    @GetMapping(path="/departements/{id}") //TESTER
    Optional<Departement> departementById(@PathVariable Long id);
}
