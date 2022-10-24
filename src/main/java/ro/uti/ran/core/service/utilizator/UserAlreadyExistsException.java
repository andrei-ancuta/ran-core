package ro.uti.ran.core.service.utilizator;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-05 14:30
 */
public class UserAlreadyExistsException extends RanBusinessException {


    public UserAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.UserAlreadyExistsException;
    }
}
