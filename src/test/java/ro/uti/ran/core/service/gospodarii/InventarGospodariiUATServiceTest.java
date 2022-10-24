package ro.uti.ran.core.service.gospodarii;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.ws.internal.gospodarii.InfoInventarGospUat;

/**
 * Created by Anastasia cea micuta on 2/2/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                PortalPersistenceLayerConfig.class,
                ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
@PropertySource({"classpath:application.properties", "classpath:logback.properties"})
public class InventarGospodariiUATServiceTest {
    @Autowired
    InventarGospodariiUATService service;

    @Test
    public void testInventarGospodariiUAT() throws Exception {
        InfoInventarGospUat inventarGospUat = new InfoInventarGospUat();
        inventarGospUat.setAn(2105);
        inventarGospUat.setCodSiruta(12345);
        inventarGospUat.setValoare(333);
        service.updateOrCreate(inventarGospUat);
    }
}
