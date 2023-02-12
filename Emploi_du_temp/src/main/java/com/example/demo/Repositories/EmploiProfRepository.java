package com.example.demo.Repositories;


import com.example.demo.Entities.EmploiProf;
import com.example.demo.Entities.JoursProf;
import com.example.demo.Model.ElementM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RepositoryRestResource
@Transactional
@CrossOrigin(origins="http://localhost:4200")
public interface EmploiProfRepository extends JpaRepository<EmploiProf , Long> {
    @Query("select e from EmploiProf e where e.idProf = ?1")
    EmploiProf findByIdProf(Long id);



}
