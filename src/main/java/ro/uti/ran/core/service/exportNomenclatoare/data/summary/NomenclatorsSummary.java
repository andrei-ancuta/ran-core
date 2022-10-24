package ro.uti.ran.core.service.exportNomenclatoare.data.summary;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author horia
 */
@XmlRootElement(name = "sumarNomenclatoare")
@XmlAccessorType(XmlAccessType.FIELD)
public class NomenclatorsSummary {

    @XmlTransient
    private String summaryDate;

    @XmlElementWrapper(name = "nomenclatoare")
    @XmlElements({@XmlElement(name = "nomenclator")})
    private List<NomenclatorMetadata> nomenclators;

    public List<NomenclatorMetadata> getNomenclators() {
        return nomenclators;
    }

    public void setNomenclators(List<NomenclatorMetadata> nomenclators) {
        this.nomenclators = nomenclators;
    }

    public String getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(String summaryDate) {
        this.summaryDate = summaryDate;
    }

}
