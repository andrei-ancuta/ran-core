package ro.uti.ran.core.ws.internal.incarcare;

import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 15:54
 */
public class IncarcariListResult extends ListResult implements HasItems<DetaliiIncarcare> {


    /**
     * Lista incarcari
     */
    private List<DetaliiIncarcare> items;

    public List<DetaliiIncarcare> getItems() {
        return items;
    }

    public void setItems(List<DetaliiIncarcare> items) {
        this.items = items;
    }
}
