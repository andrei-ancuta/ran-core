package ro.uti.ran.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import ro.uti.ran.core.ws.client.renns.RennsAddressesService;
import ro.uti.ran.core.ws.client.renns.RennsAddressesServiceImpl;
import ro.uti.ran.core.ws.client.renns.RennsRoadsService;
import ro.uti.ran.core.ws.client.renns.RennsRoadsServiceImpl;

import java.io.IOException;

/**
 * Created by bogdan.ardeleanu on 11/15/2016.
 */
//@Configuration
public class WsLayerConfig {

    @Autowired
    private Environment env;

    @Bean
    SaajSoapMessageFactory saajSoapMessageFactory() {
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.setSoapVersion(SoapVersion.SOAP_11);
        messageFactory.afterPropertiesSet();
        return messageFactory;
    }

    /*@Bean
    WebServiceTemplate webServiceTemplate() throws Exception {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMessageFactory(saajSoapMessageFactory());
        template.setInterceptors(new ClientInterceptor[]{
                        wss4jSecurityInterceptor()
//                        securityInterceptor()
                }

        );
        template.afterPropertiesSet();
        return template;
    }*/

    @Bean
    Wss4jSecurityInterceptor wss4jSecurityInterceptor() throws Exception {
        Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
        securityInterceptor.setSecurementUsername(env.getProperty("ws.renns.credentials.username"));
        securityInterceptor.setSecurementPassword(env.getProperty("ws.renns.credentials.password"));
//        securityInterceptor.setSecurementPasswordType(WSConstants.PW_TEXT);
        securityInterceptor.setSecurementActions("Timestamp UsernameToken");
        securityInterceptor.setSecurementTimeToLive(18000);
        securityInterceptor.setTimestampPrecisionInMilliseconds(true);
        securityInterceptor.afterPropertiesSet();
        return securityInterceptor;
    }

    /*@Bean
    XwsSecurityInterceptor securityInterceptor() {
        XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
        securityInterceptor.setSecureRequest(true);
        securityInterceptor.setValidateRequest(true);
        *//*securityInterceptor.setCallbackHandler(new CallbackHandler() {
            @Override
            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

            }
        });*//*
        securityInterceptor.setCallbackHandler(new SimpleUsernamePasswordCallbackHandler("ran_soap", "Qazwsxedc1234!"));
        securityInterceptor.setPolicyConfiguration(new ClassPathResource("ws/securityPolicy.xml"));
        return securityInterceptor;
    }
*/

    @Bean
    public RennsRoadsService rennsRoadsService() throws Exception {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("ro.uti.ran.core.ws.client.renns.roads");

        RennsRoadsServiceImpl service = new RennsRoadsServiceImpl();
       /* service.setDefaultUri(env.getProperty("ws.renns.url"));
        service.setMessageFactory(saajSoapMessageFactory());
        service.setMarshaller(marshaller);
        service.setUnmarshaller(marshaller);
        service.setInterceptors(new ClientInterceptor[]{
                wss4jSecurityInterceptor(),
                new LogWebServiceMessageCallback()
        });*/
        return service;
    }

    @Bean
    public RennsAddressesService rennsAddressesService() throws Exception {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("ro.uti.ran.core.ws.client.addresses");

        RennsAddressesServiceImpl service = new RennsAddressesServiceImpl();
       /* service.setDefaultUri(env.getProperty("ws.renns.url"));
        service.setMessageFactory(saajSoapMessageFactory());
        service.setMarshaller(marshaller);
        service.setUnmarshaller(marshaller);
        service.setInterceptors(new ClientInterceptor[]{
                wss4jSecurityInterceptor(),
                new LogWebServiceMessageCallback()
        });*/
        return service;
    }

    private static class LogWebServiceMessageCallback implements ClientInterceptor {

        @Override
        public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {

            try {
                messageContext.getRequest().writeTo(System.out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
            return true;
        }

        @Override
        public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
            return true;
        }

        @Override
        public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {

        }
    }

}
