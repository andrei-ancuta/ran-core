package ro.uti.ran.core.ws.sesiune;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.OpenAmConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.service.idm.*;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-08 16:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {AuthenticationServiceOpenAMWrapperImplTests.ImportContextConfiguration.class, OpenAmConfig.class})
@ActiveProfiles(Profiles.DEV)
public class AuthenticationServiceOpenAMWrapperImplTests {

    @Configuration
    @ComponentScan({"ro.uti.ran.core.service.idm"})
    static class ImportContextConfiguration {

//        @Bean
//        public IdmService idmService(){
//            return new IdmServiceOpenAMRestApiImpl();
//        }
    }


    @Autowired
    @Qualifier(value = "idmService")
    IdmService idmService;

    @Test
    public void testLoginDataStore() throws Exception {

        LoginResult loginResult = idmService.login("test", "12345678");

        System.out.println("loginResult = " + loginResult.isSuccess());
    }

    @Test
    public void testLoginDataStore1() throws Exception {

        LoginResult loginResult = idmService.login("demo-uat1@host.ro", "12345678");

        System.out.println("loginResult = " + loginResult.isSuccess());
    }

    @Test
    public void testLoginCertificateHeader() throws Exception {
    	  
    	String pemCertificate= "-----BEGIN CERTIFICATE-----\n" +
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
    	
        LoginResult loginResult = idmService.login(pemCertificate);

        System.out.println("loginResult = " + loginResult.isSuccess());
    }
  
    @Test
    public void testCreateIdentity() throws Exception{
        CreateIdentityResult result = idmService.createIdentity(new Identity(){
            {
                setUsername("vitalie");
                setUserpassword("12345678");
                setMail("vitalie@host.com");
            }
        });

        System.out.println("result = " + result);
    }

    @Test
    public void testChangePassword() throws Exception {
        ChangePasswordResult result = idmService.changeUserPassword("vitalie", "12345678");
        System.out.println("result = " + result);
    }

    @Test
    public void testGetIdentity() throws Exception {
        Identity result = idmService.getIdentity("vitalie");
        System.out.println("result = " + result);
    }

    @Test
    public void testGetIdentityNotExists() throws Exception {
        Identity result = idmService.getIdentity("not-exists");
        System.out.println("result = " + result);
    }

}
