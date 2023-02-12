package com.example.demo.Repositories;


import com.example.demo.Entities.Jours;
import com.example.demo.Entities.Seance;
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
public interface SeanceRepository extends JpaRepository<Seance,Long> {

    @Modifying
    @Transactional
    @Query("select j from Jours j where j.emploi.id = ?1")
    List<Jours> findByEmpoli(Long id);
//    @Query(value = "SELECT * FROM seance S WHERE S.emploi_id =:id ", nativeQuery = true)
//    List<Jours> findByEmpoli(@Param("id") Long id);
    Seance findByIdSeance(Long id);





}
