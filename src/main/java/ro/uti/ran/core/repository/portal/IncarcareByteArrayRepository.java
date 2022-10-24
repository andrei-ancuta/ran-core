package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.IncarcareByteArray;
import ro.uti.ran.core.model.portal.StareIncarcare;
import ro.uti.ran.core.repository.base.RanRepository;


/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-24 13:41
 */
public interface IncarcareByteArrayRepository extends RanRepository<IncarcareByteArray, Long> {

    IncarcareByteArray findByIndexIncarcare(Long indexIncarcare);

    IncarcareByteArray findFirstByStareIncarcare(StareIncarcare stareIncarcare);
}
