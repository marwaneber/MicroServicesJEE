package com.example.serviceinscriptionadministrative.web;

import com.example.serviceinscriptionadministrative.Entities.EtudiantInscAdmin;
import com.example.serviceinscriptionadministrative.Entities.InscriptionAdministrative;
import com.example.serviceinscriptionadministrative.Enums.EtatIns;
import com.example.serviceinscriptionadministrative.Exception.InscriptionDefinedException;
import com.example.serviceinscriptionadministrative.Fein.AnneeUniverRestClient;
import com.example.serviceinscriptionadministrative.Fein.EtudiantRestClient;
import com.example.serviceinscriptionadministrative.Fein.FiliereRestClient;
import com.example.serviceinscriptionadministrative.Model.AnneeUniversitaire;
import com.example.serviceinscriptionadministrative.Model.Etape;
import com.example.serviceinscriptionadministrative.Model.Etudiant;
import com.example.serviceinscriptionadministrative.Repository.EtudiantInscAdminRepository;
import com.example.serviceinscriptionadministrative.Repository.InscriptionAdministrativeRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Path;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class InsAdmRestController {
    private InscriptionAdministrativeRepository inscriptionAdministrativeRepository;
    private EtudiantRestClient etudiantRestClient;
    private EtudiantInscAdminRepository etudiantInscAdminRepository;
    private AnneeUniverRestClient anneeUniverRestClient;
    private FiliereRestClient filiereRestClient;

    public InsAdmRestController(InscriptionAdministrativeRepository inscriptionAdministrativeRepository, EtudiantRestClient etudiantRestClient,
                                EtudiantInscAdminRepository etudiantInscAdminRepository, AnneeUniverRestClient anneeUniverRestClient,
                                FiliereRestClient filiereRestClient) {
        this.inscriptionAdministrativeRepository = inscriptionAdministrativeRepository;
        this.etudiantRestClient = etudiantRestClient;
        this.etudiantInscAdminRepository = etudiantInscAdminRepository;
        this.anneeUniverRestClient = anneeUniverRestClient;
        this.filiereRestClient = filiereRestClient;
    }
    //Valider une inscription en ligne passer de etudiantInscrEnligne au etudiantInscAdmin
    @PostMapping(path = "/valider/{id}")
    public EtudiantInscAdmin validerParEtudiant(@PathVariable Long id) throws InscriptionDefinedException {
        AnneeUniversitaire anneeUniversitaire = anneeUniverRestClient.getAnneeCourante();
        Etudiant etudiantInscrEnligne = etudiantRestClient.getStudent(id).orElse(null);
        EtudiantInscAdmin etudiantInscAdmin1 = etudiantInscAdminRepository.findEtudiantByCne(etudiantInscrEnligne.getCNE()).orElse(null);
        if (etudiantInscAdmin1 == null && etudiantInscrEnligne != null) { //L'etudiant n'est pas encours inscrit
            EtudiantInscAdmin etudiantInscAdmin = new EtudiantInscAdmin();
            etudiantInscAdmin.setCNE(etudiantInscrEnligne.getCNE());
            etudiantInscAdmin.setCIN_Passeport(etudiantInscrEnligne.getCIN_Passeport());
            etudiantInscAdmin.setName(etudiantInscrEnligne.getFirstname());
            etudiantInscAdmin.setNameAr(etudiantInscrEnligne.getFirstnameAr());
            etudiantInscAdmin.setLastname(etudiantInscrEnligne.getLastname());
            etudiantInscAdmin.setLastnameAr(etudiantInscrEnligne.getLastnameAr());
            etudiantInscAdmin.setEmail(etudiantInscrEnligne.getEmail());
            etudiantInscAdmin.setTelephone(etudiantInscrEnligne.getTelephone());
            etudiantInscAdmin.setPhoto(etudiantInscrEnligne.getPhoto());
            etudiantInscAdmin.setSexe(etudiantInscrEnligne.getSexe());
            etudiantInscAdmin.setNationalité(etudiantInscrEnligne.getNationalite());
            etudiantInscAdmin.setDateNaissance(etudiantInscrEnligne.getDateNaissance());
            etudiantInscAdmin.setLieuNaissance(etudiantInscrEnligne.getLieuNaissance());
            etudiantInscAdmin.setLieuNaissanceAr(etudiantInscrEnligne.getLieuNaissanceAr());
            etudiantInscAdmin.setVille(etudiantInscrEnligne.getVille());
            etudiantInscAdmin.setProvince(etudiantInscrEnligne.getProvince());
            etudiantInscAdmin.setAnneeBac(etudiantInscrEnligne.getAnneeBac());
            etudiantInscAdmin.setSerieBac(etudiantInscrEnligne.getSerieBac());
            etudiantInscAdmin.setMention(etudiantInscrEnligne.getMention());
            etudiantInscAdmin.setLycee(etudiantInscrEnligne.getLycee());
            etudiantInscAdmin.setLieuBac(etudiantInscrEnligne.getLieuBac());
            etudiantInscAdmin.setAcademie(etudiantInscrEnligne.getAcademie());
            etudiantInscAdmin.setTEM_Fonc(etudiantInscrEnligne.isTEM_Fonc());
            etudiantInscAdmin.setHandicap(etudiantInscrEnligne.isHandicap());
            etudiantInscAdmin.setTEM_JourMois(etudiantInscrEnligne.isTEM_JourMois());
            etudiantInscAdmin.setId_inscription_enligne(etudiantInscrEnligne.getIdStudent());
            etudiantInscAdmin.setDateInscriptionEnligne(etudiantInscrEnligne.getDateInscription());
            //Nous pouvons ajouter des critères sur l'affectation d'une filière parmi les trois choix entrés par l'étudiant
            etudiantInscAdmin.setIdFiliere(etudiantInscrEnligne.getIdFiliere1());
            etudiantInscAdmin.setAnneePremInscr(anneeUniversitaire.getNomAnneeUniversitaire());
            etudiantInscAdminRepository.save(etudiantInscAdmin);
            return etudiantInscAdminRepository.save(etudiantInscAdmin);
        } else {
            throw new InscriptionDefinedException("L'inscription de l'étudiant avec le CNE =  est déjà validée !");
        }
    }

    //Valider l'inscription de tous les érudiants d'une année
//    @PostMapping(path = "/validerParYear/{year}")
//    public void validerParAnnee(@PathVariable int year) throws InscriptionDefinedException {
//        List<Etudiant> etudiantList = etudiantRestClient.findByDateInscription_Year(year);
//        for (Etudiant etudiant : etudiantList) {
//            validerParEtudiant(etudiant.getIdStudent());
//        }
//    }

    //Ajouter une inscription administrative d'un étudiant dans une étape
    @PostMapping(path = "/inscrire/{idEtud}/{et}")
    public InscriptionAdministrative inscriptionAdministrative(@PathVariable Long idEtud
            , @PathVariable int et) throws InscriptionDefinedException {
        //Methode qui renvoi l'etape a partir de son nombre et la filiere en question
        //Par exemple : etape 1 et filiere 3
        //Date de modification 08/01/2023
        EtudiantInscAdmin etudiantInscAdmin = etudiantInscAdminRepository.findById(idEtud).get();
        Etape etape = filiereRestClient.etapeFiliere(etudiantInscAdmin.getIdFiliere(),et);
//        Etape etape = filiereRestClient.etapeByNb(et);
        AnneeUniversitaire anneeUniversitaire = anneeUniverRestClient.getAnneeCourante();
        if (etape.getOI()){
            InscriptionAdministrative inscriptionAdministrative = new InscriptionAdministrative();
            inscriptionAdministrative.setEtudiantInscAdmin(etudiantInscAdmin);
            inscriptionAdministrative.setEtatIns(EtatIns.E);
            inscriptionAdministrative.setAnneeUniv(anneeUniversitaire.getNomAnneeUniversitaire());
            inscriptionAdministrative.setIdEtape(etape.getIdEtape());
            inscriptionAdministrative.setIdFiliere(etudiantInscAdmin.getIdFiliere());
//            inscriptionAdministrative.setEtudiantInscAdmin(etudiantInscAdmin);
//            etudiantInscAdmin.setNouveauEtudiant(false);
//            etudiantInscAdminRepository.save(etudiantInscAdmin);
            return inscriptionAdministrativeRepository.save(inscriptionAdministrative);
        }else {
            throw new InscriptionDefinedException("Cette etape n'est pas ouverte à l'inscription !");
        }
    }

    //Avoir tous les étudiants
    @GetMapping(path = "/etudiantsInscAdmin") //tester
    public List<EtudiantInscAdmin> allEtudiants() {
        return etudiantInscAdminRepository.findAll();
    }
    //Avoir un etudiant by id
    @GetMapping(path="/etudiantsInscAdmin/{idEtudiantInscAdmin}")
    public Optional<EtudiantInscAdmin> etudiantInscAdminById(@PathVariable Long idEtudiantInscAdmin){
        return etudiantInscAdminRepository.findById(idEtudiantInscAdmin);
    }
    //Avoir les étudiants par CNE
    @GetMapping(path = "/etudiantsInscAdminByCne/{cne}") //tester
    public EtudiantInscAdmin findEtudiantByCne(@PathVariable String cne) {
        return etudiantInscAdminRepository.findEtudiantByCne(cne).get();
    }


    //Faire inscrire tous les nouveaux etudiants dans une filiere et dans la première étape
    @PostMapping(path = "/inscrireNouveaux")
    public  List<EtudiantInscAdmin> inscrireNouveaux() {
        List<EtudiantInscAdmin> etudiantList = allEtudiants();
        AnneeUniversitaire anneeUniversitaire = anneeUniverRestClient.getAnneeCourante();
        for (EtudiantInscAdmin etudiantInscAdmin : etudiantList) {
            if (extraireLannee(etudiantInscAdmin.getDateInscriptionEnligne())==anneeUniversitaire.getNomAnneeUniversitaire()) {
                try {
                    inscriptionAdministrative(etudiantInscAdmin.getId(),1);
                } catch (InscriptionDefinedException e) {
                    e.printStackTrace();
                }
                etudiantInscAdminRepository.save(etudiantInscAdmin);
            }
        }
        return allEtudiants();
    }

    public int extraireLannee(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(df.format(date));
        return year;
    }

    @GetMapping(path = "/InscriptionsAdministrative") //tester
    public List<InscriptionAdministrative> listIns() {
        List<InscriptionAdministrative> inscriptionsList = inscriptionAdministrativeRepository.findAll();
        return inscriptionsList;
    }

    @GetMapping(path = "/InscriptionsAdministrative/{id}") //tester
    public InscriptionAdministrative getInscriptionsAdministrativeByid(@PathVariable(name = "id") Long id) {
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdministrativeRepository.findByIdInsAdmin(id).get();
        System.out.println(inscriptionAdministrative);
        return inscriptionAdministrative;
    }

    @PutMapping(path = "/AnnulerInscriptionEtudiant/{idInscription}")
    public void AnnulerIns(@PathVariable Long idInscription) {
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdministrativeRepository.findByIdInsAdmin(idInscription).get();
        inscriptionAdministrative.setEtatIns(EtatIns.A);
        inscriptionAdministrativeRepository.save(inscriptionAdministrative);
    }

    @PutMapping(path = "/FinirInscriptionEtudiant/{idInscription}")
    public void FinirIns(@PathVariable Long idInscription) {
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdministrativeRepository.findByIdInsAdmin(idInscription).get();
        inscriptionAdministrative.setEtatIns(EtatIns.F);
        inscriptionAdministrativeRepository.save(inscriptionAdministrative);
    }

    @PutMapping(path = "/modifyInscription/{idIsc}")
    @ResponseBody
    public InscriptionAdministrative modifyInscription(@PathVariable Long idIsc,
                                                       @RequestBody InscriptionAdministrative inscriptionAdministrative
    ) throws InscriptionDefinedException {
        InscriptionAdministrative oldInscription = inscriptionAdministrativeRepository.findByIdInsAdmin(idIsc).orElse(null);
        if(oldInscription!=null){
            oldInscription.setAnneeUniv(inscriptionAdministrative.getAnneeUniv());
            oldInscription.setEtatIns(inscriptionAdministrative.getEtatIns());
            oldInscription.setEtudiantInscAdmin(inscriptionAdministrative.getEtudiantInscAdmin());
            oldInscription.setIdEtape(inscriptionAdministrative.getIdEtape());
            oldInscription.setIdFiliere(inscriptionAdministrative.getIdFiliere());
            return inscriptionAdministrativeRepository.save(oldInscription);
        }else {
            throw new InscriptionDefinedException("Cette inscription n'existe pas!!");
        }
    }
    @PostMapping("/addIns")
    @ResponseBody
    public void addIns(@RequestBody InscriptionAdministrative inscriptionAdministrative){
        inscriptionAdministrativeRepository.save(inscriptionAdministrative);
    }
    //Avoir tous les inscriptions administrative d'un etudiant
    @GetMapping(path = "/InscriptionsAdministrativesEtudiant/{id_etudiant}") // tester
    public List<InscriptionAdministrative> getInsByIdEtudiant(@PathVariable(name = "id_etudiant")Long idEtudiant)
            throws InscriptionDefinedException{
        EtudiantInscAdmin etudiantInscAdmin = etudiantInscAdminById(idEtudiant).orElse(null);
        if(etudiantInscAdmin != null) {
            return inscriptionAdministrativeRepository.findInscriptionAdministrativeByEtudiantInscAdmin_Id(idEtudiant);
        }else {
            throw new InscriptionDefinedException("Cet étudiant n'existe pas!!");
        }
        }

        //Ajouter par l'équipe : Walid & Youssef ********************
    @GetMapping(path = "/InscriptionsAdministrativeEtudiantAnnee/{id_etudiant}/{annee}")
    public InscriptionAdministrative getInsByIdEtudiantAnnee(@PathVariable(name = "id_etudiant")Long idEtudiant,@PathVariable(name = "annee") int anneeUniv){
        List<InscriptionAdministrative> ins= inscriptionAdministrativeRepository.findInscriptionAdministrativeByEtudiantInscAdmin_Id(idEtudiant);
        InscriptionAdministrative a=new InscriptionAdministrative();
        for (InscriptionAdministrative e:ins){
            if (e.getAnneeUniv()==anneeUniv) {
                if (e.getEtatIns()==EtatIns.E || e.getEtatIns()==EtatIns.F) { ///Pour avoir la possibilité de consulter les notes d'une inscription finie
                    a=e;
                }
            }
        }
        return a;
    }
    @GetMapping(path = "/InsAdminByAnnee/{annee}") // N.B: Seul les inscriptions dont l'état est E //Par REGRAGUI
    public List<InscriptionAdministrative> getInsByAnne(@PathVariable int annee){
        return inscriptionAdministrativeRepository.findInscriptionAdministrativeByAnneeUniv(annee);
    }
    //on va tester cette methode
    @GetMapping(path = "/getIdEtape/{id}")
    public Long GetIdEtapeFromIdInscAdmin(@PathVariable Long id){

        InscriptionAdministrative  inscription= inscriptionAdministrativeRepository.findInscriptionAdministrativeByIdInsAdmin(id);
        return  inscription.getIdEtape();

    }

    @GetMapping(path = "/getIdE/{id}")
    public Long getIde(@PathVariable Long id){
        InscriptionAdministrative inscriptionAdministrative=inscriptionAdministrativeRepository.findInscriptionAdministrativeByIdInsAdmin(id);
        return inscriptionAdministrative.getEtudiantInscAdmin().getId();
    }

    // Fin de modification ********************

    @GetMapping(path="/inscriptionsAdministrativeEncous/{annee}")
    public List<InscriptionAdministrative> inscriptionsAdministrativeEncous(@PathVariable int annee){
        return inscriptionAdministrativeRepository.findInscriptionAdministrativesByAnneeUnivAndAndEtatIns(annee, EtatIns.E);
    }

    //Etudiant par inscription administrative
    @GetMapping(path = "/etudiantParInscriptionAdmin/{idInscAdm}")//tester
    public EtudiantInscAdmin etudiantParInscriptionAdmin(@PathVariable Long idInscAdm){
        return getInscriptionsAdministrativeByid(idInscAdm).getEtudiantInscAdmin();
    }


    //Manipulation des fichier Excel //N'est pas encore tester
    @PostMapping("/addIns/import")
    public void addInscrByFile(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

            InscriptionAdministrative inscription = new InscriptionAdministrative();

            XSSFRow row = worksheet.getRow(i);
            EtudiantInscAdmin etudiantInscAdmin = etudiantInscAdminRepository.findById((long) row.getCell(1).getNumericCellValue()).get();
            inscription.setEtudiantInscAdmin(etudiantInscAdmin);
            inscription.setIdFiliere((long) row.getCell(3).getNumericCellValue());
            inscription.setEtatIns(EtatIns.valueOf((row.getCell(4).getStringCellValue())));
            addIns(inscription);
        }

    }
    @PostMapping("/Annuler/file")
    public void AnnulerInsFromFile(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException, InscriptionDefinedException {

        XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = worksheet.getRow(i);
            Long id_et = (long) row.getCell(1).getNumericCellValue();
            List<InscriptionAdministrative> ins = getInsByIdEtudiant(id_et);
            String Et = "E";
            for (InscriptionAdministrative inscriptionAdministrative: ins){
                if (Et.equals(inscriptionAdministrative.getEtatIns())==true) {
                    AnnulerIns(id_et);
                } else {
                    System.out.println("L'étudiant avec l'ID " + id_et + "n'a aucune inscription en cours");
                }
            }

        }
    }
    @GetMapping(path="/inscriptionAdminEtudiantAnneE/{idEtudiant}/{annee}/{etat}")
    public InscriptionAdministrative inscriptionAdminEtudiantAnneE(@PathVariable Long idEtudiant, @PathVariable int annee, @PathVariable EtatIns etat){
        return inscriptionAdministrativeRepository.findInscriptionAdministrativeByEtudiantInscAdmin_IdAndAnneeUnivAndEtatIns(idEtudiant,annee,etat);
    }
    //Les étudiants d'une filière
    @GetMapping(path="/etudiantsFiliere/{idFiliere}")
        public List<EtudiantInscAdmin> etudiantsFiliere(@PathVariable Long idFiliere){
        return etudiantInscAdminRepository.findEtudiantInscAdminsByIdFiliere(idFiliere);
    }
    //Avoir tous les etudiants d'une filiere et d'une etape
    @GetMapping(path="/etudiantsFilierEtape/{idFiliere}/{idEtape}")
    public List<EtudiantInscAdmin> etudiantInscAdminList(@PathVariable Long idFiliere, @PathVariable Long idEtape){
        List<EtudiantInscAdmin> list = etudiantsFiliere(idFiliere);
        List<EtudiantInscAdmin> list2 = Collections.emptyList();
        List list3 = new ArrayList<>(list2);
        for (EtudiantInscAdmin etudiantInscAdmin:list){
            List<InscriptionAdministrative> list1 = (List<InscriptionAdministrative>) etudiantInscAdmin.getInscriptionAdministratives();
               for (InscriptionAdministrative insc: list1){
                   if(insc.getIdEtape()==idEtape && insc.getEtatIns()==EtatIns.E){
                        list3.add(etudiantInscAdmin);
                   }
               }
        }
        return list3;
    }
}

