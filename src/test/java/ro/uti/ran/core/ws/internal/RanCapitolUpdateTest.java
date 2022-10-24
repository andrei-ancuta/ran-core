package ro.uti.ran.core.ws.internal;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.StopWatch;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.repository.registru.NomUatRepository;
import ro.uti.ran.core.service.backend.DateRegistruXmlParser;
import ro.uti.ran.core.service.registru.RegistruService;
import ro.uti.ran.core.service.registru.RegistruServiceImpl;
import ro.uti.ran.core.ws.internal.interogare.InterogareDate;
import ro.uti.ran.core.ws.internal.transmitere.TransmitereDate;
import ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere;
import ro.uti.ran.core.xml.model.AnRaportare;
import ro.uti.ran.core.xml.model.AnRaportareCentralizator;
import ro.uti.ran.core.xml.model.Gospodarie;
import ro.uti.ran.core.xml.model.RanDoc;
import ro.uti.ran.core.xml.model.capitol.*;
import ro.uti.ran.core.xml.model.types.CNP;
import ro.uti.ran.core.xml.model.types.TipIndicativ;
import ro.uti.ran.core.xml.model.types.UUID;

import java.io.File;
import java.util.Date;
import java.util.Random;

import static ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere.AUTOMAT;

/**
 * Created by Dan on 24-Nov-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
@PropertySource({"classpath:application.properties", "classpath:logback.properties"})
public class RanCapitolUpdateTest {


    private static final boolean LOG_TO_OUT = true;

    @ContextConfiguration
    @ComponentScan({
            "ro.uti.ran.core.service.backend",
            "ro.uti.ran.core.service.index",
            "ro.uti.ran.core.service.parametru",
            "ro.uti.ran.core.ws.internal.interogare",
            "ro.uti.ran.core.ws.internal.transmitere"
    })
    static class ImportConfiguration {
        @Bean
        public RegistruService getRegistruService() {
            return new RegistruServiceImpl();
        }

    }

    @Autowired
    @Qualifier(value = "interogareDateService")
    private InterogareDate interogareDateWS;

    @Autowired
    @Qualifier(value = "transmitereDateService")
    private TransmitereDate transmitereDateWS;

    @Autowired
    private DateRegistruXmlParser dateRegistruXmlParser;

    @Autowired
    private NomUatRepository nomUatRepository;

    @Before
    public void beforeTests() {
        FileUtils.deleteQuietly(new File("ranGenerator_out"));
    }

    @Test
    public void updateRanData() throws Exception {
        String identificatorGospodarie = "1770744";
        File samplesDir = new File(this.getClass().getClassLoader().getResource("samples/2015-10-28").toURI());
        for (File chapterSampleFile : samplesDir.listFiles()) {
            try {
                generateRanCapitol(chapterSampleFile,identificatorGospodarie );
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" ERR: " + chapterSampleFile.getName() + " FAILED!");
                System.out.println(" <><><><><><><><><><><><><><><><><><><><> ");
                continue;
            }
        }
    }


    public RanDoc generateRanCapitol(File ranCapitolFile, String identificatorGospodarie) throws Exception {
        System.out.println("-------------------------------------------");
        System.out.println("loading:  " + ranCapitolFile.getName());
        String xmlRanCapitol = FileUtils.readFileToString(ranCapitolFile);

        RanAuthorization ranAuthorization = buildRanAuthorization();
        UUID uuid = buildUUID();
        Integer sirutaUAT = nomUatRepository.findOne(ranAuthorization.getIdEntity()).getCodSiruta();
        Integer an = null;
        Integer semestru = null;
        Capitol capitol = null;
        CNP cnp = null;


        RanDoc ranDoc = dateRegistruXmlParser.getPojoFromXML(xmlRanCapitol);
        ranDoc.getHeader().setDataExport(new Date());
        ranDoc.getHeader().setCodXml(uuid);
        ranDoc.getHeader().setSirutaUAT(sirutaUAT);
        ranDoc.getHeader().setIndicativ(TipIndicativ.ADAUGA_SI_INLOCUIESTE);

        Object gospodarieSauRaportare = ranDoc.getBody().getGospodarieSauRaportare();
        if (gospodarieSauRaportare instanceof Gospodarie) {
            Gospodarie gospodarie = (Gospodarie) gospodarieSauRaportare;
            gospodarie.setIdentificator(identificatorGospodarie);

            Object unitateGospodarie = gospodarie.getUnitateGospodarie();
            if (unitateGospodarie instanceof AnRaportare) {
                AnRaportare anRaportare = (AnRaportare) unitateGospodarie;
                an = anRaportare.getAn();
                capitol = (Capitol) anRaportare.getCapitolCuAnRaportare();

                if (capitol instanceof Capitol_7) {
                    semestru = ((Capitol_7) capitol).getSemestru();
                } else if (capitol instanceof Capitol_8) {
                    semestru = ((Capitol_8) capitol).getSemestru();
                }
            }
            if (unitateGospodarie instanceof Capitol) {
                capitol = (Capitol) unitateGospodarie;

                if (unitateGospodarie instanceof Capitol_0_12) {
                    buildCapitol_0_12((Capitol_0_12) unitateGospodarie).getRandCapitol().getAdresaGospodarie().setSirutaUAT(sirutaUAT);
                } else if (unitateGospodarie instanceof Capitol_0_34) {
                    buildCapitol_0_34((Capitol_0_34) unitateGospodarie).getRandCapitol().getAdresaGospodarie().setSirutaUAT(sirutaUAT);
                }
            }
        } else if (gospodarieSauRaportare instanceof AnRaportareCentralizator) {

        } else {
            throw new UnsupportedOperationException("Unknown GospodarieSauRaportare field.");
        }

        xmlRanCapitol = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

        System.out.println("---- Capitol transmis:");
        System.out.println("-------- codXml = " + uuid.getValue());
        System.out.println("-------- identificatorGospodarie = " + identificatorGospodarie);
        System.out.println("-------- sirutaUAT = " + sirutaUAT);
        System.out.println("-------- codCapitol = " + capitol.getCodCapitol());
        System.out.println("-------- an = " + an);
        System.out.println("-------- semestru = " + semestru);

        StopWatch watch = new StopWatch();
        watch.start();

        transmitereDateWS.transmitere(xmlRanCapitol, AUTOMAT, ranAuthorization);
        watch.stop();
        System.out.println("-------- OK transmitere (" + watch.getTotalTimeSeconds() + "s)");
        watch = new StopWatch();
        watch.start();

        String xml = interogareDateWS.getDateCapitol(identificatorGospodarie, sirutaUAT, capitol.getCodCapitol(), an, semestru, ranAuthorization);
        watch.stop();
        System.out.println("-------- OK incarcare (" + watch.getTotalTimeSeconds() + "s)");
        System.out.println("-------- XML incarcare --------------------");
        System.out.println(xml);
        System.out.println("-------------------------------------------");

        if (LOG_TO_OUT) {
            FileUtils.writeStringToFile(new File("ranGenerator_out" + File.separatorChar + capitol.getCodCapitol() + '_' + identificatorGospodarie + "_transmitere.xml"), xmlRanCapitol);
            FileUtils.writeStringToFile(new File("ranGenerator_out" + File.separatorChar + capitol.getCodCapitol() + '_' + identificatorGospodarie + "_interogare.xml"), xml);

        }

        return ranDoc;
    }

    private RanAuthorization buildRanAuthorization() {
        RanAuthorization ranAuthorization = new RanAuthorization();
        ranAuthorization.setContext("UAT");
        ranAuthorization.setIdEntity(2929L);

        return ranAuthorization;
    }

    private UUID buildUUID() {
        UUID uuid = new UUID();
        uuid.setValue(java.util.UUID.randomUUID().toString());
        return uuid;
    }

    private CNP generateCNP() {
        long _13charsLoadFactor = 1000000000000L;

        CNP cnp = new CNP();
        cnp.setValue(String.valueOf(_13charsLoadFactor + Math.abs(new Random().nextLong()) % _13charsLoadFactor));
        return cnp;
    }

    private Capitol_0_12 buildCapitol_0_12(Capitol_0_12 capitol_0_12) {
        //capitol_0_12.getRandCapitol().getGospodar().setCnp(generateCNP());
        return capitol_0_12;
    }

    private Capitol_0_34 buildCapitol_0_34(Capitol_0_34 capitol_0_34) {
        //capitol_0_34.getRandCapitol().getReprezentantLegal().setCnp(generateCNP());
        return capitol_0_34;
    }


}
