package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.CulturaProd;
import ro.uti.ran.core.model.registru.FructProd;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.util.List;

/**
 * Created by smash on 18/11/15.
 */
public interface FructProdRepository extends RanRepository<FructProd, Long> {

    @Query(value = "select fp from FructProd  fp " +
            "inner join fp.capFructProd nomFruct " +
            "inner join fp.nomUat nomUat " +
            "where nomUat.codSiruta = :codSiruta and fp.an = :an and nomFruct.id=:nomParentId and nomFruct.nomCapitol.cod=:codCapitol AND fp.fkNomJudet = :fkNomJudet")
    FructProd findByAnAndUatAndNomFructAndFkNomJudet(@Param("an") Integer an, @Param("codSiruta") Integer codSiruta, @Param("nomParentId") Long nomParentId, @Param("codCapitol") TipCapitol capitol, @Param("fkNomJudet") Long fkNomJudet);

    @Query(value = "select fp from FructProd fp " +
            "inner join fp.nomUat nomUat " +
            "inner join fp.capFructProd capFructProd " +
            "where nomUat.codSiruta = :codSiruta and fp.an = :an and capFructProd.nomCapitol.cod=:codCapitol AND fp.fkNomJudet = :fkNomJudet")
    List<FructProd> findAllByYearForSirutaAndFkNomJudet(@Param(value = "an") Integer an, @Param("codSiruta") Integer codSiruta, @Param("codCapitol") TipCapitol capitol, @Param("fkNomJudet") Long fkNomJudet);


}
