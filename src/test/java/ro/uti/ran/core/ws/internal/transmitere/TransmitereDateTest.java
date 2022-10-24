package ro.uti.ran.core.ws.internal.transmitere;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.service.registru.FluxRegistruService;
import ro.uti.ran.core.service.registru.FluxRegistruServiceImpl;
import ro.uti.ran.core.service.registru.RegistruService;
import ro.uti.ran.core.service.registru.RegistruServiceImpl;
import ro.uti.ran.core.ws.WsUtilsService;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere;
import ro.uti.ran.core.xml.model.RanDoc;
import ro.uti.ran.core.xml.model.types.UUID;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere.*;

/**
 * Created by Anastasia cea micuta on 10/22/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                PortalPersistenceLayerConfig.class,
                ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class TransmitereDateTest {

    @Autowired
    @Qualifier(value = "transmitereDateService")
    private TransmitereDate transmitereDate;


    @Test
    public void testTransmitere_Capitol() throws Exception {
        URL resourceURL = this.getClass().getClassLoader().getResource("samples/v3/gospodarie/CAP0_1.xml");
        String xmlCapitol = FileUtils.readFileToString(new File(resourceURL.toURI()));

        RanAuthorization ranAuthorization = new RanAuthorization();
        ranAuthorization.setContext("UAT");
        ranAuthorization.setIdEntity(2929L);
        // RAL
        ranAuthorization.setLocal(true);

//        transmitereDate.transmitere(xmlCapitol_0_12.getBytes(Charset.forName("UTF-8")), ranAuthorization);
        transmitereDate.transmitere(xmlCapitol, AUTOMAT, ranAuthorization);
    }

    @Test
    public void testTransmitere_Capitol_12a() throws Exception {
        testTransmitere_Capitol_Centralizator("12a");
    }

    @Test
    public void testTransmitere_Capitol_12a1() throws Exception {
        testTransmitere_Capitol_Centralizator("12a1");
    }

    @Test
    public void testTransmitere_Capitol_12b1() throws Exception {
        testTransmitere_Capitol_Centralizator("12b1");
    }

    @Test
    public void testTransmitere_Capitol_12b2() throws Exception {
        testTransmitere_Capitol_Centralizator("12b2");
    }

    @Test
    public void testTransmitere_Capitol_12c() throws Exception {
        testTransmitere_Capitol_Centralizator("12c");
    }

    @Test
    public void testTransmitere_Capitol_12d() throws Exception {
        testTransmitere_Capitol_Centralizator("12d");
    }

    @Test
    public void testTransmitere_Capitol_12e() throws Exception {
        testTransmitere_Capitol_Centralizator("12e");
    }

    @Test
    public void testTransmitere_Capitol_12f() throws Exception {
        testTransmitere_Capitol_Centralizator("12f");
    }

    @Test
    public void testTransmitere_Capitol_13cent() throws Exception {
        testTransmitere_Capitol_Centralizator("13centralizator");
    }

    @Test
    public void testAnulareCapitol12a() throws Exception {
        InputStream resourceStream = RanDoc.class.getClassLoader().getResourceAsStream("samples/RANschema1_AnulareCap12a.xml");
        String xmlCapitol_d = IOUtils.toString(resourceStream);
        startTransmitere(xmlCapitol_d);
    }

    @Test
    public void testAnulareCapitol12a1() throws Exception {
        InputStream resourceStream = RanDoc.class.getClassLoader().getResourceAsStream("samples/RANschema1_AnulareCap12a1.xml");
        String xmlCapitol_d = IOUtils.toString(resourceStream);
        startTransmitere(xmlCapitol_d);
    }

    @Test
    public void testAnulareCapitol12b1() throws Exception {
        InputStream resourceStream = RanDoc.class.getClassLoader().getResourceAsStream("samples/RANschema1_AnulareCap12b1.xml");
        String xmlCapitol_d = IOUtils.toString(resourceStream);
        startTransmitere(xmlCapitol_d);
    }

    @Test
    public void testAnulareCapitol12b2() throws Exception {
        InputStream resourceStream = RanDoc.class.getClassLoader().getResourceAsStream("samples/RANschema1_AnulareCap12b2.xml");
        String xmlCapitol_d = IOUtils.toString(resourceStream);
        startTransmitere(xmlCapitol_d);
    }

    @Test
    public void testAnulareCapitol12c() throws Exception {
        InputStream resourceStream = RanDoc.class.getClassLoader().getResourceAsStream("samples/RANschema1_AnulareCap12c.xml");
        String xmlCapitol_d = IOUtils.toString(resourceStream);
        startTransmitere(xmlCapitol_d);
    }

    @Test
    public void testAnulareCapitol12d() throws Exception {
        InputStream resourceStream = RanDoc.class.getClassLoader().getResourceAsStream("samples/RANschema1_AnulareCap12d.xml");
        String xmlCapitol_d = IOUtils.toString(resourceStream);
        startTransmitere(xmlCapitol_d);
    }

    @Test
    public void testAnulareCapitol12e() throws Exception {
        InputStream resourceStream = RanDoc.class.getClassLoader().getResourceAsStream("samples/RANschema1_AnulareCap12e.xml");
        String xmlCapitol_d = IOUtils.toString(resourceStream);
        startTransmitere(xmlCapitol_d);
    }

    @Test
    public void testAnulareCapitol12f() throws Exception {
        InputStream resourceStream = RanDoc.class.getClassLoader().getResourceAsStream("samples/RANschema1_AnulareCap12f.xml");
        String xmlCapitol_d = IOUtils.toString(resourceStream);
        startTransmitere(xmlCapitol_d);
    }

    @Test
    public void testAnulareCapitol13cent() throws Exception {
        InputStream resourceStream = RanDoc.class.getClassLoader().getResourceAsStream("samples/RANschema1_AnulareCap13centralizator.xml");
        String xmlCapitol_d = IOUtils.toString(resourceStream);
        startTransmitere(xmlCapitol_d);
    }

    private void startTransmitere(String xmlCapitol_d) throws Exception {
        RanAuthorization ranAuthorization = new RanAuthorization();
        ranAuthorization.setContext("UAT");
        ranAuthorization.setIdEntity(11L);
        transmitereDate.transmitere(xmlCapitol_d, AUTOMAT, ranAuthorization);
    }

    private void testTransmitere_Capitol_Centralizator(String code) throws Exception {
        InputStream resourceStream = RanDoc.class.getClassLoader().getResourceAsStream("samples/v3/centralizatoare_uat/RANschema1_Cap"+ code +".xml");
        String xml = IOUtils.toString(resourceStream);
        xml = xml.replace("1c3ba146-a8ae-ac82-9c8d-6f6365affec3", java.util.UUID.randomUUID().toString());

        RanAuthorization ranAuthorization = new RanAuthorization();
        ranAuthorization.setContext("UAT");
        ranAuthorization.setIdEntity(11L);

        transmitereDate.transmitere(xml, AUTOMAT, ranAuthorization);
    }

}
