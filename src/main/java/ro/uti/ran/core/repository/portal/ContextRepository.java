package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.Context;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-24 13:41
 */
public interface ContextRepository extends RanRepository<Context, Long> {

    Context findByCod(String codContext);
}
