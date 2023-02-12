package com.example.serviceDeliberationAnnuelle.feign;

import com.example.serviceDeliberationAnnuelle.model.Etudiant;
import com.example.serviceDeliberationAnnuelle.model.InscriptionPedagogique;
import com.example.serviceDeliberationAnnuelle.model.Module;
import com.example.serviceDeliberationAnnuelle.model.Semestre;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "INSCRIPTION-PEDAGOGIQUE-SERVICE", url = "localhost:8095")//, url = "localhost:8095"
public interface InscriptionPedagogiqueRestClient {
    @GetMapping(path = "/etudiantsInscritPedagModule/{idFiliere}/{idModule}/{annee}")
    List<Etudiant> etudiantsInscritPedagModule (@PathVariable Long idFiliere ,@PathVariable Long idModule, @PathVariable int annee);
    @GetMapping(path = "/modulesEtudiantSemestre/{idEtu}/{idSem}")
    List<Module> modulesEtudiantSemestre(@PathVariable Long idEtu, @PathVariable Long idSem);

    @GetMapping(path = "/inscriptionEtudiantModule/{idInscripAd}/{idModule}")
    List<InscriptionPedagogique> inscriptionEtudiantModule(@PathVariable Long idInscripAd, @PathVariable Long idModule);

    @GetMapping(path = "/etudiantsInscritPedagSemestre/{idSemestre}/{idFiliere}")
    List<Etudiant> etudiantsInscritPedagSemestre(@PathVariable Long idSemestre, @PathVariable Long idFiliere);

    @GetMapping(path="/getInscriptionPedagogiqueSemestreResultatNote/{idInscripAd}/{idSemestre}")
    List<InscriptionPedagogique> getInscriptionPedagogiqueSemestreResultatNote(@PathVariable Long idInscripAd, @PathVariable Long idSemestre);

//    @GetMapping(path = "/modulesEtudiantSemestre/{idEtu}/{idEtape}")
//    List<Semestre> semestresEtudiantEtape(@PathVariable Long idEtu, @PathVariable Long idEtape);

    @GetMapping(path = "/semestresEtudiantEtape/{idEtu}/{idEtape}")
    List<Semestre> semestresEtudiantEtape(@PathVariable Long idEtu, @PathVariable Long idEtape);


    @GetMapping(path="/getInscriptionPedagogiqueEtapeResultatNote/{idInscripAd}/{idEtape}")
    List<InscriptionPedagogique> getInscriptionPedagogiqueEtapeResultatNote(@PathVariable Long idInscripAd, @PathVariable Long idEtape);

    @GetMapping(path = "/etudiantsInscritPedagEtape/{idEtape}/{idFiliere}")
    List<Etudiant> etudiantsInscritPedagEtape(@PathVariable Long idEtape, @PathVariable Long idFiliere);

}
