package com.example.serviceprofesseur.repositories;

import com.example.serviceprofesseur.entities.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;


@RepositoryRestResource(path="PROFESSEUR")
@CrossOrigin(origins="http://localhost:4200")
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
    @Query("select p from professeurs p where p.idDepar = ?1")
    List<Professeur> findByIdDepar(Long idDepar);
    @Query("select p from professeurs p where p.idProf = ?1")
    Professeur getProfesseurById(Long idProf);
}
