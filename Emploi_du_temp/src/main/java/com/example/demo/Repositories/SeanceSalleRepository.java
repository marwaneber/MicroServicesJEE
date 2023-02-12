package com.example.demo.Repositories;


import com.example.demo.Entities.Jours;
import com.example.demo.Entities.Seance;
import com.example.demo.Entities.SeanceSalle;
import com.example.demo.Enumeration.Heure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource
@Transactional
@CrossOrigin(origins="http://localhost:4200")
public interface SeanceSalleRepository extends JpaRepository<SeanceSalle,Long> {

    SeanceSalle findByIdAndHeure(Long id , Heure heure);




}
