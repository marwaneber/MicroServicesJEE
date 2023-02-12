package com.example.serviceprofesseur.web;
import com.example.serviceprofesseur.entities.Professeur;
import com.example.serviceprofesseur.exception.ProfesseurDefinedException;
import com.example.serviceprofesseur.feign.ModuleRestClient;
import com.example.serviceprofesseur.modules.Element;
import com.example.serviceprofesseur.repositories.ProfesseurRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class ProfesseurRestController {
    private ProfesseurRepository professeurRepository;
    private ModuleRestClient moduleRestClient;
    public ProfesseurRestController(ProfesseurRepository professeurRepository, ModuleRestClient moduleRestClient) {
        this.professeurRepository = professeurRepository;
        this.moduleRestClient = moduleRestClient;
    }
//    @GetMapping(path="/professeurs") // tester
//    public List<Professeur> listeProfesseurs(){
//        List<Professeur> professeurList = professeurRepository.findAll();
//        for (Professeur professeur:professeurList){
//            professeur.setElementCollection(listeElementProfesseur(professeur.getIdProf()));
//            professeurRepository.save(professeur);
//        }
//        return professeurList;
//    }

    @GetMapping(path="/professeurs")//tester
    public List<Professeur> listeProfesseurs(){
        return professeurRepository.findAll();
    }
    //List des elements d'une professeur
    @GetMapping(path="/elements/professeur/{idProf}") //tester
    public List<Element> listeElementProfesseur(@PathVariable Long idProf){
        return moduleRestClient.listeElementsProfesseur(idProf);
    }

    @GetMapping(path="/professeurs/{idProf}") // tester
    public Professeur getProfesseurById(@PathVariable Long idProf){
        Professeur professeur= professeurRepository.findById(idProf).get();
        //professeur.setElementCollection(listeElementProfesseur(professeur.getIdProf()));
        return professeur;
    }

    @GetMapping(path = "/professeurs/departement/{idDepart}") //tester
    public List<Professeur> professeursDepart(@PathVariable Long idDepart){
        return professeurRepository.findByIdDepar(idDepart);
    }

    @PostMapping(path = "/addProfesseur")
    @ResponseBody
    public Professeur addProfesseur(@RequestBody Professeur professeur){
        return professeurRepository.save(professeur);
    }

    @PutMapping(path = "/modifyProfesseur/{professeurID}")
    @ResponseBody
    public Professeur modifyProfesseur(
            @RequestBody Professeur professeur,
            @PathVariable Long professeurID
    ) throws ProfesseurDefinedException {
        Professeur oldProfesseur = professeurRepository.findById(professeurID).orElse(null);
        if(oldProfesseur != null){
            oldProfesseur.setNomProf(professeur.getNomProf());
            oldProfesseur.setPrenomProf(professeur.getPrenomProf());
            oldProfesseur.setIdDepar(professeur.getIdDepar());
            oldProfesseur.setRole(professeur.getRole());
            oldProfesseur.setDiscipline(professeur.getDiscipline());
            return professeurRepository.save(oldProfesseur);
        }else {
            throw new ProfesseurDefinedException("Ce professeur n'existe pas");
        }
    }
    //Supprimer un professeur
    @DeleteMapping(path = "professeurs/{id}/deleteProf")
    public void deleteProf(@PathVariable Long id) throws ProfesseurDefinedException {
        boolean profExist = professeurRepository.existsById(id);
        if (!profExist){
            throw new ProfesseurDefinedException("Professeur avec id "+id+"n'existe pas");
        }
        professeurRepository.deleteById(id);
    }
    //Affecter un Element à un professeur
    @PostMapping(path = "/prosfesseurElement/{idProf}/{idEle}")
    public Professeur  prosfesseurElement(@PathVariable Long idProf, @PathVariable Long idEle){
        Professeur professeur =professeurRepository.findById(idProf).get();
        Collection<Element> elementCollection = professeur.getElementCollection();
        elementCollection.add(moduleRestClient.elementByID(idEle));
        professeur.setElementCollection(elementCollection);
        return professeurRepository.save(professeur);
    }

    //Les noms des elements enseignés par un prof
    @GetMapping(path = "/nomElementsProf/{idProf}")
    public List<String> nomElementsProf(@PathVariable Long idProf){
        List<Element> list = listeElementProfesseur(idProf);
        List<String> strings = new ArrayList<>();
        for (Element element:list){
            strings.add(element.getLibLong());
        }
        return strings;
    }
}
