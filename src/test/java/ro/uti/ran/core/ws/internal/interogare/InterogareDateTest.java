package ro.uti.ran.core.ws.internal.interogare;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.ws.handlers.RanWsHandlerFilter;
import ro.uti.ran.core.ws.handlers.filter.MessageStorageExecutor;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.internal.registru.TransmisiiServiceImpl;
import ro.uti.ran.core.ws.model.interogare.IdentificatorGospodarie;
import ro.uti.ran.core.ws.model.interogare.IdentificatorPF;
import ro.uti.ran.core.xml.model.types.CNP;

import java.util.List;

/**
 * Created by Anastasia cea micuta on 10/22/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                PortalPersistenceLayerConfig.class,
                ImportConfiguration.class,
                TransmisiiServiceImpl.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class InterogareDateTest {

    @Autowired
    @Qualifier(value = "interogareDateService")
    private InterogareDate interogareDate;

    @Before
    public void beforeTests() {
        RanWsHandlerFilter.setMessageStorageExecutor(new MessageStorageExecutor());

    }

    @Test
    public void testGetDateCapitol() throws Exception {
        // DEV
        String xml = interogareDate.getDateCapitol("9748228", 168452, TipCapitol.CAP0_12.toString(), null, null, buildRanAuthorization());
        System.out.println(xml);
    }

    @Test
    public void testGetDateCapitolPF() throws Exception {
        IdentificatorPF identificatorPF = new IdentificatorPF();
        CNP cnp = new CNP();
        cnp.setValue("1850000000000");
        identificatorPF.setIdentificator(cnp);
        String xml = interogareDate.getDateCapitolPF(identificatorPF, 168452, TipCapitol.CAP3.toString(), 2015, null, buildRanAuthorization());
        System.out.println(xml);
    }


    @Test
    public void testGetDateCapitolPJ() throws Exception {
        String xml = interogareDate.getDateCapitolPJ("12345", 49625, TipCapitol.CAP0_34.toString(), null, null, buildRanAuthorization());
        System.out.println(xml);
    }

    @Test
    public void testGetListaGospodariiPF() throws Exception {
        CNP cnp = new CNP();
        cnp.setValue("1234567890123");

        IdentificatorPF identificatorPF = new IdentificatorPF();
        identificatorPF.setIdentificator(cnp);
        List<IdentificatorGospodarie> idGospodarii = interogareDate.getListaGospodariiPF(identificatorPF, true, buildRanAuthorization());
        System.out.println("nr. gospodarii PF = " + idGospodarii.size());
    }

    @Test
    public void testGetListaGospodariiPJ() throws Exception {
        List<IdentificatorGospodarie> idGospodarii = interogareDate.getListaGospodariiPJ("12345", true, buildRanAuthorization());
        System.out.println("nr. gospodarii PJ = " + idGospodarii.size());
    }

    private RanAuthorization buildRanAuthorization() {
        RanAuthorization ranAuthorization = new RanAuthorization();
        ranAuthorization.setContext("UAT");
        ranAuthorization.setIdEntity(2929L);
        // RAL
        ranAuthorization.setLocal(true);

        return ranAuthorization;
    }
}
