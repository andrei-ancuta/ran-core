package ro.uti.ran.core.service.idm;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-06 19:54
 */
public class LoginResult implements Serializable{

    /**
     * Rezultatul autentificare
     */
    private boolean success;

    /**
     * Session token
     */
    private String token;

    /**
     * Motiv autentificare esuata
     */
    private String reason;

    /**
     * Mesaj descriptiv rezultat autentificare
     */
    private String message;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
