package ro.uti.ran.core.ws.internal.sistem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
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
import ro.uti.ran.core.model.portal.Context;
import ro.uti.ran.core.service.idm.openam.IdmServiceOpenAM12RestApiImpl;
import ro.uti.ran.core.service.sistem.*;
import ro.uti.ran.core.service.utilizator.UatConfigServiceImpl;
import ro.uti.ran.core.service.utilizator.UtilizatorServiceImpl;
import ro.uti.ran.core.ws.WsUtilsService;
import ro.uti.ran.core.ws.internal.utilizator.*;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import java.util.List;

/**
 * Created by miroslav.rusnac on 02/02/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.DEV)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {DevDataSourceConfig.class,
                InfoUtilizatorTest.ImportConfiguration.class,
                UtilizatorServiceImpl.class,
                PortalPersistenceLayerConfig.class,
                IdmServiceOpenAM12RestApiImpl.class,
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
                  }
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InfoUtilizatorTest {

    @ContextConfiguration
    @ComponentScan({
            "ro.uti.ran.core.ws.internal.utilizator",
            "ro.uti.ran.core.ws.utils",
            "ro.uti.ran.core.service.idm"
    })

    @PropertySource("classpath:application.properties")

    static class ImportConfiguration {

        @Bean
        public InfoUtilizator getInfoUtilizatorService() {
            return new InfoUtilizatorImpl();
        }

        @Bean
        public WsUtilsService getWsUtilsService() {
            return new WsUtilsService();
        }

    }

    @Autowired
    @Qualifier(value = "infoUtilizatorService")
    InfoUtilizator utilizatorService;

    @Test
    public void Test2GetContectUtilizator() throws  Exception{
        List<Context> contexts=utilizatorService.getContexteUtilizator("demo-ancpi@host.ro");
        System.out.println("== Contexte utilizator <demo-ancpi@host.ro> ==");
        for(Context context:contexts){
            System.out.println(context.getCod());
            System.out.println(context.getId());
        }
    }

    @Test
    public void Test3GetRoluriUtilizator() throws Exception{
       List<Rol> roles =  utilizatorService.getRoluriUtilizator("demo-ancpi@host.ro", "UAT");
        System.out.println("== Roluri utilizator <demo-ancpi@host.ro> ==");
        for(Rol rol:roles){
            System.out.println("Cod rol:"+rol.getCod()+" Denumire rol:"+rol.getDenumire());
        }
    }

    @Test
    public void Test1GetDetaliiUtilizator() throws Exception{
        DetaliiUtilizator detaliiUtilizator=utilizatorService.getDetaliiUtilizator("demo-ancpi@host.ro");
        System.out.println("== Detalii utilizator <demo-ancpi@host.ro> ==");
        System.out.println("Nume:"+detaliiUtilizator.getNume());
        System.out.println("Prenume:"+detaliiUtilizator.getPrenume());
        System.out.println("CNP:"+detaliiUtilizator.getCnp());
        System.out.println("Email:"+detaliiUtilizator.getEmail());
        String isActiv=detaliiUtilizator.getActiv()?"da":"nu";
        System.out.println("Este activ:"+isActiv);

    }
}
