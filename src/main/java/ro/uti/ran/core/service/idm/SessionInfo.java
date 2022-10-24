package ro.uti.ran.core.service.idm;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-12-07 06:46
 */
public class SessionInfo {
    private String token;
    private String username;
    private String realm;
    private boolean valid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
