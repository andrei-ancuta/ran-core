package ro.uti.ran.core.ws.internal.utilizator;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-25 17:17
 */
public class UatConfig implements Serializable {
    boolean notificareRaportare;

    boolean transmitereManuala;

    public boolean isNotificareRaportare() {
        return notificareRaportare;
    }

    public void setNotificareRaportare(boolean notificareRaportare) {
        this.notificareRaportare = notificareRaportare;
    }

    public boolean isTransmitereManuala() {
        return transmitereManuala;
    }

    public void setTransmitereManuala(boolean transmitereManuala) {
        this.transmitereManuala = transmitereManuala;
    }
}
