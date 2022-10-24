package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-14 10:57
 */
public interface JudetRepository extends RanRepository<Judet, Long> {
    Judet findByCodSiruta(Long codSiruta);
}
