package com.example.serviceDeliberationAnnuelle.service;

import com.example.serviceDeliberationAnnuelle.Enumeration.EtatIns;
import com.example.serviceDeliberationAnnuelle.Enumeration.Resultat;
import com.example.serviceDeliberationAnnuelle.Enumeration.TypeElementPedagogique;
import com.example.serviceDeliberationAnnuelle.Exception.DeliberationEtapeDefinedException;
import com.example.serviceDeliberationAnnuelle.feign.*;
import com.example.serviceDeliberationAnnuelle.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EtapeService {
    private DeliberationOrdinaireRestClient deliberationOrdinaireRestClient;
    private DeliberationRattRestClient deliberationRattRestClient;
    private AnneeUniverRestClient anneeUniverRestClient;
    private FiliereRestClient filiereRestClient;
    private InscriptionAdminRestClient inscriptionAdminRestClient;
    private ModuleRestClient moduleRestClient;
    private InscriptionPedagogiqueRestClient inscriptionPedagogiqueRestClient;



    public EtapeService(DeliberationOrdinaireRestClient deliberationOrdinaireRestClient, DeliberationRattRestClient deliberationRattRestClient, AnneeUniverRestClient anneeUniverRestClient, FiliereRestClient filiereRestClient,
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

    //Avant de commencer il faut prendre en considération que la délibération d'etape n'aura lieu que si
    // la délibération semestre de tous les semestres de l'etape eut lieu.
    //Apres on recuppere les notes des semestres de micro-service inscription pédagogique
    //et finalement on passe à la délibération de l'année.


    public InscriptionPedagogique sauvegarderResultatEtapeEtudiant(Long idEtudiant, Long idEtape){
        Etape etape = filiereRestClient.getEtapeById(idEtape);
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdminRestClient.inscriptionAdminEtudiantAnneE(idEtudiant,anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire(),
                EtatIns.E);
        InscriptionPedagogique inscriptionPedagogique = new InscriptionPedagogique();
        List<Semestre> semestreList = filiereRestClient.listeSemestresEtapeFiliere(idEtape, inscriptionAdministrative.getIdFiliere()); // Tous les semestres d'une etape d'une filiere
        float NotesSemestres = 0;
        float Cof = 0;
        int cntSemestre = 0;
        float cntSemestreVC=0;
        for (Semestre sem:semestreList){ // on parcourt tous les semestres de l'etape
            float noteSemestre=0;
            List<InscriptionPedagogique> inscriptionPedagogiqueSemestre = inscriptionPedagogiqueRestClient.getInscriptionPedagogiqueSemestreResultatNote
                    (inscriptionAdministrative.getIdInsAdmin(), sem.getIdSemestre());
            for (InscriptionPedagogique ins:inscriptionPedagogiqueSemestre){//On parcourt les inscriptions dans chaque semestre
                if(noteSemestre<ins.getNote()){
                    noteSemestre=ins.getNote();
                }
                if (ins.getResultat()==Resultat.VC){
                    cntSemestreVC++;
                }
            }
            NotesSemestres += noteSemestre*sem.getCofSem();
            Cof+=sem.getCofSem();
            if(noteSemestre<sem.getNotMin() || cntSemestreVC>0){
                cntSemestre++;
            }
        }
        float notEtape = NotesSemestres/Cof;
        inscriptionPedagogique.setTypeelp(TypeElementPedagogique.Etape);
        inscriptionPedagogique.setIdElmPed(idEtape);
        inscriptionPedagogique.setAnneeuniv(anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
        inscriptionPedagogique.setIdInscripAdmin(inscriptionAdministrative.getIdInsAdmin());
        inscriptionPedagogique.setNote(notEtape);

        if (notEtape<etape.getNotMin()){
            inscriptionPedagogique.setResultat(Resultat.N);
        } else {
            if (cntSemestre>0){
                inscriptionPedagogique.setResultat(Resultat.VC);
            }else{
                inscriptionPedagogique.setResultat(Resultat.V);
            }
        }
        return inscriptionPedagogique;
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

    public boolean testSemestre(Long idSemestre, Long idEtudiant) {
            int t1;
            List<Module> modules = inscriptionPedagogiqueRestClient.modulesEtudiantSemestre(idEtudiant,idSemestre);
            t1 = (int) modules.stream().filter(module -> !testModule(module.getIdModule())).count();
            return t1==0;
    }
    public int testEtape(Long idEtape, Long idEtudiant) throws DeliberationEtapeDefinedException{
        Etudiant etudiant=inscriptionAdminRestClient.etudiantInscAdminById(idEtudiant).orElse(null);
        if (etudiant != null){
            int t1;
//            List<Semestre> semestres = filiereRestClient.semestresEtapeFiliere(idfiliere, etape1.getLibEtape());
            List<Semestre> semestres = inscriptionPedagogiqueRestClient.semestresEtudiantEtape(idEtudiant,idEtape);
            if (!semestres.isEmpty()){
                t1 = (int) semestres.stream().filter(semestre -> !testSemestre(semestre.getIdSemestre(),idEtudiant)).count();
                return t1;
            } else {
                throw new DeliberationEtapeDefinedException("L'etudiant avec l'id "+ idEtudiant+" n'est pas inscrit pedagogiquement dans l'etape d'id= "+idEtape);
            }

        } else {
            throw new DeliberationEtapeDefinedException("L'etudiant avec l'id "+ idEtudiant+" n'est pas inscrit administrativement!!");
        }
    }

    public List<ResultatEtape> delibirationEtape(Long idEtape, Long idFiliere) throws DeliberationEtapeDefinedException {
        List<Etudiant> etudiantList = inscriptionPedagogiqueRestClient.etudiantsInscritPedagEtape(idEtape,idFiliere);
        Etape etape = filiereRestClient.getEtapeById(idEtape);
        Filiere filiere = filiereRestClient.getFiliereById(idFiliere).orElse(null);
        if(etape!=null && filiere != null){
            List<ResultatEtape> resultatsEtape = new ArrayList<>();
            for (Etudiant etudiant:etudiantList){
                ResultatEtape resultatEtape = new ResultatEtape();
                InscriptionAdministrative inscriptionAdministrative = inscriptionAdminRestClient.getInsByIdEtudiantAnnee(etudiant.getId(), anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
//                System.out.println(idEtape+"////"+inscriptionAdministrative.getIdFiliere());
                List<Semestre> semestreList = filiereRestClient.listeSemestresEtapeFiliere(idEtape, inscriptionAdministrative.getIdFiliere()); // Tous les semestres d'une etape d'une filiere
                List<NoteSemestre> notesSemestre1 = new ArrayList<>();
                for (Semestre semestre:semestreList){
                    NoteSemestre noteSemestre = new NoteSemestre();
                    List<InscriptionPedagogique> pedagogiqueList = inscriptionPedagogiqueRestClient.getInscriptionPedagogiqueSemestreResultatNote
                            (inscriptionAdministrative.getIdInsAdmin(), semestre.getIdSemestre());
                    float noteS=0;
                    for (InscriptionPedagogique ins:pedagogiqueList){
                        if(noteS<ins.getNote()){
                            noteS=ins.getNote();
                            noteSemestre.setAnneeS(ins.getAnneeuniv());
                        }
                    }
                    noteSemestre.setNoteSemestreF(noteS);
                    noteSemestre.setLibCourt(semestre.getLibCourt());
                    noteSemestre.setLibLong(semestre.getLibLong());
                    notesSemestre1.add(noteSemestre);
                }

                resultatEtape.setEtudiantCne(etudiant.getCNE());
                resultatEtape.setEtudiantNom(etudiant.getName());
                resultatEtape.setEtudiantPrenom(etudiant.getLastname());
                resultatEtape.setNoteSemestres(notesSemestre1);
                resultatEtape.setEtapeLib(etape.getLibEtape());
                resultatEtape.setAnnee(anneeUniverRestClient.getAnneeCourante().getNomAnneeUniversitaire());
                //Pensez à lier la filiere avec professeur, au lieu de nom on avoir l'id prof
                resultatEtape.setResponsable(filiere.getResponsableFiliere());
                resultatEtape.setFiliere(filiere.getLibLong());

                List<InscriptionPedagogique> inscriptionPedagogiques = inscriptionPedagogiqueRestClient.getInscriptionPedagogiqueEtapeResultatNote(inscriptionAdministrative.getIdInsAdmin(),idEtape);
                float noteE=0;
                for (InscriptionPedagogique ins:inscriptionPedagogiques){
                    if(noteE<ins.getNote()){
                        noteE=ins.getNote();
                        resultatEtape.setMoyenneEtape(noteE);
                        resultatEtape.setResultat(ins.getResultat());
                    }
                }
                resultatsEtape.add(resultatEtape);
            }
            return resultatsEtape;
        } else {
            throw new DeliberationEtapeDefinedException("La filiere avec l'id= "+idFiliere+" ou/et l'etape' d'id= "+idEtape+" n'existent pas!!");
        }
    }

}


