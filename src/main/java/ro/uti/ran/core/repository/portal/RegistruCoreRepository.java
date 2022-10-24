package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.RegistruCore;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Created by Stanciu Neculai on 05.Feb.2016.
 */
public interface RegistruCoreRepository extends RanRepository<RegistruCore,Long>{
    RegistruCore findByIndexRegistru(String indexRegistru);
}
