package com.example.filiereservice.web;

import com.example.filiereservice.Exception.FiliereDefinedException;
import com.example.filiereservice.Feign.ModuleRestClient;
import com.example.filiereservice.entities.Etape;
import com.example.filiereservice.entities.Filiere;
import com.example.filiereservice.entities.Semestre;
import com.example.filiereservice.repositories.EtapeRepository;
import com.example.filiereservice.repositories.FiliereRepository;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class EtapeRestController {
    private EtapeRepository etapeRepository;
    private ModuleRestClient moduleRestClient;

    public EtapeRestController(EtapeRepository etapeRepository, ModuleRestClient moduleRestClient) {
        this.etapeRepository = etapeRepository;
        this.moduleRestClient = moduleRestClient;
    }

    @GetMapping(path="/etapes") //Tester
    public List<Etape> etapeList(){
        List<Etape> etapeList =etapeRepository.findAll();
        for (Etape etape:etapeList){
            Collection<Semestre> semestreCollection = etape.getSemestreCollection();
            for (Semestre semestre: semestreCollection){
                semestre.setModules(moduleRestClient.listeModulesSemestre(semestre.getIdSemestre()));
            }
        }
        return etapeList;
    }

    @GetMapping(path="/etapes/{id}")  //Tester
    public Etape etapeById(@PathVariable Long id){
        return etapeRepository.findById(id).get();
    }

    //Cette methode renvoi l'etape à partir de son numero et sa filiere
    //Ajouter le 08/01/2023
    @GetMapping(path = "/etape/filiere/{idFiliere}/{etape}")//tester
    public Etape etapeFiliere(@PathVariable Long idFiliere, @PathVariable int etape){
        return etapeRepository.findEtapeByFiliere_IdAndLibEtape(idFiliere,etape);
    }
    @GetMapping(path = "/semestres/etape/filiere/{idFiliere}/{etape}")//test
    public List<Semestre> semestresEtapeFiliere(@PathVariable Long idFiliere, @PathVariable int etape){
        return (List<Semestre>) etapeFiliere(idFiliere,etape).getSemestreCollection();
    }
    //fin de l'ajout
    @GetMapping(path="/etapeParNum/{num}") //Tester
    public Etape etapeByNb(@PathVariable int num){
       return etapeRepository.trouverEtapeParNom(num);
    }


    @PutMapping(path="/modifyEtape/{etapeID}")
    @ResponseBody
    public Etape modifyEtape(
            @RequestBody Etape etape,
            @PathVariable Long etapeID
    ) throws FiliereDefinedException {
        Etape oldEtape = etapeRepository.findById(etapeID).orElse(null);
        if(oldEtape != null){
            oldEtape.setLibEtape(etape.getLibEtape());
            oldEtape.setOI(etape.getOI());
            return etapeRepository.save(oldEtape);
        } else {
            throw new FiliereDefinedException("Cette etape n'existe pas");
        }
    }

    @DeleteMapping(path="etapes/{id}/deleteEtape")
    public void deleteEtape(@PathVariable Long id) throws FiliereDefinedException {
        boolean elemExist = etapeRepository.existsById(id);
        if (!elemExist){
            throw new FiliereDefinedException("Element with id "+id+"not found");
        }
        etapeRepository.deleteById(id);

    }
}
