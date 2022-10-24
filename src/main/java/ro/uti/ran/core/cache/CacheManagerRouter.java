package ro.uti.ran.core.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Unificare cache-uri
 *
 * @author Mihi
 */
@Component
public class CacheManagerRouter implements CacheManager {

    private final Log log = LogFactory.getLog(CacheManagerRouter.class);

    @Autowired
    private CacheManagerSwitch cacheManagerSwitch;

    @Override
    public Cache getCache(String cacheName) {

        Cache cache = cacheManagerSwitch.getCache(cacheName);

        if (cache != null) {
            log.debug("Cache " + cacheName + "  found in UTI caches: " + cache.getClass().getCanonicalName());
            return cache;
        }
        log.debug("Cache " + cacheName + " not found");
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheManagerSwitch.getCacheNames();
    }

    @PostConstruct
    private void clean() {
        Cache cache = null;
        for (String cacheName : getCacheNames()) {
            cache = getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        }
    }

}
