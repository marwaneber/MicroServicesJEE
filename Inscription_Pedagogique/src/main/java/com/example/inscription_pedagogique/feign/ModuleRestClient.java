package com.example.inscription_pedagogique.feign;

import com.example.inscription_pedagogique.model.Element;
import com.example.inscription_pedagogique.model.Module;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "MODULE-SERVICE", url = "localhost:8090")
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
}
