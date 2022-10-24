package ro.uti.ran.core.ws.model.transmitere;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-29 17:13
 */
public class InformatiiTransmisie implements Serializable {

    private StareTransmisie stareTransmisie;

    private boolean isRecipisaSemnata;

    private byte[] recipisa;

    @XmlElement(required = true)
    public StareTransmisie getStareTransmisie() {
        return stareTransmisie;
    }

    public void setStareTransmisie(StareTransmisie stareTransmisie) {
        this.stareTransmisie = stareTransmisie;
    }

    @XmlElement(required = true)
    public boolean getIsRecipisaSemnata() {
        return isRecipisaSemnata;
    }

    public void setIsRecipisaSemnata(boolean isRecipisaSemnata) {
        this.isRecipisaSemnata = isRecipisaSemnata;
    }

    @XmlElement(required = true)
    public byte[] getRecipisa() {
        return recipisa;
    }

    public void setRecipisa(byte[] recipisa) {
        this.recipisa = recipisa;
    }
}
