package ro.uti.ran.core.ws.internal.utilizator;

import ro.uti.ran.core.model.portal.Utilizator;
import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 09:41
 */
public class UtilizatoriListResult extends ListResult implements HasItems<Utilizator> {

    /**
     * Lista utilizatori
     */
    private List<Utilizator> items;

    public List<Utilizator> getItems() {
        return items;
    }

    public void setItems(List<Utilizator> items) {
        this.items = items;
    }
}
