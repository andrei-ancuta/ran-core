package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-08 17:05
 */
public interface ParametruRepository extends RanRepository<Parametru, Long> {

    Parametru findByCod(String codParametru);
    
    Parametru findByDenumire(String denumireParamteru);
}
