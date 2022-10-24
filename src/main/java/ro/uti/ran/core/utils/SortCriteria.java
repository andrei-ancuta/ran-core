package ro.uti.ran.core.utils;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-18 16:08
 */
public class SortCriteria implements Serializable {

    private String path;

    private Order order;

    public SortCriteria() {
    }

    public SortCriteria(String path, Order order) {
        this.path = path;
        this.order = order;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "SortCriteria{" +
                "path='" + path + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
