package ro.uti.ran.core.ws.internal.audit;

import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Created by smash on 04/01/16.
 */

@XmlType
public class JurnalizareOperatiiList extends ListResult implements HasItems {

    private List<JurnalizareOperatieDetalii> items;


    public JurnalizareOperatiiList() {
    }

    public JurnalizareOperatiiList(List<JurnalizareOperatieDetalii> items) {
        if(items!=null && !items.isEmpty()){
            this.items = items;
        }
    }

    public List<JurnalizareOperatieDetalii> getJurnalizareOperatieDetaliiList(){
        return this.getItems();
    }

    public void setJurnalizareOperatieDetaliiList(List<JurnalizareOperatieDetalii> operatieDetaliiList){
        this.items = operatieDetaliiList;
    }

    @Override
    public void setItems(List items) {
        this.items = items;
    }

    @Override
    public List getItems() {
        return this.items;
    }
}
