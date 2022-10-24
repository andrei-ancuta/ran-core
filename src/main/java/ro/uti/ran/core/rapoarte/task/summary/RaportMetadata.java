package ro.uti.ran.core.rapoarte.task.summary;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by miroslav.rusnac on 29/03/2016.
 */
@XmlRootElement(name = "raport")
@XmlAccessorType(XmlAccessType.FIELD)
public class RaportMetadata {

    @XmlElement(name = "denumire", required = true)
    private String raportName;


    public String getRaportName() {
        return raportName;
    }

    public void setRaportName(String raportName) {
        this.raportName = raportName;
    }

}
