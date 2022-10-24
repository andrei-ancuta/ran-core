package ro.uti.ran.core.service.sistem;

import com.sun.identity.shared.locale.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Sache on 12/15/2015.
 */

@Transactional
@Component
public class MessageBundleServiceImpl implements MessageBundleService {

    private static final Logger logger = LoggerFactory.getLogger(MessageBundleService.class);

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String key) {
        try {
            return messageSource.getMessage(key, null, null);
        } catch (Throwable throwable) {
            logger.error("Mesajul cu cheia \"" + key + "\" nu a fost gasit!");
            return null;
        }
    }

    public String getMessage(String key, String[] args) {
        try {
            return messageSource.getMessage(key, args, null);
        } catch (Throwable throwable) {
            logger.error("Mesajul cu cheia \"" + key + "\" nu a fost gasit!");
            return null;
        }
    }

    public String getMessage(String key, String locale) {
        try {
            return messageSource.getMessage(key, null, locale != null ? Locale.getLocale(locale) : null);
        } catch (Throwable throwable) {
            logger.error("Mesajul cu cheia \"" + key + "\" nu a fost gasit!");
            return null;
        }
    }

    public String getMessage(String key, String[] args, String locale) {
        try {
            return messageSource.getMessage(key, args, locale != null ? Locale.getLocale(locale) : null);
        } catch (Throwable throwable) {
            logger.error("Mesajul cu cheia \"" + key + "\" nu a fost gasit!");
            return null;
        }
    }
}
