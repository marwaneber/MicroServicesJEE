package com.example.moduleservice.repositories;

import com.example.moduleservice.Modules.Semestre;
import com.example.moduleservice.entities.Element;
import com.example.moduleservice.entities.Module;
import org.bouncycastle.math.raw.Mod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;


@RepositoryRestResource(path="MODULES")
@CrossOrigin(origins="http://localhost:4200")
public interface ModulesRepository extends JpaRepository<Module,Long> {
    List<Module> findByIdFiliere(Long idFiliere);
    List<Module> findModulesByIdSemestreIs(Long id);
    List<Module> findByIdProf(Long id);
    List<Module> findByIdFiliereAndIdSemestre(Long idf,Long ids);
    Module findByElements(Element element);

}
