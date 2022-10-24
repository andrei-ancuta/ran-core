package ro.uti.ran.core.repository.registru;

import ro.uti.ran.core.model.registru.InventarGospUat;
import ro.uti.ran.core.repository.base.RanRepository;

public interface IInventarGospUatRepository extends RanRepository<InventarGospUat, Long> {

    InventarGospUat findByAnAndNomUatCodSiruta(Integer an, Integer codSiruta);
}
