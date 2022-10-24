package ro.uti.ran.core.repository.registru;

import ro.uti.ran.core.model.registru.GeometrieAplicareIngras;
import ro.uti.ran.core.repository.base.RanRepository;

import java.util.List;

/**
 * Created by miroslav.rusnac on 09/06/2016.
 */
public interface GeometrieAplicareIngrasRepository extends RanRepository<GeometrieAplicareIngras,Long> {

    List<GeometrieAplicareIngras> findByFkAplicareIngrasamant(Long fkAplicareIngrasamant);
}
