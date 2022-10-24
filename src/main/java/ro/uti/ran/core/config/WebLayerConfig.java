package ro.uti.ran.core.config;

import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import ro.uti.ran.core.rapoarte.mappers.CaseInsensitivePathMatcher;
import ro.uti.ran.core.rapoarte.servlet.DownloadServlet;
import ro.uti.ran.core.rapoarte.servlet.RapoarteServlet;
import ro.uti.ran.core.servlet.JsonExportServlet;

/**
 * Created by Sache on 12/11/2015.
 */

@Configuration
@Profile(Profiles.DEV)
@EnableWebMvc
@ComponentScan("ro.uti.ran.core.rapoarte")
public class WebLayerConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();

    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        //registry.addViewController("/rapoarte").setViewName("rapoarte");
        super.addViewControllers(registry);
    }

    @Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(0);

        return resolver;
    }

//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/js/**").addResourceLocations("/assets/js/");
//        registry.addResourceHandler("/css/**").addResourceLocations("/assets/css/");
//        registry.addResourceHandler("/images/**").addResourceLocations("/assets/css/");
//       // registry.addResourceHandler("/rapoarte/**").addResourceLocations("/rapoarte/");
//    }



    @Bean
    public ServletRegistrationBean delegateServiceExporterServlet() {
        return new ServletRegistrationBean(new JsonExportServlet(), "/JsonExportServlet");
    }


//
//    @Bean
//    public ServletRegistrationBean downloadServlet() {
//        return new ServletRegistrationBean(new DownloadServlet(), "/rapoarte/download");
//
//    }

    //Case insensitive
    @Bean
    public PathMatcher pathMatcher(){
        return new CaseInsensitivePathMatcher();
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        //handlerMapping.setInterceptors(getInterceptors());
        handlerMapping.setPathMatcher(pathMatcher());
        return handlerMapping;
    }



}
