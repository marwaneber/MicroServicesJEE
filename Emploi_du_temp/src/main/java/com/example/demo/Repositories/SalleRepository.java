package com.example.demo.Repositories;


import com.example.demo.Entities.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@RepositoryRestResource
@Transactional
@CrossOrigin(origins="http://localhost:4200")
public interface SalleRepository extends JpaRepository<Salle,Long> {
    Salle findSalleById(Long id);



}
