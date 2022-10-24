package ro.uti.ran.core.service.exportNomenclatoare.data;


import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: mala
 */
@XmlRootElement(name = "nomenclator")
@XmlAccessorType(XmlAccessType.FIELD)
public class NomenclatorExport {

    @XmlElement(name = "informatiiNomenclator")
    private NomenclatorExportMetadata catalogExportMetadata;

    @XmlElementWrapper(name = "valori")
    @XmlElements({@XmlElement(name = "valoare")})
    private List<NomenclatorExportValue> values;

    public List<NomenclatorExportValue> getValues() {
        return values;
    }

    public void setValues(List<NomenclatorExportValue> values) {
        this.values = values;
    }

    public NomenclatorExportMetadata getCatalogExportMetadata() {
        return catalogExportMetadata;
    }

    public void setCatalogExportMetadata(NomenclatorExportMetadata catalogExportMetadata) {
        this.catalogExportMetadata = catalogExportMetadata;
    }
}
