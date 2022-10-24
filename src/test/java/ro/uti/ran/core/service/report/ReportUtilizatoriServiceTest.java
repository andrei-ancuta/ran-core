package ro.uti.ran.core.service.report;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.client.RestTemplate;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.model.portal.Utilizator;
import ro.uti.ran.core.service.idm.openam.IdmServiceOpenAM11RestApiImpl;
import ro.uti.ran.core.service.idm.openam.IdmServiceOpenAM12RestApiImpl;
import ro.uti.ran.core.service.report.ReportUtilizatoriService;
import ro.uti.ran.core.service.sistem.MailServiceImpl;
import ro.uti.ran.core.service.sistem.MessageBundleServiceImpl;
import ro.uti.ran.core.service.sistem.SistemServiceImpl;
import ro.uti.ran.core.service.sistem.TokenGenerator;
import ro.uti.ran.core.service.utilizator.UatConfigServiceImpl;
import ro.uti.ran.core.service.utilizator.UtilizatorService;
import ro.uti.ran.core.service.utilizator.UtilizatorServiceImpl;
import ro.uti.ran.core.service.utilizator.UtilizatoriSearchFilter;
import ro.uti.ran.core.ws.internal.sistem.InfoUtilizatorTest;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

/**
 * Created by adrian.boldisor on 3/11/2016.
 */

@ActiveProfiles(profiles = {Profiles.DEV})
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                UtilizatorServiceImpl.class,
                PortalPersistenceLayerConfig.class,
                IdmServiceOpenAM11RestApiImpl.class,
                RestTemplate.class,
                OpenAmConfig.class,
                SistemServiceImpl.class,
                TokenGenerator.class,
                StandardPasswordEncoder.class,
                ExceptionUtil.class,
                UatConfigServiceImpl.class,
                RegistruPersistenceLayerConfig.class,
                ImportConfiguration.class,
                MailServiceImpl.class,
                JavaMailSenderImpl.class,
                MessageBundleServiceImpl.class,
                ObjectMapper.class
        })
public class ReportUtilizatoriServiceTest {


    @ContextConfiguration
    @ComponentScan({"ro.uti.ran.core.ws.internal.utilizator",
            "ro.uti.ran.core.ws.utils",
            "ro.uti.ran.core.service.idm"})
    @PropertySource("classpath:application.properties")
    static class ImportConfiguration {


    }



    @Autowired
    UtilizatorService utilizatorService;


    @Test
    public void testGetUtilizatoriList() throws Exception {

        UtilizatoriSearchFilter searchFilter = new UtilizatoriSearchFilter();
        utilizatorService.getListaUtilizatoriExport(searchFilter, null,"xls");

    }



}
