package ro.uti.ran.core.ws.external;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Anastasia cea micuta on 11/29/2015.
 */
public class RanAuthentication {
    private String username;

    private String password;

    @XmlElement(required = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement(required = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
