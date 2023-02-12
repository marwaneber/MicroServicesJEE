package com.example.anneeuniversitaireservice.web;

import com.example.anneeuniversitaireservice.Exception.AuDefinedException;
import com.example.anneeuniversitaireservice.Repositories.AnneeuniversitaireRepository;
import com.example.anneeuniversitaireservice.entities.AnneeUniversitaire;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class AnneUniversitaireRestController {
    private AnneeuniversitaireRepository anneeuniversitaireRepository;
    private AnneUniversitaireRestController(AnneeuniversitaireRepository anneeuniversitaireRepository){
        this.anneeuniversitaireRepository = anneeuniversitaireRepository;
    }
    @GetMapping(path="/AUs") //Tester
    public List<AnneeUniversitaire> getAllAus(){
        return anneeuniversitaireRepository.findAll();
    }

    @PostMapping(path = "/addAU") //Tester
    @ResponseBody
    public List<AnneeUniversitaire> addAU(@RequestBody AnneeUniversitaire anneeUniversitaire) throws AuDefinedException{
        int nom = anneeUniversitaire.getNomAnneeUniversitaire();
        boolean isCourant = anneeUniversitaire.getIsCourant();
        AnneeUniversitaire anneeUniversitaire1 = anneeuniversitaireRepository.findByNomAnneeUniversitaire(nom).orElse(null);
        if (anneeUniversitaire1 == null){
            if (isCourant){
                List<AnneeUniversitaire> anneeUniversitaires = getAllAus();
                for(AnneeUniversitaire anneeUniversitaire2:anneeUniversitaires){
                    anneeUniversitaire2.setIsCourant(false);
                    anneeuniversitaireRepository.save(anneeUniversitaire2);
                }
            }
            anneeuniversitaireRepository.save(anneeUniversitaire);
        } else {
            throw  new AuDefinedException("Cette annee universitaire : "+nom+" déjà existante");
        }
        return getAllAus();
    }
    @PutMapping(path = "/modifyAU/{auID}")//Tester
    @ResponseBody
    public AnneeUniversitaire modifyAU(
            @RequestBody AnneeUniversitaire anneeUniversitaire,
            @PathVariable Long auID
    ) throws AuDefinedException {
        boolean isCourant = anneeUniversitaire.getIsCourant();
        int nom = anneeUniversitaire.getNomAnneeUniversitaire();
        AnneeUniversitaire oldAu = anneeuniversitaireRepository.findById(auID).orElse(null);
        if(oldAu != null){
            if (isCourant){
                List<AnneeUniversitaire> anneeUniversitaires = getAllAus();
                for(AnneeUniversitaire anneeUniversitaire2:anneeUniversitaires){
                    anneeUniversitaire2.setIsCourant(false);
                    anneeuniversitaireRepository.save(anneeUniversitaire2);
                }
                oldAu.setIsCourant(isCourant);
            } else {
                oldAu.setIsCourant(isCourant);
            }
            if(nom!=0){ //Si le nom de l'année universitaire n'est pas saisi, on conserve l'ancien nom
                oldAu.setNomAnneeUniversitaire(anneeUniversitaire.getNomAnneeUniversitaire());
            }
            return anneeuniversitaireRepository.save(oldAu);
        }else {
            throw new AuDefinedException("Cette Annee Universitaire n'existe pas");
        }
    }

    @GetMapping(path = "/AUs/{id}") //tester
    public Optional<AnneeUniversitaire> getAUById(@PathVariable Long id){
        return anneeuniversitaireRepository.findById(id);
    }
    @GetMapping(path="/AnneeCourante") // Tester
    public AnneeUniversitaire getAnneeCourante(){
        return anneeuniversitaireRepository.findByIsCourant(Boolean.TRUE).get();
    }
    @DeleteMapping(path = "AUs/{id}/deleteAu") //tester
    public void deleteAu(@PathVariable Long id) throws AuDefinedException {
        boolean auExist = anneeuniversitaireRepository.existsById(id);
        if (!auExist){
            throw new AuDefinedException("AU with id "+id+" not found");
        }
        anneeuniversitaireRepository.deleteById(id);
    }
    //Definir une annee comme courante
    @PutMapping(path = "auCourante/{annee}") //Tester
    public List<AnneeUniversitaire> auCourante(@PathVariable int annee) throws AuDefinedException{
        AnneeUniversitaire anneeCourant = anneeuniversitaireRepository.findByNomAnneeUniversitaire(annee).orElse(null);
        if(anneeCourant!=null){
            List<AnneeUniversitaire> anneeUniversitaires = getAllAus();
            for(AnneeUniversitaire anneeUniversitaire:anneeUniversitaires){
                anneeUniversitaire.setIsCourant(false);
                anneeuniversitaireRepository.save(anneeUniversitaire);
            }
            anneeCourant.setIsCourant(true);
        } else {
            throw new AuDefinedException("Cette Annee Universitaire n'existe pas");
        }
        anneeuniversitaireRepository.save(anneeCourant);
        return getAllAus();
    }
}
