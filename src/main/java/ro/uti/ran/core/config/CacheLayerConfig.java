package ro.uti.ran.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.*;
import org.springframework.context.annotation.Configuration;
import ro.uti.ran.core.cache.CacheManagerRouter;

/**
 * Created with IntelliJ IDEA. User: mala
 */
@Configuration
@EnableCaching(proxyTargetClass = true)
public class CacheLayerConfig implements CachingConfigurer {

    @Autowired
    private CacheManagerRouter cacheManager;

    public CacheManager cacheManager() {
        return cacheManager;

    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    /**
     * Se poate defini un KeyGenerator custom, cel default nu tine cont de numele clasei sau al metodei, doar de argumente. KeyGenerator e
     * folosit atunci cand @Cacheable e aplicat avand 'key=""'(default).
     * <p/>
     * See also the method {@link org.springframework.cache.interceptor.CacheAspectSupport.CacheOperationContext#generateKey(Object result)}
     *
     * @return DefaultKeyGenerator
     */
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler();
    }

}
