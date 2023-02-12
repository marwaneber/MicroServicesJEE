package com.example.demo.Repositories;


import com.example.demo.Entities.EmploiProf;
import com.example.demo.Entities.EmploiSalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@RepositoryRestResource
@Transactional
@CrossOrigin(origins="http://localhost:4200")
public interface EmploiSalleRepository extends JpaRepository<EmploiSalle, Long> {
    @Query("select e from EmploiSalle e where e.id = ?1")
    Optional<EmploiSalle> findById(Long idEmploiSalle);
    EmploiSalle findByIdSalle(Long idSalle);
//    @Query("select e from EmploiProf e where e.idProf = ?1")
//    EmploiProf findByIdProf(Long id);
}
