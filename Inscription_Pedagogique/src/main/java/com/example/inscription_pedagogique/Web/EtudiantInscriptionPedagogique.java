package com.example.inscription_pedagogique.Web;


import com.example.inscription_pedagogique.Enumeration.EtatIns;
import com.example.inscription_pedagogique.Enumeration.Resultat;
import com.example.inscription_pedagogique.Enumeration.TypeElementPedagogique;
import com.example.inscription_pedagogique.Exception.InscriptionPedagogiqueDefinedException;
import com.example.inscription_pedagogique.Repositories.InscriptionPedagogiqueRepository;
import com.example.inscription_pedagogique.entities.InscriptionPedagogique;
import com.example.inscription_pedagogique.feign.*;
import com.example.inscription_pedagogique.model.*;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.util.*;

@RestController
public class EtudiantInscriptionPedagogique {

    InscriptionAdminRestClient inscriptionAdminRestClient;
    InscriptionPedagogiqueRepository inscriptionPedagogiqueRepository;
    FiliereRestClient filiereRestClient;
    ModuleRestClient moduleRestClient;
    AnneeUniverRestClient anneeUniverRestClient;
    DeliberationOrdinaireRestClient deliberationOrdinaireRestClient;
    DeliberationRattRestClient deliberationRattRestClient;
    DeliberationSemestreRestClient deliberationSemestreRestClient;
    DeliberationAnnuelleRestClient deliberationAnnuelleRestClient;
    public  EtudiantInscriptionPedagogique(InscriptionPedagogiqueRepository inscriptionPedagogiqueRepository, InscriptionAdminRestClient inscriptionAdminRestClient,
                                           FiliereRestClient filiereRestClient,ModuleRestClient moduleRestClient, AnneeUniverRestClient anneeUniverRestClient,
                                           DeliberationOrdinaireRestClient deliberationOrdinaireRestClient,
                                           DeliberationRattRestClient deliberationRattRestClient,
                                           DeliberationSemestreRestClient deliberationSemestreRestClient,
                                           DeliberationAnnuelleRestClient deliberationAnnuelleRestClient){
        this.inscriptionAdminRestClient=inscriptionAdminRestClient;
        this.inscriptionPedagogiqueRepository=inscriptionPedagogiqueRepository;
        this.filiereRestClient=filiereRestClient;
        this.moduleRestClient=moduleRestClient;
        this.anneeUniverRestClient = anneeUniverRestClient;
        this.deliberationOrdinaireRestClient = deliberationOrdinaireRestClient;
        this.deliberationRattRestClient = deliberationRattRestClient;
        this.deliberationSemestreRestClient=deliberationSemestreRestClient;
        this.deliberationAnnuelleRestClient=deliberationAnnuelleRestClient;
    }


    @GetMapping(path = "/saveinscriptionpedagogique/{idInscrAdmin}")
    public void insriptionPedagogiqueById (@PathVariable Long idInscrAdmin){
        InscriptionPedagogique inscriptionPedagogique1 = new InscriptionPedagogique();

        Etape etape1= filiereRestClient.getEtapeById(inscriptionAdminRestClient.GetIdEtapeFromIdInscAdmin(idInscrAdmin));
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdminRestClient.getInscriptionsAdministrativeByid(idInscrAdmin);
        Long idfiliere = inscriptionAdministrative.getIdFiliere();

        //Creation inscription pedagogique par etape
        inscriptionPedagogique1.setIdInscripAdmin(idInscrAdmin);
        inscriptionPedagogique1.setIdElmPed(etape1.getIdEtape());
        inscriptionPedagogique1.setTypeelp(TypeElementPedagogique.Etape);
        inscriptionPedagogique1.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
        inscriptionPedagogiqueRepository.save(inscriptionPedagogique1);

        //semestre
        List<Semestre> semestres = filiereRestClient.semestresEtapeFiliere(idfiliere, etape1.getLibEtape());
        //Remplace
//        List<Semestre> semestres = filiereRestClient.getSemestresByEtape(etape1.getIdEtape());

        for (Semestre semestre :semestres ){
            InscriptionPedagogique inscriptionPedagogique=new InscriptionPedagogique();
            inscriptionPedagogique.setIdInscripAdmin(idInscrAdmin);
            inscriptionPedagogique.setIdElmPed(semestre.getIdSemestre());
            inscriptionPedagogique.setTypeelp(TypeElementPedagogique.Semestre);
            inscriptionPedagogique.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
            inscriptionPedagogiqueRepository.save(inscriptionPedagogique);
            List<Module> modules = moduleRestClient.listeModulesSemestreFiliere(semestre.getIdSemestre(),idfiliere);
            //module
            for (Module module : modules){
                InscriptionPedagogique inscriptionPedagogique2=new InscriptionPedagogique();
                inscriptionPedagogique2.setIdInscripAdmin(idInscrAdmin);
                inscriptionPedagogique2.setIdElmPed(module.getIdModule());
                inscriptionPedagogique2.setTypeelp(TypeElementPedagogique.Module);
                inscriptionPedagogique2.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                inscriptionPedagogiqueRepository.save(inscriptionPedagogique2);
            }
        }

    }

