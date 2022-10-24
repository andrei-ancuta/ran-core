package ro.uti.ran.core.exception;


import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;
import ro.uti.ran.core.exception.codes.MessageStoreCodes;

/**
 * Created by octav on 17/12/13.
 */
public class MessageStoreException extends RanBusinessException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MessageStoreException(MessageStoreCodes code) {
        super(code);
    }

    public MessageStoreException(MessageStoreCodes code, Object... args) {
        super(code, args);
    }

    public MessageStoreException(MessageStoreCodes code, Throwable cause) {
        super(code, cause);
    }

    public MessageStoreException(MessageStoreCodes code, Throwable cause, Object... args) {
        super(code, cause, args);
    }

    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.MessageStoreException;
    }
}
