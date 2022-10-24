package ro.uti.ran.core.ws.internal.utilizator;

import ro.uti.ran.core.model.portal.Utilizator;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-30 01:00
 */
public class RezultatVerificareUtilizator implements Serializable {

    public enum Rezolutie{
        EXISTA_RAN,
        EXISTA_LDAP,
        NU_EXISTA
    }

    private Rezolutie rezolutie;

    /**
     * Completat
     */
    private Utilizator utilizator;

    private Utilizator utilizatorIdm;


    public Rezolutie getRezolutie() {
        return rezolutie;
    }

    public void setRezolutie(Rezolutie rezolutie) {
        this.rezolutie = rezolutie;
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    public Utilizator getUtilizatorIdm() {
        return utilizatorIdm;
    }

    public void setUtilizatorIdm(Utilizator utilizatorIdm) {
        this.utilizatorIdm = utilizatorIdm;
    }
}
