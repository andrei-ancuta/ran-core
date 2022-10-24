package ro.uti.ran.core.exception;

import ro.uti.ran.core.ws.fault.RanRuntimeException;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-05 14:07
 */
public class UnsupportedOperationException extends RanRuntimeException {
    public UnsupportedOperationException(String message) {
        super(message);
    }
}
