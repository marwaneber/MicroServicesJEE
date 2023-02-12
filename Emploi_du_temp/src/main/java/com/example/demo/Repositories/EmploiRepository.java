package com.example.demo.Repositories;

import com.example.demo.Entities.Emploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource
@Transactional
@CrossOrigin(origins="http://localhost:4200")
public interface EmploiRepository extends JpaRepository<Emploi,Long> {
    Emploi findByIdSemestre(Long id);


}
