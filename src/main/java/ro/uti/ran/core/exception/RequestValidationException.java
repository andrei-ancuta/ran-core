package ro.uti.ran.core.exception;


import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;
import ro.uti.ran.core.exception.codes.RequestCodes;

public class RequestValidationException extends RanBusinessException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public RequestValidationException(RequestCodes c) {
        super(c);
    }

    public RequestValidationException(RequestCodes c, Object... args) {
        super(c, args);
    }

    public RequestValidationException(RequestCodes code, Throwable cause) {
        super(code, cause);
    }

    public RequestValidationException(RequestCodes code, Throwable cause, Object... args) {
        super(code, cause, args);
    }

    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.RequestValidationException;
    }

}
