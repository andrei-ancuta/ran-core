package ro.uti.ran.core.ws.internal.incarcare;

import ro.uti.ran.core.model.portal.RegistruPortalDetailsField;
import ro.uti.ran.core.utils.ISortCriteria;
import ro.uti.ran.core.utils.Order;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-27 14:28
 */
public class RegistruPortalSortCriteria implements ISortCriteria{
    private RegistruPortalDetailsField field;
    private Order order;

    public RegistruPortalSortCriteria() {
    }

    public RegistruPortalSortCriteria(RegistruPortalDetailsField field, Order order) {
        this.field = field;
        this.order = order;
    }

    public RegistruPortalDetailsField getField() {
        return field;
    }

    public void setField(RegistruPortalDetailsField field) {
        this.field = field;
    }

    @Override
    public String getPath() {
        return field.getPath();
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
