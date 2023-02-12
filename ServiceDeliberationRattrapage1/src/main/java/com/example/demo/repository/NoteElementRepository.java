package com.example.demo.repository;

import com.example.demo.entities.NoteElementR;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NoteElementRepository extends JpaRepository<NoteElementR,Long> {
   @Query("select n from NoteElementR n where n.idElement = ?1 and n.anneeU = ?2")
   List<NoteElementR> findAllByIdElementAndAnneeU(Long idElement, int anneeU);
   @Query("select n from NoteElementR n where n.anneeU = ?1 and n.idEtudiant = ?2 and n.idElement = ?3")
   NoteElementR findNoteElementModulesByAnneeUAndIdEtudiantAndIdElement(int annee, Long idEtudiant, Long IdElement);
   List<NoteElementR> findNoteElementRSByIdElementAndAnneeU(Long idElement, int annee);
}
