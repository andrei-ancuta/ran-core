package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.uti.ran.core.model.registru.Preemptiune;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by Dan on 09-Nov-15.
 */
public interface PreemptiuneRepository extends RanRepository<Preemptiune, Long> {

    @Query("SELECT a1 FROM Preemptiune a1 where a1.gospodarie.idGospodarie = :idGospodarie AND a1.fkNomJudet = :fkNomJudet")
    List<Preemptiune> findByGospodarieAndFkNomJudet(@Param("idGospodarie") Long idGospodarie,@Param("fkNomJudet") Long fkNomJudet);
}
