package com.example.demo.Repositories;

import com.example.demo.Entities.Emploi;
import com.example.demo.Entities.EmploiProf;
import com.example.demo.Entities.Jours;
import com.example.demo.Entities.JoursProf;
import com.example.demo.Enumeration.Jour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@Transactional
@CrossOrigin(origins="http://localhost:4200")
public interface JoursRepository extends JpaRepository<Jours, Long> {

    Jours findByEmploiAndJour(Emploi emploi , Jour jours);
}
