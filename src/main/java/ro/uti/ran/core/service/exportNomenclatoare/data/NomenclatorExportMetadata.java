package ro.uti.ran.core.service.exportNomenclatoare.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA. User: mala
 */
@XmlRootElement(name = "informatiiNomenclator")
@XmlAccessorType(XmlAccessType.FIELD)
public class NomenclatorExportMetadata {

    @XmlElement(name = "denumire", required = true)
    private String displayName;

    @XmlElement(name = "descriere")
    private String description;

    @XmlElement(name = "dataExport", required = true)
    private String date;

    @XmlElement(name = "dataUltimeiActualizari", required = true)
    private String lastModifyDate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(String lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }
}
