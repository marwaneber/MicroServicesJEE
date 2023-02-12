package com.example.serviceprofesseur.feign;

import com.example.serviceprofesseur.modules.Element;
import com.example.serviceprofesseur.modules.Module;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "MODULE-SERVICE", url = "localhost:8090")
public interface ModuleRestClient {
//    @GetMapping(path = "/modules")
//    List<Module> pageModules();
    @GetMapping(path="/elements/professeur/{id}")
    List<Element> listeElementsProfesseur (@PathVariable Long id);
    @GetMapping(path = "/modules/{id}")
    Module getModuleById(@PathVariable Long id);
    @GetMapping(path="/elements/{idElement}")
    Element elementByID(@PathVariable Long idElement);

//    @PostMapping(path = "/addModule")
//    Module addModule(@RequestBody Module module);

}
