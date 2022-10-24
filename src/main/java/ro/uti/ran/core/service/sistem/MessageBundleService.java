package ro.uti.ran.core.service.sistem;

/**
 * Created by Sache on 12/15/2015.
 */
public interface MessageBundleService {

    public String getMessage(String key);

    public String getMessage(String key, String[] args);

    public String getMessage(String key, String locale);

    public String getMessage(String key, String[] args, String locale);
}
