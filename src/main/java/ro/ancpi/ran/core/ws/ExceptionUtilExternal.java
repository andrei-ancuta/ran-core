package ro.ancpi.ran.core.ws;

import org.springframework.stereotype.Component;
import ro.ancpi.ran.core.ws.fault.RanException;
import ro.ancpi.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

/**
 * Created by Sache on 8/17/2016.
 */
@Component
public class ExceptionUtilExternal extends ExceptionUtil {

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
}
