package ro.uti.ran.core.ws.internal.utilizator;

import ro.uti.ran.core.model.portal.Rol;
import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 10:08
 */
public class RoluriListResult extends ListResult implements HasItems<Rol> {

    private List<Rol> items;

    public List<Rol> getItems() {
        return items;
    }

    public void setItems(List<Rol> items) {
        this.items = items;
    }
}
