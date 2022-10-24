package ro.uti.ran.core.repository.criteria;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-28 18:40
 */
public class OperationNotSupportedException extends RuntimeException {

    public OperationNotSupportedException(String message) {
        super(message);
    }

    public OperationNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
