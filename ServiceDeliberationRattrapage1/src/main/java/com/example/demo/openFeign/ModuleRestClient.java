package com.example.demo.openFeign;

import com.example.demo.model.Element;
import com.example.demo.model.Module;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "MODULE-SERVICE",url = "localhost:8090")
public interface ModuleRestClient {
    @GetMapping(path="/modules/semestre/{id}")
    List<Module> listeModulesSemestre (@PathVariable Long id);
    @GetMapping(path = "/modules/{id}")
    Module getModuleById(@PathVariable Long id);
    @GetMapping(path="/moduleElement/{idElement}")
    Module moduleElement(@PathVariable Long idElement);
    @GetMapping(path="/elements/{idElement}")
    Element elementByID(@PathVariable Long idElement);
    @GetMapping(path="/modules/{modID}/elements")//tester
    List<Element> getElementsBymodule(@PathVariable Long modID);
}
