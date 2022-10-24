package ro.uti.ran.core.utils;

/**
 * Created by bogdan.ardeleanu on 1/29/2016.
 */
public class Tuple<T, U, V> {

    private final T t;
    private final U u;
    private final V v;

    public Tuple(T t, U u, V v) {
        this.t = t;
        this.u = u;
        this.v = v;
    }

    public T getLeft() {
        return t;
    }

    public U getCenter() {
        return u;
    }

    public V getRight() {
        return v;
    }

}
