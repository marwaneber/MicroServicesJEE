package com.example.filiereservice.repositories;

import com.example.filiereservice.entities.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path="FILIERES")
public interface FiliereRepository extends JpaRepository<Filiere,Long> {
    List<Filiere> findFiliereByIdDepartIs(Long id);
    @Query(value = "select fil from Filiere fil where fil.typeFormation.idTypeFormation =:x")
    List<Filiere> findFilieresByTypeFormation(@Param("x") Long id);


}
