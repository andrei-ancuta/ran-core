package ro.uti.ran.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Stanciu Neculai on 04.Jan.2016.
 */
@Configuration
@EnableAspectJAutoProxy
@PropertySource("classpath:audit.properties")
public class AspectConfig {

}
