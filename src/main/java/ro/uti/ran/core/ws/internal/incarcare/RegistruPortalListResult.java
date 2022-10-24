package ro.uti.ran.core.ws.internal.incarcare;

import ro.uti.ran.core.utils.HasItems;
import ro.uti.ran.core.utils.ListResult;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 15:54
 */
public class RegistruPortalListResult extends ListResult implements HasItems<DetaliiFisierXml> {


    /**
     * Lista incarcari
     */
    private List<DetaliiFisierXml> items;

    public List<DetaliiFisierXml> getItems() {
        return items;
    }

    public void setItems(List<DetaliiFisierXml> items) {
        this.items = items;
    }
}
