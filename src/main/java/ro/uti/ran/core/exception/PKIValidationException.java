package ro.uti.ran.core.exception;


import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;
import ro.uti.ran.core.exception.codes.PKIValidationCodes;

/**
 * Created with IntelliJ IDEA.
 * User: mihai.vaduva
 * Date: 11/28/13
 * Time: 2:54 PM
 */
public class PKIValidationException extends SecurityException {

    public PKIValidationException(PKIValidationCodes c) {
        super(c);
    }

    public PKIValidationException(PKIValidationCodes e, Throwable cause) {
        super(e, cause);
    }

    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.PKIValidationException;
    }
}
