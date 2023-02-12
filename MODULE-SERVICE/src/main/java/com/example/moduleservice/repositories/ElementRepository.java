package com.example.moduleservice.repositories;

import com.example.moduleservice.entities.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;


@RepositoryRestResource(path="ELEMENTS")
@CrossOrigin(origins="http://localhost:4200")
public interface ElementRepository extends JpaRepository<Element,Long> {
    List<Element> findByIdProf(Long idProf);
    Optional<Element> findById(Long id);
    @Query(value = "select ele from Element ele where ele.module.idModule=:x")
    List<Element> findElementsByModule(@Param("x") Long id);

}
