package ro.uti.ran.core.service.incarcareZip;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Created by Stanciu Neculai on 10.Dec.2015.
 */
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    private static final Log logger = LogFactory.getLog(CustomAsyncExceptionHandler.class);
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        StringBuilder sb = new StringBuilder(50);
        sb.append("Eroare la metoda:").append(method.getName()).append(" cu parametrii: ").append(params.toString());
        logger.error(sb.toString(),ex);
    }
}
