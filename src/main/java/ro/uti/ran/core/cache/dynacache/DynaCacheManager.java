package ro.uti.ran.core.cache.dynacache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Cache Manager pt. WAS
 * @author Mihi
 *
 */
@Component
public class DynaCacheManager implements CacheManager {

	private DynaCacheList caches = new DynaCacheList();
	
	@Override
	public Cache getCache(String name) {
		return caches.get(name);
	}

	@Override
	public Collection<String> getCacheNames() {
		return new HashSet(Collections.list(caches.CONFIG.getKeys()));
	}

}
