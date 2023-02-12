package com.example.filiereservice.Feign;

import com.example.filiereservice.Modules.InscriptionAdministrative;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "InscriptionAdministrative-service", url = "localhost:8883")
public interface InscriptionAdministrativeRestClient {
    @GetMapping(path = "/InscriptionsAdministrative")
    List<InscriptionAdministrative> listIns();
}
