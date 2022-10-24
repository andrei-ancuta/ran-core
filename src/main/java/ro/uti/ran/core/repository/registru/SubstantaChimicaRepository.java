package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.SubstantaChimica;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.util.List;

/**
 * Created by Dan on 30-Oct-15.
 */
public interface SubstantaChimicaRepository extends RanRepository<SubstantaChimica, Long> {

    @Query("SELECT a1 FROM SubstantaChimica a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an AND a1.capSubstantaChimica.nomCapitol.cod = :codCapitol AND a1.fkNomJudet = :fkNomJudet")
    List<SubstantaChimica> findByAnAndGospodarieAndCapitolAndFkNomJudet(@Param("an") Integer an, @Param("idGospodarie") Long idGospodarie,@Param("codCapitol") TipCapitol codCapitol,@Param("fkNomJudet") Long fkNomJudet);

    @Query("SELECT a1 FROM SubstantaChimica a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an  AND a1.capSubstantaChimica.id = :idNom")
    SubstantaChimica findByAnAndGospodarieAndNomSubstantaChimica(@Param("an") Integer an, @Param("idGospodarie") Long idGospodarie, @Param("idNom") Long idNom);
}
