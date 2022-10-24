package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.PlantatieProd;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.util.List;

/**
 * Created by smash on 19/11/15.
 */
public interface PlantatieProdRepository extends RanRepository<PlantatieProd, Long> {
    @Query(value = "select p from PlantatieProd p " +
            "inner join p.capPlantatieProd nom " +
            "inner join nom.nomCapitol nomCap " +
            "inner join p.nomUat nomUat " +
            "where nomUat.codSiruta = :codSiruta and nomCap.cod = :tipCapitol and p.an = :an and nom.id=:nomParentId AND p.fkNomJudet = :fkNomJudet")
    PlantatieProd findByAnAndUatAndCapitolAndCapPlantatieProdAndFkNomJudet(@Param("an") Integer an, @Param("codSiruta") Integer codSiruta, @Param("tipCapitol") TipCapitol tipCapitol, @Param("nomParentId") Long nomParentId, @Param("fkNomJudet") Long fkNomJudet);


    @Query(value = "select stProd from PlantatieProd stProd " +
            "inner join stProd.nomUat uat " +
            "inner join stProd.capPlantatieProd pProd " +
            "inner join pProd.nomCapitol cap " +
            "where uat.codSiruta=:codSiruta and stProd.an=:an and cap.cod=:codCapitol AND stProd.fkNomJudet = :fkNomJudet")
    List<PlantatieProd> findAllByAnAndCodSirutaAndFkNomJudet(@Param("an") Integer an, @Param("codSiruta") Integer codSiruta, @Param("codCapitol") TipCapitol capitol, @Param("fkNomJudet") Long fkNomJudet);

}
