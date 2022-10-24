package ro.uti.ran.core.repository.registru;

import org.springframework.data.repository.CrudRepository;
import ro.uti.ran.core.model.registru.NomJudet;
import ro.uti.ran.core.model.registru.NomUat;

/**
 * Created by Anastasia cea micuta on 10/20/2015.
 */
public interface NomJudetRepository extends CrudRepository<NomJudet, Long> {
    NomJudet findByCodSiruta(Integer codSiruta);
}
