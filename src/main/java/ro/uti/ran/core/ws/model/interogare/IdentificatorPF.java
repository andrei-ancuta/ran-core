package ro.uti.ran.core.ws.model.interogare;

import ro.uti.ran.core.xml.model.types.CNP;
import ro.uti.ran.core.xml.model.types.NIF;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * Created by Anastasia cea micuta on 1/25/2016.
 */
public class IdentificatorPF {
    private Object identificator;

    @XmlElements({
            @XmlElement(name = "cnp", required = true, type = CNP.class),
            @XmlElement(name = "nif", required = true, type = NIF.class)
    })
    public Object getIdentificator() {
        return identificator;
    }

    public void setIdentificator(Object identificator) {
        this.identificator = identificator;
    }
}
