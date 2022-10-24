package ro.uti.ran.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOTokenManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import ro.uti.ran.core.service.idm.IdmService;
import ro.uti.ran.core.service.idm.openam.MyClientHttpRequestInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-09 11:35
 */
@Configuration
//@Profile(Profiles.PRODUCTION)
@PropertySource("classpath:openam.properties")
public class OpenAmConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAmConfig.class);


    @Autowired
    private Environment env;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @Lazy
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @Lazy
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);

        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(requestFactory));

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new MyClientHttpRequestInterceptor());

        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    @Lazy
    public SSOTokenManager ssoTokenManager() throws SSOException {
        return SSOTokenManager.getInstance();
    }

    @Bean
    @Autowired
    public IdmService idmService(List<IdmService> idmServices) {
        String idmServiceName = env.getProperty("idmServiceName");
        LOGGER.info("idmServiceName"+ idmServiceName);
        return applicationContext.getBean(StringUtils.uncapitalize(idmServiceName), IdmService.class);
    }

}
