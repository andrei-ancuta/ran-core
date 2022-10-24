package ro.uti.ran.core.exception;

import ro.uti.ran.core.exception.base.MessageProvider;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;

/**
 * Created by Stanciu Neculai on 26.Jan.2016.
 */
public class TipTransmitereDateException extends RanBusinessException {

    public TipTransmitereDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TipTransmitereDateException(String message) {
        super(message);
    }

    public TipTransmitereDateException(Throwable cause) {
        super(cause);
    }

    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.TipTransmitereDateException;
    }
}
