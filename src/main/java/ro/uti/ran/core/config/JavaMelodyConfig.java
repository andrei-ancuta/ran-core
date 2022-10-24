package ro.uti.ran.core.config;

import javax.servlet.Filter;
import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;

import net.bull.javamelody.JdbcWrapper;
import net.bull.javamelody.MonitoringFilter;
import net.bull.javamelody.MonitoringSpringAdvisor;
import net.bull.javamelody.SessionListener;

import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

/**
 * 
 * @author mihai.plavichianu
 *
 */
@Configuration
@ImportResource("classpath:net/bull/javamelody/monitoring-spring-aspectj.xml")
public class JavaMelodyConfig  {
	
	@Bean
    public HttpSessionListener javaMelodyListener() {
        return new SessionListener();
    }
	
	@Bean
    public Filter javaMelodyFilter() {
        return new MonitoringFilter();
    }
	
	@Bean(name="facadeMonitoringAdvisor")
    public MonitoringSpringAdvisor getMonitoringSpringAdvisor() {
         MonitoringSpringAdvisor monitoringSpringAdvisor = new MonitoringSpringAdvisor();
         monitoringSpringAdvisor.setPointcut(getJdkRegexpMethodPointcut());
         return monitoringSpringAdvisor;
    }

    @Bean
    public JdkRegexpMethodPointcut getJdkRegexpMethodPointcut() {
         JdkRegexpMethodPointcut jdkRegexpMethodPointcut = new JdkRegexpMethodPointcut();
         jdkRegexpMethodPointcut.setPatterns("ro.uti.ran.core.service.*.*(..)");
         return jdkRegexpMethodPointcut;
    }
    
    protected static DataSource createProxy(String name, DataSource dataSource) {
    	return JdbcWrapper.SINGLETON.createDataSourceProxy(name, dataSource);
    }
    
}
