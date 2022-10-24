package ro.uti.ran.core.service.backend.capitol;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;

/**
 * Created by smash on 05/11/15.
 */
public interface CapitolCentralizatorService extends CapitolBaseService {
    /**
     * @param capitolCentralizatorDTO - datele care trebuiesc completate din DB
     */
    void populeazaDateCapitol(CapitolCentralizatorDTO capitolCentralizatorDTO) throws RanBusinessException;

}
