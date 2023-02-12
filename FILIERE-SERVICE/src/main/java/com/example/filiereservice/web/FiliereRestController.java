package com.example.filiereservice.web;

import com.example.filiereservice.Exception.FiliereDefinedException;
import com.example.filiereservice.Feign.DepartementRestClient;
import com.example.filiereservice.Feign.ModuleRestClient;
import com.example.filiereservice.Modules.Departement;
import com.example.filiereservice.entities.Etape;
import com.example.filiereservice.entities.Filiere;
import com.example.filiereservice.entities.Semestre;
import com.example.filiereservice.repositories.EtapeRepository;
import com.example.filiereservice.repositories.FiliereRepository;
import com.example.filiereservice.repositories.SemestreRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class FiliereRestController {
    private FiliereRepository filiereRepository;
    private SemestreRepository semestreRepository;
    private ModuleRestClient moduleRestClient;
    private EtapeRepository etapeRepository;
    private DepartementRestClient departementRestClient;
    public FiliereRestController(FiliereRepository filiereRepository, EtapeRepository etapeRepository,
                                 SemestreRepository semestreRepository, ModuleRestClient moduleRestClient,
                                 DepartementRestClient departementRestClient){
        this.filiereRepository = filiereRepository;
        this.semestreRepository = semestreRepository;
        this.moduleRestClient = moduleRestClient;
        this.etapeRepository = etapeRepository;
        this.departementRestClient =departementRestClient;
    }

    @PostMapping(path = "/addFiliere")
    @ResponseBody
    public Filiere addFiliere(@RequestBody Filiere filiere){
        return filiereRepository.save(filiere);
    }



    @PostMapping(path = "/{filiereID}/addEtape")
    @ResponseBody
    public Etape addEtape(@RequestBody Etape etape, @PathVariable Long filiereID){
        Filiere filiere = filiereRepository.findById(filiereID).orElse(null);
        etape.setFiliere(filiere);
        System.out.println("ID FILIERE " + filiereID);
        System.out.println("Numero de l'etape :" + etape.getLibEtape()+"année(s)");
        System.out.println("L'état de l'étape ouverte à l'inscription = " + etape.getOI());
        return etapeRepository.save(etape);
    }

    @PutMapping(path = "/modifyFiliere/{filiereID}")
    @ResponseBody
    public Filiere modifyFiliere(
            @RequestBody Filiere filiere,
            @PathVariable Long filiereID
    ) throws FiliereDefinedException {
        Filiere oldFiliere = filiereRepository.findById(filiereID).orElse(null);
        if(oldFiliere != null){
            oldFiliere.setLibLong(filiere.getLibLong());
            oldFiliere.setLibCourt(filiere.getLibCourt());
            oldFiliere.setLibArLong(filiere.getLibArLong());
            oldFiliere.setLibArCourt(filiere.getLibArCourt());
            oldFiliere.setNbrSemestre(filiere.getNbrSemestre());
            oldFiliere.setResponsableFiliere(filiere.getResponsableFiliere());
            oldFiliere.setNbrEtape(filiere.getNbrEtape());
            oldFiliere.setNbrAnneeDiplomantes(filiere.getNbrAnneeDiplomantes());
            oldFiliere.setResponsableFiliere(filiere.getResponsableFiliere());
            return filiereRepository.save(oldFiliere);
        }else {
            throw new FiliereDefinedException("Cette filiere n'existe pas");
        }
    }

    @PutMapping(path = "/{etapeID}/modifySemestre/{semestreId}")
    @ResponseBody
    public Semestre modifySemestre(
            @RequestBody Semestre semestre,
            @PathVariable Long etapeID,
            @PathVariable Long semestreId
    ) throws FiliereDefinedException {
        Etape etape = etapeRepository.findById(etapeID).orElse(null);
        Semestre oldSemestre = semestreRepository.findById(semestreId).orElse(null);
        if(etape != null || oldSemestre != null){
            assert oldSemestre != null;
            assert etape != null;
            oldSemestre.setLibLong(semestre.getLibLong());
            oldSemestre.setLibCourt(semestre.getLibCourt());
            oldSemestre.setLibArLong(semestre.getLibArLong());
            oldSemestre.setLibArLong(semestre.getLibArLong());
            oldSemestre.setEtape(etape);
            return semestreRepository.save(oldSemestre);

        }else {
            throw new FiliereDefinedException("La filière ou la semestre n'existe pas");
        }
    }
    @GetMapping(path="/filieres") // tester
    public List<Filiere> getAllFiliere(){
        List<Filiere> filiereList = filiereRepository.findAll();
        return getFiliereList(filiereList);
    }

    private List<Filiere> getFiliereList(List<Filiere> filiereList) {
        for (Filiere filiere: filiereList){
           Collection<Etape> etapeCollection = filiere.getEtapeCollection();
            for (Etape etape: etapeCollection){
                Collection<Semestre> semestreCollection = etape.getSemestreCollection();
                for (Semestre semestre: semestreCollection){
                    semestre.setModules(moduleRestClient.listeModulesSemestre(semestre.getIdSemestre()));
                }
            }
        }
        return filiereList;
    }

    @GetMapping(path = "/filieres/{id}") //tester
    public Optional<Filiere> getFiliereById(@PathVariable Long id){
        Optional<Filiere> filiere = filiereRepository.findById(id);
        Collection<Etape> etapeCollection = filiere.get().getEtapeCollection();
        for (Etape etape: etapeCollection){
            Collection<Semestre> semestreCollection = etape.getSemestreCollection();
            for (Semestre semestre: semestreCollection){
                semestre.setModules(moduleRestClient.listeModulesSemestre(semestre.getIdSemestre()));
            }
        }
        return filiere;
    }
    //Tous les semestres
    @GetMapping(path="/semestres") //tester
    public List<Semestre> getAllSemestres(){
        List<Semestre> semestreList = semestreRepository.findAll();
        for (Semestre semestre: semestreList){
            semestre.setModules(moduleRestClient.listeModulesSemestre(semestre.getIdSemestre()));
        }
        return semestreList;
    }
    //Semestre by Id


    //Liste des filiere d'un departement
    @GetMapping(path="/departement/{departID}")//tester
    List<Filiere> listeFiliereDepartement(@PathVariable Long departID){
        List<Filiere> filiereList = filiereRepository.findFiliereByIdDepartIs(departID);
        return getFiliereList(filiereList);
    }
    //Trouver les etapes une filiere
    @GetMapping(path="/filiere/{idFil}/etapes") //tester
    Collection<Etape> listeEtapesFiliere(@PathVariable Long idFil){
        return etapeRepository.findEtapesByFiliere_Id(idFil);
    }
    //Avoir les semestres d'une etape
    @GetMapping(path="/etapes/{etapeID}/semestres") //tester
    public List<Semestre> getSemestresByEtape(@PathVariable Long etapeID){
        return semestreRepository.findSemestresByEtape_IdEtape(etapeID);
    }

    //Avoir les semestres d'une etape d'un filiere
    @GetMapping(path="/semestres/etape/{ide}/filiere/{idf}") //tester
    public List<Semestre> listeSemestresEtapeFiliere(@PathVariable Long ide , @PathVariable Long idf){
        List<Semestre> semestres=new ArrayList<>();
        Filiere filiere = filiereRepository.findById(idf).get();
        Collection<Etape> etapeList = listeEtapesFiliere(idf);
        for (Etape etape:etapeList){
            if (etape.getIdEtape()==ide){
                semestres = (List<Semestre>) etape.getSemestreCollection();
            }
        }
        return semestres;
    }


    //Supprimer une filière
    @DeleteMapping(path = "filieres/{id}/deleteFil")
    public void deleteMod(@PathVariable Long id) throws FiliereDefinedException {
        boolean filExist = filiereRepository.existsById(id);
        if (!filExist){
            throw new FiliereDefinedException("Filiere with id "+id+"not found");
        }
        filiereRepository.deleteById(id);
    }
    //Supprimer une semestre
    @DeleteMapping(path = "semestre/{id}/deleteSem")
    public void deleteSem(@PathVariable Long id) throws FiliereDefinedException {
        boolean semExist = semestreRepository.existsById(id);
        if (!semExist){
            throw new FiliereDefinedException("Semestre avec le Id "+id+" n'existe pas");
        }
        semestreRepository.deleteById(id);
    }

    @GetMapping(path="/etapes/{etapeID}/{libc}/semestres")
    public Semestre getSemestresByEtapeandlib(@PathVariable Long etapeID , @PathVariable String libc) {
        return semestreRepository.findByEtape_IdEtapeAndLibCourt(etapeID,libc);
    }
    @GetMapping(path="/semestreByid/{semID}")
    public Semestre getSemestresByid(@PathVariable Long  semID) {
        return semestreRepository.findByIdSemestre(semID);
    }
    @GetMapping(path="/semestres/{semID}") //tester
    public Optional<Semestre> getSemByID(@PathVariable Long semID){
        Optional<Semestre> semestre = semestreRepository.findById(semID);
        semestre.get().setModules(moduleRestClient.listeModulesSemestre(semID));
        return semestre;
    }

    @GetMapping(path="/filiereBySemestre/{idSemestre}")
    public Filiere filiereBySemestre(@PathVariable Long idSemestre){
        Semestre semestre = semestreRepository.findById(idSemestre).get();
        return semestre.getEtape().getFiliere();
    }

}
