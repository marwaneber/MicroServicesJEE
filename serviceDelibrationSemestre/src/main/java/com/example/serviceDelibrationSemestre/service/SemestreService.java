package com.example.serviceDelibrationSemestre.service;

import com.example.serviceDelibrationSemestre.Enumeration.EtatIns;
import com.example.serviceDelibrationSemestre.Enumeration.Resultat;
import com.example.serviceDelibrationSemestre.Enumeration.TypeElementPedagogique;
import com.example.serviceDelibrationSemestre.Exception.DeliberationSemestreDefinedException;
import com.example.serviceDelibrationSemestre.feign.*;
import com.example.serviceDelibrationSemestre.model.*;
import org.bouncycastle.math.raw.Mod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SemestreService {
    private DeliberationOrdinaireRestClient deliberationOrdinaireRestClient;
    private DeliberationRattRestClient deliberationRattRestClient;
    private AnneeUniverRestClient anneeUniverRestClient;
    private FiliereRestClient filiereRestClient;
    private InscriptionAdminRestClient inscriptionAdminRestClient;
    private ModuleRestClient moduleRestClient;
    private InscriptionPedagogiqueRestClient inscriptionPedagogiqueRestClient;



    public SemestreService(DeliberationOrdinaireRestClient deliberationOrdinaireRestClient, DeliberationRattRestClient deliberationRattRestClient, AnneeUniverRestClient anneeUniverRestClient, FiliereRestClient filiereRestClient,
                           InscriptionAdminRestClient inscriptionAdminRestClient, ModuleRestClient moduleRestClient,
                           InscriptionPedagogiqueRestClient inscriptionPedagogiqueRestClient) {
        this.deliberationOrdinaireRestClient = deliberationOrdinaireRestClient;
        this.deliberationRattRestClient = deliberationRattRestClient;
        this.anneeUniverRestClient = anneeUniverRestClient;
        this.filiereRestClient = filiereRestClient;
        this.inscriptionAdminRestClient = inscriptionAdminRestClient;
        this.moduleRestClient = moduleRestClient;
        this.inscriptionPedagogiqueRestClient = inscriptionPedagogiqueRestClient;
    }

    //Avant de commencer il faut prendre en considération que la délibération de semestre n'aura lieu que si
    // la délibération rattrapage de tous les elements aura lieu.
    //Apres on enregistre les notes des modules dans le micro-service inscription pédagogique (pour l'inscription)
    //Et au meme temps on pourra passer à la délibération de semestre
    //puis enregistre les notes des semestres dans le micro-service inscription pédagogique
    //et finalement on passe à la délibération de l'année.

    public InscriptionPedagogique sauvegarderNoteModule(Long idEtudiant, Long idModule) throws DeliberationSemestreDefinedException {
        InscriptionPedagogique inscriptionPedagogique = new InscriptionPedagogique();
        inscriptionPedagogique.setAnneeuniv(anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
        inscriptionPedagogique.setTypeelp(TypeElementPedagogique.Module);
        Module module = moduleRestClient.getModule(idModule);
        if (module != null){
            inscriptionPedagogique.setIdElmPed(idModule);
        } else{
            throw new DeliberationSemestreDefinedException("le module "+module.getLibLong()+"n'existe pas!!");
        }
        List<Element> elements = moduleRestClient.getElementsBymodule(idModule);

        InscriptionAdministrative inscriptionAdministrative = inscriptionAdminRestClient.inscriptionAdminEtudiantAnneE(idEtudiant,anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire(), EtatIns.E);
        Etudiant etudiant = inscriptionAdminRestClient.etudiantParInscriptionAdmin(inscriptionAdministrative.getIdInsAdmin());
        //Est ce que cet etudiant inscrit pédagogiquement dans cet element?
        List<Etudiant> etudiantList = inscriptionPedagogiqueRestClient.etudiantsInscritPedagModule(etudiant.getIdFiliere(),idModule,anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
        if (etudiantList.contains(etudiant)){
            inscriptionPedagogique.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
            float NotesElement = 0;
            float Cof = 0;
            int cntElemNV = 0;
            for (Element ele:elements){
                NoteElementModuleO noteElementModuleO;
                noteElementModuleO = deliberationOrdinaireRestClient.noteElementModule(idEtudiant, ele.getIdElement());
                if(noteElementModuleO !=null){
                    if(noteElementModuleO.getNoteElement()<ele.getNoteMin()){ //Un module Ratt ou bien NV
                        NoteElementModuleR noteElementModuleR = deliberationRattRestClient.noteElementModule(idEtudiant, ele.getIdElement());
                        if(noteElementModuleR !=null){
                            NotesElement += noteElementModuleR.getNoteElementFinale()*ele.getCoefElem();
                            Cof += ele.getCoefElem();
                            if (noteElementModuleR.getNoteElementFinale()<ele.getNoteMin()){
                                cntElemNV++;
                            }
                        } else {
                            throw new DeliberationSemestreDefinedException("la note rattrapage de l'element "+ele.getLibLong()+"n'existe pas!!");
                        }
                    }else {
                        NotesElement += noteElementModuleO.getNoteElement()*ele.getCoefElem();
                        Cof += ele.getCoefElem();
                    }
                }  else {
                    throw new DeliberationSemestreDefinedException("la note ordinaire de l'element "+ele.getLibLong()+" n'existe pas!!");
                }
            }
            float notModuleF = NotesElement/Cof;
            inscriptionPedagogique.setNote(notModuleF);
            if(notModuleF<module.getNoteMin()){
                inscriptionPedagogique.setResultat(Resultat.N);
            } else {
                if (cntElemNV>0){
                    inscriptionPedagogique.setResultat(Resultat.VC);
                } else {
                    inscriptionPedagogique.setResultat(Resultat.V);
                }
            }
        } else {
            throw new DeliberationSemestreDefinedException("L'etudiant avec le CNE =  "+etudiant.getCNE()+"n'est pas inscrit dans ce module : "+module.getLibLong());
        }
        return inscriptionPedagogique;
    }

    public InscriptionPedagogique sauvegarderResultatSemestreEtudiant(Long idEtudiant, Long idSemestre) throws DeliberationSemestreDefinedException{
        //Est ce que le semestre est bien un semestre de la filiere en question
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdminRestClient.inscriptionAdminEtudiantAnneE(idEtudiant,anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire(),
                EtatIns.E);
        if (inscriptionAdministrative!=null){
        Etape etape = filiereRestClient.getEtapeById(inscriptionAdministrative.getIdEtape());
        //Controler ici
        List<Semestre> semestres = filiereRestClient.semestresEtapeFiliere(inscriptionAdministrative.getIdFiliere(), etape.getLibEtape());
        Semestre semestre = filiereRestClient.getSemestresByid(idSemestre);
        if (semestres.contains(semestre)){
            InscriptionPedagogique inscriptionPedagogique = new InscriptionPedagogique();
            List<Module> modules = inscriptionPedagogiqueRestClient.modulesEtudiantSemestre(idEtudiant,idSemestre); // Les modules de semestre ouverts pour cette année
            List<Module> moduleList = moduleRestClient.listeModulesSemestreFiliere(idSemestre,inscriptionAdministrative.getIdFiliere()); // Tous les modules du semestre
            // modules.size() <= moduleList.size()
            float NotesModules = 0;
            float Cof = 0;
            int cntModule = 0;
            float cntModuleVC=0;
            for (Module mod:moduleList){ // on parcourt tous les modules du semestre
                float noteModule=0;
                //Les cas possibles :
                //1er Cas : Etudiant inscrit dans tous les modules de ce semestre pour la premiere fois cette année
                // //inscriptionPedagogiqueModule.size() = 1 pour chaque module du semestre
                // Par exemple : Seck inscrit dans le semestre 1 pour la première fois cette année, inscriptionPedagogiqueModule.size() = 1 pour
                // chaque module de S1

                //2eme Cas : Etudiant effectue la n ieme inscription dans m module de ce semestre cette année
                // //inscriptionPedagogiqueModule.size() = n pour les m modules
                // Par exemple : Ahmed inscrit dans le 1 semestre pour la 2 fois cette année avec seulement 2 (M3 et M4) parmi 6
                // modules, donc pour les quatres modules on récupére le max des notes et pour les deux autres on récupére la nots précedente
                // Les résultats sont :
                // M1   12  2020    ;   M2  15  2020
                //M3    8   2020    ;   M4  7   2020
                //M3    7   2022    ;   M4  9   2022
                //M5    14  2020    ;   M6  10.5    2020
                List<InscriptionPedagogique> inscriptionPedagogiqueModule = inscriptionPedagogiqueRestClient.inscriptionEtudiantModule
                        (inscriptionAdministrative.getIdInsAdmin(), mod.getIdModule());
                //inscriptionPedagogiqueModule.size()//Le nombre d'inscription dans le module
                //Avoir tous les inscriptions pedagogique/module d'un etudiant
                for (InscriptionPedagogique ins:inscriptionPedagogiqueModule){//On parcourt les inscriptions dans chaque module
                    //Par exemple pour M1   ;   pour M3 ;   pour M4
                    if(noteModule<ins.getNote()){//0<12; 0<8 ; 0<7; 7<9
                        noteModule=ins.getNote();//=12 ; =8; =7; =9
                    }
                    if (ins.getResultat()==Resultat.VC){
                        cntModuleVC++;//0   0
                    }
                }
                NotesModules += noteModule*mod.getCoef();
                Cof+=mod.getCoef();
                if(noteModule<mod.getNoteMin() || cntModuleVC>0){
                    cntModule++;
                }
            }
            float notSemestre = NotesModules/Cof;
            inscriptionPedagogique.setTypeelp(TypeElementPedagogique.Semestre);
            inscriptionPedagogique.setIdElmPed(idSemestre);
            inscriptionPedagogique.setAnneeuniv(anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
            inscriptionPedagogique.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
            inscriptionPedagogique.setNote(notSemestre);

            if (notSemestre<semestre.getNotMin()){
                inscriptionPedagogique.setResultat(Resultat.N);
            } else {
                if (cntModule>0){
                    inscriptionPedagogique.setResultat(Resultat.VC);
                }else{
                    inscriptionPedagogique.setResultat(Resultat.V);
                }
            }
            return inscriptionPedagogique;
        } else{
            throw new DeliberationSemestreDefinedException("Le semestre d'id= "+idSemestre+" n'est pas un semestre de cette filiere d'id="+inscriptionAdministrative.getIdFiliere());
        }
        } else{
            throw new DeliberationSemestreDefinedException("L'etudiant d'id= "+idEtudiant+" n'a pas d'inscription administrative en cours pour cette année ");
        }

    }


    //Cette methode renvoi le nombre d'element incomplet (Manque des notes)
    public boolean testElementO(Long idElement){
        Module module = moduleRestClient.moduleElement(idElement);
        List<NoteElementModuleO> list = deliberationOrdinaireRestClient.avoirTousLesNotesElement(idElement);
        List<Etudiant> etudiantList = inscriptionPedagogiqueRestClient.etudiantsInscritPedagModule(module.getIdFiliere(), module.getIdModule(), anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
        return list.size() == etudiantList.size();
    }

    public boolean testElementR(Long idElement){
        List<NoteElementModuleR> list = deliberationRattRestClient.avoirTousLesNotesElement(idElement);
        List<Etudiant> etudiantList = deliberationOrdinaireRestClient.etudiantsRatt(idElement, anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
        return list.size() == etudiantList.size();
    }

    public boolean testModule(Long idModule){
        int t1,t2;
        List<Element> elements = moduleRestClient.getElementsBymodule(idModule);
        t1 = (int) elements.stream().filter(element -> !testElementO(element.getIdElement())).count();
        t2 = (int) elements.stream().filter(element -> !testElementR(element.getIdElement())).count();
        return t1+t2==0;
    }

    public int testSemestre(Long idSemestre, Long idEtudiant) throws DeliberationSemestreDefinedException{
        Etudiant etudiant = inscriptionAdminRestClient.etudiantInscAdminById(idEtudiant).orElse(null);
        if (etudiant != null){
            int t1;
            List<Module> modules = inscriptionPedagogiqueRestClient.modulesEtudiantSemestre(idEtudiant,idSemestre); // Les modules de semestre ouverts pour cette année
//            List<Module> modules = moduleRestClient.listeModulesSemestreFiliere(idSemestre,etudiant.getIdFiliere());
            t1 = (int) modules.stream().filter(module -> !testModule(module.getIdModule())).count();
            return t1;
        } else {
            throw new DeliberationSemestreDefinedException("L'etudiant avec l'id "+ idEtudiant+" n'est pas inscrit administrativement!!");
        }
    }

    public List<ResultatSemestre> delibirationSmestre(Long idSemestre, Long idFiliere) throws DeliberationSemestreDefinedException {
        List<Etudiant> etudiantList = inscriptionPedagogiqueRestClient.etudiantsInscritPedagSemestre(idSemestre,idFiliere);
        if (!etudiantList.isEmpty()){
            InscriptionAdministrative inscriptionAdministrative2 = inscriptionAdminRestClient.inscriptionAdminEtudiantAnneE(etudiantList.get(0).getId(),
                    anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire(),EtatIns.E);
            Etape etape = filiereRestClient.getEtapeById(inscriptionAdministrative2.getIdEtape());
            List<Semestre> semestres = filiereRestClient.semestresEtapeFiliere(inscriptionAdministrative2.getIdFiliere(), etape.getLibEtape());
            Semestre semestre = filiereRestClient.getSemestresByid(idSemestre);
        if (semestres.contains(semestre)){
        Filiere filiere = filiereRestClient.getFiliereById(idFiliere).orElse(null);
        if(semestre!=null && filiere != null){
            List<ResultatSemestre> resultatsSemestre = new ArrayList<>();
            for (Etudiant etudiant:etudiantList){
                ResultatSemestre resultatSemestre = new ResultatSemestre();
                InscriptionAdministrative inscriptionAdministrative = inscriptionAdminRestClient.getInsByIdEtudiantAnnee(etudiant.getId(), anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
//                List<Module> modules = inscriptionPedagogiqueRestClient.modulesEtudiantSemestre(etudiant.getId(),idSemestre);
                List<Module> moduleList = moduleRestClient.listeModulesSemestreFiliere(idSemestre,inscriptionAdministrative.getIdFiliere()); // Tous les modules du semestre
                List<NoteModule> notesModule1 = new ArrayList<>();
                for (Module module:moduleList){
                    NoteModule noteModule = new NoteModule();
                    List<InscriptionPedagogique> pedagogiqueList = inscriptionPedagogiqueRestClient.inscriptionEtudiantModule
                            (inscriptionAdministrative.getIdInsAdmin(), module.getIdModule());
                    float noteM=0;
                    for (InscriptionPedagogique ins:pedagogiqueList){
                        if(noteM<ins.getNote()){
                            noteM=ins.getNote();
                            noteModule.setAnneeM(ins.getAnneeuniv());//Renvoi l'annee de module ayant la note maximale
                        }
                    }
                    noteModule.setNoteModuleF(noteM);
                    noteModule.setLibCourt(module.getLibCourt());
                    noteModule.setLibLong(module.getLibLong());
                    notesModule1.add(noteModule);
                }

                resultatSemestre.setEtudiantCne(etudiant.getCNE());
                resultatSemestre.setEtudiantNom(etudiant.getName());
                resultatSemestre.setEtudiantPrenom(etudiant.getLastname());
                resultatSemestre.setNoteModules(notesModule1);
                resultatSemestre.setSemestreLibCourt(semestre.getLibCourt());
                resultatSemestre.setSemestreLibLong(semestre.getLibLong());
                resultatSemestre.setAnnee(anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
                //Pensez à lier la filiere avec professeur, au lieu de nom on avoir l'id prof
                resultatSemestre.setResponsable(filiere.getResponsableFiliere());
                resultatSemestre.setFiliere(filiere.getLibLong());

                List<InscriptionPedagogique> inscriptionPedagogiques = inscriptionPedagogiqueRestClient.getInscriptionPedagogiqueSemestreResultatNote(inscriptionAdministrative.getIdInsAdmin(),idSemestre);
                float noteS=0;
                for (InscriptionPedagogique ins:inscriptionPedagogiques){
                    if(noteS<ins.getNote()){
                        noteS=ins.getNote();
                        resultatSemestre.setMoyenneSemestre(noteS);
                        resultatSemestre.setResultat(ins.getResultat());
                    }
                }
                resultatsSemestre.add(resultatSemestre);
            }
            return resultatsSemestre;
        } else {
            throw new DeliberationSemestreDefinedException("La filiere avec l'id= "+idFiliere+" ou/et la semestre d'id= "+idSemestre+" n'existent pas!!");
        }
        } else{
            throw new DeliberationSemestreDefinedException("Le semestre d'id= "+idSemestre+" n'est pas un semestre de cette filiere d'id="+inscriptionAdministrative2.getIdFiliere());
        }
        } else{
            throw new DeliberationSemestreDefinedException("Aucune inscription pedagogique dans le semestre d'id= "+idSemestre);
        }
    }

}


