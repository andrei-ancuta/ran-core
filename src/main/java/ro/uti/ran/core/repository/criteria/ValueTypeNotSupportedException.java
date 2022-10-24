package ro.uti.ran.core.repository.criteria;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-29 10:46
 */
public class ValueTypeNotSupportedException extends RuntimeException {
    public ValueTypeNotSupportedException(String message) {
        super(message);
    }

    public ValueTypeNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
