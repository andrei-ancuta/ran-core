package ro.uti.ran.core.ws.internal.registru;

import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 15:54
 */
public class RegistruListResult extends ListResult implements HasItems<Registru> {


    /**
     * Lista registru
     */
    private List<Registru> items;

    public List<Registru> getItems() {
        return items;
    }

    public void setItems(List<Registru> items) {
        this.items = items;
    }
}
