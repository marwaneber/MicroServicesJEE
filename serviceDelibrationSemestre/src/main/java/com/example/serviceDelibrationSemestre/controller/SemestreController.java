package com.example.serviceDelibrationSemestre.controller;
import com.example.serviceDelibrationSemestre.Exception.DeliberationSemestreDefinedException;
import com.example.serviceDelibrationSemestre.feign.FiliereRestClient;
import com.example.serviceDelibrationSemestre.help.GeneratePdfReport;
import com.example.serviceDelibrationSemestre.model.InscriptionPedagogique;
import com.example.serviceDelibrationSemestre.model.ResultatSemestre;
import com.example.serviceDelibrationSemestre.service.SemestreService;
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
public class SemestreController {
    private SemestreService semestreService;

    private FiliereRestClient filiereRestClient;


    public SemestreController(SemestreService semestreService, FiliereRestClient filiereRestClient) {
        this.semestreService = semestreService;
        this.filiereRestClient = filiereRestClient;
    }

    @GetMapping(path = "/sauvegarderNoteModule/{idEtudiant}/{idModule}")
    public InscriptionPedagogique sauvegarderNoteModule(@PathVariable Long idEtudiant, @PathVariable Long idModule) throws DeliberationSemestreDefinedException {
        if(semestreService.testModule(idModule)){
                return semestreService.sauvegarderNoteModule(idEtudiant,idModule);
        } else{
            throw new DeliberationSemestreDefinedException("Cette opération n'est pas possible pour le moment!!");
        }
    }

    @GetMapping(path = "/sauvegarderResultatSemestreEtudiant/{idSemestre}/{idEtudiant}")
    public InscriptionPedagogique sauvegarderResultatSemestreEtudiant(@PathVariable Long idSemestre, @PathVariable Long idEtudiant) throws DeliberationSemestreDefinedException{
        int t1 = semestreService.testSemestre(idSemestre, idEtudiant);
        if(t1==0){
            return semestreService.sauvegarderResultatSemestreEtudiant(idEtudiant,idSemestre);
        } else{
            throw new DeliberationSemestreDefinedException("Cette opération n'est pas possible pour le moment!!, il rest encore "+t1+" modules sans notes");
        }
    }

    // Generer un fichier pdf de la deliberation des semestre
    @GetMapping(value = "/pdfreportSemestre/{idFiliere}/{idSemestre}", produces = MediaType.APPLICATION_PDF_VALUE )
    public ResponseEntity<InputStreamResource> etudiantsReport(@PathVariable Long idFiliere,
                                                               @PathVariable Long idSemestre) throws DeliberationSemestreDefinedException {
        List<ResultatSemestre> resultatSemestres = semestreService.delibirationSmestre(idSemestre, idFiliere);
        ByteArrayInputStream bis = GeneratePdfReport.semestreReport(resultatSemestres);
        var headers = new HttpHeaders();
        headers.add("Content-Disposition","inline; finelname=etudiantsreport.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
