package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.ParcelaTeren;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.util.List;

/**
 * Created by Dan on 30-Oct-15.
 */
public interface ParcelaTerenRepository extends RanRepository<ParcelaTeren, Long> {

    @Query("SELECT a1 FROM ParcelaTeren a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an AND a1.capCategorieFolosinta.nomCapitol.cod = :tipCapitol  AND a1.fkNomJudet = :fkNomJudet")
    List<ParcelaTeren> findByAnAndGospodarieAndCapitolAndFkNomJudet(@Param("an") Integer an, @Param("idGospodarie") Long idGospodarie,@Param("tipCapitol") TipCapitol tipCapitol,@Param("fkNomJudet") Long fkNomJudet);
}
