package ro.uti.ran.core.ws.internal.nomenclator;

import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 12:42
 */
public class JudetListResult extends ListResult implements HasItems<Judet> {

    private List<Judet> items;

    public List<Judet> getItems() {
        return items;
    }

    public void setItems(List<Judet> items) {
        this.items = items;
    }
}
