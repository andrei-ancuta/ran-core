package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-14 10:57
 */
public interface UatRepository extends RanRepository<UAT, Long> {
    UAT findByCodSiruta(int codSiruta);
}
