package ro.uti.ran.core.ws.internal.nomenclatoare;

/**
 * Created by miroslav.rusnac on 03/02/2016.
 */

import org.apache.commons.lang.SerializationUtils;
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
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.model.registru.CapPlantatie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.repository.registru.NomenclatorRepository;
import ro.uti.ran.core.repository.registru.NomenclatorRepositoryImpl;
import ro.uti.ran.core.repository.registru.RegistruRepository;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorServiceImpl;
import ro.uti.ran.core.service.exportNomenclatoare.IExportNomenclators;
import ro.uti.ran.core.service.exportNomenclatoare.data.summary.NomenclatorsSummary;
import ro.uti.ran.core.service.exportNomenclatoare.impl.ComplexExportableNomServicesFactoryImpl;
import ro.uti.ran.core.service.exportNomenclatoare.impl.NomenclatorExportRepository;
import ro.uti.ran.core.service.exportNomenclatoare.impl.NomenclatorExportServiceImpl;
import ro.uti.ran.core.service.nomenclator.NomenclatorSearchFilter;
import ro.uti.ran.core.service.nomenclator.NomenclatorUtilityImpl;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.WsUtilsService;
import ro.uti.ran.core.ws.internal.nomenclator.NomenclatorListResult;
import ro.uti.ran.core.ws.internal.nomenclator.NomenclatorWS;
import ro.uti.ran.core.ws.internal.nomenclator.NomenclatorWSImpl;
import ro.uti.ran.core.ws.internal.utilizator.AdminUtilizator;
import ro.uti.ran.core.ws.internal.utilizator.AdminUtilizatorImpl;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.DEV)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {DevDataSourceConfig.class,
                NomenclatoareTest.ImportConfiguration.class,
                NomenclatorUtilityImpl.class,
                RegistruPersistenceLayerConfig.class,
                PortalPersistenceLayerConfig.class,
                NomenclatorRepositoryImpl.class
                //NomenclatorExportServiceImpl.class,
                //ComplexExportableNomServicesFactoryImpl.class

        }
)

@TransactionConfiguration(transactionManager = "registruTransactionManager", defaultRollback = true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NomenclatoareTest {
    @ContextConfiguration
    @ComponentScan({
            "ro.uti.ran.core.ws.internal.nomenclator",
            "ro.uti.ran.core.ws.utils"
    })
    static class ImportConfiguration {

        @Bean
        public NomenclatorWS getInfoUtilizatorService() {
            return new NomenclatorWSImpl() {
            };
        }

        @Bean
        public WsUtilsService getWsUtilsService() {
            return new WsUtilsService();
        }

    }

    @Autowired
    @Qualifier(value = "nomenclatorWS")
    NomenclatorWS nomenclatoreWService;


    @Test
    @Transactional
    public void TestGetListaNomenclatoare() throws Exception {
        PagingInfo pagingInfo = new PagingInfo(0, 1000);
        SortInfo sortInfo = new SortInfo();
        NomenclatorSearchFilter searchfilter = new NomenclatorSearchFilter();
        searchfilter.setType(TipNomenclator.CapPlantatie);
        NomenclatorListResult sumar = nomenclatoreWService.getListaNomenclator(searchfilter, pagingInfo, sortInfo);
        ///prima intrate
        CapPlantatie primaIntrare = (CapPlantatie) sumar.getItems().get(1);
        String codTest = primaIntrare.getCod();

        CapPlantatie newNomenclator = (CapPlantatie) SerializationUtils.clone(nomenclatoreWService.getIntrareNomenclatorByCodeOrNull(TipNomenclator.CapPlantatie, codTest));
        //  CapPlantatie intrareNomenclator = new CapPlantatie();
        newNomenclator.setCod("100");
        newNomenclator.setBaseId(100L);
        newNomenclator.setId(null);
        newNomenclator.setDescriere("Test intrare noua");
        Nomenclator result = nomenclatoreWService.salveazaIntrareNomenclator(newNomenclator);

        //verificare rezultat
        assertEquals("Verifiace nomenclator introdus", result, nomenclatoreWService.getIntrareNomenclatorByCodeOrNull(TipNomenclator.CapPlantatie, "100"));

        nomenclatoreWService.stergeIntrareNomenclator(TipNomenclator.CapPlantatie, newNomenclator.getId());
        newNomenclator = (CapPlantatie) SerializationUtils.clone(nomenclatoreWService.getIntrareNomenclatorByCodeOrNull(TipNomenclator.CapPlantatie, "100"));
        //verificare
        assertNull("Nu a fost sters nomenclatorul", newNomenclator);


        //modificare intrare existenta  Eroare serviciul nomenclatorWS metoda salveazaIntrareNomenclator
        newNomenclator = (CapPlantatie) SerializationUtils.clone(nomenclatoreWService.getIntrareNomenclatorByCodeOrNull(TipNomenclator.CapPlantatie, codTest));
        newNomenclator.setDescriere("Modificare descriere");
        result = nomenclatoreWService.salveazaIntrareNomenclator(newNomenclator);
        //verificare rezultat
        assertEquals("Verifiace nomenclator modificat", result, nomenclatoreWService.getIntrareNomenclatorByCodeOrNull(TipNomenclator.CapPlantatie, codTest));




        //modificare cod intrare existenta
        newNomenclator.setCod("100");
        try {
            nomenclatoreWService.salveazaIntrareNomenclator(newNomenclator);
            newNomenclator = (CapPlantatie) nomenclatoreWService.getIntrareNomenclatorByCodeOrNull(TipNomenclator.CapPlantatie, codTest);
            assertNotNull("Nu mai gasesc intrarea cod=" + codTest, newNomenclator);
        } catch (Exception ex) {
            newNomenclator = (CapPlantatie) nomenclatoreWService.getIntrareNomenclatorByCodeOrNull(TipNomenclator.CapPlantatie, codTest);
            assertNotNull("Nu mai gasesc intrarea cod=" + codTest, newNomenclator);

        }


        //dublu cod nomenclator
        newNomenclator = (CapPlantatie) SerializationUtils.clone(nomenclatoreWService.getIntrareNomenclatorByCodeOrNull(TipNomenclator.CapPlantatie, codTest));
        newNomenclator.setId(null);
        newNomenclator.setBaseId(null);

        try {
            nomenclatoreWService.salveazaIntrareNomenclator(newNomenclator);
            nomenclatoreWService.getIntrareNomenclatorByCodeOrNull(TipNomenclator.CapPlantatie, codTest);
        } catch (Exception ex) {
            // e ok
            assertNull("S-a dublat intratrea cod=" + codTest, newNomenclator.getId());
        }


        System.out.println();
    }

}
