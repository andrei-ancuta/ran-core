package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.AplicareIngrasamant;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.util.List;

/**
 * Created by Dan on 30-Oct-15.
 */
public interface AplicareIngrasamantRepository extends RanRepository<AplicareIngrasamant, Long> {

    @Query("SELECT a1 FROM AplicareIngrasamant a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an AND a1.capAplicareIngrasamant.nomCapitol.cod = :codCapitol AND a1.fkNomJudet = :fkNomJudet")
    List<AplicareIngrasamant> findByAnAndGospodarieAndCapitolAndFkNomJudet(@Param("an") Integer an, @Param("idGospodarie") Long idGospodarie,@Param("codCapitol") TipCapitol codCapitol,@Param("fkNomJudet") Long fkNomJudet);


    @Query("SELECT a1 FROM AplicareIngrasamant a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an  AND a1.capAplicareIngrasamant.id = :idNom")
    AplicareIngrasamant findByAnAndGospodarieAndCapAplicareIngrasamant(@Param("an") Integer an, @Param("idGospodarie") Long idGospodarie, @Param("idNom") Long idNom);
}
