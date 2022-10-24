package ro.uti.ran.core.service.registru;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;


public class NumarSecventaInvalidException extends RanBusinessException {

	private static final long serialVersionUID = 1L;

	public NumarSecventaInvalidException(String message) {
        super(message);
    }

    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.NumarSecventaInvalidException;
    }
}
