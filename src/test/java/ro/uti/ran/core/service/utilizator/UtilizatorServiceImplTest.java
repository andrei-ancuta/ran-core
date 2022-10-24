package ro.uti.ran.core.service.utilizator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.model.portal.Utilizator;
import ro.uti.ran.core.service.report.ReportUtilizatoriService;
import ro.uti.ran.core.utils.GenericListResult;

import java.util.Arrays;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-12-09 17:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                PortalPersistenceLayerConfig.class,
                UtilizatorServiceImplTest.ImportConfiguration.class,
                ImportConfiguration.class,
                UtilizatorServiceImpl.class
        })

@ActiveProfiles(profiles = {Profiles.DEV})
public class UtilizatorServiceImplTest {

    @ContextConfiguration
    @ComponentScan({"ro.uti.ran", "ro.uti.ran.core.repository.portal", "ro.uti.ran.core.service.utilizator"})
    static class ImportConfiguration {


    }

    @Autowired
    UtilizatorService utilizatorService;

    @Test
    public void testGetUtilizatorByContext() {

        UtilizatoriSearchFilter searchFilter = new UtilizatoriSearchFilter();
        searchFilter.setIdContext(Arrays.asList(1L, 2L));

        GenericListResult<Utilizator> result = utilizatorService.getListaUtilizatori(searchFilter, null, null);

        System.out.println("result = " + result);
    }

    @Test
    public void testGetUtilizatorByRol() {

        UtilizatoriSearchFilter searchFilter = new UtilizatoriSearchFilter();
        searchFilter.setIdRol(Arrays.asList(1L, 2L));

        GenericListResult<Utilizator> result = utilizatorService.getListaUtilizatori(searchFilter, null, null);

        System.out.println("result = " + result);
    }



}
