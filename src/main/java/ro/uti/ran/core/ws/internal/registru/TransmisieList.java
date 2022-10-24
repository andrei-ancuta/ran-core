package ro.uti.ran.core.ws.internal.registru;

import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreea on 11/4/2015.
 */
@XmlType
public class TransmisieList extends ListResult implements HasItems<Transmisie> {

    private List<Transmisie> items = new ArrayList<Transmisie>();

    public TransmisieList(){};


    @Override
    public void setItems(List<Transmisie> items) {
     this.items = items;
    }

    @Override
    public List<Transmisie> getItems() {
        return items;
    }
}
