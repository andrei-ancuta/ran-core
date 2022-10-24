package ro.uti.ran.core.exception;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;
import ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes;

/**
 * Created by Dan on 13-Nov-15.
 */
public class InterogareDateRegistruException extends RanBusinessException {


    public InterogareDateRegistruException(InterogareDateRegistruCodes c) {
        super(c);
    }

    public InterogareDateRegistruException(InterogareDateRegistruCodes c, Object... args) {
        super(c, args);
    }

    public InterogareDateRegistruException(InterogareDateRegistruCodes code, Throwable cause) {
        super(code, cause);
    }

    public InterogareDateRegistruException(InterogareDateRegistruCodes code, Throwable cause, Object... args) {
        super(code, cause, args);
    }


    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.InterogareDateRegistruException;
    }
}
