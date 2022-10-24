package ro.uti.ran.core.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-08-27 15:06
 */
public class GenericListResult<T> extends ListResult implements HasItems<T>{

    private List<T> items;

    public GenericListResult() {
    }

    public GenericListResult(GenericListResult<T> listResult) {
        super(listResult);
        this.setItems(listResult.getItems());
    }

    public List<T> getItems() {
        if( items == null){
            items = new LinkedList<T>();
        }
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
