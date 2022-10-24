package ro.uti.ran.core.service.idm.openam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Informatii server OpenAM
 *
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-09 11:28
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerInfo {

    /**
     *
     */
    private List<String> domains;

    /**
     *
     */
    private String cookieName;


    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "domains=" + domains +
                ", cookieName='" + cookieName + '\'' +
                '}';
    }
}
