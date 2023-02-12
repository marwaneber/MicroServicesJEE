package com.example.serviceinscriptionenligne.web;
import com.example.serviceinscriptionenligne.Exception.StudentDefinedException;
import com.example.serviceinscriptionenligne.Model.Etablissement;
import com.example.serviceinscriptionenligne.Repositories.StudentRepository;
import com.example.serviceinscriptionenligne.entities.Student;
import com.example.serviceinscriptionenligne.fiegn.EtablissementRestClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class InscriptionRestController {
    private StudentRepository studentRepository;
    private EtablissementRestClient etablissementRestClient;

    public InscriptionRestController(StudentRepository studentRepository, EtablissementRestClient etablissementRestClient) {
        this.studentRepository = studentRepository;
        this.etablissementRestClient = etablissementRestClient;
    }

    @GetMapping(path = "/etudiants") //tester
    public List<Student> listStudent(){
        return studentRepository.findAll();
    }

    @GetMapping(path = "/etudiants/{id}") //tester
    public Optional<Student> getStudent(@PathVariable Long id){
        return studentRepository.findById(id);}

//    List des étudiants inscrit pour une année
//    @GetMapping(path = "/etudiantsParYear/{date}")
//    public List<Student> getStudentByDate(@PathVariable Date date){
//        return studentRepository.findStudentsByDateInscription_Year(date);
//    }

    @PostMapping(path = "/addStudent")
    @ResponseBody
    public Student addStudent(@RequestBody Student student){
        return studentRepository.save(student);
    }
    //Modifie l'étudiant
    @PutMapping(path = "/modifyStudent/{studentID}")
    @ResponseBody
    public Student modifyStudent(
            @RequestBody Student student,
            @PathVariable Long studentID
    ) throws StudentDefinedException {
        Student oldStudent = studentRepository.findById(studentID).orElse(null);
        if(oldStudent != null){
            oldStudent.setCIN_Passeport(student.getCIN_Passeport());
            oldStudent.setCNE(student.getCNE());
            oldStudent.setFirstname(student.getFirstname());
            oldStudent.setLastname(student.getLastname());
            oldStudent.setFirstnameAr(student.getFirstnameAr());
            oldStudent.setLastnameAr(student.getLastnameAr());
            oldStudent.setEmail(student.getEmail());
            oldStudent.setTelephone(student.getTelephone());
            oldStudent.setPhoto(student.getPhoto());
            oldStudent.setNationalite(student.getNationalite());
            oldStudent.setDateNaissance(student.getDateNaissance());
            oldStudent.setLieuNaissance(student.getLieuNaissance());
            oldStudent.setLieuNaissanceAr(student.getLieuNaissanceAr());
            oldStudent.setVille(student.getVille());
            oldStudent.setProvince(student.getProvince());
            oldStudent.setAnneeBac(student.getAnneeBac());
            oldStudent.setSerieBac(student.getSerieBac());
            oldStudent.setMention(student.getMention());
            oldStudent.setLycee(student.getLycee());
            oldStudent.setLieuBac(student.getLieuBac());
            oldStudent.setAcademie(student.getAcademie());
            oldStudent.setDateInscription(student.getDateInscription());
            oldStudent.setEtablissement(student.getEtablissement());
            oldStudent.setIdFiliere1(student.getIdFiliere1());
            oldStudent.setIdFiliere2(student.getIdFiliere2());
            oldStudent.setIdFiliere3(student.getIdFiliere3());
            oldStudent.setSexe(student.getSexe());
            oldStudent.setTEM_JourMois(student.isTEM_JourMois());
            oldStudent.setTEM_Fonc(student.isTEM_Fonc());
            oldStudent.setHandicap(student.isHandicap());
            return studentRepository.save(oldStudent);
        }else {
            throw new StudentDefinedException("L'étudiant avec l'ID "+ studentID +"n'exite pas");
        }
    }
    @GetMapping(path = "etudiants/filieresChoix")
    public List<Etablissement> tousLesfilieres(){
        return etablissementRestClient.getAllEtab();
    }

    @DeleteMapping(path = "etudiants/{id}/deleteStudent")
    public void deleteStudent(@PathVariable Long id) throws StudentDefinedException {
        boolean studentExist = studentRepository.existsById(id);
        if (!studentExist){
            throw new StudentDefinedException("Element with id "+id+"not found");
        }
        studentRepository.deleteById(id);
    }
    @DeleteMapping(path="/deleteAllStudent")
    public void deleteAllStudent(){
        studentRepository.deleteAll();
    }
   }
