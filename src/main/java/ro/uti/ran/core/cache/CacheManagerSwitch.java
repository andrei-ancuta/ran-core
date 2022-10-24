package ro.uti.ran.core.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.cache.dynacache.DynaCacheManager;
import ro.uti.ran.core.cache.ehcache.EhCacheManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Daca e pe WAS -> foloseste de pe WAS; altfel ia din EhCache
 * @author Mihi
 *
 */
@Component
public class CacheManagerSwitch implements CacheManager {

	private final Log log = LogFactory.getLog(CacheManagerSwitch.class);
	   
	@Autowired
	private EhCacheManager ehCacheManager;
	
	@Autowired
	private DynaCacheManager dynaCacheManager;
	
	@Override
	public Cache getCache(String cacheName) {
		
		Cache cache = dynaCacheManager.getCache(cacheName);
		
		if(cache != null) {
			log.debug("Cache " + cacheName +"  found in WebSphere caches - Using DynaCache");
			return cache;
		}
		
		log.debug("Cache " + cacheName +" not found in WebSphere caches - Using EhCache");
		return ehCacheManager.getCache(cacheName);
	}

	@Override
	public Collection<String> getCacheNames() {
		Set<String> caches = new HashSet<String>();
		caches.addAll(ehCacheManager.getCacheNames());
		caches.addAll(dynaCacheManager.getCacheNames());
		return caches;
	}

}
