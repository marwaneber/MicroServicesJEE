package com.example.inscription_pedagogique.Repositories;

import com.example.inscription_pedagogique.Enumeration.TypeElementPedagogique;
import com.example.inscription_pedagogique.Web.EtudiantInscriptionPedagogique;
import com.example.inscription_pedagogique.entities.InscriptionPedagogique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@RepositoryRestResource
public interface InscriptionPedagogiqueRepository extends JpaRepository<InscriptionPedagogique,Long> {
    InscriptionPedagogique findInscriptionPedagogiqueById(Long id);

    //List <InscriptionPedagogique> findInscriptionPedagogiqueByIdElmPedAndType_elp(Long id ,int type );
  /*  @Query(value = "SELECT InscriptionPedagogique FROM InscriptionPedagogique WHERE type_elp=:lib AND idElmPed=:id")
    List <InscriptionPedagogique> findInscriptionPedagogiqueByIdElmPedAndType_elp(@Param("lib") int lib , @Param("id")Long id);*/
    List<InscriptionPedagogique> findInscriptionPedagogiquesByIdAndAndIdElmPed(Long id, Long idelm);

    List<InscriptionPedagogique> findInscriptionPedagogiquesByIdInscripAdminAndTypeelp(Long idInscrAd, TypeElementPedagogique typeElementPedagogique);

    List<InscriptionPedagogique> findInscriptionPedagogiquesByIdInscripAdmin(Long id);
    @Query("select i from InscriptionPedagogique i where i.idInscripAdmin= ?1 and i.idElmPed = ?2 and i.typeelp = ?3")
    List<InscriptionPedagogique> findInscriptionPedagogiquesByIdEtudAndAndIdElmPedAndTypeelp(Long idInscripAdmin, Long idElmPed, TypeElementPedagogique typeElementPedagogique);

    InscriptionPedagogique findByIdElmPedAndIdInscripAdmin(Long idped ,Long idadmin);
    @Query("select i from InscriptionPedagogique i where i.typeelp = ?1 and i.idElmPed = ?2")
    List<InscriptionPedagogique> findInscriptionPedagogiquesByTypeelpAndIdElmPedAAndAnneeuniv(TypeElementPedagogique typeElementPedagogique, Long idELmPed, int annee);

    @Query("select i from InscriptionPedagogique i where i.typeelp = ?1 and i.idInscripAdmin= ?2")
    List<InscriptionPedagogique> findInscriptionPedagogiqueByTypeelpAndIdInscripAdmin(TypeElementPedagogique typeElementPedagogique, Long idInscripAdmin);

    List<InscriptionPedagogique> findInscriptionPedagogiquesByIdInscripAdminAndTypeelpAndAnneeuniv(Long idInscrAd, TypeElementPedagogique typeElementPedagogique, int annee);
}
