package ro.uti.ran.core.repository.criteria;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-18 14:26
 */
public class RepositoryCriteria<V> {

    private String path;

    private Operation operation;

    private V value;


    public RepositoryCriteria(String path, Operation operation, V value) {
        this.path = path;
        this.operation = operation;
        this.value = value;
    }

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

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
