package ro.uti.ran.core.exception;


import ro.uti.ran.core.exception.base.MessageProvider;
import ro.uti.ran.core.exception.base.RanBusinessException;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
public abstract class SecurityException extends RanBusinessException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected <E extends MessageProvider> SecurityException(E e, Object... args) {
        super(e, args);
    }

    protected <E extends MessageProvider> SecurityException(E e) {
        super(e);
    }

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    protected <E extends MessageProvider> SecurityException(E e, Throwable cause) {
        super(e, cause);
    }
}
