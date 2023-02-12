package com.example.demo.Web;

import com.example.demo.Entities.*;
import com.example.demo.Feign.ElementRestClient;
import com.example.demo.Feign.FiliereRestClient;
import com.example.demo.Model.Filiere;
import com.example.demo.Model.Module;
import com.example.demo.Model.Semestre;
import com.example.demo.Repositories.EmploiProfRepository;
import com.example.demo.Repositories.EmploiRepository;
import com.example.demo.Repositories.SalleRepository;
import com.example.demo.Repositories.SeanceRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class EmploiController {
    private EmploiProfController emploiProfController;
    private EmploiRepository emploiRepository;
    private SeanceRepository seanceRepository;
    private ElementRestClient elementRestClient;
    private EmploiProfRepository emploiProfRepository;
    private FiliereRestClient filiereRestClient;
    private SalleRepository salleRepository;
    private SalleController salleController;
    public EmploiController(SalleController salleController,EmploiProfController emploiProfController,SalleRepository salleRepository,FiliereRestClient filiereRestClient,EmploiProfRepository emploiProfRepository, EmploiRepository emploiRepository, SeanceRepository seanceRepository, ElementRestClient elementRestClient) {
        this.elementRestClient = elementRestClient;
        this.emploiRepository = emploiRepository;
        this.seanceRepository = seanceRepository;
        this.emploiProfRepository = emploiProfRepository;
        this.filiereRestClient=filiereRestClient;
        this.salleRepository=salleRepository;
        this.emploiProfController=emploiProfController;
        this.salleController = salleController;
    }

    @GetMapping(path = "/emplois") //TESTER
    public List<Emploi> getAllEmploi() {
        List<Emploi> emploiList = emploiRepository.findAll();
//        for (Emploi emploi : emploiList) {
//            List <Jours> jours =  emploi.getJours();
//            for (Jours jour : jours) {
//                List <Seance> seances = jour.getSeances();
//                for (Seance seance: seances){
//                   if (seance.getNomModule() == null){
//                        seance.setNomModule(elementRestClient.elementByID(seance.getIdModule()).getLibLong());
//
//                       Salle salle = salleRepository.findById(seance.getIdSalle()).orElse(null);
//                        seance.setNomSalle(salle.getLibsalle());
//
//
//                    Long elementid = seance.getIdModule();
//
//                    Module module = elementRestClient.getmodulebyElement(elementid);
//                    Semestre semestre = filiereRestClient.getSemestresByid(module.getIdSemestre());
//                    Filiere filiere = filiereRestClient.getFiliereById(module.getIdFiliere()).orElse(null);
//                    emploi.setIdSemestre(semestre.getIdSemestre());
//                    emploi.setNomSemestre(semestre.getLibCourt());
//                    emploi.setIdFiliere(filiere.getId());
//                    emploi.setNomFiliere(filiere.getLibCourt());
//
//
//
//
//                   }

//                }



//            }

//            emploiRepository.save(emploi);

//        }

        return emploiList;

    }

    @GetMapping(path="/emploisProf") //TESTER
    public List<EmploiProf> getAllEmploiprof(){
        List<EmploiProf> emploiprofList = emploiProfRepository.findAll();
        return emploiprofList;
    }
//
//
//    @GetMapping(path = "/emploiSemestre/{id}")
//    public Emploi emploiSemestre(@PathVariable Long id){
//        return emploiRepository.findByIdSemestre(id);
//    }
//
//
   @PostMapping("/addemplois")
   @ResponseBody
   public void save(@RequestBody Emploi e ) throws IOException {

        Emploi emploi = new Emploi();
       List<Jours> jours = e.getJours();
       for (Jours jour: jours)
       {
           jour.setEmploi(emploi);
           List<Seance> seances = jour.getSeances();


           for (Seance seance : seances){
               seance.setNomModule(elementRestClient.elementByID(seance.getIdModule()).getLibLong());

               Salle salle = salleRepository.findById(seance.getIdSalle()).orElse(null);
               seance.setNomSalle(salle.getLibsalle());
               seance.setSalle(salle);



               Long elementid = seance.getIdModule();

               Module module = elementRestClient.getmodulebyElement(elementid);
               Semestre semestre = filiereRestClient.getSemestresByid(module.getIdSemestre());
               Filiere filiere = filiereRestClient.getFiliereById(module.getIdFiliere()).orElse(null);
               emploi.setIdSemestre(semestre.getIdSemestre());
               emploi.setNomSemestre(semestre.getLibCourt());
               emploi.setIdFiliere(filiere.getId());
               emploi.setNomFiliere(filiere.getLibCourt());

               seance.setJours(jour);
           }
       }
        emploi.setJours(jours);

       emploiRepository.save(emploi);
       emploiProfController.creatAllEmploiProf();
       salleController.creatAllEmploiSalle();


   }

    @DeleteMapping(path ="/deleteemploi/{id}" )
    public  void  deleteemploibyID(@PathVariable Long id) throws IOException {
        emploiRepository.deleteById(id);
        emploiProfController.creatAllEmploiProf();
        salleController.creatAllEmploiSalle();


    }



    @PutMapping(path = "/updateEmploi/{id}")
    public void updateEmploi(@RequestBody Emploi e , @PathVariable Long id, HttpServletResponse httpServletResponse) throws IOException {


        Emploi emploi = emploiRepository.findById(id).orElse(null) ;

        List<Jours> jours = e.getJours();
        for (Jours jour: jours)
        {
            jour.setEmploi(emploi);
            List<Seance> seances = jour.getSeances();


            for (Seance seance : seances){
                seance.setNomModule(elementRestClient.elementByID(seance.getIdModule()).getLibLong());

                Salle salle = salleRepository.findById(seance.getIdSalle()).orElse(null);
                seance.setNomSalle(salle.getLibsalle());
                seance.setSalle(salle);



                Long elementid = seance.getIdModule();

                Module module = elementRestClient.getmodulebyElement(elementid);
                Semestre semestre = filiereRestClient.getSemestresByid(module.getIdSemestre());
                Filiere filiere = filiereRestClient.getFiliereById(module.getIdFiliere()).orElse(null);
                emploi.setIdSemestre(semestre.getIdSemestre());
                emploi.setNomSemestre(semestre.getLibCourt());
                emploi.setIdFiliere(filiere.getId());
                emploi.setNomFiliere(filiere.getLibCourt());

                seance.setJours(jour);
            }
        }
        emploi.setJours(jours);

         emploiRepository.save(emploi);
        emploiProfController.creatAllEmploiProf();
        salleController.creatAllEmploiSalle();




    }


    }
