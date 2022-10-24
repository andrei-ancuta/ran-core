package ro.uti.ran.core.repository.registru;

import ro.uti.ran.core.model.registru.GeometrieCultura;
import ro.uti.ran.core.model.registru.GeometriePlantatie;
import ro.uti.ran.core.model.registru.GeometrieTerenIrigat;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by miroslav.rusnac on 09/06/2016.
 */
public interface GeometrieTerenIrigatRepository extends RanRepository<GeometrieTerenIrigat,Long> {

    List<GeometrieTerenIrigat> findByFkTerenIrigat(Long fkTerenIrigat);
}
