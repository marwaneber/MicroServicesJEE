package com.etablissement.etablissementservice.web;

import com.etablissement.etablissementservice.Exception.EtablissementDefinedException;
import com.etablissement.etablissementservice.entities.Departement;
import com.etablissement.etablissementservice.entities.Etablissement;
import com.etablissement.etablissementservice.feign.FiliereRestClient;
import com.etablissement.etablissementservice.feign.ProfesseurRestClient;
import com.etablissement.etablissementservice.model.Filiere;
import com.etablissement.etablissementservice.repository.DepartementRepository;
import com.etablissement.etablissementservice.repository.EtablissementRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class EtablissmentRestController {
    private EtablissementRepository etablissementRepository;
    private DepartementRepository departementRepository;
    private FiliereRestClient filiereRestClient;
    private ProfesseurRestClient professeurRestClient;
    public EtablissmentRestController(EtablissementRepository etablissementRepository, DepartementRepository departementRepository,
                                      FiliereRestClient filiereRestClient, ProfesseurRestClient professeurRestClient){
        this.etablissementRepository=etablissementRepository;
        this.departementRepository=departementRepository;
        this.filiereRestClient = filiereRestClient;
        this.professeurRestClient = professeurRestClient;
    }
    @PostMapping(path="/addEtab")
    @ResponseBody
    public Etablissement addEtab(@RequestBody Etablissement etablissement){
        return etablissementRepository.save(etablissement);
    }

    @PostMapping(path = "/{etbID}/addDepart")
    @ResponseBody
    public Departement addDepart(@RequestBody Departement departement,
                                 @PathVariable Long etbID){
        Etablissement etablissement = etablissementRepository.findById(etbID).orElse(null);
        departement.setEtablissement(etablissement);
        System.out.println("ID ETABLISSEMENT " + etbID);
        System.out.println("Name complet de DEPARTEMENT " + departement.getLibLong());
        System.out.println("Nom court de DEPARTEMENT" + departement.getLibCourt());
        System.out.println("Chef DEPARTEMENT" + departement.getChefDepartement());
        return departementRepository.save(departement);
    }
    @PutMapping(path="/modifyEtablissment/{etbID}")
    @ResponseBody
    public Etablissement modifyEtab(
            @RequestBody Etablissement etablissement,
            @PathVariable Long etbID
    ) throws EtablissementDefinedException {
        Etablissement oldEtab = etablissementRepository.findById(etbID).orElse(null);
        if(oldEtab != null){
            oldEtab.setLibLong(etablissement.getLibLong());
            oldEtab.setLibCourt(etablissement.getLibCourt());
            oldEtab.setLibArLong(etablissement.getLibArLong());
            oldEtab.setLibArCourt(etablissement.getLibArCourt());
            return etablissementRepository.save(oldEtab);
        } else {
            throw new EtablissementDefinedException("Cette etablissment n'existe pas!!");
        }
    }

    @PutMapping(path="/{etbID}/modifyDepart/{departID}")
    @ResponseBody
    public Departement modifyDepartement(
            @RequestBody Departement departement,
            @PathVariable Long etbID,
            @PathVariable Long departID
    ) throws EtablissementDefinedException {
        Etablissement etablissement = etablissementRepository.findById(etbID).orElse(null);
        Departement oldDepartement = departementRepository.findById(departID).orElse(null);
        if (etablissement !=null || oldDepartement != null ){
            assert oldDepartement != null;
            assert etablissement != null;
            oldDepartement.setLibLong(departement.getLibLong());
            oldDepartement.setLibCourt(departement.getLibCourt());
            oldDepartement.setLibArCourt(departement.getLibArCourt());
            oldDepartement.setLibArLong(departement.getLibArLong());
            oldDepartement.setChefDepartement(departement.getChefDepartement());
            return departementRepository.save(oldDepartement);
        } else  {
            throw new EtablissementDefinedException("L'établissment ou le département n'existe pas !!");

        }
    }

    //Listes de tous les departement
    @GetMapping(path="/departements") //TESTER
    public List<Departement> departementList(){
        List<Departement> departementList = departementRepository.findAll();
        for (Departement departement: departementList){
            departement.setFiliereLists(filiereRestClient.listeFiliereDepartement(departement.getId()));
            departement.setProfesseurCollection(professeurRestClient.professeursDepart(departement.getId()));
        }
        return departementList;
    }


    //Departement By ID
    @GetMapping(path="/departements/{id}") //TESTER
    public Optional<Departement> departementById(@PathVariable Long id){
        Optional<Departement> departement = departementRepository.findById(id);
        departement.get().setFiliereLists(filiereRestClient.listeFiliereDepartement(id));
        return departement;
    }

    @GetMapping(path="/etablissements") //TESTER
    public List<Etablissement> getAllEtab(){
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        for (Etablissement etablissement: etablissementList){
            Collection<Departement> departementCollection =etablissement.getDepartementList();
            for(Departement departement: departementCollection){
                departement.setFiliereLists(filiereRestClient.listeFiliereDepartement(departement.getId()));
            }
        }
        return etablissementList;
    }

    @GetMapping(path="/etablissements/{id}") //TESTER
    public Optional<Etablissement> getEtabById(@PathVariable Long id){
        Optional<Etablissement> etablissement = etablissementRepository.findById(id);
        Collection<Departement> departementCollection = etablissement.get().getDepartementList();
        for (Departement departement: departementCollection){
            departement.setFiliereLists(filiereRestClient.listeFiliereDepartement(departement.getId()));
        }
        return etablissement;
    }
    //Liste de tous les filieres d'un departement
    @GetMapping(path="/departements/{departID}/filieres") //tester
    public List<Filiere> filiereListDepar(@PathVariable Long departID){
        return filiereRestClient.listeFiliereDepartement(departID);
    }

    @GetMapping(path="/etablissement/departement/{etbID}") // tester
    public List<Departement> listeDepartementsEtab(@PathVariable Long etbID){
        return departementRepository.getDepartementsByEtablissement_IdEtab(etbID);
    }

    //liste des filieres d'une etablissement  ajouter par abir
    @GetMapping(path="/etablissement/filieres/{etbID}")
    public List<Filiere> listeFiliereEtab(@PathVariable Long etbID){
        List<Filiere> filiereList= new ArrayList<>();
        List<Departement> departementList = listeDepartementsEtab(etbID);
        for (Departement departement: departementList){
            List<Filiere> filiereListDep = filiereListDepar(departement.getId()) ;
            for(Filiere filiere: filiereListDep){
                filiereList.add(filiere);
            }

        }
        return filiereList;
    }
    @DeleteMapping(path = "etablissements/{id}/deleteEtab")//tester
    public void deleteEtab(@PathVariable Long id) throws EtablissementDefinedException {
        boolean etabExist = etablissementRepository.existsById(id);
        if (!etabExist){
            throw new EtablissementDefinedException("Filiere with id "+id+"not found");
        }
        etablissementRepository.deleteById(id);
    }
    @DeleteMapping(path = "departements/{id}/deletedepart") //tester
    public void deletedepart(@PathVariable Long id) throws EtablissementDefinedException {
        boolean departExist = departementRepository.existsById(id);
        if (!departExist){
            throw new EtablissementDefinedException("Departement with id "+id+"not found");
        }
        departementRepository.deleteById(id);
    }

}