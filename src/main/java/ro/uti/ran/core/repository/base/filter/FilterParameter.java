package ro.uti.ran.core.repository.base.filter;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-11 10:28
 */
public class FilterParameter {
    private String name;
    private Object value;

    public FilterParameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
