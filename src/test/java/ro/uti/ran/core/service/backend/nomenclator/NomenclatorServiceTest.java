package ro.uti.ran.core.service.backend.nomenclator;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
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
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomUat;

/**
 * Created by Anastasia cea micuta on 10/20/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                NomenclatorServiceTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class NomenclatorServiceTest {
    @ContextConfiguration
    @ComponentScan({"ro.uti.ran.core.service.backend.nomenclator"})
    static class ImportConfiguration {

    }

    @Autowired
    private NomenclatorService nomenclatorService;

    @Test
    public void testFindByCode() {
        NomUat record = nomenclatorService.getNomenclatorForStringParam(NomUat, 48771,new DataRaportareValabilitate());
        System.out.println("NOM_UAT=" + ReflectionToStringBuilder.toString(record));
    }
}
