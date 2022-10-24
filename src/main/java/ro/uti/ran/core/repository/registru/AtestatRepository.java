package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.Atestat;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by Dan on 02-Nov-15.
 */
public interface AtestatRepository extends RanRepository<Atestat, Long> {

    @Query("SELECT a1 FROM Atestat a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.fkNomJudet = :fkNomJudet")
    List<Atestat> findByGospodarieAndFkNomJudet(@Param("idGospodarie") Long idGospodarie,@Param("fkNomJudet") Long fkNomJudet);
}
