package com.example.demo.Feign;


import com.example.demo.Model.ElementM;
import com.example.demo.Model.Professeur;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "SERVICE-PROFESSEUR", url = "localhost:8098")
public interface ProfesseurRestClient {
    @GetMapping(path="/professeurs") // tester
     List<Professeur> listeProfesseurs();
    @GetMapping(path="/elements/professeur/{idProf}") //tester
    List<ElementM> listeElementProfesseur(@PathVariable Long idProf);

}
