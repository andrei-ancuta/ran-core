package ro.uti.ran.core.service.idm.exception;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-30 12:13
 */
public class IdmIntegrationException extends RuntimeException {

    public IdmIntegrationException(String message) {
        super(message);
    }

    public IdmIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
