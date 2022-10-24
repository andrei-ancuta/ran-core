package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.DetinatorPj;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by Dan on 26-Oct-15.
 */
public interface DetinatorPjRepository extends RanRepository<DetinatorPj, Long> {


    @Query("SELECT d FROM DetinatorPj d where d.gospodarie.idGospodarie = :idGospodarie AND d.fkNomJudet = :fkNomJudet")
    List<DetinatorPj> findByGospodarieAndFkNomJudet(@Param("idGospodarie") Long idGospodarie, @Param("fkNomJudet") Long fkNomJudet);

    @Query("SELECT d FROM DetinatorPj d where d.persoanaRc.cui = :cui")
    List<DetinatorPj> findByCui(@Param("cui") String cui);

    @Query("SELECT d.gospodarie.identificator FROM DetinatorPj d where d.gospodarie.isActiv = 1 AND d.gospodarie.nomUat.codSiruta = :codSiruta AND  d.persoanaRc.cui = :cui")
    List<String> unicitateByCui(@Param("cui") String cui, @Param("codSiruta") Integer codSiruta);
}
