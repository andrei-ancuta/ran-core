package ro.uti.ran.core.ws.external.interogare;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.ancpi.ran.core.ws.external.interogare.InterogareDateExtern;
import ro.ancpi.ran.core.ws.external.interogare.InterogareDateExternImpl;
import ro.ancpi.ran.core.ws.external.transmitere.TransmitereDateExternImpl;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.service.exportNomenclatoare.impl.NomenclatorClassificationServiceImpl;
import ro.uti.ran.core.service.security.SecurityWsService;
import ro.uti.ran.core.utils.ZipUtil;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.uti.ran.core.xml.model.capitol.Capitol;

import java.nio.charset.StandardCharsets;

/**
 * Created by miroslav.rusnac on 16/02/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                PortalPersistenceLayerConfig.class,
                RegistruPersistenceLayerConfig.class,
                SecurityConfig.class,
                TransmitereDateExternImpl.class,
                InterogareDateExternImpl.class,
                SecurityWsService.class,
                ImportConfiguration.class,
                NomenclatorClassificationServiceImpl.class
                //TransmitereDateExternTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class InterogareDateExternTest {
    @Autowired
    @Qualifier(value = "interogareDateExternService")
    private InterogareDateExtern interogareService;

    @Test
    public void TestInterogare() throws Exception{
        RanAuthentication ranAuthentication = new RanAuthentication();
        ranAuthentication.setUsername("86810"); //codSiruta pt. Neamt
        ranAuthentication.setPassword("12345678");
        System.out.println(interogareService.getInterogareXsdSchema(ranAuthentication));
        byte[] data= interogareService.getDateCapitol("66789", 86810, TipCapitol.CAP12a.toString(), 2016, null, ranAuthentication);

        byte[] report = ZipUtil.decompress(data);
        String str = new String(report, StandardCharsets.UTF_8);

        System.out.println(str);


    }
}
