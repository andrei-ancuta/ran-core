package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.Rol;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 15:36
 */
public interface RolRepository extends RanRepository<Rol, Long> {

    Rol findByCod(String cod);
}
