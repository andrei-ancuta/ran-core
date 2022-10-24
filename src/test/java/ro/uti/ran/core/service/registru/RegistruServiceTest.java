package ro.uti.ran.core.service.registru;

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
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.xml.model.RanDoc;

/**
 * Created by bogdan.ardeleanu on 10/19/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                RegistruServiceTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class RegistruServiceTest {
    @ContextConfiguration
    @ComponentScan({"ro.uti.ran", "ro.uti.ran.core.repository.portal", "ro.uti.ran.core.service.registru", "ro.uti.ran.core.repository.registru"})
    static class ImportConfiguration {

    }

    @Autowired
    private RegistruService registruService;

  
    @Test
    public void testGetRegistruService() {
        
    	RegistruSearchFilter registruSearchFilter = new RegistruSearchFilter();
    	registruSearchFilter.setIdUat(7L);
    	PagingInfo pagingInfo = new PagingInfo();
    	//SortInfo sortInfo = new SortInfo();
    //	System.out.println(registruService.getTransmission(pagingInfo, sortInfo).getItems());
    }

   
}
