package ro.uti.ran.core.service.exportNomenclatoare.data;

import javax.xml.bind.annotation.*;

/**
 * Created by Stanciu Neculai on 20.Nov.2015.
 */
@XmlRootElement(name = "referinte")
@XmlAccessorType(XmlAccessType.FIELD)
public class NomenclatorReference {

    @XmlAttribute(name = "descriere", required = true)
    private String nomRefDescription;

    @XmlElement(name = "referinta")
    private NomenclatorExportValue reference;

    public NomenclatorExportValue getReference() {
        return reference;
    }

    public void setReference(NomenclatorExportValue reference) {
        this.reference = reference;
    }

    public String getNomRefDescription() {
        return nomRefDescription;
    }

    public void setNomRefDescription(String nomRefDescription) {
        this.nomRefDescription = nomRefDescription;
    }
}
