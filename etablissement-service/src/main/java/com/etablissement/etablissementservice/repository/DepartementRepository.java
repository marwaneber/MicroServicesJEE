package com.etablissement.etablissementservice.repository;

import com.etablissement.etablissementservice.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface DepartementRepository extends JpaRepository<Departement,Long> {
    Departement getDepartementByIdIs(Long id);

    List<Departement> getDepartementsByEtablissement_IdEtab(Long id);

}
