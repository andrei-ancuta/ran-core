package ro.uti.ran.core.ws.external.transmitere;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ro.ancpi.ran.core.ws.external.transmitere.TransmitereDateExtern;
import ro.ancpi.ran.core.ws.external.transmitere.TransmitereDateExternImpl;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.repository.registru.NomenclatorRepository;
import ro.uti.ran.core.repository.registru.NomenclatorRepositoryImpl;
import ro.uti.ran.core.service.exportNomenclatoare.impl.NomenclatorClassificationServiceImpl;
import ro.uti.ran.core.service.registru.RegistruServiceTestImpl;
import ro.uti.ran.core.service.security.SecurityWsService;
import ro.uti.ran.core.service.security.SecurityWsServiceTest;
import ro.uti.ran.core.utils.ZipUtil;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.model.transmitere.InformatiiTransmisie;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Anastasia cea micuta on 10/22/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                PortalPersistenceLayerConfig.class,
                RegistruPersistenceLayerConfig.class,
                SecurityConfig.class,
                TransmitereDateExternImpl.class,
                SecurityWsService.class,
                ImportConfiguration.class,
                RegistruServiceTestImpl.class,
                //NomenclatorClassificationServiceImpl.class,
                NomenclatorRepositoryImpl.class

                //TransmitereDateExternTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
@Transactional
@TransactionConfiguration(transactionManager="registruTransactionManager",defaultRollback = false)
public class TransmitereDateExternTest {

    @ContextConfiguration
    @ComponentScan({
            "ro.uti.ran.core.service.backend",
            "ro.uti.ran.core.service.index",
            "ro.uti.ran.core.service.parametru",
            "ro.uti.ran.core.ws.external.transmitere",
            "ro.uti.ran.core.ws.internal.transmitere",
            "ro.uti.ran.core.ws.utils",
            "ro.uti.ran.core.security",
            "ro.uti.ran",
            "ro.uti.ran.core.service.registru",
            "ro.uti.ran.core.repository.registru"
    })

    static class ImportConfiguration {


    }

    @Autowired
    @Qualifier(value = "transmitereDateExternService")
    private TransmitereDateExtern transmitereDateExtern;


    @Test
    public void testTransmitere_Capitol_0_12()  {



        RanAuthentication ranAuthentication = new RanAuthentication();
        ranAuthentication.setUsername("87665"); //codSiruta pt. Neamt
        ranAuthentication.setPassword("12345678");
        //ranAuthentication.setUsername("UAT");
        //ranAuthentication.setPassword("12345678");
        for(String capitol:files) {
            String urlCap="testGeo/"+capitol;
            URL resourceURL = this.getClass().getClassLoader().getResource(urlCap);
            System.out.println("==");
            System.out.println(resourceURL);
            System.out.println("==");
            //System.out.println(xmlCapitol_0_12);
            String xmlCapitol = null;
            try {
                xmlCapitol = FileUtils.readFileToString(new File(resourceURL.toURI()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            byte[] xmlCompresat = ZipUtil.compress(xmlCapitol.getBytes(Charset.forName("UTF-8")));
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+capitol+" >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            try {
                transmitereDateExtern.transmitere(xmlCompresat, ranAuthentication);
            } catch (ro.ancpi.ran.core.ws.fault.RanException e) {
                System.out.println(e.getFaultInfo().getMessage());
            } catch (ro.ancpi.ran.core.ws.fault.RanRuntimeException e) {
                System.out.println(e.getFaultInfo().getMessage());
            }

        }
//        InformatiiTransmisie status=transmitereDateExtern.getStatusTransmisie("29f5a22b-6fc0-4349-8457-84dba3f077d4", ranAuthentication);
//        System.out.println(status);
    }

    private final String[] files={
           // "CAP4a.xml",
            "CAP4a1.xml",
            "CAP4b1.xml",
            "CAP4b2.xml",
            "CAP4c.xml",
            "CAP5b.xml",
            "CAP5c.xml",
            "CAP5d.xml",
            "CAP6.xml",
            "CAP10a.xml"
    };


}
