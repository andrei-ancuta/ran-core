package ro.uti.ran.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.uti.ran.core.service.security.AuthenticationHandler;
import ro.uti.ran.core.service.security.AuthorizationService;
import ro.uti.ran.core.service.security.AuthorizationServiceImpl;
import ro.uti.ran.core.service.security.ExternWsContextEnum;

/**
 * Created by Stanciu Neculai on 29.Oct.2015.
 */
@Configuration
public class ExportWsConfig {

    @Bean(name = "authHandlerTransmitereDate")
    public AuthenticationHandler  authenticationHandlerTD(){
        return createAuthBeanByContext(ExternWsContextEnum.WS_TRANSMITERE_DATE);
    }
    @Bean(name = "authHandlerInterogareDate")
    public AuthenticationHandler  authenticationHandlerID(){
        return createAuthBeanByContext(ExternWsContextEnum.WS_INTEROGARE_DATE);
    }

    private AuthenticationHandler createAuthBeanByContext(ExternWsContextEnum wsContext){
        AuthorizationService authorizationService = new AuthorizationServiceImpl(wsContext);
        AuthenticationHandler authenticationHandler = new AuthenticationHandler();
        authenticationHandler.setAuthorizationService(authorizationService);
        return authenticationHandler;
    }


}
