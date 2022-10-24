package ro.uti.ran.core.service.registru;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.registru.NomStareRegistru;
import ro.uti.ran.core.model.registru.Registru;

/**
 * Created by Stanciu Neculai on 17.Dec.2015.
 */
public interface FluxRegistruService {
    void saveFluxRegistru(Registru registru,NomStareRegistru nomStareRegistru, String respinsParamOrNull);
    String extractRespinsMesaj(RanBusinessException e);
}
