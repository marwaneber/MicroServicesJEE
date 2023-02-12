package com.example.serviceinscriptionadministrative.Repository;

import com.example.serviceinscriptionadministrative.Entities.EtudiantInscAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface EtudiantInscAdminRepository extends JpaRepository<EtudiantInscAdmin,Long> {
    @Query(value = "select etu from EtudiantInscAdmin etu where etu.CNE=:x")
    Optional<EtudiantInscAdmin> findEtudiantByCne(@Param("x") String CNE);
    List<EtudiantInscAdmin> findEtudiantInscAdminsByIdFiliere(Long id);

}
