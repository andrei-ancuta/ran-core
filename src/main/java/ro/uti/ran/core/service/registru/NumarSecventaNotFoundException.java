package ro.uti.ran.core.service.registru;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;


public class NumarSecventaNotFoundException extends RanBusinessException {

	private static final long serialVersionUID = 1L;

	public NumarSecventaNotFoundException(String message) {
        super(message);
    }

    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.NumarSecventaNotFoundException;
    }
}
