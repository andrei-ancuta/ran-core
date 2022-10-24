package ro.uti.ran.core.service.idm.exception;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-14 09:20
 */
public class ChangePasswordException extends IdmIntegrationException {

    public ChangePasswordException(String message) {
        super(message);
    }

    public ChangePasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
