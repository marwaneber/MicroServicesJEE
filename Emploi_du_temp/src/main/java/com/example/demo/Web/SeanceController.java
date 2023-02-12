package com.example.demo.Web;


import com.example.demo.Entities.*;
import com.example.demo.Enumeration.Heure;

import com.example.demo.Repositories.EmploiRepository;
import com.example.demo.Repositories.SeanceRepository;
import com.example.demo.Repositories.SeanceSalleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Enumeration.Heure.*;
import static com.example.demo.Enumeration.Heure.H16;

@RestController
@CrossOrigin(origins="http://localhost:4200")

    public class SeanceController {
    private SeanceRepository seanceRepository;
    private EmploiRepository emploiRepository;
    private SeanceSalleRepository seanceSalleRepository;
    public SeanceController(SeanceSalleRepository seanceSalleRepository,SeanceRepository seanceRepository, EmploiRepository emploiRepository) {
        this.seanceRepository = seanceRepository;
        this.emploiRepository = emploiRepository;
    this.seanceSalleRepository= seanceSalleRepository;}

    @GetMapping(path = "/list/{id}")
    public List<Jours> listseances(@PathVariable Long id){
        List<Jours> list = seanceRepository.findByEmpoli(id);
        for (Jours jours:list){
            ArrayList<Seance> novelist = new ArrayList<>();
            List<Seance> list1 = jours.getSeances();
            Heure[] monTableau = {H8, H10, H14, H16};
            for (int j=0;j<4;j++){
                for (int i=0; i<list1.size();i++){
                    if (list1.get(i).getHeure()==monTableau[j]){
                        novelist.add(list1.get(i));
                    }
                }
                if (novelist.size()==j){

                    Seance seance = new Seance();
                    seance.setIdSeance(0L);
                    seance.setIdModule(0L);
                    seance.setNomModule(" ");
                    seance.setNomSalle(" ");
                    seance.setHeure(monTableau[j]);
                    novelist.add(seance);
                }
            }
            jours.setSeances(novelist);
        }
        return list;
    }

//    @PostMapping(path = "/add/{id_semestre}")
//    public Emploi save(@RequestBody Jours jours, Long id_semestre){
//        Emploi emploi= emploiRepository.save(new Emploi(null,null,id_semestre,null));
//        jours.setEmploi(emploi);
//        seanceRepository.save(jours);
//        return emploi;
//
//    }


//    @PostMapping(path = "/addlist/{id_semestre}")
//    public List<Jours> savelist(@RequestBody List<Jours> seance, Long id_semestre){
//        Emploi emploi= emploiRepository.save(new Emploi(null,null,id_semestre,null));
//        for (Jours jours :seance){
//            jours.setEmploi(emploi);
//            seanceRepository.save(jours);
//        }
//        return seance;
//    }





//    @PutMapping(path = "/update/{id}/{id_semestre}")
//    public void update(@PathVariable Long id, @RequestBody List<Jours> jours, Long id_semestre){
//        List <Jours> seances1= seanceRepository.findByEmpoli(id);
//        for (Jours jour: jours){
//            for (Jours jours1 :seances1){
//                if (jour.getJour()== jours1.getJour()){
//                    Long i= jours1.getId();
//                    jour.setId(i);
//                    Emploi e=new Emploi(id,null,id_semestre,null);
//                    jour.setEmploi(e);
//                    seanceRepository.save(jour);
//                }
//            }
//        }
//    }

    @GetMapping(path = "/seance/{id}")
    Seance getSeanceByid(@PathVariable Long id ){

        return seanceRepository.findByIdSeance(id);
    }

    @GetMapping(path = "/seanceSalle/{id}/{heure}")
    SeanceSalle getSeanceSalleByIdAndHeure(@PathVariable Long id, @PathVariable Heure heure){
        System.out.println(seanceSalleRepository.findByIdAndHeure(id,heure));
     return seanceSalleRepository.findByIdAndHeure(id,heure);
    }



}
