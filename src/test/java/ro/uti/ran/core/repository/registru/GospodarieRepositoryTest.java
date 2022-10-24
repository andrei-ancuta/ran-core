package ro.uti.ran.core.repository.registru;

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
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.model.registru.Gospodarie;

/**
 * Created by Anastasia cea micuta on 10/21/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                GospodarieRepositoryTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class GospodarieRepositoryTest {

    @ContextConfiguration
    @ComponentScan({"ro.uti.ran.core.repository"})
    static class ImportConfiguration {

    }

    @Autowired
    private GospodarieRepository gospodarieRepository;

    @Test
    public void testGetGospodarie() {
        Gospodarie gospodarie = gospodarieRepository.findOne(1599L);
        System.out.println(ReflectionToStringBuilder.toString(gospodarie, ToStringStyle.MULTI_LINE_STYLE));
    }
}
