package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.Incarcare;
import ro.uti.ran.core.model.portal.StareIncarcare;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Created by Stanciu Neculai on 03.Nov.2015.
 */
public interface StareIncarcareRepository extends RanRepository<StareIncarcare, Long> {
    StareIncarcare findByCod(String cod);
}
