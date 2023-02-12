package com.example.demo.Web;

import com.example.demo.Entities.*;
import com.example.demo.Enumeration.Heure;
import com.example.demo.Enumeration.Jour;
import com.example.demo.Feign.ElementRestClient;
import com.example.demo.Feign.ProfesseurRestClient;
import com.example.demo.Repositories.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Enumeration.Heure.*;
import static com.example.demo.Enumeration.Heure.H16;

@RestController
@CrossOrigin(origins="http://localhost:4200")

public class SalleController {
    private EmploiRepository emploiRepository;
    private EmploiProfRepository emploiProfRepository;
    private SeanceRepository seanceRepository;
    private ElementRestClient elementRestClient;
    private ProfesseurRestClient professeurRestClient;
    private JoursProfRepository joursProfRepository;
    private JoursRepository joursRepository;
    private SalleRepository salleRepository;
    private EmploiSalleRepository emploiSalleRepository;
    private SeanceSalleRepository seanceSalleRepository;

    public SalleController(SeanceSalleRepository seanceSalleRepository,EmploiSalleRepository emploiSalleRepository,SalleRepository salleRepository,JoursRepository joursRepository,JoursProfRepository joursProfRepository,EmploiProfRepository emploiProfRepository, EmploiRepository emploiRepository, SeanceRepository seanceRepository, ElementRestClient elementRestClient,  ProfesseurRestClient professeurRestClient){
        this.elementRestClient=elementRestClient;
        this.emploiRepository = emploiRepository;
        this.seanceRepository = seanceRepository;
        this.professeurRestClient=professeurRestClient;
        this.emploiProfRepository=emploiProfRepository;
        this.joursProfRepository = joursProfRepository;
        this.joursRepository = joursRepository;
        this.salleRepository=salleRepository;
        this.emploiSalleRepository=emploiSalleRepository;
        this.seanceSalleRepository= seanceSalleRepository;
    }

    @GetMapping(path = "/salles")
    public List<Salle> findallsalles(){

        return salleRepository.findAll();
    }
    @PostMapping(path = "/addSalle")
    public Salle saveSalle(@RequestBody Salle e){

       return salleRepository.save(e);
    }
    @GetMapping(path = "/emploiSalle")
    public List<EmploiSalle> getAllEmploiSalle(){
        return emploiSalleRepository.findAll();
    }
    @GetMapping(path = "/emploiSallesByIdSalle/{idSalle}")
    public List<JoursSalle> getEmploiSalle(@PathVariable Long idSalle) {
        EmploiSalle emploiSalle = emploiSalleRepository.findByIdSalle(idSalle);
        List<JoursSalle> list = emploiSalleRepository.findById(emploiSalle.getId()).get().getJoursSalles();
        for (JoursSalle joursSalle:list){
            ArrayList<SeanceSalle> nouvellelist = new ArrayList<>();
            List<SeanceSalle> list1 = joursSalle.getSeanceSalles();
            Heure[] monTableau = {H8, H10, H14, H16};
            for (int j=0;j<4;j++){
                for (int i=0; i<list1.size();i++){ //contient h8
                    if (list1.get(i).getHeure()==monTableau[j]){   /// (14,8,16);
                        nouvellelist.add(list1.get(i));
                    }
                }
                if (nouvellelist.size()==j){
                    SeanceSalle seanceSalle = new SeanceSalle();
                    seanceSalle.setId(0L);
                    seanceSalle.setId(0L);
                    seanceSalle.setNomModule(" ");
                    seanceSalle.setHeure(monTableau[j]);
                    nouvellelist.add(seanceSalle);

                }

            }
            joursSalle.setSeanceSalles(nouvellelist);
        }
        return list;
    }


    @DeleteMapping(path ="/deletesalle/{id}" )
    public  void  deletesallebyID(@PathVariable Long id){
        salleRepository.deleteById(id);
    }

    @PutMapping(path = "/updateSalle/{id}")
    public Salle updateSalle(@RequestBody Salle s , @PathVariable Long id){
        Salle salle = salleRepository.findSalleById(id);

            salle.setDescription(s.getDescription());
            salle.setLibsalle(s.getLibsalle());
        System.out.println(salle.getSeances()!=null);
        if(salle.getSeances()!=null){
            salle.setSeances(s.getSeances());

        }


       return salleRepository.save(salle);

    }

