package com.example.demo.openFeign;

import com.example.demo.model.Element;
import com.example.demo.model.Etudiant;
import com.example.demo.model.Module;
import com.example.demo.model.NoteElementO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name= "SERVICE-DELIBERATION-ORDINAIRE", url = "localhost:8003")
public interface OrdinaireRestClient {

//    @GetMapping("/findEtudiant/{cne}")
//    Etudiant findEtudiantByCNE(@PathVariable String cne);
//    @GetMapping("/findModule/{id}")
//    Module findModuleByID(@PathVariable Long id);
//    @GetMapping("/findElement/{id}")
//    Element findElementByID(@PathVariable Long id);

    @GetMapping("/Note/{cne}/{idSemestre}/{idModule}/{idElement}/{anneeU}")
    NoteElementO getNoteByladiff(@PathVariable String cne,@PathVariable Long idSemestre, @PathVariable Long idModule,@PathVariable Long idElement,@PathVariable int anneeU);

    //Ajouter Par REGRAGUI
    @GetMapping("/listDesEtudiantsRatt/{idElement}/{annee}")
    List<Etudiant> etudiantsRatt(@PathVariable Long idElement,@PathVariable int annee);

    @GetMapping(path = "/deliberationordin/{idEtudiant}/{idElement}")
    NoteElementO noteElementModule(@PathVariable Long idEtudiant, @PathVariable Long idElement);

    @GetMapping(path = "/listElementByAnneIdElement/{idElement}/{annee}")
    List<NoteElementO> listElementByAnneIdElement(@PathVariable Long idElement, @PathVariable int annee);
}
