package ro.uti.ran.core.service.exportNomenclatoare;

import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.model.registru.TipNomenclator;

import java.util.List;

/**
 * Created by Stanciu Neculai on 11.Feb.2016.
 */
public interface NomenclatorClassificationService {
    List<NomCapitol> availableNonCapitolListFor(TipNomenclator tipNomenclator);
}
