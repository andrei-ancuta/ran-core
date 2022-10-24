package ro.uti.ran.core.service.exportNomenclatoare.exception;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
public class NomenclatorExportException extends RanBusinessException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NomenclatorExportException(NomenclatorExportCodes codes) {
        super(codes);
    }

    public NomenclatorExportException(NomenclatorExportCodes codes, Object... args) {
        super(codes, args);
    }

    public NomenclatorExportException(NomenclatorExportCodes code, Throwable cause) {
        super(code, cause);
    }

    public NomenclatorExportException(NomenclatorExportCodes code, Throwable cause, Object... args) {
        super(code, cause, args);
    }

    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.NomenclatorValidationException;
    }
}
