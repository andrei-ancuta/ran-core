package ro.uti.ran.core.ws.internal.registru;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorServiceImpl;
import ro.uti.ran.core.service.index.IndexGeneratorUUIDImpl;
import ro.uti.ran.core.service.registru.FluxRegistruServiceImpl;
import ro.uti.ran.core.service.registru.RegistruServiceImpl;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.WsUtilsService;

/**
 * Created by miroslav.rusnac on 04/02/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.DEV)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {DevDataSourceConfig.class,
                TransmisiiTest.ImportConfiguration.class,
                RegistruServiceImpl.class,
                IndexGeneratorUUIDImpl.class,
                RegistruPersistenceLayerConfig.class,
                NomenclatorServiceImpl.class,
                FluxRegistruServiceImpl.class,
                PortalPersistenceLayerConfig.class
        }
)

@TransactionConfiguration(transactionManager = "registruTransactionManager", defaultRollback = true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransmisiiTest {

    @ContextConfiguration
    @ComponentScan({
            "ro.uti.ran.core.ws.internal.registru",
            "ro.uti.ran.core.ws.utils",


    })
    static class ImportConfiguration {

        @Bean
        public ITransmisiiService getTransmisieService() {
            return new TransmisiiServiceImpl();
            };

        @Bean
        public WsUtilsService getWsUtilsService() {
            return new WsUtilsService();
        }
   }

    @Autowired
    @Qualifier(value="transmisiiService")
    ITransmisiiService transmisiiService;

    @Test
    public void TestListaTransmisii() throws Exception{
        FiltruTransmisii filtru= new FiltruTransmisii();
        filtru.setIndexRegistru("d4402160-6b4f-4504-ae36-5eddd58c9ac1");
        PagingInfo pagingInfo = new PagingInfo(0, 1000);
        SortInfo sortInfo = new SortInfo();
        TransmisieList listaTransmisii=transmisiiService.getListaIncarcari(filtru,pagingInfo,sortInfo);
        System.out.println();
    }
}
