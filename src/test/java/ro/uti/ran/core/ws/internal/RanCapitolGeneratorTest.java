package ro.uti.ran.core.ws.internal;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.StopWatch;
import ro.uti.ran.core.ImportConfiguration;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.PortalPersistenceLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.repository.registru.NomUatRepository;
import ro.uti.ran.core.service.backend.DateRegistruXmlParser;
import ro.uti.ran.core.ws.handlers.RanWsHandlerFilter;
import ro.uti.ran.core.ws.handlers.filter.MessageStorageExecutor;
import ro.uti.ran.core.ws.internal.interogare.InterogareDate;
import ro.uti.ran.core.ws.internal.interogare.InterogareDateCentralizatoare;
import ro.uti.ran.core.ws.internal.transmitere.TransmitereDate;
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

import static ro.uti.ran.core.ws.internal.transmitere.RanCapitolAssertUtil.assertEqualsUsingJsonSerialization;
import static ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere.AUTOMAT;

/**
 * Created by bogdan.ardeleanu on 11/7/2015.
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
@PropertySource({"classpath:application.properties", "classpath:logback.properties"})
public class RanCapitolGeneratorTest {

    private static final boolean LOG_TO_OUT = true;

    @Autowired
    @Qualifier(value = "interogareDateService")
    private InterogareDate interogareDateWS;

    @Autowired
    @Qualifier(value = "interogareDateCentralizatoare")
    private InterogareDateCentralizatoare interogareDateCentralizatoareWS;

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
        RanWsHandlerFilter.setMessageStorageExecutor(new MessageStorageExecutor());

    }

    @Test
    public void generateRanData() throws Exception {
        File samplesDir = new File(this.getClass().getClassLoader().getResource("samples/v3/gospodarie").toURI());
        for (File firstChapterSampleFile : samplesDir.listFiles()) {
            if (firstChapterSampleFile.getName().startsWith("CAP0_")) {
                RanDoc firstChapterRanDoc = null;
                try {
                    firstChapterRanDoc = generateRanCapitol(firstChapterSampleFile);
//                } catch (Throwable e) {
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(" ERR: " + firstChapterSampleFile.getName() + " FAILED!");
                    System.out.println(" <><><><><><><><><><><><><><><><><><><><> ");
                    continue;
                }

                for (File otherChapterSampleFile : samplesDir.listFiles()) {
                    if (!otherChapterSampleFile.getName().startsWith("CAP0_")) {
                        try {
                            firstChapterRanDoc = generateRanCapitol(otherChapterSampleFile, firstChapterRanDoc);
//                        } catch (Throwable e) {
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(" ERR: " + otherChapterSampleFile.getName() + " FAILED!");
                            System.out.println(" <><><><><><><><><><><><><><><><><><><><> ");
                            continue;
                        }
                    }
                }
            }
        }
    }

    @Test
    public void generateRanData_centralizatoare() throws Exception {
        File samplesDir = new File(this.getClass().getClassLoader().getResource("samples/v3/centralizatoare_uat").toURI());
        for (File chapterSampleFile : samplesDir.listFiles()) {
            try {
                RanDoc ranDocCentralizatorUAT = generateRanCapitol(chapterSampleFile);
//          } catch (Throwable e) {
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" ERR: " + chapterSampleFile.getName() + " FAILED!");
                System.out.println(" <><><><><><><><><><><><><><><><><><><><> ");
                continue;
            }
        }
    }

    public RanDoc generateRanCapitol(File ranCapitolFile) throws Exception {
        return generateRanCapitol(ranCapitolFile, null);
    }

    public RanDoc generateRanCapitol(File ranCapitolFile, RanDoc firstChapterRanDoc) throws Exception {
        System.out.println("-------------------------------------------");
        System.out.println("loading:  " + ranCapitolFile.getName());
        String xmlRanCapitol = FileUtils.readFileToString(ranCapitolFile);

        RanAuthorization ranAuthorization = buildRanAuthorization();
        UUID uuid = buildUUID();
        String identificatorGospodarie = buildIdentificatorGospodarie();
        Integer sirutaUAT = nomUatRepository.findOne(ranAuthorization.getIdEntity()).getCodSiruta();
        Integer an = null;
        Integer semestru = null;
        Capitol capitol = null;
        CNP cnp = null;
        boolean isCentralizatorUAT = false;

        if (null != firstChapterRanDoc) {
            identificatorGospodarie = ((Gospodarie) firstChapterRanDoc.getBody().getGospodarieSauRaportare()).getIdentificator();
        }

        RanDoc ranDocTransmitere = dateRegistruXmlParser.getPojoFromXML(xmlRanCapitol);
        ranDocTransmitere.getHeader().setDataExport(new Date());
        ranDocTransmitere.getHeader().setCodXml(uuid);
        ranDocTransmitere.getHeader().setSirutaUAT(sirutaUAT);
        ranDocTransmitere.getHeader().setIndicativ(TipIndicativ.ADAUGA_SI_INLOCUIESTE);

        Object gospodarieSauRaportare = ranDocTransmitere.getBody().getGospodarieSauRaportare();
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
            AnRaportareCentralizator anRaportareCentralizator = (AnRaportareCentralizator) gospodarieSauRaportare;
            an = anRaportareCentralizator.getAn();
            anRaportareCentralizator.getCapitolCentralizatorCuAnRaportare();

            capitol = (Capitol) anRaportareCentralizator.getCapitolCentralizatorCuAnRaportare();
            isCentralizatorUAT = true;
        } else {
            throw new UnsupportedOperationException("Unknown GospodarieSauRaportare field.");
        }

        xmlRanCapitol = dateRegistruXmlParser.getXMLFromPojo(ranDocTransmitere);

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

        String xml = isCentralizatorUAT ?
                interogareDateCentralizatoareWS.getDateCapitolCentralizator(an, capitol.getCodCapitol(), sirutaUAT, ranAuthorization) :
                interogareDateWS.getDateCapitol(identificatorGospodarie, sirutaUAT, capitol.getCodCapitol(), an, semestru, ranAuthorization);
        watch.stop();
        System.out.println("-------- OK incarcare (" + watch.getTotalTimeSeconds() + "s)");
        System.out.println("-------- XML incarcare --------------------");
        System.out.println(xml);
        System.out.println("-------------------------------------------");

        if (LOG_TO_OUT) {
            FileUtils.writeStringToFile(new File("ranGenerator_out" + File.separatorChar + capitol.getCodCapitol() + '_' + identificatorGospodarie + "_transmitere.xml"), xmlRanCapitol);
            FileUtils.writeStringToFile(new File("ranGenerator_out" + File.separatorChar + capitol.getCodCapitol() + '_' + identificatorGospodarie + "_interogare.xml"), xml);

        }

        // verificare integritate date transmise (transmitere vs. interogare)
        assertEqualsUsingJsonSerialization(dateRegistruXmlParser.getPojoFromXML(xmlRanCapitol), dateRegistruXmlParser.getPojoFromXML(xml));

        return ranDocTransmitere;
    }

    private RanAuthorization buildRanAuthorization() {
        RanAuthorization ranAuthorization = new RanAuthorization();
        ranAuthorization.setContext("UAT");
        ranAuthorization.setIdEntity(2929L);

        return ranAuthorization;
    }

    private String buildIdentificatorGospodarie() {
        return String.valueOf(Math.abs(new Random().nextInt(9999999)));
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