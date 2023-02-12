package com.example.serviceinscriptionenligne.Repositories;

import com.example.serviceinscriptionenligne.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource
public interface StudentRepository extends JpaRepository<Student,Long> {
//    @Query(value = "select e from EtudiantInscriptionEnLigne e where e.dateInscription >= :x")
//    List<Student> findStudentsByDateInscription_Year(@Param("x") Date year);
//    @Query(value = "select student from Student student where student.date_inscription.getYear =:x")
//    List<Student> findByDateInscription_Year(@Param("x") Date year);

}