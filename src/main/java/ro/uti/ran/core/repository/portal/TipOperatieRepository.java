package ro.uti.ran.core.repository.portal;

import ro.uti.ran.core.model.portal.TipOperatie;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Created by Stanciu Neculai on 05.Jan.2016.
 */
public interface TipOperatieRepository extends RanRepository<TipOperatie, Long> {
    TipOperatie findByCod(String cod);
}
