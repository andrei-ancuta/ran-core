package ro.uti.ran.core.service.exportNomenclatoare;

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
import ro.uti.ran.core.config.RootApplicationConfig;
import ro.uti.ran.core.model.registru.TipNomenclator;

import java.nio.charset.Charset;

/**
 * Created by Anastasia cea micuta on 11/29/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                RootApplicationConfig.class,
                NomenclatorExportServiceTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class NomenclatorExportServiceTest {
    @ContextConfiguration
    @ComponentScan({"ro.uti.ran"})
    static class ImportConfiguration {

    }

    @Autowired
    private NomenclatorExportService nomenclatorExportService;

    @Autowired
    private IExportNomenclators exportNomenclators;

    @Autowired
    private NomenclatorSchemaGen nomenclatorSchemaGen;

    @Test
    public void exportAllNomenclators() throws Exception {
        nomenclatorExportService.exportAllNomenclators();
    }

    @Test
    public void exporListaNomenclatoare() throws Exception {
        byte[] bytes = exportNomenclators.getNomenclatorsSummaryContent();
        System.out.println(new String(bytes, Charset.forName("UTF-8")));
    }

    @Test
    public void exporNomenclator() throws Exception {
        String codNomenclator = "NomCultura";

        String tipNomenclator = codNomenclator;
        int pos = codNomenclator.indexOf('-');
        if (pos > 0) {
            tipNomenclator = tipNomenclator.substring(0, pos);
        }

        byte[] bytes = exportNomenclators.exportNomenclatorContent(TipNomenclator.checkCodNomenclator(tipNomenclator), codNomenclator);
        System.out.println(new String(bytes, Charset.forName("UTF-8")));
    }

    @Test
    public void getXsdSchema() throws Exception {
        System.out.println("ListaNomenclatoareXsdSchema:\n" + nomenclatorSchemaGen.generateListaNomenclatoareXsdSchema());
        System.out.println("\n\nNomenclatorXsdSchema:\n" + nomenclatorSchemaGen.generateNomenclatorXsdSchema());

    }

}
