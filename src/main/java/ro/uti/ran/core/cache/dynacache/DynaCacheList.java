package ro.uti.ran.core.cache.dynacache;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lista de cacheuri de pe WAS (Lazy)
 * @author Mihi
 *
 */
public class DynaCacheList extends ConcurrentHashMap<String, DynaCacheElement>{

	private static final long serialVersionUID = 1L;
	
	private InitialContext ctx;
	
	protected static final ResourceBundle CONFIG = ResourceBundle.getBundle("dynacache.ttl");
	
	public DynaCacheList() {
		init();
	}
	
	@Override
	public DynaCacheElement get(Object key) {
		
		DynaCacheElement cache = super.get(key);
		
		if(cache != null) {
			return cache;
		}
		
		cache = getFromContext((String)key);
		
		//atomic operation
		if(cache != null) {
			this.putIfAbsent((String)key, cache);
		}
		
		return cache;
	}
	
	private DynaCacheElement getFromContext(String key) {
		DynaCacheElement cache;
				
		try {
		  	Map result = (Map) ctx.lookup("services/cache/" + key);
		
		  	if(result == null) {
		  		 return null;
		  	}
           
		  	configCache(result, key);
		  	cache = new DynaCacheElement(key, result);
		  	
        } catch (Exception e) {
        	return null;
        }
		
		return cache;
	}
	
	
	private void configCache(Map cache, String cacheName) throws Exception {
		
		String ttl = CONFIG.getString(cacheName);
		if(ttl != null) {
			Method method = cache.getClass().getMethod("setTimeToLive", int.class);
			method.invoke(cache, new Integer(ttl));
		}
		
	}
	
	private void init() {
		try {
			ctx = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
}
