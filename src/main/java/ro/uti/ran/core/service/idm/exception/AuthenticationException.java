package ro.uti.ran.core.service.idm.exception;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-08 23:05
 */
public class AuthenticationException extends IdmIntegrationException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
