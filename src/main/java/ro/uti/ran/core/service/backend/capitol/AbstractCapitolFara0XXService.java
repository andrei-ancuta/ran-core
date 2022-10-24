package ro.uti.ran.core.service.backend.capitol;

import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;

/**
 * Created by Dan on 03-Feb-16.
 */
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public abstract class AbstractCapitolFara0XXService extends AbstractCapitolService {

    @Override
    public void salveazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            addUpdateDateRegistru(ranDocDTO);
        } else if (IndicativXml.ANULEAZA.equals(ranDocDTO.getIndicativ())) {
            anuleazaDateRegistru(ranDocDTO);
        } else {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.INDICATIV_HEADER_BODY_NECORESPUNZATOR, null != ranDocDTO.getIndicativ() ? ranDocDTO.getIndicativ().name() : null);
        }

        // Altfel nu se face rollback pe constraint-uri DB
        gospodarieRepository.flush();
    }

    public abstract void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException;

    public abstract void anuleazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException;
}
