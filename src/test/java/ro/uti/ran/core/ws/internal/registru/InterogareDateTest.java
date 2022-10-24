package ro.uti.ran.core.ws.internal.registru;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.internal.interogare.InterogareDate;
import ro.uti.ran.core.ws.model.interogare.IdentificatorGospodarie;
import ro.uti.ran.core.ws.model.interogare.IdentificatorPF;
import ro.uti.ran.core.xml.model.types.CNP;

import java.util.List;

/**
 * Created by miroslav.rusnac on 05/02/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.DEV)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                PortalPersistenceLayerConfig.class,
                ImportConfiguration.class,
                TransmisiiServiceImpl.class
        }
)
@TransactionConfiguration(transactionManager = "registruTransactionManager", defaultRollback = true)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InterogareDateTest {

    @Autowired
    @Qualifier(value = "interogareDateService")
    InterogareDate interogareDateService;

    @Test
    public void TestInterogareDate() throws Exception {
        String xsd = interogareDateService.getInterogareXsdSchema(buildRanAuthorization());


        CNP cnp = new CNP();
        cnp.setValue("1650202110251");
        IdentificatorPF identificatorPF = new IdentificatorPF();
        identificatorPF.setIdentificator(cnp);


        List<IdentificatorGospodarie> idGospodarii = interogareDateService.getListaGospodariiPF(identificatorPF, true, buildRanAuthorization());
        System.out.println("nr. gospodarii PF = " + idGospodarii.size());

        //  ArrayList<IdentificatorGospodarie> idGospodariiPF =interogareDateService.getListaGospodariiPF(identificatorPF, buildRanAuthorization());

        List<IdentificatorGospodarie> idGospodariiPJ = interogareDateService.getListaGospodariiPJ("7678777", true, buildRanAuthorization());
        System.out.println("nr. gospodarii PJ = " + idGospodarii.size());
        //String cp=TipCapitol.CAP0_12
        interogareDateService.getDateCapitol("5739063", 168452, TipCapitol.CAP0_12.toString(), 2016, null, buildRanAuthorization());


        System.out.println();
    }


    private RanAuthorization buildRanAuthorization() {
        RanAuthorization ranAuthorization = new RanAuthorization();
        ranAuthorization.setContext("UAT");
        ranAuthorization.setIdEntity(2929L);
        return ranAuthorization;
    }
}
