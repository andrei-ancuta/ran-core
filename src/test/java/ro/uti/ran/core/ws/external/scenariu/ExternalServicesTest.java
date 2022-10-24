package ro.uti.ran.core.ws.external.scenariu;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.*;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.service.exportNomenclatoare.impl.NomenclatorClassificationServiceImpl;
import ro.uti.ran.core.service.registru.RegistruServiceTestImpl;
import ro.uti.ran.core.service.security.SecurityWsService;
import ro.uti.ran.core.utils.ZipUtil;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.ancpi.ran.core.ws.external.interogare.InterogareDateCentralizatoareExtern;
import ro.ancpi.ran.core.ws.external.interogare.InterogareDateCentralizatoareExternImpl;
import ro.ancpi.ran.core.ws.external.interogare.InterogareDateExtern;
import ro.ancpi.ran.core.ws.external.interogare.InterogareDateExternImpl;
import ro.ancpi.ran.core.ws.external.transmitere.TransmitereDateExtern;
import ro.ancpi.ran.core.ws.external.transmitere.TransmitereDateExternImpl;
import ro.uti.ran.core.ws.model.interogare.IdentificatorGospodarie;
import ro.uti.ran.core.ws.model.interogare.IdentificatorPF;
import ro.uti.ran.core.ws.model.transmitere.InformatiiTransmisie;
import ro.uti.ran.core.xml.model.types.CNP;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by miroslav.rusnac on 18/02/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                TestDatasourceConfig.class,
                PortalPersistenceLayerConfig.class,
                RegistruPersistenceLayerConfig.class,
                SecurityConfig.class,
                TransmitereDateExternImpl.class,
                InterogareDateExternImpl.class,
                InterogareDateCentralizatoareExternImpl.class,
                SecurityWsService.class,
                ImportConfiguration.class,
                RegistruServiceTestImpl.class,
                NomenclatorClassificationServiceImpl.class

        })
@ActiveProfiles(profiles = {Profiles.TEST})
@Transactional
@TransactionConfiguration(transactionManager="registruTransactionManager",defaultRollback = false)
public class ExternalServicesTest {

    @Autowired
    @Qualifier(value = "transmitereDateExternService")
    private TransmitereDateExtern transmitereDateExtern;

    @Autowired
    @Qualifier(value="interogareDateExternService")
    private InterogareDateExtern interogareDateExtern;

    @Autowired
    @Qualifier(value = "interogareDateCentralizatoareExternService")
    private InterogareDateCentralizatoareExtern interogareDateCentralizatoareExtern;

    @Test
    public void testCentralizat() throws Exception {
        //Capitol 0_12
        URL resourceURL = this.getClass().getClassLoader().getResource("dataTransfer/schema v7_cap_0_1.xml");
        //System.out.println(resourceURL);
        String dataOut = FileUtils.readFileToString(new File(resourceURL.toURI()));
        RanAuthentication ranAuthentication = new RanAuthentication();
        ranAuthentication.setUsername("86810"); //codSiruta pt. Neamt
        ranAuthentication.setPassword("12345678");
        byte[] xmlCompresat = ZipUtil.compress(dataOut.getBytes(Charset.forName("UTF-8")));
        transmitereDateExtern.transmitere(xmlCompresat, ranAuthentication);
        InformatiiTransmisie status=transmitereDateExtern.getStatusTransmisie("2900622b-6fc0-4349-8457-84dba3f077d4", ranAuthentication);
        assertEquals(status.getStareTransmisie().toString(), "SALVATA");

        resourceURL = this.getClass().getClassLoader().getResource("dataTransfer/schema v7_cap_3.xml");
        //System.out.println(resourceURL);
        dataOut = FileUtils.readFileToString(new File(resourceURL.toURI()));
        xmlCompresat = ZipUtil.compress(dataOut.getBytes(Charset.forName("UTF-8")));
        transmitereDateExtern.transmitere(xmlCompresat, ranAuthentication);
        status=transmitereDateExtern.getStatusTransmisie("e65f290c-9f2f-4d43-812b-72471935d6b0", ranAuthentication);
        assertEquals(status.getStareTransmisie().toString(), "SALVATA");

        resourceURL = this.getClass().getClassLoader().getResource("dataTransfer/schema v7_cap_2a.xml");
        //System.out.println(resourceURL);
        dataOut = FileUtils.readFileToString(new File(resourceURL.toURI()));
        xmlCompresat = ZipUtil.compress(dataOut.getBytes(Charset.forName("UTF-8")));
        transmitereDateExtern.transmitere(xmlCompresat, ranAuthentication);
        status=transmitereDateExtern.getStatusTransmisie("f462f900-98c6-4bbc-ba52-ec144da7ecdb", ranAuthentication);
        assertEquals(status.getStareTransmisie().toString(), "SALVATA");


        byte[] responce=interogareDateExtern.getDateCapitol("66789", 86810, TipCapitol.CAP2a.toString(), 2015, null, ranAuthentication);
        String xmlDecompresat= new String(ZipUtil.decompress(responce), StandardCharsets.UTF_8);
        assertTrue(xmlDecompresat.contains("<sirutaUAT>86810</sirutaUAT>"));

        CNP cnp = new CNP();
        cnp.setValue("1650202110251");
        IdentificatorPF identificatorPF=new IdentificatorPF();
        identificatorPF.setIdentificator(cnp);

        responce = interogareDateExtern.getDateCapitolPF(identificatorPF,86810,TipCapitol.CAP2a.toString(),2015,null,ranAuthentication);


        ArrayList<IdentificatorGospodarie> listaGospodariiPF = interogareDateExtern.getListaGospodariiPF(identificatorPF, true,ranAuthentication);
        assertFalse(listaGospodariiPF.isEmpty());


        //centralizator
        resourceURL = this.getClass().getClassLoader().getResource("dataTransfer/centr_12a.xml");
        //System.out.println(resourceURL);
        dataOut = FileUtils.readFileToString(new File(resourceURL.toURI()));
        xmlCompresat = ZipUtil.compress(dataOut.getBytes(Charset.forName("UTF-8")));
        transmitereDateExtern.transmitere(xmlCompresat, ranAuthentication);
        status=transmitereDateExtern.getStatusTransmisie("1c3ba146-a8ae-ac82-9c8d-6f6365affec3", ranAuthentication);
        assertEquals(status.getStareTransmisie().toString(), "SALVATA");


        responce=interogareDateCentralizatoareExtern.getDateCapitolCentralizator(2015,TipCapitol.CAP12a.toString(),86810,ranAuthentication);
        xmlDecompresat= new String(ZipUtil.decompress(responce), StandardCharsets.UTF_8);
        assertTrue(xmlDecompresat.contains("<sirutaUAT>86810</sirutaUAT>"));
        System.out.println();
    }
}
