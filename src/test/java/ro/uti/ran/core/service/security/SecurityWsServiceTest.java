package ro.uti.ran.core.service.security;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.uti.ran.core.ws.internal.RanAuthorization;

/**
 * Created by Anastasia cea micuta on 11/29/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.DEV)
@ContextConfiguration(
        loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                PortalPersistenceLayerConfig.class,
                RegistruPersistenceLayerConfig.class,
                SecurityConfig.class,
                SecurityWsServiceTest.ImportConfiguration.class
        }
)
public class SecurityWsServiceTest {
    @ContextConfiguration
    @ComponentScan({"ro.uti.ran", "ro.uti.ran.core.security", "ro.uti.ran.core.service.registru", "ro.uti.ran.core.repository.registru"})
    static class ImportConfiguration {

    }
    @Autowired
    private SecurityWsService securityWsService;

    @Test
    public void testUAT() throws Exception {
        RanAuthentication ranAuthentication = new RanAuthentication();

        // UAT
        ranAuthentication.setUsername("12345"); //codSiruta pt. Neamt
        ranAuthentication.setPassword("11111111111111111111"); // SHA1 pt. 11111111111111111111 (1x20)
        RanAuthorization ranAuthorization = securityWsService.authenticate(ranAuthentication);
        System.out.println("RanAuthorization=" + ReflectionToStringBuilder.toString(ranAuthorization));

    }

    @Test
    public void testJudet() throws Exception {
        RanAuthentication ranAuthentication = new RanAuthentication();

        // UAT
        ranAuthentication.setUsername("12345"); //codSiruta pt. Neamt
        ranAuthentication.setPassword("11111111111111111111"); // SHA1 pt. 11111111111111111111 (1x20)
        RanAuthorization ranAuthorization = securityWsService.authenticate(ranAuthentication);
        System.out.println("RanAuthorization=" + ReflectionToStringBuilder.toString(ranAuthorization));

    }

    @Test
    public void testInstitutie() throws Exception {
        RanAuthentication ranAuthentication = new RanAuthentication();

        // UAT
        ranAuthentication.setUsername("ANCPI"); //codSiruta pt. Neamt
        ranAuthentication.setPassword("11111111111111111111"); // SHA1 pt. 11111111111111111111 (1x20)
        RanAuthorization ranAuthorization = securityWsService.authenticate(ranAuthentication);
        System.out.println("RanAuthorization=" + ReflectionToStringBuilder.toString(ranAuthorization));

    }
}
