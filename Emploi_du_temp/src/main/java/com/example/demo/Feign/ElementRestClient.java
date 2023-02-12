package com.example.demo.Feign;


import com.example.demo.Model.ElementM;
import com.example.demo.Model.Module;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.Element;

@FeignClient(name = "MODULE-SERVICE", url = "localhost:8090")

public interface ElementRestClient {
    @GetMapping(path="/elements/{idElement}")
    ElementM elementByID(@PathVariable Long idElement);
    @GetMapping(path = "/modulebyelement/{id}")
    Module getmodulebyElement(@PathVariable Long id);
}
