package com.example.serviceinscriptionadministrative.Fein;

import com.example.serviceinscriptionadministrative.Model.Module;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "MODULE-SERVICE", url = "localhost:8090")
public interface ModuleRestClient {
    @GetMapping(path="/modules/semestre/{id}")
    List<Module> listeModulesSemestre (@PathVariable Long id);
    @GetMapping(path = "/modules/{id}")
    Module getModuleById(@PathVariable Long id);
}