    @GetMapping(path = "/autoinscription")
    public void autoInscription(){
        Long idetud=0L;
        int anneeCourante = anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire();
        List<InscriptionAdministrative> inscriptionAdministratives = inscriptionAdminRestClient.inscriptionsAdministrativeEncous(anneeCourante);
        System.out.println(anneeCourante);
        for ( InscriptionAdministrative inscriptionAdministrative : inscriptionAdministratives){
            idetud=inscriptionAdminRestClient.getIde(inscriptionAdministrative.getIdInsAdmin());
            Long idfilierec = inscriptionAdministrative.getIdFiliere();
            System.out.println(idfilierec);
            InscriptionAdministrative  inscriptadminpred1 = inscriptionAdminRestClient.getInsByIdEtudiantAnnee(idetud, anneeCourante-1);
            System.out.println(inscriptadminpred1);
            List<InscriptionPedagogique> inscriPedag = null;
            List<InscriptionPedagogique> testempty=inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
            if (testempty.isEmpty()){

// nv etudiant
                if (inscriptadminpred1.getIdInsAdmin()==null){
                    System.out.println("NV ETUDIANT");

                    InscriptionPedagogique inscriptionPedagogique1 = new InscriptionPedagogique();

                    Etape etape1= filiereRestClient.getEtapeById(inscriptionAdminRestClient.GetIdEtapeFromIdInscAdmin(inscriptionAdministrative.getIdInsAdmin()));

                    //Creation inscription pedagogique par etape
                    inscriptionPedagogique1.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
                    inscriptionPedagogique1.setIdElmPed(etape1.getIdEtape());
                    inscriptionPedagogique1.setTypeelp(TypeElementPedagogique.Etape);
                    inscriptionPedagogique1.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                    inscriptionPedagogiqueRepository.save(inscriptionPedagogique1);


                    //
                    List<Semestre> semestres = filiereRestClient.semestresEtapeFiliere(inscriptionAdministrative.getIdFiliere(), etape1.getLibEtape());
                    //Remplace
//                    List<Semestre> semestres = filiereRestClient.getSemestresByEtape(etape1.getIdEtape());

                    for (Semestre semestre :semestres ){
                        InscriptionPedagogique inscriptionPedagogique=new InscriptionPedagogique();
                        inscriptionPedagogique.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
                        inscriptionPedagogique.setIdElmPed(semestre.getIdSemestre());
                        inscriptionPedagogique.setTypeelp(TypeElementPedagogique.Semestre);
                        inscriptionPedagogique.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                        inscriptionPedagogiqueRepository.save(inscriptionPedagogique);
                        List<Module> modules = moduleRestClient.listeModulesSemestreFiliere(semestre.getIdSemestre(),idfilierec);

                        for (Module module : modules){
                            InscriptionPedagogique inscriptionPedagogique2=new InscriptionPedagogique();
                            inscriptionPedagogique2.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
                            inscriptionPedagogique2.setIdElmPed(module.getIdModule());
                            inscriptionPedagogique2.setTypeelp(TypeElementPedagogique.Module);
                            inscriptionPedagogique2.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                            inscriptionPedagogiqueRepository.save(inscriptionPedagogique2);
                        }

                    }}


                // Etudiant(e) deja inscrit

                else if (inscriptadminpred1.getIdInsAdmin()!=null) {//etape
                    System.out.println("Etudiant(e) deja inscrit");

                    inscriPedag = inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByIdInscripAdminAndTypeelp(inscriptadminpred1.getIdInsAdmin(), TypeElementPedagogique.Etape);

                    if (inscriPedag.get(0).getResultat() == Resultat.V || inscriPedag.get(0).getResultat() == Resultat.VC) {
                        System.out.println("Etape validé");

                        InscriptionPedagogique inscripPed = new InscriptionPedagogique();
                        Etape etapepred = filiereRestClient.getEtapeById(inscriptionAdminRestClient.GetIdEtapeFromIdInscAdmin(inscriptadminpred1.getIdInsAdmin()));
                        System.out.println(etapepred.getLibEtape());
                        int libetap = etapepred.getLibEtape() + 1;
                        System.out.println(libetap);
                        Long idfiliere = inscriptadminpred1.getIdFiliere();
                        Long idetape = filiereRestClient.findIdetapeByFiliere_IdAndLibEtape(idfiliere, libetap);

                        inscripPed.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
                        inscripPed.setIdElmPed(idetape);
                        inscripPed.setTypeelp(TypeElementPedagogique.Etape);
                        inscripPed.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                        inscriptionPedagogiqueRepository.save(inscripPed);
                        List<Semestre> semestres = filiereRestClient.semestresEtapeFiliere(idfiliere, libetap);
                        //Remplace
//                        List<Semestre> semestres = filiereRestClient.getSemestresByEtape(idetape);

                        for (Semestre semestre : semestres) {
                            InscriptionPedagogique inscriptionPedagogique = new InscriptionPedagogique();
                            inscriptionPedagogique.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
                            inscriptionPedagogique.setIdElmPed(semestre.getIdSemestre());
                            inscriptionPedagogique.setTypeelp(TypeElementPedagogique.Semestre);
                            inscriptionPedagogique.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                            inscriptionPedagogiqueRepository.save(inscriptionPedagogique);
                            List<Module> modules = moduleRestClient.listeModulesSemestreFiliere(semestre.getIdSemestre(),idfilierec);
                            //module
                            for (Module module : modules) {
                                InscriptionPedagogique inscriptionPedagogique2 = new InscriptionPedagogique();
                                inscriptionPedagogique2.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
                                inscriptionPedagogique2.setIdElmPed(module.getIdModule());
                                inscriptionPedagogique2.setTypeelp(TypeElementPedagogique.Module);
                                inscriptionPedagogique2.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                                inscriptionPedagogiqueRepository.save(inscriptionPedagogique2);
                            }
                        }
                    }
                    //cas etape non validé et semestre validé

                    else if(inscriPedag.get(0).getResultat() == Resultat.N){
                        System.out.println("Etape non validé");
                        System.out.println("Semestre validé");

                        inscriPedag = inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByIdInscripAdminAndTypeelp(inscriptadminpred1.getIdInsAdmin(), TypeElementPedagogique.Semestre);
                        for (InscriptionPedagogique inscriptionPedagogique : inscriPedag) {
                            //semestre validé
                            if (inscriptionPedagogique.getResultat() == Resultat.V || inscriptionPedagogique.getResultat() == Resultat.VC) {

                                Semestre semestre = filiereRestClient.getSemestresByid(inscriptionPedagogique.getIdElmPed());
                                String lib = semestre.getLibCourt();
                                String[] sems = {"S1", "S2", "S3", "S4", "S5", "S6"};
                                InscriptionPedagogique inscripPed = new InscriptionPedagogique();
                                Etape etapepred = filiereRestClient.getEtapeById(inscriptionAdminRestClient.GetIdEtapeFromIdInscAdmin(inscriptadminpred1.getIdInsAdmin()));
                                int libetap = etapepred.getLibEtape() + 1;
                                Long idfiliere = inscriptadminpred1.getIdFiliere();
                                Long idetape = filiereRestClient.findIdetapeByFiliere_IdAndLibEtape(idfiliere, libetap);
                                Semestre semestreNv = filiereRestClient.getSemestresByEtapeandlib(idetape, sems[Arrays.binarySearch(sems, lib) + 2]);

                                inscripPed.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
                                inscripPed.setIdElmPed(semestreNv.getIdSemestre());
                                inscripPed.setTypeelp(TypeElementPedagogique.Semestre);
                                inscripPed.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                                inscriptionPedagogiqueRepository.save(inscripPed);
                                List<Module> modules = moduleRestClient.listeModulesSemestreFiliere(semestreNv.getIdSemestre(),idfilierec);
                                //module
                                for (Module module : modules) {
                                    InscriptionPedagogique inscriptionPedagogique2 = new InscriptionPedagogique();
                                    inscriptionPedagogique2.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
                                    inscriptionPedagogique2.setIdElmPed(module.getIdModule());
                                    inscriptionPedagogique2.setTypeelp(TypeElementPedagogique.Module);
                                    inscriptionPedagogique2.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                                    inscriptionPedagogiqueRepository.save(inscriptionPedagogique2);
                                }
                            }
                        }
                        //cas semestre non validé test des  modules
                        System.out.println("semestre non validé test des  modules");
                        inscriPedag = inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByIdInscripAdminAndTypeelp(inscriptadminpred1.getIdInsAdmin(), TypeElementPedagogique.Module);
                        Module module = new Module();

                        //Parcourir la liste des module
                        for (InscriptionPedagogique inscriptionPedagogique : inscriPedag) {
                            if (inscriptionPedagogique.getResultat() == Resultat.N) {
                                System.out.println("module non validé");

                                //inscrire dans le semestre qui contient ce module
                                module = moduleRestClient.getModule(inscriptionPedagogique.getIdElmPed());
                                System.out.println(module);
                                //semestre qui contint ce module
                                Semestre semestre = filiereRestClient.getSemestresByid(module.getIdSemestre());
                                // verification que le semestre n'existe pas dans l'inscription admin courante
                                // à discuter inscription dans le semestre
                                if (inscriptionPedagogiqueRepository.findByIdElmPedAndIdInscripAdmin(semestre.getIdSemestre(), inscriptionAdministrative.getIdInsAdmin()) == null) {
                                    InscriptionPedagogique inscripPed = new InscriptionPedagogique();
                                    inscripPed.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
                                    inscripPed.setIdElmPed(semestre.getIdSemestre());
                                    inscripPed.setTypeelp(TypeElementPedagogique.Semestre);
                                    inscripPed.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                                    inscriptionPedagogiqueRepository.save(inscripPed);
                                }
                                //etape

                                //inscription dans le module
                                InscriptionPedagogique inscripPed2 = new InscriptionPedagogique();
                                inscripPed2.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
                                inscripPed2.setIdElmPed(module.getIdModule());
                                inscripPed2.setTypeelp(TypeElementPedagogique.Module);
                                inscripPed2.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                                inscriptionPedagogiqueRepository.save(inscripPed2);


                            }
                            //module  validé
                            else if (inscriptionPedagogique.getResultat() == Resultat.V || inscriptionPedagogique.getResultat() == Resultat.VC){
                                System.out.println("************************************************************");
                                System.out.println("Module validé");
                                module = moduleRestClient.getModule(inscriptionPedagogique.getIdElmPed());
                                System.out.println("test erreur --- -- - - ");
                                System.out.println(module);
                                //semestre qui contint ce module
                                Semestre semestre = filiereRestClient.getSemestresByid(module.getIdSemestre());
                                System.out.println(semestre);

                                String lib = semestre.getLibCourt();
                                System.out.println(lib);

                                String[] sems = {"S1", "S2", "S3", "S4", "S5", "S6"};
                                Etape etapepred = filiereRestClient.getEtapeById(inscriptionAdminRestClient.GetIdEtapeFromIdInscAdmin(inscriptadminpred1.getIdInsAdmin()));
                                int libetap = etapepred.getLibEtape() + 1;
                                Long idfiliere = inscriptadminpred1.getIdFiliere();
                                Long idetape = filiereRestClient.findIdetapeByFiliere_IdAndLibEtape(idfiliere, libetap);
                                Semestre semestreParallele = filiereRestClient.getSemestresByEtapeandlib(idetape, sems[Arrays.binarySearch(sems, lib) + 2]);
                                System.out.println(semestreParallele);
                                // verification que le semestre n'existe pas dans l'inscription admin courante
                                if (inscriptionPedagogiqueRepository.findByIdElmPedAndIdInscripAdmin(semestreParallele.getIdSemestre(), inscriptionAdministrative.getIdInsAdmin()) == null) {
                                    InscriptionPedagogique inscripPed3 = new InscriptionPedagogique();
                                    inscripPed3.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
                                    inscripPed3.setIdElmPed(semestreParallele.getIdSemestre());
                                    inscripPed3.setTypeelp(TypeElementPedagogique.Semestre);
                                    inscripPed3.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                                    inscriptionPedagogiqueRepository.save(inscripPed3);
                                }
                                // liste des modules du nouveau semestre

                                List<Module> modules = moduleRestClient.listeModulesSemestreFiliere(semestreParallele.getIdSemestre(),idfilierec);
                                System.out.println("liste des modules du nouveau semestre");
                                System.out.println(modules);
                                List<Module> modulesNv = new ArrayList<>();
                                for (Module moduletest : modules) {
                                    InscriptionPedagogique test = inscriptionPedagogiqueRepository.findByIdElmPedAndIdInscripAdmin(moduletest.getIdModule(), inscriptadminpred1.getIdInsAdmin());
                                    System.out.println(test);
                                    if ((test == null) || (test.getResultat() == Resultat.N)) {
                                        modulesNv.add(moduleRestClient.getModule(moduletest.getIdModule()));

                                    }
                                }

                                Random rand = new Random();
                                Module nvModule = modulesNv.get(rand.nextInt(modulesNv.size()));
                                if (inscriptionPedagogiqueRepository.findByIdElmPedAndIdInscripAdmin(nvModule.getIdModule(), inscriptionAdministrative.getIdInsAdmin()) == null) {
                                    InscriptionPedagogique inscripPed3 = new InscriptionPedagogique();
                                    inscripPed3.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
                                    inscripPed3.setIdElmPed(nvModule.getIdModule());
                                    inscripPed3.setTypeelp(TypeElementPedagogique.Module);
                                    inscripPed3.setAnneeuniv(inscriptionAdministrative.getAnneeUniv());
                                    inscriptionPedagogiqueRepository.save(inscripPed3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @GetMapping(path = "/inscriptionpedagogique/{id}")
    public InscriptionPedagogique getInscriptionByid (@PathVariable Long id){
        return inscriptionPedagogiqueRepository.findInscriptionPedagogiqueById(id);    }

    @GetMapping(path = "/inscriptionpedagogiquebytypeelm/{id}/{type_element}")
    public List <InscriptionPedagogique> getInscriptionByidAndTypeElm(@PathVariable Long id , @PathVariable String type_element){
        TypeElementPedagogique a=null;
        type_element=type_element.toLowerCase();
        if (type_element.equals("etape") ){
            a=TypeElementPedagogique.Etape;
        }
        else if (type_element.equals("semestre")){
            a=TypeElementPedagogique.Semestre;
        }
        else if ( type_element.equals("module"))
        {
            a=TypeElementPedagogique.Module;
        }
        
        return inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByIdInscripAdminAndTypeelp(id,a);
        }

@GetMapping (path = "/test/{id}/{idelm}")
public  List<InscriptionPedagogique> test (@PathVariable Long id , @PathVariable Long idelm){
   return inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByIdAndAndIdElmPed(id,idelm);

}


    @GetMapping(path = "/Allinscriptionpedagogique/")
    public List<InscriptionPedagogique> getInscriptionsPedagogique (){
        return inscriptionPedagogiqueRepository.findAll();
    }

    //List des étudiants inscrit pédagogiquement dans un module
    @GetMapping(path = "/etudiantsInscritPedagModule/{idFiliere}/{idModule}/{annee}")
    public List<EtudiantInscAdmin> etudiantsInscritPedagModule(@PathVariable Long idFiliere ,@PathVariable Long idModule, @PathVariable int annee){
        List<InscriptionPedagogique> list = inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByTypeelpAndIdElmPedAAndAnneeuniv(TypeElementPedagogique.Module,idModule, annee);
//        N.B : Si deux Filiere peuvent avoir le meme module il faut ajouter le idFiliere
        return getEtudiantInscAdminList(idFiliere, list);
    }

    //List des etudiants inscrit pédagogiquement dans un semestre
    @GetMapping(path = "/etudiantsInscritPedagSemestre/{idSemestre}/{idFiliere}")
    public List<EtudiantInscAdmin> etudiantsInscritPedagSemestre(@PathVariable Long idSemestre, @PathVariable Long idFiliere){

        List<InscriptionPedagogique> list = inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByTypeelpAndIdElmPedAAndAnneeuniv(
                TypeElementPedagogique.Semestre,idSemestre, anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
//        System.out.println(list);
        return getEtudiantInscAdminList(idFiliere, list);
    }

    //List des etudiants inscrit pédagogiquement dans une etape
    @GetMapping(path = "/etudiantsInscritPedagEtape/{idEtape}/{idFiliere}")
    public List<EtudiantInscAdmin> etudiantsInscritPedagEtape(@PathVariable Long idEtape, @PathVariable Long idFiliere){

        List<InscriptionPedagogique> list = inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByTypeelpAndIdElmPedAAndAnneeuniv(
                TypeElementPedagogique.Etape,idEtape, anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
//        System.out.println(list);
        return getEtudiantInscAdminList(idFiliere, list);
    }
    private List<EtudiantInscAdmin> getEtudiantInscAdminList(@PathVariable Long idFiliere, List<InscriptionPedagogique> list) {
        List<EtudiantInscAdmin> list1 = Collections.emptyList();
        List list2 = new ArrayList<>(list1);
        for (InscriptionPedagogique ins:list){
            Long idInscripAdmin = ins.getIdInscripAdmin();
//            System.out.println(idInscripAdmin);
            EtudiantInscAdmin etudiant = inscriptionAdminRestClient.etudiantParInscriptionAdmin(idInscripAdmin);
            if(etudiant.getIdFiliere()==idFiliere){
                list2.add(etudiant);
            }
        }
        return list2;
    }
    //Avoir le semestre d'un module
    @GetMapping(path = "/semestreModuleIs/{idModule}")
    public Semestre semestreModuleIs(@PathVariable Long idModule){
        Module module = moduleRestClient.getModule(idModule);
        return filiereRestClient.getSemestresByid(module.getIdSemestre());
    }

    //Avoir tous les modules ayant le résultat null d'un etudiant de l'annee en cours
    @GetMapping(path="/tousLesModulesNonEncoreRempli/{idEtudiant}")
    List<InscriptionPedagogique> tousLesModulesNonEncoreRempli(@PathVariable Long idEtudiant){
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdminRestClient.getInsByIdEtudiantAnnee(idEtudiant,anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
        Long idInscriptionAdministrative = inscriptionAdministrative.getIdInsAdmin();
        List<InscriptionPedagogique> list = getInscriptionByidAndTypeElm(idInscriptionAdministrative, String.valueOf(TypeElementPedagogique.Module));
        List<InscriptionPedagogique> list1 = Collections.emptyList();
        List list2 = new ArrayList<>(list1);
        for (InscriptionPedagogique ins:list){
            Resultat resultat = ins.getResultat();
            if (resultat == null){
                list2.add(ins);
            }
        }
        return list2;
    }
    //Avoir tous les modules ayant le résultat null d'un etudiant d'un semestre de l'annee en cours
    @GetMapping(path="/tousLesModulesNonEncoreRempliSemestre/{idEtudiant}/{idSemestre}")
    List<InscriptionPedagogique> tousLesModulesNonEncoreRempliSemestre(@PathVariable Long idEtudiant, @PathVariable Long idSemestre){
        List<InscriptionPedagogique> inscriptionPedagogiqueList = tousLesModulesNonEncoreRempli(idEtudiant);
        List<InscriptionPedagogique> list1 = Collections.emptyList();
        List list2 = new ArrayList<>(list1);
        for (InscriptionPedagogique ins:inscriptionPedagogiqueList){
            Module module = moduleRestClient.getModule(ins.getIdElmPed());
            if ( module.getIdSemestre()== idSemestre){
                list2.add(ins);
            }
        }
        return list2;
    }

//Avoir les modules ouverts d'un etudiant pour un semestre bien défini pour l'annee courante
    @GetMapping(path = "/modulesEtudiantSemestre/{idEtu}/{idSem}")
    public List<Module> modulesEtudiantSemestre(@PathVariable Long idEtu, @PathVariable Long idSem){
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdminRestClient.getInsByIdEtudiantAnnee(idEtu,anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
        List<InscriptionPedagogique> list = inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByIdInscripAdminAndTypeelpAndAnneeuniv(
                inscriptionAdministrative.getIdInsAdmin(),TypeElementPedagogique.Module,anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
        List<Module> modules = new ArrayList<>();
        for (InscriptionPedagogique ins:list){
            Module module = moduleRestClient.getModule(ins.getIdElmPed());
            if(module.getIdSemestre()==idSem){
                modules.add(module);
            }
        }

        //N/B Le nombre de module n'est pas toujours égale au 6 (nombre de module d'un semestre)
        return modules;
    }

    //Avoir les semestres ouverts d'un etudiant pour une etape (et une filiere ) bien défini pour l'annee courante
    @GetMapping(path = "/semestresEtudiantEtape/{idEtu}/{idEtape}")
    public List<Semestre> semestresEtudiantEtape(@PathVariable Long idEtu, @PathVariable Long idEtape){
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdminRestClient.getInsByIdEtudiantAnnee(idEtu,anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
        List<InscriptionPedagogique> list = inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByIdInscripAdminAndTypeelpAndAnneeuniv(
                inscriptionAdministrative.getIdInsAdmin(),TypeElementPedagogique.Semestre,anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
        List<Semestre> semestres = new ArrayList<>();
        for (InscriptionPedagogique ins:list){
            Semestre semestre = filiereRestClient.getSemestresByid(ins.getIdElmPed());
            Etape etape = filiereRestClient.getEtapeById(idEtape);
            //Liste des semestres d'une etape
            List<Semestre> semestreList =filiereRestClient.semestresEtapeFiliere(inscriptionAdministrative.getIdFiliere(), etape.getLibEtape());
            if(semestreList.contains(semestre)){
                semestres.add(semestre);
            }
        }
        //N/B Le nombre de semestre n'est pas toujours égale à 2 (nombre de semestre d'une etape)
        return semestres;
    }
    //Avoir tous les inscriptions pedagogique/module d'un etudiant
    @GetMapping(path = "/inscriptionEtudiantModule/{idInscripAd}/{idModule}")
    public List<InscriptionPedagogique> inscriptionEtudiantModule(@PathVariable Long idInscripAd, @PathVariable Long idModule){
        return inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByIdEtudAndAndIdElmPedAndTypeelp(idInscripAd,idModule,TypeElementPedagogique.Module);
    }
    //Remarque :
    //Si un module nv dans une année et la réinscription dans ce module aura lieu dans une année ultérieure
    //Là, et pour faire la délibération de semestre il faut prendre le maximum des notes enregistré

    //Avoir tous les inscriptions pedagogique/semestre d'un etudiant
    @GetMapping(path="/getInscriptionPedagogiqueSemestreResultatNote/{idInscripAd}/{idSemestre}")
    public List<InscriptionPedagogique> getInscriptionPedagogiqueSemestreResultatNote(@PathVariable Long idInscripAd, @PathVariable Long idSemestre){
        return inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByIdEtudAndAndIdElmPedAndTypeelp(idInscripAd,idSemestre,TypeElementPedagogique.Semestre);
    }

    //Avoir tous les inscriptions pedagogique/Etape d'un etudiant
    @GetMapping(path="/getInscriptionPedagogiqueEtapeResultatNote/{idInscripAd}/{idEtape}")
    public List<InscriptionPedagogique> getInscriptionPedagogiqueEtapeResultatNote(@PathVariable Long idInscripAd, @PathVariable Long idEtape){
        return inscriptionPedagogiqueRepository.findInscriptionPedagogiquesByIdEtudAndAndIdElmPedAndTypeelp(idInscripAd,idEtape,TypeElementPedagogique.Etape);
    }



    @PostMapping(path = "/sauvegarderNoteModule/{idEtudiant}/{idModule}")
    public InscriptionPedagogique sauvegarderNoteModule (@PathVariable Long idEtudiant, @PathVariable Long idModule) throws InscriptionPedagogiqueDefinedException {
        EtudiantInscAdmin etudiantInscAdmin = inscriptionAdminRestClient.etudiantInscAdminById(idEtudiant).orElse(null);
        Module module = moduleRestClient.getModule(idModule);
        InscriptionPedagogique inscriptionPedagogiqueRe = new InscriptionPedagogique();
        if(module!=null && etudiantInscAdmin != null){
            InscriptionAdministrative inscriptionAdministrative = inscriptionAdminRestClient.getInsByIdEtudiantAnnee(idEtudiant, anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
            if(inscriptionAdministrative!=null){
                List<InscriptionPedagogique> inscriptionPedagogiques = inscriptionEtudiantModule(inscriptionAdministrative.getIdInsAdmin(),idModule);
                if(!inscriptionPedagogiques.isEmpty()){
                    for (InscriptionPedagogique inscriptionPedagogique : inscriptionPedagogiques){
                        if(inscriptionPedagogique.getAnneeuniv()==anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire()){
                            InscriptionPedagogique inscPeda = deliberationSemestreRestClient.sauvegarderNoteModule(idEtudiant, idModule);
                            if(inscPeda!=null){//probleme
                                fonction(inscriptionPedagogique, inscPeda);
                                inscriptionPedagogiqueRe = inscriptionPedagogique;
                            }else{
                                throw new InscriptionPedagogiqueDefinedException("Les notes de l'etudiant d'id= "+idEtudiant+" n'ont pas encore remplit pour le module d'id= "+idModule+" pour l'annee courante "+anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
                            }
                        } else {
                            throw new InscriptionPedagogiqueDefinedException(" l'etudiant d'id= "+idEtudiant+" n'est pas inscrit pédagogiquement dans le module d'id= "+idModule+" pour l'annee courante "+anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
                        }
                    }
                } else {
                    throw new InscriptionPedagogiqueDefinedException(" l'etudiant d'id= "+idEtudiant+" n'est pas inscrit pédagogiquement dans le module d'id= "+idModule);
                }

            }else {
                throw new InscriptionPedagogiqueDefinedException(" l'etudiant d'id= "+idEtudiant+" n'a pas d'inscription administrative!!");
            }
            return inscriptionPedagogiqueRepository.save(inscriptionPedagogiqueRe);
        } else {
            throw new InscriptionPedagogiqueDefinedException("Le module avec l'id = "+idModule+" ou/et l'etudiant d'id= "+idEtudiant+" n'existent pas!!");
        }
    }

    @PostMapping(path = "/sauvegarderResultatSemestreEtudiant/{idSemestre}/{idEtudiant}")
    public InscriptionPedagogique sauvegarderNoteSemestre (@PathVariable Long idEtudiant, @PathVariable Long idSemestre) throws InscriptionPedagogiqueDefinedException {
        EtudiantInscAdmin etudiantInscAdmin = inscriptionAdminRestClient.etudiantInscAdminById(idEtudiant).orElse(null);
        Semestre semestre = filiereRestClient.getSemestresByid(idSemestre);
        InscriptionPedagogique inscriptionPedagogiqueRe = new InscriptionPedagogique();
        if(semestre!=null && etudiantInscAdmin != null){
            InscriptionAdministrative inscriptionAdministrative = inscriptionAdminRestClient.getInsByIdEtudiantAnnee(idEtudiant, anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
            if(inscriptionAdministrative!=null){
                List<InscriptionPedagogique> inscriptionPedagogiques = getInscriptionPedagogiqueSemestreResultatNote(inscriptionAdministrative.getIdInsAdmin(),idSemestre);
                if(!inscriptionPedagogiques.isEmpty()){
                    for (InscriptionPedagogique inscriptionPedagogique : inscriptionPedagogiques){
                        if(inscriptionPedagogique.getAnneeuniv()==anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire()){
                            InscriptionPedagogique inscPeda = deliberationSemestreRestClient.sauvegarderResultatSemestreEtudiant(idSemestre, idEtudiant);
                            if(inscPeda!=null){//probleme
                                fonction(inscriptionPedagogique, inscPeda);
                                inscriptionPedagogiqueRe = inscriptionPedagogique;
                            }else{
                                throw new InscriptionPedagogiqueDefinedException(" l'etudiant d'id= "+idEtudiant+" n'est pas inscrit pédagogiquement dans le semestre d'id= "+idSemestre+" pour l'annee courante +"+anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
                            }
                        } else {
                            throw new InscriptionPedagogiqueDefinedException(" l'etudiant d'id= "+idEtudiant+" n'est pas inscrit pédagogiquement dans le semestre d'id= "+idSemestre+" pour l'annee courante +"+anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
                        }

                    }

                } else {
                    throw new InscriptionPedagogiqueDefinedException(" l'etudiant d'id= "+idEtudiant+" n'est pas inscrit pédagogiquement dans le semestre d'id= "+idSemestre);
                }

            }else {
                throw new InscriptionPedagogiqueDefinedException(" l'etudiant d'id= "+idEtudiant+" n'a pas d'inscription administrative!!");
            }
        return inscriptionPedagogiqueRepository.save(inscriptionPedagogiqueRe);
        } else {
            throw new InscriptionPedagogiqueDefinedException("La semestre avec l'id = "+idSemestre+" ou/et l'etudiant d'id= "+idEtudiant+" n'existent pas!!");
        }
    }
    //Sauvegarder les notes d'etape d'un etudiant inscrit dans cette etape
    @PutMapping (path = "/sauvegarderResultatEtapeEtudiant/{idEtape}/{idEtudiant}")
    public InscriptionPedagogique sauvegarderNoteEtape (@PathVariable Long idEtudiant, @PathVariable Long idEtape) throws InscriptionPedagogiqueDefinedException {
        EtudiantInscAdmin etudiantInscAdmin = inscriptionAdminRestClient.etudiantInscAdminById(idEtudiant).orElse(null);
        Etape etape = filiereRestClient.getEtapeById(idEtape);
//        Semestre semestre = filiereRestClient.getSemestresByid(idSemestre);
        InscriptionPedagogique inscriptionPedagogiqueRe = new InscriptionPedagogique();
        if(etape!=null && etudiantInscAdmin != null){
            InscriptionAdministrative inscriptionAdministrative = inscriptionAdminRestClient.getInsByIdEtudiantAnnee(idEtudiant, anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
            if(inscriptionAdministrative!=null){
                List<InscriptionPedagogique> inscriptionPedagogiques = getInscriptionPedagogiqueEtapeResultatNote(inscriptionAdministrative.getIdInsAdmin(),idEtape);
                if(!inscriptionPedagogiques.isEmpty()){
                    for (InscriptionPedagogique inscriptionPedagogique : inscriptionPedagogiques){
                        if(inscriptionPedagogique.getAnneeuniv()==anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire()){
                            InscriptionPedagogique inscPeda = deliberationAnnuelleRestClient.sauvegarderResultatEtapeEtudiant(idEtape, idEtudiant);
//                            InscriptionPedagogique inscPeda = deliberationSemestreRestClient.sauvegarderResultatSemestreEtudiant(idSemestre, idEtudiant);
                            if(inscPeda!=null){//probleme
                                fonction(inscriptionPedagogique, inscPeda);
                                inscriptionPedagogiqueRe = inscriptionPedagogique;
                            }else{
                                throw new InscriptionPedagogiqueDefinedException(" l'etudiant d'id= "+idEtudiant+" n'est pas inscrit pédagogiquement dans l'étape  d'id= "+idEtape+" pour l'annee courante +"+anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
                            }
                        } else {
                            throw new InscriptionPedagogiqueDefinedException(" l'etudiant d'id= "+idEtudiant+" n'est pas inscrit pédagogiquement dans l'étape d'id= "+idEtape+" pour l'annee courante +"+anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
                        }

                    }

                } else {
                    throw new InscriptionPedagogiqueDefinedException(" l'etudiant d'id= "+idEtudiant+" n'est pas inscrit pédagogiquement dans l'étape d'id= "+idEtape);
                }

            }else {
                throw new InscriptionPedagogiqueDefinedException(" l'etudiant d'id= "+idEtudiant+" n'a pas d'inscription administrative!!");
            }
            //Changer l'etat d'inscription administrative de E vers F dans cette etape de tous les etudiants inscrit dans l'etape
            inscriptionAdminRestClient.FinirIns(inscriptionAdministrative.getIdInsAdmin());
            return inscriptionPedagogiqueRepository.save(inscriptionPedagogiqueRe);
        } else {
            throw new InscriptionPedagogiqueDefinedException("L'etape avec l'id = "+idEtape+" ou/et l'etudiant d'id= "+idEtudiant+" n'existent pas!!");
        }
    }

    //sauvegarder les notes de tous les étudiants inscrits dans une étape
    @PutMapping (path="/sauvegarderResultatEtapeTousEtudiants/{idFiliere}/{idEtape}")
    public void sauvegarderTousNoteEtape (@PathVariable Long idFiliere,@PathVariable Long idEtape) throws InscriptionPedagogiqueDefinedException {
        List<EtudiantInscAdmin> etudiantsList = etudiantsInscritPedagEtape(idEtape,idFiliere);
        for (EtudiantInscAdmin etudiantInscAdmin:etudiantsList){
            sauvegarderNoteEtape(etudiantInscAdmin.getId(),idEtape);
        }
    }

    private void fonction(InscriptionPedagogique inscriptionPedagogique, InscriptionPedagogique inscriptionPedagogique2) throws InscriptionPedagogiqueDefinedException {
        if (inscriptionPedagogique.getTypeelp()==inscriptionPedagogique2.getTypeelp()
                && inscriptionPedagogique.getIdElmPed()==inscriptionPedagogique2.getIdElmPed()
        && inscriptionPedagogique.getTypeelp()==inscriptionPedagogique2.getTypeelp()
        && inscriptionPedagogique.getAnneeuniv()==anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire()){
            inscriptionPedagogique.setNote(inscriptionPedagogique2.getNote());
            inscriptionPedagogique.setResultat(inscriptionPedagogique2.getResultat());
        }else{
            throw new InscriptionPedagogiqueDefinedException("Erreur !!!");
        }

    }




}

