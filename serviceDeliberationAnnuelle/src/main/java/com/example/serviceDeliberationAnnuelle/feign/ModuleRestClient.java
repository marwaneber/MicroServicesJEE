package com.example.serviceDeliberationAnnuelle.feign;

import com.example.serviceDeliberationAnnuelle.model.Element;
import com.example.serviceDeliberationAnnuelle.model.Module;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "MODULE-SERVICE",url = "localhost:8090")//,url = "localhost:8090"
public interface ModuleRestClient {
    @GetMapping(path="/modules/semestre/{id}")
    List<Module> findModulesByIdSemestreIs(@PathVariable Long id);
    @GetMapping(path = "/modules/{id}")
    Module getModule(@PathVariable(name = "id") Long id);
    @GetMapping(path="/modules/professeur/{id}") //tester
    List<Module> listModulesProfesseur(@PathVariable Long id);
    @GetMapping(path="/modules/{modID}/elements")//tester
    List<Element> getElementsBymodule(@PathVariable Long modID);
    @GetMapping(path="/modules/semestre/{ids}/filiere/{idf}") //tester
    List<Module> listeModulesSemestreFiliere(@PathVariable Long ids , @PathVariable Long idf);
    @GetMapping(path="/elements/{idElement}") //tester
    Element elementByID(@PathVariable Long idElement);
    @GetMapping(path="/moduleElement/{idElement}")
    Module moduleElement(@PathVariable Long idElement);

}
