package ro.uti.ran.core.rapoarte.task.summary;

import ro.uti.ran.core.rapoarte.model.Rapoarte;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miroslav.rusnac on 29/03/2016.
 */

@XmlRootElement(name = "sumarRapoarte")
public class SumarRapoarte {

    private List<Rapoarte> raport;

//    public SumarRapoarte(){}
//    public SumarRapoarte(List<Rapoarte> raport) {
//        //super();
//        this.raport = raport;
//    }

   // @XmlElementWrapper(name = "rapoarte")

    public List<Rapoarte> getRapoarte() {
        return raport;
    }

    @XmlElementWrapper(name = "rapoarte")
    @XmlElements({@XmlElement(name = "raport")})
    public void setRapoarte(List<Rapoarte> raport) {
        this.raport = raport;
    }

    public void add( Rapoarte raport ) {
        if (this.raport == null) {
            this.raport = new ArrayList<Rapoarte>();
        }
        this.raport.add(raport);
    }


}