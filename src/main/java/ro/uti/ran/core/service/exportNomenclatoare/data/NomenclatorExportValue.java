package ro.uti.ran.core.service.exportNomenclatoare.data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: mala
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "valoare")
public class NomenclatorExportValue {

    @XmlAttribute(name = "cod", required = true)
    private String code;

    @XmlAttribute(name = "descriere", required = true)
    private String description;

    @XmlAttribute(name = "dataStart", required = false)
    private String validFrom;

    @XmlAttribute(name = "dataStop", required = false)
    private String validTo;

    @XmlAttribute(name = "codParinte", required = false)
    private String parentCode;

    @XmlElement(name = "referinte", required = false)
    private List<NomenclatorReference> references;

    public NomenclatorExportValue() {
    }

    public NomenclatorExportValue(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public List<NomenclatorReference> getReferences() {
        return references;
    }

    public void setReferences(List<NomenclatorReference> references) {
        this.references = references;
    }
}
