package ro.uti.ran.core.ws.internal.registru;

/**
 * Created by miroslav.rusnac on 04/02/2016.
 */

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
import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorServiceImpl;
import ro.uti.ran.core.service.index.IndexGeneratorUUIDImpl;
import ro.uti.ran.core.service.registru.FluxRegistruServiceImpl;
import ro.uti.ran.core.service.registru.RegistruSearchFilter;
import ro.uti.ran.core.service.registru.RegistruService;
import ro.uti.ran.core.service.registru.RegistruServiceImpl;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.WsUtilsService;
import ro.uti.ran.core.ws.internal.nomenclator.NomenclatorWS;
import ro.uti.ran.core.ws.internal.nomenclator.NomenclatorWSImpl;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.DEV)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {DevDataSourceConfig.class,
                RegistruDateTest.ImportConfiguration.class,
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
public class RegistruDateTest {
    @ContextConfiguration
    @ComponentScan({
            "ro.uti.ran.core.ws.internal.registru",
            "ro.uti.ran.core.ws.utils",
            "ro.uti.ran.core.service.registru"

    })
    static class ImportConfiguration {

        @Bean
        public RegistruDate getRegistruDateService() {
            return new RegistruDateImpl(){
            };
        }


        @Bean
        public WsUtilsService getWsUtilsService() {
            return new WsUtilsService();
        }



    }

    @Autowired
    @Qualifier(value = "registruDateService")
    RegistruDate registruDateService;


    @Test
    public void TestGetRegistruDate() throws Exception{
        PagingInfo pagingInfo = new PagingInfo(0, 1000);
        SortInfo sortInfo = new SortInfo();
        RegistruSearchFilter filter = new RegistruSearchFilter();
        RegistruListResult listaRegistru=registruDateService.getListaRegistru(filter, pagingInfo, sortInfo);
        System.out.println();

    }

}
