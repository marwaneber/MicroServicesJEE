package com.example.anneeuniversitaireservice.Repositories;

import com.example.anneeuniversitaireservice.entities.AnneeUniversitaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface AnneeuniversitaireRepository extends JpaRepository<AnneeUniversitaire,Long> {
    Optional<AnneeUniversitaire> findByNomAnneeUniversitaire(int nomAnneeUniversitaire);
    Optional<AnneeUniversitaire> findByIsCourant(Boolean isCourant);
}
