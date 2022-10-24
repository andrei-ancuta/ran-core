package ro.uti.ran.core.service.idm;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-27 16:59
 */
public class ChangePasswordResult implements Serializable{

    /**
     * Rezultatul logout
     */
    private boolean success;

    /**
     * Mesaj descriptiv rezultat logout
     */
    private String message;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
