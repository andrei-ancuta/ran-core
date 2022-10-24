package ro.uti.ran.core.ws.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-08-21 10:03
 */
public class AppContextListener implements ServletContextListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOGGER.debug("RAN Core WS started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOGGER.debug("RAN Core WS stopped");
    }
}
