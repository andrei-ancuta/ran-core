package ro.uti.ran.core.repository.registru;

import org.springframework.data.repository.CrudRepository;
import ro.uti.ran.core.model.registru.NomTipAdresa;

/**
 * Created by Anastasia cea micuta on 10/20/2015.
 */
public interface NomTipAdresaRepository extends CrudRepository<NomTipAdresa, Long>{
    NomTipAdresa findByCod(String cod);
}
