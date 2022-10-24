package ro.uti.ran.core.service.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.config.RootApplicationConfig;

/**
 * Created by gheorghe.nastasache on 10/19/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                RootApplicationConfig.class,
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                SemnareRecipisaRegistruTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
@PropertySource("classpath:application.properties")
public class SemnareRecipisaRegistruTest {
    @ContextConfiguration
    @ComponentScan({"ro.uti.ran.core.repository.registru.RegistruRepository"})
    static class ImportConfiguration {
    }

    @Autowired
    private SemnareRecipisaService semnareRecipisaService;

    @Test
    public void testProcesareDateRegistruService() {
        semnareRecipisaService.copiazaRecipiseIN();
    }



}
