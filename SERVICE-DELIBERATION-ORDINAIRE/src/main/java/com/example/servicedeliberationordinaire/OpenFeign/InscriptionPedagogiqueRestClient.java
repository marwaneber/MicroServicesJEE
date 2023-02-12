package com.example.servicedeliberationordinaire.OpenFeign;

import com.example.servicedeliberationordinaire.Model.Etudiant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "INSCRIPTION-PEDAGOGIQUE-SERVICE", url = "localhost:8095")
public interface InscriptionPedagogiqueRestClient {
    @GetMapping(path = "/etudiantsInscritPedagModule/{idFiliere}/{idModule}/{annee}")
    List<Etudiant> etudiantsInscritPedagModule (@PathVariable Long idFiliere ,@PathVariable Long idModule, @PathVariable int annee);
}
