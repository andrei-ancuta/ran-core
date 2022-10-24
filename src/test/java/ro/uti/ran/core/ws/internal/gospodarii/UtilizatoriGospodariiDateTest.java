package ro.uti.ran.core.ws.internal.gospodarii;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;

/**
 * Created by adrian.boldisor on 2/9/2016.
 */



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                PortalPersistenceLayerConfig.class,
                ImportConfiguration.class,
                UtilizatorGospServiceImpl.class


        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class UtilizatoriGospodariiDateTest {

    @Autowired
    UtilizatorGospService utilizatorGospService;


    @Test
    public void testGetSomeData() throws Exception {
        SortInfo sortInfo = new SortInfo();
        utilizatorGospService.getUtilizatorGospList(sortInfo,new PagingInfo(),1000L,10L);

    }



}
