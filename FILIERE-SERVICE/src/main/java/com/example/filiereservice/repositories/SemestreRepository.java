package com.example.filiereservice.repositories;

import com.example.filiereservice.entities.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;
import java.util.List;

@RepositoryRestResource(path="SEMESTRES")
public interface SemestreRepository extends JpaRepository<Semestre,Long> {

//    Collection<Semestre> findSemestresByIdFiliIs(Long id);

//    @Query(value = "select sem from Semestre sem where sem.etape.idEtape=:x")
//    List<Semestre> findSemestresByEtape(@Param("x") Long id);

    //Les semestres d'une etapes
    List<Semestre> findSemestresByEtape_IdEtape(Long id);


    @Query("select s from Semestre s where s.etape.idEtape = ?1 and s.LibCourt = ?2")
    Semestre findByEtape_IdEtapeAndLibCourt(Long id , String libc);
    Semestre findByIdSemestre(Long id);

}
