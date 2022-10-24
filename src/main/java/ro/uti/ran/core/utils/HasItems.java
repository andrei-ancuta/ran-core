package ro.uti.ran.core.utils;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-18 14:14
 */
public interface HasItems<T> {

    void setItems(List<T> items);

    List<T> getItems();
}
