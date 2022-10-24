package ro.uti.ran.core.repository.registru;

import ro.uti.ran.core.model.registru.GeometrieCultura;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by miroslav.rusnac on 08/06/2016.
 */
public interface GeometrieCulturaRepository extends RanRepository<GeometrieCultura,Long> {

    List<GeometrieCultura> findByFkCultura(Long fkCultura);

}
