package ro.uti.ran.core.repository.registru;

import org.springframework.data.repository.CrudRepository;
import ro.uti.ran.core.model.registru.NomTipDetinator;

/**
 * Created by Anastasia cea micuta on 10/20/2015.
 */
public interface NomTipDetinatorRepository extends CrudRepository<NomTipDetinator, Long> {
    NomTipDetinator findByCod(String cod);
}
