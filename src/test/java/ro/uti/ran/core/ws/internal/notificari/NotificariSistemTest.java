package ro.uti.ran.core.ws.internal.notificari;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import ro.uti.ran.core.ImportConfiguration;


import ro.uti.ran.core.config.*;
import ro.uti.ran.core.service.utilizator.UtilizatorService;
import ro.uti.ran.core.service.utilizator.UtilizatorServiceImpl;
import ro.uti.ran.core.service.utilizator.UtilizatoriSearchFilter;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;

import java.nio.charset.StandardCharsets;


/**
 * Created by adrian.boldisor on 3/23/2016.
 */



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                 NotificariSistemTest.ImportConfiguration.class,
                 DevDataSourceConfig.class,
                 MailLayerConfig.class,
                 UtilizatorServiceImpl.class,
                 PortalPersistenceLayerConfig.class,
                 OpenAmConfig.class,
                 StandardPasswordEncoder.class,
                 RegistruPersistenceLayerConfig.class,
                 ImportConfiguration.class,
                 JavaMailSenderImpl.class,







        })
@ActiveProfiles(profiles = {Profiles.DEV})
@PropertySource({"classpath:application.properties", "classpath:logback.properties","classpath:devMail.properties"})
public class NotificariSistemTest {


    @ContextConfiguration
    @ComponentScan({
            "ro.uti.ran.core.ws.internal.notificari",
            "ro.uti.ran.core.ws.internal.parametru",
            "ro.uti.ran.core.service.idm",
            "ro.uti.ran.core.service.sistem",
            "ro.uti.ran.core.service.utilizator"
    })
    @PropertySource("classpath:application.properties")
    static class ImportConfiguration {

        }

    @Autowired
    NotificariSistem notificareRaportAdHoc;

    @Autowired
    UtilizatorService utilizatorService;

//    @Test
//    public void notificareRaportAdHocTest(){
//
//        UtilizatoriSearchFilter utilizatoriSearchFilter = new UtilizatoriSearchFilter();
//        SortInfo sortInfo = new SortInfo();
//
//        byte[] report = utilizatorService.getListaUtilizatoriExport(utilizatoriSearchFilter,sortInfo, "html");
//
//
//        String str = new String(report, StandardCharsets.UTF_8);
//
//      //  notificareRaportAdHoc.notificareSimplaRaportAdHoc("adrian.boldisor@uti.eu.com","Utilizatori Raport",str);
//    }

    @Test
    public void notificareRaportAdHocTestCuAtasament() throws RanException, RanRuntimeException{

        UtilizatoriSearchFilter utilizatoriSearchFilter = new UtilizatoriSearchFilter();
        SortInfo sortInfo = new SortInfo();

        byte[] report = utilizatorService.getListaUtilizatoriExport(utilizatoriSearchFilter,sortInfo, "html");
        byte[] attachment = utilizatorService.getListaUtilizatoriExport(utilizatoriSearchFilter,sortInfo, "JPEG");

        String str = new String(report, StandardCharsets.UTF_8);

       notificareRaportAdHoc.notificareRaportAdHoc("ran-admin@host.ro","Utilizatori Raport",str, attachment);
   }


}
