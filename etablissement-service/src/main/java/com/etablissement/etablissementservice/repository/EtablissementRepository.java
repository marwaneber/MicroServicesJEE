package com.etablissement.etablissementservice.repository;

import com.etablissement.etablissementservice.entities.Etablissement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EtablissementRepository extends JpaRepository<Etablissement,Long> {
}
