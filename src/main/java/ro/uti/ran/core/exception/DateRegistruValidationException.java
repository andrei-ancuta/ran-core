package ro.uti.ran.core.exception;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;

/**
 * Created by Dan on 13-Oct-15.
 */
public class  DateRegistruValidationException extends RanBusinessException {

    public DateRegistruValidationException(String message) {
        super(message);
    }

    public DateRegistruValidationException(Throwable cause) {
        super(cause);
    }

    public DateRegistruValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateRegistruValidationException(DateRegistruValidationCodes c) {
        super(c);
    }

    public DateRegistruValidationException(DateRegistruValidationCodes c, Object... args) {
        super(c, args);
    }

    public DateRegistruValidationException(DateRegistruValidationCodes code, Throwable cause) {
        super(code, cause);
    }

    public DateRegistruValidationException(DateRegistruValidationCodes code, Throwable cause, Object... args) {
        super(code, cause, args);
    }


    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.DateRegistruValidationException;
    }
}
