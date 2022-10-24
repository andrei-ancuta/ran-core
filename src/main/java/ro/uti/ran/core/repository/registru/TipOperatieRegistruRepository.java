package ro.uti.ran.core.repository.registru;


import ro.uti.ran.core.model.registru.TipOperatieSin;
import ro.uti.ran.core.repository.base.RanRepository;

/**
 * Created by Stanciu Neculai on 05.Jan.2016.
 */
public interface TipOperatieRegistruRepository extends RanRepository<TipOperatieSin, Long> {
    TipOperatieSin findByCod(String cod);
}
