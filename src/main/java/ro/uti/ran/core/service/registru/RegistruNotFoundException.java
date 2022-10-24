package ro.uti.ran.core.service.registru;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-30 16:26
 */
public class RegistruNotFoundException extends RanBusinessException {

    public RegistruNotFoundException(String message) {
        super(message);
    }

    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.RegistruNotFoundException;
    }
}
