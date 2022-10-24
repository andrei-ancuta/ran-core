package ro.uti.ran.core.service.idm.openam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-14 09:12
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateIdentityResponse {

    private String username;

    private String realm;

    private String[] objectclass;

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

    public String[] getObjectclass() {
        return objectclass;
    }

    public void setObjectclass(String[] objectclass) {
        this.objectclass = objectclass;
    }
}
