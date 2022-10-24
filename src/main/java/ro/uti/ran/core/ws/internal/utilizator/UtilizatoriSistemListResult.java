package ro.uti.ran.core.ws.internal.utilizator;

import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 09:41
 */
public class UtilizatoriSistemListResult extends ListResult implements HasItems<Sistem> {

    /**
     * Lista utilizatori
     */
    private List<Sistem> items;

    public List<Sistem> getItems() {
        return items;
    }

    public void setItems(List<Sistem> items) {
        this.items = items;
    }
}
