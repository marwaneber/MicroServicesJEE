package com.example.servicedeliberationordinaire.Web;

import com.example.servicedeliberationordinaire.Entities.NoteElementModule;
import com.example.servicedeliberationordinaire.Enumeration.Resultat;
import com.example.servicedeliberationordinaire.Exception.DeliberationOrdinareDefinedException;
import com.example.servicedeliberationordinaire.Model.*;
import com.example.servicedeliberationordinaire.OpenFeign.*;
import com.example.servicedeliberationordinaire.Repository.NoteElementRepository;
import com.example.servicedeliberationordinaire.help.ExcelGenerator;
import com.example.servicedeliberationordinaire.help.GeneratePdfReport;
import com.example.servicedeliberationordinaire.help.notesFromExcel;
//import lombok.var;
import lombok.var;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class NoteElementController {
    private NoteElementRepository noteElementRepository;
    private ModuleRestClient moduleRestClient;
    private FiliereRestClient filiereRestClient;
    private ProfesseurRestClient professeurRestClient;
    private AnneeUniverRestClient anneeUniverRestClient;
    private InscriptionAdminRestClient inscriptionAdminRestClient;


    private InscriptionPedagogiqueRestClient inscriptionPedagogiqueRestClient;

    public NoteElementController(NoteElementRepository noteElementRepository,
                                 InscriptionPedagogiqueRestClient inscriptionPedagogiqueRestClient, ModuleRestClient moduleRestClient,
                                 FiliereRestClient filiereRestClient,
                                 ProfesseurRestClient professeurRestClient,
                                 AnneeUniverRestClient anneeUniverRestClient,
                                 InscriptionAdminRestClient inscriptionAdminRestClient

                                 /***EtudiantRestClient etudiantRestClient, ModuleRestClient moduleRestClient**/) {
        this.noteElementRepository = noteElementRepository;
        this.moduleRestClient = moduleRestClient;
        this.inscriptionPedagogiqueRestClient=inscriptionPedagogiqueRestClient;
        this.filiereRestClient=filiereRestClient;
        this.professeurRestClient=professeurRestClient;
        this.anneeUniverRestClient = anneeUniverRestClient;
        this.inscriptionAdminRestClient = inscriptionAdminRestClient;
    }

    //Un api de recuperation de note ordinaire d'un etudiant
    @GetMapping("/Note/{cne}/{idSemestre}/{idModule}/{idElement}/{anneeU}")
    NoteElementModule getNoteByLadiff(@PathVariable String cne,@PathVariable Long idSemestre,@PathVariable Long idModule,@PathVariable Long idElement, @PathVariable int anneeU){
        System.out.println(cne+idModule+idElement+anneeU);
        String ladiff = cne+idSemestre+idModule+idElement+anneeU;
        return noteElementRepository.findByLadiff(ladiff);

    }


    // Generer un fichier excel a remplir les notes d'un element // C'est fait
    @GetMapping("/export-to-excel/{idElement}")
    public void exportIntoExcelFile(HttpServletResponse response, @PathVariable Long idElement) throws IOException {
        int annee = anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire();
        Module module = moduleRestClient.moduleElement(idElement);
        Element element = moduleRestClient.elementByID(idElement);
        Professeur professeur = professeurRestClient.getProfesseurById(element.getIdProf());
        Semestre semestre = filiereRestClient.getSemestresByid(module.getIdSemestre());
        Filiere filiere = filiereRestClient.getFiliereById(module.getIdFiliere());
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        int anneeSu = annee+1;
        String titre = filiere.getLibCourt()+"_"+semestre.getLibCourt()+"_"+element.getLibCourt()+"_"+annee+"_"+anneeSu+"Norm";
        String headerValue = "attachment; filename=ExportNotes" + titre + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<Etudiant> listOfStudents = inscriptionPedagogiqueRestClient.etudiantsInscritPedagModule(filiere.getId(), module.getIdModule(), annee);
        System.out.println(filiere.getLibLong()+" "+module.getLibLong()+" "+semestre.getLibLong()+" "+element.getLibLong()+" "+"id Prof= "+module.getIdProf());
        ExcelGenerator generator = new ExcelGenerator(listOfStudents,filiere.getLibLong(),module.getLibLong(),semestre.getLibLong(),
                element.getLibLong(),professeur.getNomProf()+' '+professeur.getPrenomProf(),annee);
        generator.generateExcelFile(response);
    }



    // Importer les notes des elements et les enregistrer a partir d'un ficher excel
    @PostMapping( "/ImportFile/{idElement}")
    public void importExcelFile(@RequestParam() MultipartFile file, @PathVariable  Long idElement) throws IOException {
        int annee = anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire();
        Element element = moduleRestClient.elementByID(idElement);
        Module module = moduleRestClient.moduleElement(idElement);
        try {
        List<NoteElementModule> notesElement = notesFromExcel.noteModuleList(file,element,module, annee);
        notesElement.forEach(noteElement -> {
            Etudiant etudiant = inscriptionAdminRestClient.findEtudiantByCne(noteElement.getEtudiant().getCNE());
            noteElement.setEtudiant(etudiant);
            noteElement.setIdEtudiant(etudiant.getId());
        });
        noteElementRepository.saveAll(notesElement);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Recuperer les notes d'un element de module bien defini pour un etudiant bien defini
    @GetMapping(path = "/deliberationordin/{idEtudiant}/{idElement}")
    public NoteElementModule noteElementModule(@PathVariable Long idEtudiant, @PathVariable Long idElement) throws DeliberationOrdinareDefinedException {
        Etudiant etudiant = inscriptionAdminRestClient.etudiantInscAdminById(idEtudiant).orElse(null);
        Module module = moduleRestClient.moduleElement(idElement);
        List<Etudiant> etudiants = inscriptionPedagogiqueRestClient.etudiantsInscritPedagModule(etudiant.getIdFiliere(), module.getIdModule(),anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
        NoteElementModule noteElementModule = noteElementRepository.findNoteElementModulesByAnneeUAndIdEtudiantAndIdElement(anneeUniverRestClient.
                getAnneeCourante().getNomAnneeUniversitaire(),idEtudiant,idElement);
        if (etudiant!=null){
            if(etudiants.contains(etudiant)){
                if(noteElementModule != null){
                    noteElementModule.setEtudiant(etudiant);
                    noteElementModule.setModule(moduleRestClient.moduleElement(idElement));
                    noteElementModule.setElement(moduleRestClient.elementByID(idElement));
                return noteElementModule;
            } else {
                throw new DeliberationOrdinareDefinedException("Les notes de l'element avec L'Id="+idElement+" n'existent pas!!");
            }
        } else {
            throw new DeliberationOrdinareDefinedException("L'étudiant avec L'Id="+idEtudiant+"n'existe pas parmi les étudiants inscrit pédagogiquement dans le module de cet element!!");
        }
        } else {
            throw new DeliberationOrdinareDefinedException("L'étudiant avec L'Id="+idEtudiant+"n'existe pas parmi les etudiants inscrit administrativement!!");
        }
    }
    //Avoir tous les notes des element
    @GetMapping(path="/avoirTousLesNotes")
    public List<NoteElementModule> noteElementModuleList(){
        List<NoteElementModule> list =noteElementRepository.findAll();
        for (NoteElementModule note:list){
            note.setElement(moduleRestClient.elementByID(note.getIdElement()));
            note.setEtudiant(inscriptionAdminRestClient.etudiantInscAdminById(note.getIdEtudiant()).get());
            note.setModule(moduleRestClient.getModuleById(note.getIdModule()));
        }
        return list;
    }

    @GetMapping("/listDesEtudiantsRatt/{idElement}/{annee}")
    public List<Etudiant> etudiantsRatt(@PathVariable Long idElement, @PathVariable int annee){
        List<NoteElementModule> notes = noteElementRepository.findNoteElementModulesByAnneeUAndValideteAndIdElement(annee, Resultat.R,idElement);
        List<Etudiant> list1 = Collections.emptyList();
        List list2 = new ArrayList<>(list1);
        for (NoteElementModule n:notes){
            Long idEtudiant = n.getIdEtudiant();
            list2.add(inscriptionAdminRestClient.etudiantInscAdminById(idEtudiant));
        }
        return list2;
    }
    //Gener un fichier pdf des notes d'un element
    @GetMapping(value = "/pdfreport/{anneU}/{idElement}", produces = MediaType.APPLICATION_PDF_VALUE )
    public ResponseEntity<InputStreamResource> etudiantsReport(@PathVariable int anneU, @PathVariable Long idElement){
        String titre = infoElement(idElement,anneU);
        List<NoteElementModule> notesElement = noteElementRepository.findAllByAnneeUAndAndIdElement(anneU,idElement);
//        System.out.println(notesElement);
//        var notesElement= noteElementRepository.findAllByAnneeUAndAndIdElement(anneU,idElement);
        try {
            //  Block of code to try
            return getInputStreamResourceResponseEntity(notesElement, titre);
        }
        catch(Exception e) {
            //  Block of code to handle errors
        }

        return getInputStreamResourceResponseEntity(notesElement, titre);
    }

    private ResponseEntity<InputStreamResource> getInputStreamResourceResponseEntity(List<NoteElementModule> notesElement,String titre) {
        notesElement.forEach(noteElement->{
//            System.out.println(noteElement);
            Etudiant etudiant = inscriptionAdminRestClient.etudiantInscAdminById(noteElement.getIdEtudiant()).get();
//            Etudiant etudiant = inscriptionAdminRestClient.findEtudiantByCne(noteElement.getEtudiant().getCNE());
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
    @GetMapping(path = "/listElementByAnneIdElement/{idElement}/{annee}")
    public List<NoteElementModule> listElementByAnneIdElement(@PathVariable Long idElement, @PathVariable int annee){
        return noteElementRepository.findAllByAnneeUAndAndIdElement(annee,idElement);
    }

    public String infoElement(Long IdElement, int anneU){
        Element element = moduleRestClient.elementByID(IdElement);
        Module module = moduleRestClient.moduleElement(IdElement);
        Semestre semestre = filiereRestClient.getSemestresByid(module.getIdSemestre());
        Filiere filiere = filiereRestClient.getFiliereById(module.getIdFiliere());
        int anneeSu = anneU+1;
        return filiere.getLibCourt()+"_"+semestre.getLibCourt()+"_"+element.getLibCourt()+"_"+anneU+"_"+anneeSu+"Ratt";
    }

    @GetMapping(path = "avoirTousLesNotesElement/{idElement}")
    public List<NoteElementModule> avoirTousLesNotesElement(@PathVariable Long idElement){
        return noteElementRepository.findNoteElementModulesByIdElementAndAnneeU(idElement,anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
    }
}

