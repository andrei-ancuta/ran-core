package ro.uti.ran.core.ws.parametru;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.service.parametru.ParametruServiceDevImpl;
import ro.uti.ran.core.ws.WsUtilsService;
import ro.uti.ran.core.ws.internal.parametru.ParametruService;
import ro.uti.ran.core.ws.internal.parametru.ParametruServiceImpl;

import java.util.List;

/**
 * Created by miroslav.rusnac on 29/01/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.DEV)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {DevDataSourceConfig.class,
                ParametruServiceDevImpl.class,
                RegistruPersistenceLayerConfig.class,
                PortalPersistenceLayerConfig.class,
                ParametruTest.ImportConfiguration.class}
)

public class ParametruTest {

    @ContextConfiguration
    @ComponentScan({
            "ro.uti.ran.core.ws.internal.parametru",
            "ro.uti.ran.core.ws.utils"
    })
    static class ImportConfiguration {

        @Bean
        public ParametruService getParametruServiceInt() {
            return new ParametruServiceImpl();
        }

        @Bean
        public WsUtilsService getWsUtilsService() {
            return new WsUtilsService();
        }

    }

    @Autowired
    //@Qualifier(value = "parametruService")
    ParametruService parametruService;

    @Test
    public void TestListaParametri() throws Exception {
        System.out.println("Test lista parametri");
        List<Parametru> listaParanetri = parametruService.getListaParametri();
        for (Parametru parametru : listaParanetri) {
            System.out.println(parametru.getCod());
            System.out.println(parametru.getDenumire());
            System.out.println(parametru.getDescriere());
            System.out.println(parametru.getId());
            System.out.println(parametru.getValoare());
            System.out.println(parametru.getValoareImplicita());
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
    }

    @Test
    public void TestParametru() throws Exception{
        Parametru parametru =parametruService.getParametru("transmisii.cautare.nrzile.dela");
        System.out.println("======== Cod parametru 12 =================================");
        System.out.println(parametru.getCod());
        System.out.println(parametru.getDenumire());
        System.out.println(parametru.getDescriere());
        System.out.println(parametru.getId());
        System.out.println(parametru.getValoare());
        System.out.println(parametru.getValoareImplicita());
        System.out.println("=============================================================");

    }

    @Test
    public void TestSalveazaParametru() throws Exception{
        Parametru parametruIntrare = new Parametru();
        parametruIntrare.setId(100L);
        parametruIntrare.setCod("100");
        parametruIntrare.setDenumire("Test");
        parametruIntrare.setDescriere("Testare ws salveaza parametru");
        parametruIntrare.setValoare("___value___");


        Parametru parametru = parametruService.salveazaParametru(parametruIntrare,null);

        System.out.println("======== Salvare parametru =================================");
        System.out.println(parametru.getCod());
        System.out.println(parametru.getDenumire());
        System.out.println(parametru.getDescriere());
        System.out.println(parametru.getId());
        System.out.println(parametru.getValoare());
        System.out.println(parametru.getValoareImplicita());
        System.out.println("=============================================================");

    }

}
