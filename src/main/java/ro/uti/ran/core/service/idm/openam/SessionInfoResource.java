package ro.uti.ran.core.service.idm.openam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-12-07 08:45
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionInfoResource {
    private boolean valid;
    private String uid;
    private String realm;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
}
