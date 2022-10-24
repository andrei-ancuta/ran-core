package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.Cultura;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.util.List;

/**
 * Created by Dan on 28-Oct-15.
 */
public interface CulturaRepository extends RanRepository<Cultura, Long> {

    @Query("SELECT a1 FROM Cultura a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an AND a1.capCultura.nomCapitol.cod = :tipCapitol AND a1.fkNomJudet = :fkNomJudet")
    List<Cultura> findByAnAndGospodarieAndCapitolAndFkNomJudet(@Param("an") Integer an, @Param("idGospodarie") Long idGospodarie, @Param("tipCapitol") TipCapitol tipCapitol,@Param("fkNomJudet") Long fkNomJudet);

    @Query("SELECT a1 FROM Cultura a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an AND a1.capCultura.nomCapitol.cod = :tipCapitol AND a1.capCultura.id = :idNomCultura")
    Cultura findByAnAndGospodarieAndCapitolAndNomCultura(@Param("an") Integer an, @Param("idGospodarie") Long idGospodarie, @Param("tipCapitol") TipCapitol tipCapitol, @Param("idNomCultura") Long idNomCultura);

    @Query("SELECT a1 FROM Cultura a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an AND a1.nomTipSpatiuProt.cod = :codTipSpatiuProt  AND a1.capCultura.nomCapitol.cod = :tipCapitol AND a1.fkNomJudet = :fkNomJudet")
    List<Cultura> findByAnAndGospodarieAndTipSpatiuProtAndCapitolAndFkNomJudet(@Param("an") Integer an, @Param("idGospodarie") Long idGospodarie, @Param("codTipSpatiuProt") String codTipSpatiuProt, @Param("tipCapitol") TipCapitol tipCapitol,@Param("fkNomJudet") Long fkNomJudet);

}
