package com.example.demo.Web;

import com.example.demo.Entities.*;
import com.example.demo.Enumeration.Heure;
import com.example.demo.Enumeration.Jour;
import com.example.demo.Feign.ElementRestClient;
import com.example.demo.Feign.ProfesseurRestClient;
import com.example.demo.Model.ElementM;
import com.example.demo.Model.Professeur;
import com.example.demo.Repositories.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.demo.Enumeration.Heure.*;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class EmploiProfController {
    private EmploiRepository emploiRepository;
    private EmploiProfRepository emploiProfRepository;
    private SeanceRepository seanceRepository;
    private ElementRestClient elementRestClient;
    private ProfesseurRestClient professeurRestClient;
    private JoursProfRepository joursProfRepository;
    private JoursRepository joursRepository;
    private SalleRepository salleRepository;
    private EmploiSalleRepository emploiSalleRepository;

    public EmploiProfController(EmploiSalleRepository emploiSalleRepository, SalleRepository salleRepository, JoursRepository joursRepository, JoursProfRepository joursProfRepository, EmploiProfRepository emploiProfRepository, EmploiRepository emploiRepository, SeanceRepository seanceRepository, ElementRestClient elementRestClient, ProfesseurRestClient professeurRestClient) {
        this.elementRestClient = elementRestClient;
        this.emploiRepository = emploiRepository;
        this.seanceRepository = seanceRepository;
        this.professeurRestClient = professeurRestClient;
        this.emploiProfRepository = emploiProfRepository;
        this.joursProfRepository = joursProfRepository;
        this.joursRepository = joursRepository;
        this.salleRepository = salleRepository;
        this.emploiSalleRepository = emploiSalleRepository;
    }

    @GetMapping(path = "/autoEmploiProf") //TESTER
    public void creatAllEmploiProf() throws IOException {
        emploiProfRepository.deleteAll();

        List<Professeur> professeurs = professeurRestClient.listeProfesseurs();
        for (Professeur prof : professeurs) {
            EmploiProf emploiProfexistant = emploiProfRepository.findByIdProf(prof.getIdProf());
            EmploiProf emploiprof = new EmploiProf();
            emploiprof.setIdProf(prof.getIdProf());
            emploiprof.setNomProf(prof.getNomProf());
            emploiprof.setPrenomProf(prof.getPrenomProf());

            List<ElementM> elements = professeurRestClient.listeElementProfesseur(prof.getIdProf());

            Jour[] jourenum = {Jour.Lundi, Jour.Mardi, Jour.Mercredi, Jour.Jeudi, Jour.Vendredi, Jour.Samedi};

            List<JoursProf> listjoursProfs = new ArrayList<>();

            for (Jour jr : jourenum) {
                List<Emploi> emplois = emploiRepository.findAll();
                List<SeanceProf> seanceProfs = new ArrayList<>();
                JoursProf joursProf = new JoursProf();
                joursProf.setJour(jr);
                for (Emploi emploi : emplois) {
                    Jours jour = joursRepository.findByEmploiAndJour(emploi, jr);

                    if (jour != null) {
                        List<Seance> seances = jour.getSeances();
                        for (ElementM elementM : elements) {
                            for (Seance seance : seances) {
                                if (seance.getIdModule() == (elementM.getIdElement())) {
                                    SeanceProf seanceprof = new SeanceProf();
                                    seanceprof.setIdModule(elementM.getIdElement());
                                    seanceprof.setNomModule(elementM.getLibLong());
                                    seanceprof.setHeure(seance.getHeure());
                                    seanceprof.setJoursProf(joursProf);
                                    seanceProfs.add(seanceprof);
                                    System.out.println("*********************************************");
                                    System.out.println(seanceProfs);
                                }
                            }
                        }
                    }
                }
                joursProf.setEmploiProf(emploiprof);
                joursProf.setSeanceProfs(seanceProfs);
                listjoursProfs.add(joursProf);
            }
            emploiprof.setJoursProfs(listjoursProfs);
            emploiProfRepository.save(emploiprof);
        }
    }

    @GetMapping(path = "/emploiProfs")
    public List<EmploiProf> getAllEmploiProf() {
        return emploiProfRepository.findAll();
    }

    @GetMapping(path = "/emploiProfsByIdProf/{idProf}")
    public List<JoursProf> getAllEmploiProf(@PathVariable Long idProf) {
        List<JoursProf> list = emploiProfRepository.findByIdProf(idProf).getJoursProfs();
        for (JoursProf joursProf:list){
            ArrayList<SeanceProf> nouvellelist = new ArrayList<>();
            List<SeanceProf> list1 = joursProf.getSeanceProfs();
            Heure[] monTableau = {H8, H10, H14, H16};
            for (int j=0;j<4;j++){

                for (int i=0; i<list1.size();i++){ //contient h8
                    if (list1.get(i).getHeure()==monTableau[j]){   /// (14,8,16);
                        nouvellelist.add(list1.get(i));
                    }
                }
                if (nouvellelist.size()==j){
                    SeanceProf seanceProf = new SeanceProf();
                    seanceProf.setId(0L);
                    seanceProf.setId(0L);
                    seanceProf.setNomModule(" ");
                    seanceProf.setHeure(monTableau[j]);
                    nouvellelist.add(seanceProf);

                }

            }
            joursProf.setSeanceProfs(nouvellelist);
            }
        return list;
    }


    @DeleteMapping(path ="/deleteemploiprof/{id}" )
    public  void  deleteemploiProfbyID(@PathVariable Long id){

        emploiProfRepository.deleteById(id);
    }
    @DeleteMapping(path = "/deleteallemploiprof")
    public void  deleteAllEmploiprof(){
        emploiProfRepository.deleteAll();
    }
}
