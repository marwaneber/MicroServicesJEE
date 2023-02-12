package com.example.serviceDelibrationSemestre.feign;

import com.example.serviceDelibrationSemestre.model.NoteElementModuleR;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "SERVICE-DELIBERATION-RATTRAPAGE", url = "localhost:8083")//, url = "localhost:8083"
public interface DeliberationRattRestClient {
    @GetMapping(path = "/deliberationRatt/{idEtudiant}/{idElement}")
    NoteElementModuleR noteElementModule(@PathVariable Long idEtudiant, @PathVariable Long idElement);
    @GetMapping(path = "avoirTousLesNotesElement/{idElement}")
    List<NoteElementModuleR> avoirTousLesNotesElement(@PathVariable Long idElement);


}
