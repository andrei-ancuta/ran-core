package ro.uti.ran.core.service.nomenclator;

import ro.uti.ran.core.repository.criteria.Operation;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-12-04 13:44
 */
public class NomenclatorSearchCriteria {
    private String path;
    private Operation operation;
    private Object value;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
