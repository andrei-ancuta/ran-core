package ro.uti.ran.core.repository.criteria;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-28 16:52
 */
public class InvalidCriteriaException extends RuntimeException {

    public InvalidCriteriaException(String message) {
        super(message);
    }

    public InvalidCriteriaException(Throwable cause) {
        super(cause);
    }
}
