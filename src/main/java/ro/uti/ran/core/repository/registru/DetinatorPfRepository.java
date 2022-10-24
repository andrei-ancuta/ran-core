package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.DetinatorPf;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by Dan on 16-Oct-15.
 */
public interface DetinatorPfRepository extends RanRepository<DetinatorPf, Long> {
    @Query("SELECT d FROM DetinatorPf d where d.gospodarie.idGospodarie = :idGospodarie AND d.fkNomJudet = :fkNomJudet")
    List<DetinatorPf> findByGospodarieAndFkNomJudet(@Param("idGospodarie") Long idGospodarie, @Param("fkNomJudet") Long fkNomJudet);

    @Query("SELECT d FROM DetinatorPf d where d.persoanaFizica.cnp = :cnp")
    List<DetinatorPf> findByCnp(@Param("cnp") String cnp);

    @Query("SELECT d FROM DetinatorPf d where d.persoanaFizica.nif = :nif")
    List<DetinatorPf> findByNif(@Param("nif") String nif);

    @Query("SELECT d.gospodarie.identificator FROM DetinatorPf d where d.gospodarie.isActiv = 1 AND d.gospodarie.nomUat.codSiruta = :codSiruta AND d.persoanaFizica.cnp = :cnp")
    List<String> unicitateByCnp(@Param("cnp") String cnp, @Param("codSiruta") Integer codSiruta);

    @Query("SELECT d.gospodarie.identificator FROM DetinatorPf d where d.gospodarie.isActiv = 1 AND d.gospodarie.nomUat.codSiruta = :codSiruta AND d.persoanaFizica.nif = :nif")
    List<String> unicitateByNif(@Param("nif") String nif, @Param("codSiruta") Integer codSiruta);
}
