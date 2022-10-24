package ro.uti.ran;

import com.sun.xml.ws.transport.http.servlet.WSSpringServlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.core.env.AbstractEnvironment;

import ro.uti.ran.core.config.DevScheduleLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.ftp.FtpServlet;
import ro.uti.ran.core.rapoarte.servlet.RapoarteServlet;
import ro.uti.ran.core.session.SessionStatusController;
import ro.uti.ran.core.ws.handlers.RanHandlerFilter;


@Configuration
@EnableAutoConfiguration
@ComponentScan
@ImportResource(
        {"classpath:spring-sun-jaxws.xml" }
)
@SpringBootApplication
        //(exclude=DispatcherServletAutoConfiguration.class)

@Import({
        DevScheduleLayerConfig.class
})

public class ApplicationRunner {

    public static void main(String[] args) throws Exception {
        //Enable a "dev" profile
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Profiles.DEV);
        System.setProperty("devMailPropertiesPath", "classpath:devMail.properties");
        SpringApplication.run(ApplicationRunner.class, args);
    }

    @Bean
    public ServletRegistrationBean ftpServlet() {
        final ServletRegistrationBean ftpServlet = new ServletRegistrationBean(new FtpServlet(), "/ftpInfo");
        ftpServlet.setLoadOnStartup(2);
        return ftpServlet;
    }


    @Bean
    public ServletRegistrationBean sessionStatusServlet() {
        final ServletRegistrationBean ftpServlet = new ServletRegistrationBean(new SessionStatusController(), "/rest/session-status");
        ftpServlet.setLoadOnStartup(4);
        return ftpServlet;
    }

    @Bean
    public ServletRegistrationBean jaxwsServlet() {
        final ServletRegistrationBean jaxws = new ServletRegistrationBean(new WSSpringServlet(), "/service/*");
        jaxws.setLoadOnStartup(1);
        return jaxws;
    }

    @Bean
    public ServletRegistrationBean rapoarteServlet() {
        ServletRegistrationBean rb = new ServletRegistrationBean(new RapoarteServlet(),"/rapoarte");
        rb.setLoadOnStartup(3);
        return rb;

    }

    @Bean
    public FilterRegistrationBean someFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RanHandlerFilter());
        registration.addUrlPatterns("/rapoarte");
        registration.setName("ranHandlerFilter");
        return registration;
    }




//   	@Bean
//	public Filter () {
//		return new RanWsHandlerFilter();
//	}

//    @Bean
//    public ContextLoaderListener ctxContextListener() {
//        return new ContextLoaderListener();
//    }

/* @Bean
    public ServletRegistrationBean rapoarteServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(WebLayerConfig.class);
        dispatcherServlet.setApplicationContext(applicationContext);
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet, "/rapoarte/*");
        servletRegistrationBean.setName("rapoarte");
        return servletRegistrationBean;
    }*/

   /* @Bean
    public FilterRegistrationBean securityFilterChainRegistration() {
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
        delegatingFilterProxy.setTargetBeanName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(delegatingFilterProxy);
        registrationBean.setName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
        registrationBean.addUrlPatterns("*//*");
        return registrationBean;
    }*/



   // final DelegatingFilterProxy springSecurityFilterChain = new DelegatingFilterProxy("springSecurityFilterChain");


//    @Bean
//    public Filter SecurityFilter() {
//        return new DelegatingFilterProxy();
//    }




//    @Bean
//    public FilterRegistrationBean contextFilterRegistrationBean() {
//        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        List<String> urlPatterns = new ArrayList<String>();
//        urlPatterns.add("/rapoarte/*");
//        registrationBean.setUrlPatterns(urlPatterns);
//        //DelegatingFilterProxy springSecurityFilterChain = new SecurityFilter();
//        registrationBean.setFilter(springSecurityFilterChain);
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }


    /*@Bean
    public ServletListenerRegistrationBean jaxwsListerner() {
        ServletListenerRegistrationBean jaxws = new ServletListenerRegistrationBean(new WSServletContextListener());
        return jaxws;
    }
*/

//    @Override
//    public void onStartup(final ServletContext servletContext) throws ServletException {
//        super.onStartup(servletContext);
//        servletContext.addListener(new WSServletContextListener());
//    }


//    public void onStartup(ServletContext servletContext) throws ServletException {
//        servletContext.addListener(new ContextLoaderListener());
//    }


}
