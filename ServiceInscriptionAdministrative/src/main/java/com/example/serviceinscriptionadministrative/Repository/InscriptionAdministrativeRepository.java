package com.example.serviceinscriptionadministrative.Repository;

import com.example.serviceinscriptionadministrative.Entities.EtudiantInscAdmin;
import com.example.serviceinscriptionadministrative.Entities.InscriptionAdministrative;
import com.example.serviceinscriptionadministrative.Enums.EtatIns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface InscriptionAdministrativeRepository extends JpaRepository<InscriptionAdministrative,Integer> {
    Optional<InscriptionAdministrative> findByIdInsAdmin(Long idInsAdmin);
    @Query("select i from InscriptionAdministrative i where i.etudiantInscAdmin.id = ?1")
    List<InscriptionAdministrative> findInscriptionAdministrativeByEtudiantInscAdmin_Id(Long idEtudiant);
    @Query("select i from InscriptionAdministrative i where i.AnneeUniv = ?1")
    List<InscriptionAdministrative> findInscriptionAdministrativeByAnneeUniv(int annee);
    @Query("select i from InscriptionAdministrative i where i.idInsAdmin = ?1")
    InscriptionAdministrative findInscriptionAdministrativeByIdInsAdmin(Long id);

    @Query("select i from InscriptionAdministrative i where i.AnneeUniv = ?1 and i.EtatIns = ?2")
    List<InscriptionAdministrative> findInscriptionAdministrativesByAnneeUnivAndAndEtatIns(int anne, EtatIns etatIns);

    @Query("select i from InscriptionAdministrative i " +
            "where i.etudiantInscAdmin.id = ?1 and i.AnneeUniv = ?2 and i.EtatIns= ?3")
    InscriptionAdministrative findInscriptionAdministrativeByEtudiantInscAdmin_IdAndAnneeUnivAndEtatIns(Long idEtudiant, int annee, EtatIns etatIns);


}
