package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.CulturaProd;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.util.List;

/**
 * Created by smash on 04/11/15.
 */
public interface CulturaProdRepository extends RanRepository<CulturaProd, Long> {

    @Query(value = "select cp from CulturaProd  cp " +
            "inner join cp.capCulturaProd nomCult " +
            "inner join nomCult.nomCapitol nomCap " +
            "inner join cp.nomUat nomUat " +
            "where nomUat.codSiruta = :codSiruta and nomCap.cod = :tipCapitol and cp.an = :an and nomCult.id=:nomParentId AND cp.fkNomJudet = :fkNomJudet")
    CulturaProd findByAnAndUatAndCapitolAndNomCulturaAndFkNomJudet(@Param("an") Integer an, @Param("codSiruta") Integer codSiruta, @Param("tipCapitol") TipCapitol tipCapitol, @Param("nomParentId") Long nomParentId, @Param("fkNomJudet") Long fkNomJudet);

    @Query(value = "select cp from CulturaProd cp " +
            "inner join cp.capCulturaProd nomCult " +
            "inner join nomCult.nomCapitol nomCap " +
            "inner join cp.nomUat nomUat " +
            "where nomUat.codSiruta = :codSiruta and nomCap.cod = :capitol and cp.an = :an AND cp.fkNomJudet = :fkNomJudet")
    List<CulturaProd> findAllByTipCapitolAndYearForSirutaAndFkNomJudet(@Param(value = "an") Integer an, @Param("capitol") TipCapitol capitol, @Param("codSiruta") Integer codSiruta, @Param("fkNomJudet") Long fkNomJudet);

    @Query(value = "select cspp from CulturaProd cspp " +
            "inner join  cspp.capCulturaProd capCultProd " +
            "inner join cspp.nomTipSpatiuProt nomTipSP " +
            "inner join cspp.nomUat nomUat " +
            "where nomUat.codSiruta = :codSiruta and nomTipSP.cod = :tipSpatiu and cspp.an = :an and capCultProd.nomCapitol.cod = :codCapitol AND cspp.fkNomJudet = :fkNomJudet")
    List<CulturaProd> findAllByTipSpatiuAndYearForSirutaAndFkNomJudet(@Param(value = "an") Integer an, @Param("tipSpatiu") String tipSpatiu, @Param("codSiruta") Integer codSiruta, @Param("codCapitol") TipCapitol codCapitol, @Param("fkNomJudet") Long fkNomJudet);

}
