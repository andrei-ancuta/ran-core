package ro.uti.ran.core.ws.internal.incarcare;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 15:51
 */
public class RezultatIncarcare implements Serializable {

    private Date dataPrimire;

    private String identificatorTransmisie;

    private String mesaj;

    public Date getDataPrimire() {
        return dataPrimire;
    }

    public void setDataPrimire(Date dataPrimire) {
        this.dataPrimire = dataPrimire;
    }

    public String getIdentificatorTransmisie() {
        return identificatorTransmisie;
    }

    public void setIdentificatorTransmisie(String identificatorTransmisie) {
        this.identificatorTransmisie = identificatorTransmisie;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }
}
