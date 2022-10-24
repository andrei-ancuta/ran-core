package ro.uti.ran.core.ws.utils;

import org.springframework.stereotype.Component;

import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;


/**
 * @author mihai.plavichianu
 */
@Component
public class ExceptionUtil {

    protected static ThreadLocal<Throwable> exceptionStackTrace = new ThreadLocal<Throwable>();

    public RanException buildException(RanException e) {
        logException(e);
        ExceptionUtil.exceptionStackTrace.set(e);
        return e;

    }

    public RanRuntimeException buildException(RanRuntimeException e) {
        logException(e);
        ExceptionUtil.exceptionStackTrace.set(e);
        return e;

    }

    protected void logException(Exception e) {
        e.printStackTrace();
    }

    public Throwable getExceptionStackTrace() {
        return exceptionStackTrace.get();
    }

}
