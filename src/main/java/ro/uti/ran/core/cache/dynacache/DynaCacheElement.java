package ro.uti.ran.core.cache.dynacache;

import org.springframework.cache.Cache;

import java.util.Map;

/**
 * Implementarea Cache pt. WAS
 *
 * @author Mihi
 */
public class DynaCacheElement implements Cache {

    private String name;
    private Map cache;

    public DynaCacheElement(String name, Map cache) {
        this.name = name;
        this.cache = cache;
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public void evict(Object key) {
        cache.remove(key);
    }

    @Override
    public ValueWrapper get(Object key) {

        //nu se initializeaza replicarea obiectului
        Object o = null;

        try {
            o = cache.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cache.containsKey(key) && o != null) {
            return new Element(o);
        }

        return null;

    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object value = cache.get(key);
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return cache;
    }

    @Override
    public void put(Object key, Object value) {
        if (value != null) {
            cache.put(key, value);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        final Object existingValue = cache.get(key);
        if (existingValue == null) {
            cache.put(key, value);
            return null;
        } else {
            return new Element(existingValue);
        }
    }

    private class Element implements ValueWrapper {

        private Object value;

        public Element(Object value) {
            this.value = value;
        }

        @Override
        public Object get() {
            return this.value;
        }

    }

}