package com.example.moduleservice.Feign;

import com.example.moduleservice.Modules.Semestre;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "FILIERE-SERVICE", url = "localhost:8081")
public interface SemestreRestClient {
    @GetMapping(path="/semestres/{semID}") //tester
    Optional<Semestre> getSemByID(@PathVariable Long semID);

}
