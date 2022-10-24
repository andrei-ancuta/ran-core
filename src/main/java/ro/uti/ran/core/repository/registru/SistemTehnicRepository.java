package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.SistemTehnic;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.util.List;

/**
 * Created by Dan on 30-Oct-15.
 */
public interface SistemTehnicRepository extends RanRepository<SistemTehnic, Long> {

    @Query("SELECT a1 FROM SistemTehnic a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an AND a1.capSistemTehnic.nomCapitol.cod = :codCapitol AND a1.fkNomJudet = :fkNomJudet")
    List<SistemTehnic> findByAnAndGospodarieAndCapitolAndFkNomJudet(@Param("an") Integer an, @Param("idGospodarie") Long idGospodarie,@Param("codCapitol") TipCapitol codCapitol,@Param("fkNomJudet") Long fkNomJudet);
}
