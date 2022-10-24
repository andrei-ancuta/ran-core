package ro.uti.ran.core.cache.ehcache;

import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;


/**
 * EhCache manager
 * @author Mihi
 *
 */
@Component
public class EhCacheManager extends EhCacheCacheManager {
    public static final Logger log = LoggerFactory.getLogger(EhCacheManager.class);

    public EhCacheManager() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setShared(true);
        factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        try {
            factoryBean.afterPropertiesSet();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        // create spring ehcache
        CacheManager cacheManager = factoryBean.getObject();
        setCacheManager(cacheManager);
	}
}
