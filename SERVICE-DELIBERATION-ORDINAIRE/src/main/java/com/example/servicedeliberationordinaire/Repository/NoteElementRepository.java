package com.example.servicedeliberationordinaire.Repository;

import com.example.servicedeliberationordinaire.Entities.NoteElementModule;
import com.example.servicedeliberationordinaire.Enumeration.Resultat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface NoteElementRepository extends JpaRepository<NoteElementModule,Long> {
    @Query("select n from NoteElementModule n where n.anneeU = ?1 and n.idElement = ?2")
    List<NoteElementModule> findAllByAnneeUAndAndIdElement(int anneeU, Long idElement);
    @Query("select e from NoteElementModule e where e.ladiff=:ladiff")
    NoteElementModule findByLadiff(@Param("ladiff") String ladiff);
    @Query("select n from NoteElementModule n where n.anneeU = ?1 and n.idEtudiant = ?2 and n.idElement = ?3")
    NoteElementModule findNoteElementModulesByAnneeUAndIdEtudiantAndIdElement(int annee, Long idEtudiant, Long IdElement);
    @Query("select n from NoteElementModule n where n.anneeU = ?1 and n.validete = ?2 and n.idElement=?3")
    List<NoteElementModule> findNoteElementModulesByAnneeUAndValideteAndIdElement(int annee, Resultat resultat, Long idElement);

    List<NoteElementModule> findNoteElementModulesByIdElementAndAnneeU(Long idElement, int annee);

    /*List<NoteElementModule> findAllByAnneeUAndId(int anneeU, Long idFiliere, Long idSemestre, Long idElement);

    /*e.
    List<NoteElementModule> findNoteElementModulesByAnneUAndIdFiliereAndIdSemestreAndIdElementAndValidete(int anneeU, Long idFiliere, Long idSemestre, Long idElement, String validite);
    List<NoteElementModule> findNoteElementModulesByAnneUAndIdFiliereAndIdSemestreAndIdElement(int anneeU, Long idFiliere, Long idSemestre, Long idElement);

     */
//    @Query("select n from NoteElementModule n where n.anneeU = ?1 and n.idEtudiant = ?2 and n.idElement = ?3")
//    NoteElementModule findNoteElementModuleByAnneeUAndIdEtudiantAndIdElement(int annee, Long IdEtudiant, Long IdElement);

}
