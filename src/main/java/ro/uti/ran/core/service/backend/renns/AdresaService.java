package ro.uti.ran.core.service.backend.renns;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.registru.Adresa;

/**
 * Created by Dan on 29-Jan-16.
 */
public interface AdresaService {
    void updateFromRenns(Adresa adresa) throws RanBusinessException;
}
