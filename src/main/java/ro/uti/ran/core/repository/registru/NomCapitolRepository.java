package ro.uti.ran.core.repository.registru;

import org.springframework.data.jpa.repository.Query;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.util.List;

/**
 * Created by Stanciu Neculai on 23.Nov.2015.
 */
public interface NomCapitolRepository extends RanRepository<NomCapitol, Long> {

    @Query(value = "SELECT c FROM NomCapitol c WHERE c.id IN ?1 ORDER BY c.id")
    List<NomCapitol> findByIdList(List<Long> userIdList);

    @Query(value = "SELECT c FROM NomCapitol c WHERE c.cod IN ?1 ORDER BY c.id")
    List<NomCapitol> findByCodesList(List<TipCapitol> codesList);
}
