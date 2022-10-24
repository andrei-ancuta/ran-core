package ro.uti.ran.core.service.nomenclator;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.base.RanExceptionBaseCodes;

/**
 * Created by ciprian.chiriac@greensoft.com.ro on 15.02.2016.
 */
public class NomenclatorException extends RanBusinessException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NomenclatorException(NomenclatorExceptionCodes codes) {
        super(codes);
    }

    public NomenclatorException(NomenclatorExceptionCodes codes, Object... args) {
        super(codes, args);
    }

    public NomenclatorException(NomenclatorExceptionCodes code, Throwable cause) {
        super(code, cause);
    }

    public NomenclatorException(NomenclatorExceptionCodes code, Throwable cause, Object... args) {
        super(code, cause, args);
    }


    @Override
    protected RanExceptionBaseCodes getBaseCode() {
        return RanExceptionBaseCodes.NomenclatorException;
    }
}
