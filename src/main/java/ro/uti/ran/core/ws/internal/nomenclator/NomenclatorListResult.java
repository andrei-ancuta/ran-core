package ro.uti.ran.core.ws.internal.nomenclator;

import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 12:42
 */
public class NomenclatorListResult extends ListResult implements HasItems<Nomenclator> {

    private List<Nomenclator> items;

    public List<Nomenclator> getItems() {
        return items;
    }

    public void setItems(List<Nomenclator> items) {
        this.items = items;
    }
}
