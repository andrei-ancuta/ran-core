package ro.uti.ran.core.service.incarcare;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.model.portal.RegistruPortalDetails;
import ro.uti.ran.core.service.registru.IncarcareService;
import ro.uti.ran.core.utils.GenericListResult;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-12-09 17:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                IncarcareServiceImplTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class IncarcareServiceImplTest {

    @ContextConfiguration
    @ComponentScan({"ro.uti.ran", "ro.uti.ran.core.repository.portal", "ro.uti.ran.core.service.registru", "ro.uti.ran.core.repository.portal"})
    static class ImportConfiguration {

    }

    @Autowired
    IncarcareService incarcareService;

    @Test
    public void testGetDetaliiIncarcare() {

        GenericListResult<RegistruPortalDetails> result = incarcareService.getDetaliiRegistruPortal(11L, null, null);

        System.out.println("result = " + result);
    }
}
