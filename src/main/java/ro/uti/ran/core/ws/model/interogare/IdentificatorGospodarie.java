package ro.uti.ran.core.ws.model.interogare;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Anastasia cea micuta on 10/11/2015.
 */
public class IdentificatorGospodarie {
    private String id;

    private Integer sirutaUAT;

    private String denumireUAT;

    private boolean activ;

    @XmlElement(required = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(required = true)
    public Integer getSirutaUAT() {
        return sirutaUAT;
    }

    public void setSirutaUAT(Integer sirutaUAT) {
        this.sirutaUAT = sirutaUAT;
    }

    @XmlElement(required = true)
    public String getDenumireUAT() {
        return denumireUAT;
    }

    public void setDenumireUAT(String denumireUAT) {
        this.denumireUAT = denumireUAT;
    }

    @XmlElement(required = true)
    public boolean getActiv() {
        return activ;
    }

    public void setActiv(boolean activ) {
        this.activ = activ;
    }
}
