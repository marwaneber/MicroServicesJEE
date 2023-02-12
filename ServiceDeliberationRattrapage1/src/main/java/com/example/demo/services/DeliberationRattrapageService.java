package com.example.demo.services;


import org.springframework.stereotype.Service;


@Service
public class DeliberationRattrapageService {
//
//    private NoteElementRepository noteElementRepository;
//    private OrdinaireRestClient ordinaireRestClient;
//    private ModuleRestClient moduleRestClient;
//    private InscriptionAdminRestClient inscriptionAdminRestClient;
//
//
//    public DeliberationRattrapageService(NoteElementRepository noteElementRepository, OrdinaireRestClient ordinaireRestClient, AnneeUniverRestClient anneeUniverRestClient, FiliereRestClient filiereRestClient, InscriptionAdminRestClient inscriptionAdminRestClient, ModuleRestClient moduleRestClient, ProfesseurRestClient professeurRestClient) {
//        this.noteElementRepository = noteElementRepository;
//        this.ordinaireRestClient = ordinaireRestClient;
//        this.moduleRestClient = moduleRestClient;
//        this.inscriptionAdminRestClient = inscriptionAdminRestClient;
//    }
//
//
//    // Traiter les fichiers excel
//    public void importExcelFile(MultipartFile file,Long idFiliere, Long idSemestre, int anneeU,Long idModule,Long idElement ) throws IOException {
//        Module module = moduleRestClient.moduleElement(idElement);
//        Element element = moduleRestClient.elementByID(idElement);
//         // #File file = new File(fileName);
//        //WorkbookFactory.create(file)
//
//        List<NoteElementR> noteElementRS =StudentsFromExcel.noteModuleList(file);
//        noteElementRS.forEach(noteElementR -> {
//            Etudiant etudiant=noteElementR.getEtudiant();
//            noteElementR.setEtudiant(inscriptionAdminRestClient.findEtudiantByCne(etudiant.getCNE()));
//            noteElementR.setElement(element);
//            noteElementR.setModule(module);
////            noteElementR.setNomElement(element.getLibLong());
//            noteElementR.setAnneeU(anneeU);
////            noteElementR.setIdFiliere(idFiliere);
////            noteElementR.setIdSemestre(idSemestre);
//            noteElementR.setIdElement(element.getId());
//            noteElementR.setIdModule(module.getIdModule());
//            NoteElementO noteElementO=ordinaireRestClient.getNoteByladiff(etudiant.getCNE(),idSemestre,idModule,idElement,anneeU);
//
//            noteElementR.setNoteTp(noteElementO.getNoteTP());
//            noteElementR.setNoteCotrole(noteElementO.getNoteControl());
////            noteElementR.setCne(etudiant.getCNE());
//
//            noteElementR.setLadiff(noteElementR.getEtudiant().getCNE()+idSemestre+idModule+idElement+anneeU);
//            double notefTP_R=noteElementO.getNoteTP()*element.getCoefTP();
//            double notefControle_R=noteElementO.getNoteControl()*element.getCoefControl();
//            double noteExam_R=noteElementR.getNoteExamR()*element.getCoefExam();
//            double notefinale_R= notefTP_R+notefControle_R+noteExam_R;
//            noteElementR.setNoteElementR(Math.max(notefinale_R,noteElementO.getNoteElement()));
//
//            if(notefinale_R>=element.getNoteMin()){
//                noteElementR.setValidite("valide");
//            }else{
//                noteElementR.setValidite("Non valide");
//            }
//        });
//
//
//
//        noteElementRepository.saveAll(noteElementRS);
//    }
//
//    // Liste des notes d'un element
//    public List<NoteElementR> listNoteElements(Long idElement, int anneeU) {
//        List<NoteElementR> noteModules = (List<NoteElementR>)noteElementRepository.findAllByIdElementAndAnneeU(idElement,anneeU);
//        noteModules.forEach(noteElementR -> {
//            noteElementR.setEtudiant(inscriptionAdminRestClient.findEtudiantByCne(noteElementR.getCne()));
//        });
//        return noteModules;
//    }


    /*
    public void deleteNote(String filiere, String cne, String anneeu, String nomElemnt) {
        String ladiff=cne+anneeu+nomElemnt;
        noteElementRepository.deleteByFiliereAndLadiff(filiere,ladiff);
    }

    // Ajouter une note d'un element
    public NoteElementR addNote(NoteElementR noteElementR) {
        Element element = new Element(null,0.25f,0.25f,0.25f,10 );
        float notef = noteElementR.getNoteTp()*element.getCoeffTp()
                +noteElementR.getNoteCotrole()*element.getCoeffControl()
                +noteElementR.getNoteExamR()* element.getCoeffExam();
        noteElementR.setNoteFinale(notef);
        if (notef>=element.getX()){
            noteElementR.setValidite("valide");
        }else{
            noteElementR.setValidite("Non valide");
        }
        noteElementR.setLadiff(noteElementR.getCne()+noteElementR.getAnneeU()+noteElementR.getNomElement());
        return noteElementRepository.save(noteElementR);
    }
    // Modifier une note
    public void updateNote(NoteElementR noteElementR, String filiere, String cne, String anneeu, String nomElement) {
        Element element = new Element(null,0.25f,0.25f,0.5f,10);
        float notef= noteElementR.getNoteTp()*element.getCoeffTp()+
                noteElementR.getNoteCotrole()*element.getCoeffControl()+
                noteElementR.getNoteExamR()*element.getCoeffExam();
        noteElementR.setNoteFinale(notef);
        if(notef>=element.getX()){
            noteElementR.setValidite("valide");
        }else {
            noteElementR.setValidite("Non valide");
        }
        noteElementR.setLadiff(noteElementR.getCne()+noteElementR.getAnneeU()+noteElementR.getNomElement());
        noteElementRepository.save(noteElementR);

    }
    // Recuperer une note
    public NoteElementR getNote(String filiere, String cne, String anneeu, String nomElemnt) {
        String ladiff=cne+anneeu+nomElemnt;
        return noteElementRepository.findByFiliereAndLadiff(filiere,ladiff);
    }*/
}
