package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.CategorieAnimal;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.util.List;

/**
 * Created by Dan on 29-Oct-15.
 */
public interface CategorieAnimalRepository extends RanRepository<CategorieAnimal, Long> {

    @Query("SELECT a1 FROM CategorieAnimal a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an AND a1.semestru = :semestru AND a1.capCategorieAnimal.nomCapitol.cod = :codCapitol AND a1.fkNomJudet = :fkNomJudet")
    List<CategorieAnimal> findByAnAndSemestruAndGospodarieAndCapitolAndFkNomJudet(@Param("an") Integer an, @Param("semestru") Byte semestru, @Param("idGospodarie") Long idGospodarie,@Param("codCapitol") TipCapitol codCapitol,@Param("fkNomJudet") Long fkNomJudet);

    @Query("SELECT a1 FROM CategorieAnimal a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.an = :an AND a1.semestru = :semestru AND a1.capCategorieAnimal.id = :idNom")
    CategorieAnimal findByAnAndSemestruAndGospodarieAndNomCategorieAnimal(@Param("an") Integer an, @Param("semestru") Integer semestru, @Param("idGospodarie") Long idGospodarie, @Param("idNom") Long idNom);
}
