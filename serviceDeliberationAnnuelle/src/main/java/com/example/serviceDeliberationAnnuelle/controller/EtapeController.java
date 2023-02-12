package com.example.serviceDeliberationAnnuelle.controller;
import com.example.serviceDeliberationAnnuelle.feign.FiliereRestClient;
import com.example.serviceDeliberationAnnuelle.Exception.DeliberationEtapeDefinedException;
import com.example.serviceDeliberationAnnuelle.help.GeneratePdfReport;
import com.example.serviceDeliberationAnnuelle.model.InscriptionPedagogique;
import com.example.serviceDeliberationAnnuelle.model.ResultatEtape;
import com.example.serviceDeliberationAnnuelle.model.Semestre;
import com.example.serviceDeliberationAnnuelle.service.EtapeService;
import lombok.var;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
public class EtapeController {
    private EtapeService etapeService;

    private FiliereRestClient filiereRestClient;


    public EtapeController(EtapeService etapeService, FiliereRestClient filiereRestClient) {
        this.etapeService = etapeService;
        this.filiereRestClient = filiereRestClient;
    }


    @GetMapping(path = "/sauvegarderResultatEtapeEtudiant/{idEtape}/{idEtudiant}")
    public InscriptionPedagogique sauvegarderResultatEtapeEtudiant(@PathVariable Long idEtape, @PathVariable Long idEtudiant) throws DeliberationEtapeDefinedException{
        int t1 = etapeService.testEtape(idEtape, idEtudiant);
        if(t1==0){
            return etapeService.sauvegarderResultatEtapeEtudiant(idEtudiant,idEtape);
        } else{
            throw new DeliberationEtapeDefinedException("Cette opération n'est pas possible pour le moment!!, il rest encore "+t1+" semestre incomplets");
        }
    }

    // Generer un fichier pdf de la deliberation des semestre
    @GetMapping(value = "/pdfreportEtape/{idFiliere}/{idEtape}", produces = MediaType.APPLICATION_PDF_VALUE )
    public ResponseEntity<InputStreamResource> etudiantsReport(@PathVariable Long idFiliere,
                                                               @PathVariable Long idEtape) throws DeliberationEtapeDefinedException {
        List<ResultatEtape> resultatEtapes = etapeService.delibirationEtape(idEtape, idFiliere);
        ByteArrayInputStream bis = GeneratePdfReport.etapeReport(resultatEtapes);
        var headers = new HttpHeaders();
        headers.add("Content-Disposition","inline; finelname=etudiantsreport.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
