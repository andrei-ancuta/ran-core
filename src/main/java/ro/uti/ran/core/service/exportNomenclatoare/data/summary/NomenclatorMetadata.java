package ro.uti.ran.core.service.exportNomenclatoare.data.summary;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author horia
 */
@XmlRootElement(name = "nomenclator")
@XmlAccessorType(XmlAccessType.FIELD)
public class NomenclatorMetadata {

    @XmlElement(name = "nomenclator", required = true)
    private String nomenclator;

    @XmlElement(name = "codNomenclator", required = true)
    private String nomenclatorName;

    @XmlElement(name = "dataUltimeiActualizari", required = true)
    private String lastModifyDate;

    public String getNomenclator() {
        return nomenclator;
    }

    public void setNomenclator(String nomenclator) {
        this.nomenclator = nomenclator;
    }

    public String getNomenclatorName() {
        return nomenclatorName;
    }

    public void setNomenclatorName(String nomenclatorName) {
        this.nomenclatorName = nomenclatorName;
    }

    public String getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(String lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

}
