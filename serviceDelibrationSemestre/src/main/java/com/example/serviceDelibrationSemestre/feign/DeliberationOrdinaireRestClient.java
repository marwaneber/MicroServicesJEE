package com.example.serviceDelibrationSemestre.feign;

import com.example.serviceDelibrationSemestre.model.Etudiant;
import com.example.serviceDelibrationSemestre.model.NoteElementModuleO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "SERVICE-DELIBERATION-ORDINAIRE", url = "localhost:8003")//, url = "localhost:8003"
public interface DeliberationOrdinaireRestClient {
    @GetMapping(path = "/deliberationordin/{idEtudiant}/{idElement}")
    NoteElementModuleO noteElementModule(@PathVariable Long idEtudiant, @PathVariable Long idElement);
    @GetMapping(path = "avoirTousLesNotesElement/{idElement}")
    List<NoteElementModuleO> avoirTousLesNotesElement(@PathVariable Long idElement);
    @GetMapping("/listDesEtudiantsRatt/{idElement}/{annee}")
    List<Etudiant> etudiantsRatt(@PathVariable Long idElement, @PathVariable int annee);
}
