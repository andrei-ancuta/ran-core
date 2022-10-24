package ro.uti.ran.core.service.idm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.service.idm.openam.ServerInfo;
import ro.uti.ran.core.service.sistem.MailServiceImpl;
import ro.uti.ran.core.service.sistem.MessageBundleServiceImpl;

/**
 * Created by Anastasia cea micuta on 3/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                PortalPersistenceLayerConfig.class,
                OpenAmConfig.class,
                MailLayerConfig.class,
                ImportConfiguration.class,
                IdmServiceFactoryTest.ImportConfiguration.class,
                MailServiceImpl.class,
                MessageBundleServiceImpl.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class IdmServiceFactoryTest {
    @Autowired
    @Qualifier(value = "idmService")
    private IdmService idmService;

    @Test
    public void testIdmServiceFactory() throws Exception {
        System.out.println("idmServiceClass=" + idmService.getClass().getSimpleName());

        ObjectMapper om = new ObjectMapper();

        ServerInfo serverInfo = idmService.getServerInfo();
        System.out.println("serverInfo: "+ om.writeValueAsString(serverInfo));

        LoginResult loginResult = idmService.login("ran-admin@host.ro", "12345678");
        System.out.println("loginResult: "+ om.writeValueAsString(loginResult));


        String token = loginResult.getToken();
        SessionInfo sessionInfo = idmService.getSessionInfo(token);

        System.out.println("sessionInfo: "+ om.writeValueAsString(sessionInfo));

    }

    @ContextConfiguration
    @ComponentScan({"ro.uti.ran.core.service.idm"})
    @PropertySource("classpath:application.properties")
    static class ImportConfiguration {

    }
}
