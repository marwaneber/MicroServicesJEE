package com.example.serviceinscriptionenligne.fiegn;

import com.example.serviceinscriptionenligne.Model.Etablissement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "Etablissement-service")
public interface EtablissementRestClient {
    @GetMapping(path="/etablissements") //TESTER
    List<Etablissement> getAllEtab();
}
