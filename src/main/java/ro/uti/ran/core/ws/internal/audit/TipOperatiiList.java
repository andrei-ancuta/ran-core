package ro.uti.ran.core.ws.internal.audit;

import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Created by adrian.boldisor on 3/4/2016.
 */
@XmlType
public class TipOperatiiList extends ListResult implements HasItems {


    private List<TipOperatieDetalii> items;



    public TipOperatiiList(){};


    public TipOperatiiList(List<TipOperatieDetalii> items){
        if(items!=null && !items.isEmpty()){
            this.items = items;
        }

    };

    public List<TipOperatieDetalii> getTipOperatieDetaliiList(){
        return this.getItems();
    }

    public void setTipOperatieDetaliiList(List<TipOperatieDetalii> tipOperatieDetaliiList){
        this.items = tipOperatieDetaliiList;
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
