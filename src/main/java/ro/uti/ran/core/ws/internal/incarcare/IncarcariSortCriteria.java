package ro.uti.ran.core.ws.internal.incarcare;

import ro.uti.ran.core.model.portal.IncarcareField;
import ro.uti.ran.core.utils.ISortCriteria;
import ro.uti.ran.core.utils.Order;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-27 14:28
 */
public class IncarcariSortCriteria implements ISortCriteria{
    private IncarcareField field;
    private Order order;

    public IncarcariSortCriteria() {
    }

    public IncarcariSortCriteria(IncarcareField field, Order order) {
        this.field = field;
        this.order = order;
    }

    public IncarcareField getField() {
        return field;
    }

    public void setField(IncarcareField field) {
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
