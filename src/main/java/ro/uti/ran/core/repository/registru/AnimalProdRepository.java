package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.AnimalProd;
import ro.uti.ran.core.model.registru.PlantatieProd;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.util.List;

/**
 * Created by smash on 20/11/15.
 */
public interface AnimalProdRepository extends RanRepository<AnimalProd, Long> {
    @Query(value = "select p from AnimalProd p " +
            "inner join p.capAnimalProd nom " +
            "inner join nom.nomCapitol nomCap " +
            "inner join p.nomUat nomUat " +
            "where nomUat.codSiruta = :codSiruta and nomCap.cod = :tipCapitol and p.an = :an and nom.id=:nomParentId AND p.fkNomJudet = :fkNomJudet")
    AnimalProd findByAnAndUatAndCapitolAndCapAnimalProdAndFkNomJudet(@Param("an") Integer an, @Param("codSiruta") Integer codSiruta, @Param("tipCapitol") TipCapitol tipCapitol, @Param("nomParentId") Long nomParentId, @Param("fkNomJudet") Long fkNomJudet);

    @Query(value = "select p from AnimalProd p " +
            "inner join p.capAnimalProd nom " +
            "inner join nom.nomCapitol nomCap " +
            "inner join p.nomUat nomUat " +
            "where nomUat.codSiruta = :codSiruta and nomCap.cod = :tipCapitol and p.an = :an and nom.capAnimalProd.id=:nomParentId AND p.fkNomJudet = :fkNomJudet")
    List<AnimalProd> findAllByAnAndUatAndCapitolAndFkCapAnimalProdAndFkNomJudet(@Param("an") Integer an, @Param("codSiruta") Integer codSiruta, @Param("tipCapitol") TipCapitol tipCapitol, @Param("nomParentId") Long nomParentId, @Param("fkNomJudet") Long fkNomJudet);


    @Query(value = "select anmProd from AnimalProd anmProd " +
            "inner join anmProd.nomUat uat " +
            "inner join anmProd.capAnimalProd capAnmProd " +
            "where anmProd.an=:an and uat.codSiruta=:codSiruta and capAnmProd.nomCapitol.cod=:codCapitol AND anmProd.fkNomJudet = :fkNomJudet")
    List<AnimalProd> findAllByAnAndCodSirutaAndFkNomJudet(@Param("an") Integer an, @Param("codSiruta") Integer codSiruta, @Param("codCapitol") TipCapitol capitol, @Param("fkNomJudet") Long fkNomJudet);

}