    @GetMapping(path="/autoEmploiSalle") //TESTER
    public void creatAllEmploiSalle() throws IOException {
        emploiSalleRepository.deleteAll();

        List<Salle> salles = salleRepository.findAll();
        for (Salle salle : salles) {
            EmploiSalle emploiSalle = new EmploiSalle();
            emploiSalle.setIdSalle(salle.getId());
            emploiSalle.setLibSalle(salle.getLibsalle());

            Jour[] jourenum = {Jour.Lundi,Jour.Mardi,Jour.Mercredi,Jour.Jeudi,Jour.Vendredi,Jour.Samedi};

            List<JoursSalle> listjoursSalle = new ArrayList<>();

            for (Jour jr : jourenum) {
                List<Emploi> emplois = emploiRepository.findAll();
                List<SeanceSalle> seanceSalles = new ArrayList<>();
                JoursSalle joursSalle = new JoursSalle();
                joursSalle.setJour(jr);
                for (Emploi emploi : emplois) {
                    Jours jour = joursRepository.findByEmploiAndJour(emploi, jr);
                    if (jour != null){

                        List<Seance> seances = jour.getSeances();

                        List<Seance> seancessalles = salle.getSeances();

                        for (Seance seance : seances) {
                            for (Seance seanceSallee : seancessalles) {
                                System.out.println("******************seance.getIdSeance()********************");
                                System.out.println(seance.getIdSeance());
                                System.out.println("******************seanceSallee.getIdSeance()********************");
                                System.out.println(seanceSallee.getIdSeance());

                                if (seance.getIdSeance().equals(seanceSallee.getIdSeance())){
                                    System.out.println("youssef l7mar");


                                    SeanceSalle seanceSalle = new SeanceSalle();
                                    seanceSalle.setIdModule(seance.getIdModule());
                                    seanceSalle.setNomModule(seance.getNomModule());
                                    seanceSalle.setHeure(seance.getHeure());
                                    seanceSalle.setJoursSalle(joursSalle);
                                    seanceSalles.add(seanceSalle);
                                }
                            }
                        }

                    }
                }
                joursSalle.setEmploiSalle(emploiSalle);
                joursSalle.setSeanceSalles(seanceSalles);
                listjoursSalle.add(joursSalle);
            }
            emploiSalle.setJoursSalles(listjoursSalle);
            emploiSalleRepository.save(emploiSalle);


        }




    }
    @GetMapping(path = "/SalleLibre/{jour}/{heure}")
    List<Salle> getFreeSalle(@PathVariable Jour jour ,@PathVariable Heure heure){
        List<EmploiSalle> emploiSalles = emploiSalleRepository.findAll();
        List<Long> idSalleLibre=new ArrayList<>();
        List<Salle> sallesLibre = new ArrayList<>();

        for (EmploiSalle emploiSalle : emploiSalles){
            Long idSalle = emploiSalle.getIdSalle();
            List<JoursSalle> joursSalles =  emploiSalle.getJoursSalles();
            for (JoursSalle joursSalle : joursSalles){
                if (joursSalle.getJour()==jour){
                    List<SeanceSalle> seanceSalles = joursSalle.getSeanceSalles();
                    List<Heure> heures = new ArrayList<>();

                    if (seanceSalles.isEmpty()){
                        idSalleLibre.add(idSalle);

                    }
                    else {
                        for (SeanceSalle seanceSalle: seanceSalles){
                            heures.add(seanceSalle.getHeure());

                        }
                        if (!(heures.contains(heure))){
                            idSalleLibre.add(idSalle);

                        }
                    }
                }
            }

        }
        for (Long id : idSalleLibre){
            sallesLibre.add(salleRepository.findSalleById(id));
        }

        return  sallesLibre;

    }
    @GetMapping(path = "/SalleLibre/{jour}/{heure1}/{heure2}")
    List<Salle> getFreeSalle2(@PathVariable Jour jour ,@PathVariable Heure heure1, @PathVariable Heure heure2){
        List<Salle> sallesLibre1 = getFreeSalle(jour,heure1);
        List<Salle> sallesLibre2 = getFreeSalle(jour,heure2);
        ArrayList<Salle> sallesLibre = new ArrayList<>();
        for (Salle salle:sallesLibre1){
            for (Salle salle1:sallesLibre2){
                if (salle.getId()==salle1.getId()){
                    sallesLibre.add(salle);
                }
            }
        }
        return  sallesLibre;
    }

    @DeleteMapping(path = "/deleteallemploisalle")
    public void  deleteAllEmploiSalle(){
        emploiSalleRepository.deleteAll();
    }


}
