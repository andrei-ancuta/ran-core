package ro.uti.ran.core.rapoarte.task.summary;

import ro.uti.ran.core.rapoarte.model.Rapoarte;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by miroslav.rusnac on 29/03/2016.
 */
@XmlRootElement(name = "sumarRapoarte")
@XmlAccessorType(XmlAccessType.FIELD)
public class RapoarteSummary {

    @XmlTransient
    private String summaryDate;


    @XmlElementWrapper(name = "rapoarte")
    @XmlElements({@XmlElement(name = "raport")})
    private List<RaportMetadata> rapoarte;



    public List<RaportMetadata> getRapoarte() {
        return rapoarte;
    }

    public void setRapoarte(List<RaportMetadata> rapoarte) {
        this.rapoarte = rapoarte;
    }

    public String getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(String summaryDate) {
        this.summaryDate = summaryDate;
    }

}