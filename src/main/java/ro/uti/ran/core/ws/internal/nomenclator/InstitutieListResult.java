package ro.uti.ran.core.ws.internal.nomenclator;

import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 12:42
 */
public class InstitutieListResult extends ListResult implements HasItems<Institutie> {

    private List<Institutie> items;

    public List<Institutie> getItems() {
        return items;
    }

    public void setItems(List<Institutie> items) {
        this.items = items;
    }
}
