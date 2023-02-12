package com.example.moduleservice.web;


import com.example.moduleservice.Exception.ModuleDefinedException;
import com.example.moduleservice.Feign.SemestreRestClient;
import com.example.moduleservice.Modules.Semestre;
import com.example.moduleservice.entities.Element;
import com.example.moduleservice.entities.Module;
import com.example.moduleservice.repositories.ElementRepository;
import com.example.moduleservice.repositories.ModulesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class ModuleElementRestController {
    private ElementRepository elementRepository;
    private ModulesRepository modulesRepository;
    private SemestreRestClient semestreRestClient;
    public ModuleElementRestController( ElementRepository elementRepository, ModulesRepository modulesRepository,
                                        SemestreRestClient semestreRestClient) {
        this.elementRepository = elementRepository;
        this.modulesRepository = modulesRepository;
        this.semestreRestClient=semestreRestClient;
    }


    @PostMapping(path = "/{moduleID}/addElement")
    @ResponseBody
    public Element addElement(@RequestBody Element element, @PathVariable Long moduleID) throws ModuleDefinedException {
        Module module = modulesRepository.findById(moduleID).orElse(null);

        if(module != null ){
            if(module.getNbrElement()==0){//Si le nombre d'element d'un module = 0 => il existe l'element par defaut
                Collection<Element> elementCollections = module.getElements();
                for (Element element1:elementCollections){
                    elementRepository.deleteById(element1.getIdElement());//on le supprime
                }
                module.setElements(Collections.emptyList());//Vider la liste des elements
            }
            int i = module.getNbrElement();
            module.setNbrElement(i+1);
            modulesRepository.save(module);
            System.out.println(module.getIdModule());
            System.out.println("ID MODULE " + moduleID);
            System.out.println("Nom Long de ELEMENT " + element.getLibLong());
            System.out.println("Nom Court de ELEMENT " + element.getLibCourt());
            System.out.println("Note min de ELEMENT" + element.getNoteMin());
            System.out.println("Coef ELEMENT =" + element.getCoefElem());
            element.setModule(module);
            elementRepository.save(element);
            return element;
        } else {
            throw new ModuleDefinedException("Le module n'existe pas");
        }

    }
    @GetMapping(path="/elements")//tester
    public List<Element> moduleList(){
        return elementRepository.findAll();
    }
    @GetMapping(path="/elements/{idElement}") //tester
    public Element elementByID(@PathVariable Long idElement) throws ModuleDefinedException{
        Element element = elementRepository.findById(idElement).orElse(null);
        if (element!=null){
            return element;
        }else{
            throw new ModuleDefinedException("L'element avec l'id = "+ idElement+ "n'existe pas");
        }
    }

    @GetMapping(path = "/modulebyelement/{id}")
    public Module getmodulebyElement(@PathVariable Long id){
        Element element = elementRepository.findById(id).orElse(null);
        Module module = modulesRepository.findByElements(element);


        return  module;
    }
    @GetMapping(path="/modules")//tester
    public List<Module> listesModule(){
        return modulesRepository.findAll();
    }
    //List des modules d'une semestre
    @GetMapping(path="/modules/semestre/{id}") //tester
    public List<Module> listeModulesSemestre(@PathVariable Long id){
        return modulesRepository.findModulesByIdSemestreIs(id);
    }
    //List des modules d'une filiere
    @GetMapping(path="/modules/filiere/{id}")//tester
    public List<Module> listeModulesFiliere(@PathVariable Long id){
        return modulesRepository.findByIdFiliere(id);
    }
    //Details d'une Semestre : Ajouter par REGRAGUI
    @GetMapping(path="/modules/semestreDetails/{id}") //tester
    public Optional<Semestre>  semestreDetails(@PathVariable Long id){
        Optional<Semestre> semestre = semestreRestClient.getSemByID(id);
        if (!semestre.isPresent()){
            throw new IllegalStateException("Semestre with id "+id+" not found");
        }
        return semestre;
    }

    //Avoir les élements d'un module bien spécifie
    @GetMapping(path="/modules/{modID}/elements")//tester
    public List<Element> getElementsBymodule(@PathVariable Long modID){
        return elementRepository.findElementsByModule(modID);
    }
    @GetMapping(path="/modules/semestre/{ids}/filiere/{idf}") //tester
    public List<Module> listeModulesSemestreFiliere(@PathVariable Long ids , @PathVariable Long idf){
        return modulesRepository.findByIdFiliereAndIdSemestre(idf,ids);
    }

    @GetMapping(path = "/modules/{id}")//tester
    public Module getModule(@PathVariable(name = "id") Long id){
        Module module = modulesRepository.findById(id).get();
        return module;
    }
    @PostMapping(path = "/addModule")
    @ResponseBody
    public Module addModule(@RequestBody Module module) throws ModuleDefinedException {
        creationElementParDefaut(module);
        return modulesRepository.save(module);
    }

    public void creationElementParDefaut(Module module) throws ModuleDefinedException {
        Element element = new Element();
        element.setLibCourt(module.getLibCourt());
        element.setLibLong(module.getLibLong());
        element.setCoefElem(1);
        element.setNoteMin(module.getNoteMin());
        element.setModule(module);
        element.setIdProf(module.getIdProf());
        addElement(element,module.getIdModule());
    }

    @PutMapping(path = "/modifyModule/{moduleID}")
    @ResponseBody
    public Module modifyModule(
            @RequestBody Module module,
            @PathVariable Long moduleID
    ) throws ModuleDefinedException {
        Module oldModule = modulesRepository.findById(moduleID).orElse(null);
        if(oldModule != null){
            oldModule.setLibLong(module.getLibLong());
            oldModule.setLibCourt(module.getLibCourt());
            oldModule.setNoteMin(module.getNoteMin());
            oldModule.setCoef(module.getCoef());
            oldModule.setIdSemestre(module.getIdSemestre());
            oldModule.setIdFiliere(module.getIdFiliere());
            oldModule.setIdProf(module.getIdProf());
            oldModule.setNbrElement(module.getNbrElement());
            return modulesRepository.save(oldModule);
        }else {
            throw new ModuleDefinedException("This is module not found");
        }
    }

    @DeleteMapping(path="/deleteAllElemnt")
    public void supprimerTousLesElemnts(){
        elementRepository.deleteAll();
    }


    @PutMapping(path = "/{moduleID}/modifyElement/{elementId}")
    @ResponseBody
    public Element modifyElement(
            @RequestBody Element element,
            @PathVariable Long moduleID,
            @PathVariable Long elementId
    ) throws ModuleDefinedException {
        Module module = modulesRepository.findById(moduleID).orElse(null);
        Element oldElement = elementRepository.findById(elementId).orElse(null);
        if(module != null || oldElement != null){
            assert oldElement != null;
            assert module != null;
            oldElement.setLibLong(element.getLibLong());
            oldElement.setLibCourt(element.getLibCourt());
            oldElement.setNoteMin(element.getNoteMin());
            oldElement.setCoefElem(element.getCoefElem());
            element.setModule(module);
            return elementRepository.save(oldElement);
        }else {
            throw new ModuleDefinedException("This module or element not found");
        }
    }
    //Supprimer un module
    @DeleteMapping(path = "modules/{id}/deleteMod")
    public void deleteMod(@PathVariable Long id) throws ModuleDefinedException {
        boolean modExist = modulesRepository.existsById(id);
        if (!modExist){
            throw new ModuleDefinedException("Module with id "+id+"not found");
        }
        modulesRepository.deleteById(id);

    }
    //Supprimer un element
    @DeleteMapping(path = "elements/{id}/deleteElem")
    public void deleteElem(@PathVariable Long id) throws ModuleDefinedException {
        boolean elemExist = elementRepository.existsById(id);
        if (!elemExist){
            throw new ModuleDefinedException("Element with id "+id+"not found");
        }
        Module module = modulesRepository.findById(elementRepository.findById(id).get().getModule().getIdModule()).get();
        int i = module.getNbrElement();
        if (i==1){ //Si nous somme entrain de supprimer le dernier element
            creationElementParDefaut(module);
        }
        module.setNbrElement(i-1);
        elementRepository.deleteById(id);

    }
    //Avoir tous les elements d'un professeur
    @GetMapping(path="/elements/professeur/{id}") //tester
    List<Element> listeElementsProfesseur (@PathVariable Long id){
        return elementRepository.findByIdProf(id);
    }
    //Avoir les modules d'un professeur dont il est responsable
    @GetMapping(path="/modules/professeur/{id}") //tester
    List<Module> listModulesProfesseur(@PathVariable Long id){
        return modulesRepository.findByIdProf(id);
    }
    @GetMapping(path="/moduleElement/{idElement}")
    public Module moduleElement(@PathVariable Long idElement){
        Element element = elementRepository.findById(idElement).get();
        return element.getModule();
    }

    //Modifier l'idProf = null
    @PutMapping(path = "/supElementIdProf/{elementID}")
    public Element supElementIdProf(@PathVariable Long elementID) throws ModuleDefinedException{
        Element element = elementRepository.findById(elementID).orElse(null);
        if(element!=null){
            element.setIdProf(null);
        }else {
            throw new ModuleDefinedException("Cet element n'existe pas !!");
        }
        return elementRepository.save(element);
    }
    //Modifier l'idProf != null
    @PutMapping(path = "/modifyElementIdProf/{elementID}/{idProf}")
    public Element modifyElementIdProf(@PathVariable Long elementID,@PathVariable Long idProf) throws ModuleDefinedException{
        Element element = elementRepository.findById(elementID).orElse(null);
        if(element!=null){
            element.setIdProf(idProf);
        }else {
            throw new ModuleDefinedException("Cet element n'existe pas !!");
        }
        return elementRepository.save(element);
    }

    //Avoir un module d'un semestre bien spé

}
