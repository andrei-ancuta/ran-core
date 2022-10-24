package ro.uti.ran.core.repository.incarcari;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.repository.criteria.AndRepositoryCriteria;
import ro.uti.ran.core.repository.criteria.Operation;
import ro.uti.ran.core.repository.criteria.RepositoryCriteria;
import ro.uti.ran.core.repository.portal.IncarcareRepository;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.Order;
import ro.uti.ran.core.utils.SortCriteria;
import ro.uti.ran.core.utils.SortInfo;

/**
 * Created by Anastasia cea micuta on 10/21/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                PortalPersistenceLayerConfig.class,
                IncarcareRepositoryTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class IncarcareRepositoryTest {

    @ContextConfiguration
    @ComponentScan({"ro.uti.ran.core.repository.portal"})
    static class ImportConfiguration {

    }

    @Autowired
    private IncarcareRepository incarcareRepository;

    @Test
    public void getGetList() {

        SortInfo sortInfo = new SortInfo();
        sortInfo.getCriterias().add(new SortCriteria("uat.denumire", Order.asc));

        GenericListResult list = incarcareRepository.getListResult(new AndRepositoryCriteria(){
            {
//                add(new RepositoryCriteria<Long>("id", Operation.LT, 10L));
                add(new RepositoryCriteria("uat.id", Operation.IN, new Long[]{1L}));
            }
        }, null, sortInfo);

        System.out.println(ReflectionToStringBuilder.toString(list, ToStringStyle.MULTI_LINE_STYLE));
    }
}
