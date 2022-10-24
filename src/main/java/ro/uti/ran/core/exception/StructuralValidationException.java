package ro.uti.ran.core.exception;

import ro.uti.ran.core.exception.base.MessageProvider;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;
import ro.uti.ran.core.service.backend.exception.SyntaxXmlException;

/**
 * Created by bogdan.ardeleanu on 11/23/2015.
 */
public class StructuralValidationException extends RanBusinessException {


    public <E extends MessageProvider> StructuralValidationException(E e) {
        super(e);
    }

    public <E extends MessageProvider> StructuralValidationException(E e, Object... args) {
        super(e, args);
    }

    public <E extends MessageProvider> StructuralValidationException(E e, Throwable cause) {
        super(e, cause);
    }

    public <E extends MessageProvider> StructuralValidationException(E e, Throwable cause, Object... args) {
        super(e, cause, args);
    }

    public StructuralValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public StructuralValidationException(String message) {
        super(message);
    }

    public StructuralValidationException(Throwable cause) {
        super(cause);
    }

    public StructuralValidationException(SyntaxXmlException cause){
        this(cause.toString(), cause);
    }


    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.StructuralValidationException;
    }
}
