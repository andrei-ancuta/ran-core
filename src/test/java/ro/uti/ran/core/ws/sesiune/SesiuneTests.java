package ro.uti.ran.core.ws.sesiune;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.client.RestTemplate;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.service.idm.*;
import ro.uti.ran.core.service.idm.openam.IdmServiceOpenAM12RestApiImpl;
import ro.uti.ran.core.ws.WsUtilsService;
import ro.uti.ran.core.ws.internal.sesiune.InfoSesiune;
import ro.uti.ran.core.ws.internal.sesiune.Sesiune;
import ro.uti.ran.core.ws.internal.sesiune.SesiuneImpl;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import java.util.List;


/**
 * Author: miroslav.rusnac@uti.ro
 * Date: 2015-10-15 16:47
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.DEV)
@ContextConfiguration(
        loader = AnnotationConfigContextLoader.class,
        classes = {DevDataSourceConfig.class,
                PortalPersistenceLayerConfig.class,
                ExceptionUtil.class,
                OpenAmConfig.class,
                SesiuneTests.ImportConfiguration.class}
)

@SuppressWarnings("all")
public class SesiuneTests {

    @ContextConfiguration
    @ComponentScan({
            "ro.uti.ran.core.ws.internal.sesiune",
            "ro.uti.ran.core.ws.utils"
    })
    static class ImportConfiguration {
        @Bean
        public Sesiune getRegistruService() {
            return new SesiuneImpl();
        }

        @Bean
        public WsUtilsService getWsUtilsService() {
            return new WsUtilsService();
        }

        @Bean
        public IdmService idmService() {
            return new IdmServiceOpenAM12RestApiImpl();
        }

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

    }

    static String token;

    @Autowired
    @Qualifier(value = "sesiuneService")
    Sesiune sesiuneService;


    @Test
    public void TestLogin() throws Exception {
        LoginResult loginResult = sesiuneService.login("uat-geoagiu@host.ro", "12345678");
        System.out.println("loginResult(user/password) = " + loginResult.isSuccess());
    }



    @Test
    public void testLoginDataStore() throws Exception {

        LoginResult loginResult = sesiuneService.loginWithCertificate(pemCertificate);
        if (loginResult.isSuccess()) {
            token = loginResult.getToken();

        }
        InfoSesiune attr = sesiuneService.getSessionInfo(token);

        List<CookieInfo> cookieInfos = sesiuneService.buildSessionCookies(loginResult);
        for (CookieInfo cookie : cookieInfos) {
            System.out.println(cookie.getName());
            System.out.println(cookie.getDomain());
            System.out.println(cookie.getMaxAge());
            System.out.println(cookie.getPath());
            System.out.println(cookie.getValue());


        }
        System.out.println("loginResult = " + loginResult.isSuccess());


    }

    @Test
    public void testLogout() throws Exception {

        LogoutResult loginResult = sesiuneService.logout(token);
        System.out.println("logoutResult = " + loginResult.isSuccess());

    }


    private final String pemCertificate = "-----BEGIN CERTIFICATE-----\n" +
            "MIIC3jCCAkegAwIBAgIJAPjvtkovi5mhMA0GCSqGSIb3DQEBCwUAMIGHMQswCQYD\n" +
            "VQQGEwJSTzEKMAgGA1UECAwBQjESMBAGA1UEBwwJQnVjaGFyZXN0MQwwCgYDVQQK\n" +
            "DANVVEkxDDAKBgNVBAsMA1VUSTEaMBgGA1UEAwwRcmFuLWFkbWluQGhvc3Qucm8x\n" +
            "IDAeBgkqhkiG9w0BCQEWEXJhbi1hZG1pbkBob3N0LnJvMB4XDTE2MDEwNzE0NDY0\n" +
            "MVoXDTE3MDEwNjE0NDY0MVowgYcxCzAJBgNVBAYTAlJPMQowCAYDVQQIDAFCMRIw\n" +
            "EAYDVQQHDAlCdWNoYXJlc3QxDDAKBgNVBAoMA1VUSTEMMAoGA1UECwwDVVRJMRow\n" +
            "GAYDVQQDDBFyYW4tYWRtaW5AaG9zdC5ybzEgMB4GCSqGSIb3DQEJARYRcmFuLWFk\n" +
            "bWluQGhvc3Qucm8wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBANILe7YCskD5\n" +
            "JWsFTHRNKT6KLFK7l0q1RzQdLEkCO7tE4a5tXlEFGmw+nf2XaygMJ1rAnGMdDleM\n" +
            "i4tzxWT+4H7TJCHSIpazI1fWurIbpVPpHsAcEUUvcMLFa2iqssPWW5588SJ/BNff\n" +
            "VH3D9l7SwEdGA1/Sz8eaaqyoZ8bzCxFpAgMBAAGjUDBOMB0GA1UdDgQWBBRdY8AK\n" +
            "/BxEiEr0DBaUUnWo87P7qDAfBgNVHSMEGDAWgBRdY8AK/BxEiEr0DBaUUnWo87P7\n" +
            "qDAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUAA4GBAGq8HSIg84j5nmRUIJde\n" +
            "UArgYcBExgA6Kp7pHISOziJvgr5G5qBFsCRTYcVDZklsJtonlnpGQDfOWBabbhrc\n" +
            "H6r2dsrJXDV1b2eCY8dr0MDaWjzBGcJByq//b4ExL97gcWpYF+s1vJ24q5oCJs40\n" +
            "sk9eeUbXLryQuLMSpZO+EyFQ\n" +
            "-----END CERTIFICATE-----";

}
