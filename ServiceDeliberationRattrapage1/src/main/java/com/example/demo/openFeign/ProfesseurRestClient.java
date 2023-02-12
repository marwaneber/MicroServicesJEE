package com.example.demo.openFeign;

import com.example.demo.model.Professeur;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SERVICE-PROFESSEUR", url = "localhost:8098")
public interface ProfesseurRestClient {
    @GetMapping(path="/professeurs/{idProf}") // tester
    Professeur getProfesseurById(@PathVariable Long idProf);
}
