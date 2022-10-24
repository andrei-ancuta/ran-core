package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.SuprafataUtilizare;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.util.List;

/**
 * Created by Dan on 28-Oct-15.
 */
public interface SuprafataUtilizareRepository extends RanRepository<SuprafataUtilizare, Long> {

    @Query("SELECT a1 FROM SuprafataUtilizare a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an AND a1.capModUtilizare.nomCapitol.cod = :tipCapitol AND a1.fkNomJudet = :fkNomJudet")
    List<SuprafataUtilizare> findByAnAndGospodarieAndCapitolAndFkNomJudet(@Param("an") Integer an, @Param("idGospodarie") Long idGospodarie, @Param("tipCapitol") TipCapitol tipCapitol,@Param("fkNomJudet") Long fkNomJudet);

    @Query("SELECT a1 FROM SuprafataUtilizare a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an AND a1.capModUtilizare.id = :idNom")
    SuprafataUtilizare findByAnAndGospodarieAndNomModUtilizare(@Param("an") Integer an, @Param("idGospodarie") Long idGospodarie, @Param("idNom") Long idNom);


}
