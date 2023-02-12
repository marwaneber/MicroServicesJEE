package com.example.filiereservice.Feign;

import com.example.filiereservice.Modules.Module;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;

@FeignClient(name = "MODULE-SERVICE", url = "localhost:8090")
public interface ModuleRestClient {
//    @GetMapping(path = "/modules")
//    List<Module> pageModules();
    @GetMapping(path="/modules/semestre/{id}")
    List<Module> listeModulesSemestre (@PathVariable Long id);
    @GetMapping(path = "/modules/{id}")
    Module getModuleById(@PathVariable Long id);
//    @PostMapping(path = "/addModule")
//    Module addModule(@RequestBody Module module);

}
