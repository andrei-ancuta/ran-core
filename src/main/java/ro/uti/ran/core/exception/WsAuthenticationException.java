package ro.uti.ran.core.exception;


import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;
import ro.uti.ran.core.exception.codes.WsAutenticationCodes;

public class WsAuthenticationException extends RanBusinessException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public WsAuthenticationException(WsAutenticationCodes c) {
        super(c);
    }

    public WsAuthenticationException(WsAutenticationCodes c, Object... args) {
        super(c, args);
    }

    public WsAuthenticationException(WsAutenticationCodes code, Throwable cause) {
        super(code, cause);
    }

    public WsAuthenticationException(WsAutenticationCodes code, Throwable cause, Object... args) {
        super(code, cause, args);
    }

    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.WsAuthenticationException;
    }

}
