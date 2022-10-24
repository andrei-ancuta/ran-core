package ro.uti.ran.core.ws.internal.sistem;

/**
 * Created by miroslav.rusnac on 29/01/2016.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.model.portal.ContextSistem;
import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.service.idm.openam.IdmServiceOpenAM12RestApiImpl;
import ro.uti.ran.core.service.sistem.SistemServiceImpl;
import ro.uti.ran.core.service.sistem.TokenGenerator;
import ro.uti.ran.core.service.utilizator.UatConfigServiceImpl;
import ro.uti.ran.core.service.utilizator.UtilizatorServiceImpl;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.WsUtilsService;
import ro.uti.ran.core.ws.internal.utilizator.*;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.TEST
)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {TestDatasourceConfig.class,
                PortalPersistenceLayerConfig.class,
                RegistruPersistenceLayerConfig.class,
                ExceptionUtil.class,
                OpenAmConfig.class,
                UatConfigServiceImpl.class,
                SistemServiceImpl.class,
                TokenGenerator.class,
                StandardPasswordEncoder.class,
                UtilizatorServiceImpl.class,
                IdmServiceOpenAM12RestApiImpl.class,
                SistemTest.ImportConfiguration.class
        }
)
@Transactional
//@TransactionConfiguration(transactionManager="portalTransactionManager",defaultRollback = true)
public class SistemTest {

    @ContextConfiguration
    @ComponentScan({
            "ro.uti.ran.core.ws.internal.utilizator",
            "ro.uti.ran.core.ws.utils"
    })
    static class ImportConfiguration {

        @Bean
        public UtilizatorSistemService getUtilizatorSistemService() {
            return new UtilizatorSistemServiceImpl();
        }

        @Bean
        public WsUtilsService getWsUtilsService() {
            return new WsUtilsService();
        }

    }


    @Autowired
    @Qualifier(value = "sistemService")
    UtilizatorSistemService utilizatorSistemService;




    @Test
    public void TestUtilizatoriSistem() throws Exception {
        PagingInfo pagingInfo = new PagingInfo(0, 10);
        UtilizatorSistemSearchFilter searchFilter = new UtilizatorSistemSearchFilter();
        SortInfo sortInfo = new SortInfo();
        UtilizatoriSistemListResult utilizatoriSitem = utilizatorSistemService.getUtilizatoriSistem(searchFilter, pagingInfo, sortInfo);
        System.out.println("Numar utilizatori sistem " + utilizatoriSitem.getTotalRecordCount());
        List<Sistem> utilizatori=utilizatoriSitem.getItems();
        System.out.println("====Utilizatori===");
        for(Sistem utilizator:utilizatori){
            System.out.println(infoUtilizator(utilizator));
        }
    }

    @Test
    public void TestGetUtilizatorById()throws Exception{
        Sistem rezult =utilizatorSistemService.getUtilizatorSistemById(1000L);
        System.out.println("Utilizator 1000 ");
        System.out.println(infoUtilizator(rezult));
    }

    @Test
    public void TestGetUtilizator() throws Exception{
        UtilizatorSistemSearchFilter searchFilter = new UtilizatorSistemSearchFilter();
        ContextSistem context= ContextSistem.INSTITUTIE;
        searchFilter.setIdEntity(3L);
        searchFilter.setContext(context);
        Sistem rezult = utilizatorSistemService.getUtilizatorSistem(searchFilter);
        System.out.println("Utilizator ");
        System.out.println(infoUtilizator(rezult));
    }

    @Test
    public void TestGenereazaToken() throws Exception {
        System.out.println("====Genereaza token=====");
        String token =utilizatorSistemService.genereazaToken();
        System.out.println(token);
    }

    @Test
    public void TestGetUatConfig() throws Exception{
        UatConfig rezult=utilizatorSistemService.getUatConfig(447L);
        System.out.println("UAT config:\n"+"Notificare raportare: "+rezult.isNotificareRaportare()+"\nTransmitere manuala: "+rezult.isTransmitereManuala());
    }

    @Test
    public void TestSetUatConfig() throws Exception{
        UatConfig newUatConfig= new UatConfig();
        newUatConfig.setNotificareRaportare(true);
        newUatConfig.setTransmitereManuala(true);
        utilizatorSistemService.saveUatConfig(447L,newUatConfig);
        UatConfig rezult=utilizatorSistemService.getUatConfig(447L);
        System.out.println("Modificare UAT config:\n"+"Notificare raportare: "+rezult.isNotificareRaportare()+"\nTransmitere manuala: "+rezult.isTransmitereManuala());
      }

    @Test
    public void TestSalveazaToken() throws Exception{
        String token =utilizatorSistemService.genereazaToken();
        UtilizatorSistemSearchFilter searchFilter = new UtilizatorSistemSearchFilter();
        ContextSistem context= ContextSistem.INSTITUTIE;
        searchFilter.setIdEntity(3L);
        System.out.println("Salveaza token: "+token);
        Sistem rezult = utilizatorSistemService.salveazaTokenUtilizatorSistem(3L,context,token);
       // System.out.println("Salveaza token: "+token);
        System.out.println(infoUtilizator(rezult));
    }

    private static String infoUtilizator(Sistem utilizator){
        String isActive = (utilizator.getActiv())?"da":"nu";
        String uat = (utilizator.getUat()!=null)?utilizator.getUat().getDenumire():"";
        String institutie = (utilizator.getInstitutie()!=null)?utilizator.getInstitutie().getDenumire():"";
        String judet =(utilizator.getJudet()!=null)?utilizator.getJudet().getDenumire():"";
        return "Cod: "+utilizator.getCod()+"\nDenumire: "+utilizator.getDenumire()+
                "\nLicenta: "+utilizator.getLicenta()+
                "\nData generare licenta: "+utilizator.getDataGenerareLicenta()+
                "\nJudet: "+judet+
                "\nUAT: "+uat+"\nInstitutie: "+institutie+
                "\nUtilizator activ: "+ isActive;

    }

    @Test
    public void Test1Scenariu() throws Exception{

    }
}
