package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.StareRegistru;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Created by Stanciu Neculai on 05.Nov.2015.
 */
public interface StareRegistruRepository extends RanRepository<StareRegistru,Long> {
    StareRegistru findByCod(String cod);
}
