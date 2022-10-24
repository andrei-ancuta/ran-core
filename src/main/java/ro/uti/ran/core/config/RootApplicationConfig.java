package ro.uti.ran.core.config;

import org.springframework.context.annotation.*;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-01 09:49
 */
@Configuration
//@EnableAspectJAutoProxy
@ComponentScan({"ro.uti.ran.core", "ro.ancpi.ran.core"})
@ImportResource({"classpath:spring-sun-jaxws.xml"})
@PropertySource({"classpath:application.properties", "classpath:logback.properties", "classpath:gisserver.properties"})
public class RootApplicationConfig {
}
