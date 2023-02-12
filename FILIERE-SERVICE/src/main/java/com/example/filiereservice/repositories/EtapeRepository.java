package com.example.filiereservice.repositories;

import com.example.filiereservice.entities.Etape;
import com.example.filiereservice.entities.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path="ETAPE")
public interface EtapeRepository extends JpaRepository<Etape, Long> {

    @Query(value = "select etape from Etape etape where etape.LibEtape =:x")
    Etape trouverEtapeParNom(@Param("x") int libEtape);
    //Trouver les etapes une filiere
    @Query("select e from Etape e where e.filiere.id = ?1")
    List<Etape> findEtapesByFiliere_Id(Long id);

    @Query("select e from Etape e where e.filiere.id = ?1 and e.LibEtape = ?2")
    Etape findEtapeByFiliere_IdAndLibEtape(Long idFiliere, int etape);

}
