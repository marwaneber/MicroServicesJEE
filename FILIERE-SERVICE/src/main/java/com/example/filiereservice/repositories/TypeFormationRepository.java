package com.example.filiereservice.repositories;

import com.example.filiereservice.entities.TypeFormation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="TYPEFORMATION")
public interface TypeFormationRepository extends JpaRepository<TypeFormation,Long> {

}
