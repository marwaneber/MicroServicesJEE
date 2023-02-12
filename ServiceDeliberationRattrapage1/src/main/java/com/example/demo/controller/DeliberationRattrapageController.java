package com.example.demo.controller;

import com.example.demo.Enumeration.Resultat;
import com.example.demo.Exception.DeliberationRattDefinedException;
import com.example.demo.entities.NoteElementR;
import com.example.demo.help.ExcelGenerator;
import com.example.demo.help.GeneratePdfReport;
import com.example.demo.help.notesFromExcel;
import com.example.demo.model.*;

import com.example.demo.openFeign.*;
import com.example.demo.repository.NoteElementRepository;
import com.example.demo.services.DeliberationRattrapageService;
import lombok.var;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class DeliberationRattrapageController {
    private DeliberationRattrapageService deliberationRattrapageService;
    private NoteElementRepository noteElementRepository;
    private OrdinaireRestClient ordinaireRestClient;
    private AnneeUniverRestClient anneeUniverRestClient;
    private FiliereRestClient filiereRestClient;
    private InscriptionAdminRestClient inscriptionAdminRestClient;
    private ModuleRestClient moduleRestClient;
    private ProfesseurRestClient professeurRestClient;
    private InscriptionPedagogiqueRestClient inscriptionPedagogiqueRestClient;

    public DeliberationRattrapageController(DeliberationRattrapageService deliberationRattrapageService, NoteElementRepository noteElementRepository,
                                            AnneeUniverRestClient anneeUniverRestClient, FiliereRestClient filiereRestClient, InscriptionAdminRestClient inscriptionAdminRestClient,
                                            ModuleRestClient moduleRestClient, ProfesseurRestClient professeurRestClient, OrdinaireRestClient ordinaireRestClient,
                                            InscriptionPedagogiqueRestClient inscriptionPedagogiqueRestClient) {
        this.deliberationRattrapageService = deliberationRattrapageService;
        this.noteElementRepository = noteElementRepository;
        this.anneeUniverRestClient = anneeUniverRestClient;
        this.filiereRestClient = filiereRestClient;
        this.inscriptionAdminRestClient = inscriptionAdminRestClient;
        this.moduleRestClient = moduleRestClient;
        this.professeurRestClient = professeurRestClient;
        this.ordinaireRestClient=ordinaireRestClient;
        this.inscriptionPedagogiqueRestClient = inscriptionPedagogiqueRestClient;

    }

    //Exporter un fichier excel contenant les etudiants d'un module ayant le résultat R
    @GetMapping("/export-Ratt-to-excel/{idElement}")
    public void exportIntoExcelFileRat(HttpServletResponse response, @PathVariable Long idElement) throws IOException {
        int annee = anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire();
        Module module = moduleRestClient.moduleElement(idElement);
        Element element = moduleRestClient.elementByID(idElement);
        Professeur professeur = professeurRestClient.getProfesseurById(element.getIdProf());
        Semestre semestre = filiereRestClient.getSemestresByid(module.getIdSemestre());
        Filiere filiere = filiereRestClient.getFiliereById(module.getIdFiliere());
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        int anneeSu = annee+1;
        String titre = filiere.getLibCourt()+"_"+semestre.getLibCourt()+"_"+element.getLibCourt()+"_"+annee+"_"+anneeSu+"Ratt";
        String headerValue = "attachment; filename=ExportNotes" + titre + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<Etudiant> listOfStudents = ordinaireRestClient.etudiantsRatt(idElement,annee);
        for (Etudiant et:listOfStudents){
            et.setNoteElementO(noteElementO(et.getId(), idElement));
        }
        System.out.println(filiere.getLibLong()+" "+module.getLibLong()+" "+semestre.getLibLong()+" "+element.getLibLong()+" "+"id Prof= "+module.getIdProf());
        ExcelGenerator generator = new ExcelGenerator(listOfStudents,filiere.getLibLong(),module.getLibLong(),semestre.getLibLong(),
                element.getLibLong(),professeur.getNomProf()+' '+professeur.getPrenomProf(),annee,idElement);
        generator.generateExcelFile(response);
    }
    public NoteElementO noteElementO(Long idEtudiant, Long idElement){
        return ordinaireRestClient.noteElementModule(idEtudiant,idElement);
    }


    // Importer les fichiers excels
    @PostMapping( "/ImportFile/{idElement}")
    public void importExcelFile(@RequestParam() MultipartFile file, @PathVariable  Long idElement) throws IOException {
        int annee = anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire();
        Element element = moduleRestClient.elementByID(idElement);
        Module module = moduleRestClient.moduleElement(idElement);

        try {
            List<NoteElementR> notesElement = notesFromExcel.noteModuleList(file,element,module, annee);
            notesElement.forEach(noteElement -> {
                Etudiant etudiant = inscriptionAdminRestClient.findEtudiantByCne(noteElement.getEtudiant().getCNE());
                NoteElementO noteElementO = noteElementO(etudiant.getId(), idElement);
                if (noteElementO.getNoteElement()> noteElement.getNoteElementR()){
                    noteElement.setNoteElementFinale(noteElementO.getNoteElement());
                } else {
                    noteElement.setNoteElementFinale(noteElement.getNoteElementR());
                }
                if (noteElement.getNoteElementFinale()>=element.getNoteMin()){
                    noteElement.setValidite(Resultat.V);
                } else {
                    noteElement.setValidite(Resultat.N);
                }
                noteElement.setEtudiant(etudiant);
                noteElement.setIdEtudiant(etudiant.getId());
            });
            noteElementRepository.saveAll(notesElement);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @GetMapping(value = "/pdfreport/{anneU}/{idElement}", produces = MediaType.APPLICATION_PDF_VALUE )
    public ResponseEntity<InputStreamResource> etudiantsReport(@PathVariable int anneU, @PathVariable Long idElement) throws DeliberationRattDefinedException{
        String titre = infoElement(idElement,anneU);
        var notesElement= noteElementRepository.findAllByIdElementAndAnneeU(idElement,anneU);
        if (!notesElement.isEmpty()){
            try {
                //  Block of code to try
                return getInputStreamResourceResponseEntity(notesElement, titre);

            }
            catch(Exception e) {
                //  Block of code to handle errors
            }

            return getInputStreamResourceResponseEntity(notesElement, titre);
        } else {
            throw new DeliberationRattDefinedException("Tous les étudiants ayant validés au session normale!!");
        }
    }

    private ResponseEntity<InputStreamResource> getInputStreamResourceResponseEntity(List<NoteElementR> notesElement,String titre) {
        notesElement.forEach(noteElement->{
            Etudiant etudiant = inscriptionAdminRestClient.etudiantInscAdminById(noteElement.getIdEtudiant()).get();
            Element element = moduleRestClient.elementByID(noteElement.getIdElement());
            Module module = moduleRestClient.moduleElement(element.getIdElement());
            element.setModule(module);
            element.setFiliere(filiereRestClient.getFiliereById(module.getIdFiliere()));
            element.setProfesseur(professeurRestClient.getProfesseurById(element.getIdProf()));
            element.setSemestre(filiereRestClient.getSemestresByid(module.getIdSemestre()));
            element.setAnneeUniversitaire(noteElement.getAnneeU());
            noteElement.setElement(element);
            noteElement.setEtudiant(etudiant);
        });
        ByteArrayInputStream bis = GeneratePdfReport.citiesReport(notesElement);

        var headers = new HttpHeaders();

        headers.add("Content-Disposition","inline; finelname="+titre+".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    public String infoElement(Long IdElement, int anneU) throws DeliberationRattDefinedException{
        Element element = moduleRestClient.elementByID(IdElement);
        Module module = moduleRestClient.moduleElement(IdElement);
        Semestre semestre = filiereRestClient.getSemestresByid(module.getIdSemestre());
        Filiere filiere = filiereRestClient.getFiliereById(module.getIdFiliere());
        if (element!=null && module!=null && semestre!=null && filiere != null){
            int anneeSu = anneU+1;
            return filiere.getLibCourt()+"_"+semestre.getLibCourt()+"_"+element.getLibCourt()+"_"+anneU+"_"+anneeSu+"Ratt";
        } else {
            throw new DeliberationRattDefinedException("L'element avec l'id = "+IdElement+" n'existe pas !!");
        }

    }

    @GetMapping(path = "/deliberationRatt/{idEtudiant}/{idElement}")
    public NoteElementR noteElementModule(@PathVariable Long idEtudiant, @PathVariable Long idElement) throws DeliberationRattDefinedException {
        Etudiant etudiant = inscriptionAdminRestClient.etudiantInscAdminById(idEtudiant).orElse(null);
        Module module = moduleRestClient.moduleElement(idElement);
        List<Etudiant> etudiants = inscriptionPedagogiqueRestClient.etudiantsInscritPedagModule(etudiant.getIdFiliere(),module.getIdModule(),anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
        NoteElementR noteElementModule = noteElementRepository.findNoteElementModulesByAnneeUAndIdEtudiantAndIdElement(anneeUniverRestClient.
                getAnneeCourante().getNomAnneeUniversitaire(),idEtudiant,idElement);
        if (etudiant!=null && module!=null){
            if(etudiants.contains(etudiant)){
                if(noteElementModule != null){
                    noteElementModule.setEtudiant(etudiant);
                    noteElementModule.setModule(moduleRestClient.moduleElement(idElement));
                    noteElementModule.setElement(moduleRestClient.elementByID(idElement));
                    return noteElementModule;
                } else {
                    throw new DeliberationRattDefinedException("Les notes de l'element avec L'Id="+idElement+" n'existent pas!!");
                }
            } else {
                throw new DeliberationRattDefinedException("L'étudiant avec L'Id="+idEtudiant+"n'existe pas parmi les étudiants inscrit pédagogiquement dans le module de cet element!!");
            }
        } else {
            throw new DeliberationRattDefinedException("L'étudiant avec L'Id="+idEtudiant+" n'existe pas parmi les etudiants inscrit administrativement!! ou/et l'element avec l'id= "+idElement+" n'existe pas");
        }
    }
    @GetMapping(path = "avoirTousLesNotesElement/{idElement}")
    public List<NoteElementR> avoirTousLesNotesElement(@PathVariable Long idElement){
        return noteElementRepository.findNoteElementRSByIdElementAndAnneeU(idElement,anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
    }
}
