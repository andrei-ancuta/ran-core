package ro.uti.ran.core.ws.utils;

import ro.uti.ran.core.model.portal.Sesiune;

/**
 * Informatii de autorizare/audit transmise la apel servicii
 *
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-12-22 18:09
 */
public class AuditInfo {
    private String username;
    private String context;
    private String token;
    private String remoteIp;

    private Sesiune sesiune;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public Sesiune getSesiune() {
        return sesiune;
    }

    public void setSesiune(Sesiune sesiune) {
        this.sesiune = sesiune;
    }


    @Override
    public String toString() {
        return "AuditInfo{" +
                "username='" + username + '\'' +
                ", context='" + context + '\'' +
                ", token='" + token + '\'' +
                ", remoteIp='" + remoteIp + '\'' +
                ", sesiune=" + sesiune +
                '}';
    }
}
