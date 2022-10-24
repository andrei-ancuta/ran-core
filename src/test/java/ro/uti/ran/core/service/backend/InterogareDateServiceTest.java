package ro.uti.ran.core.service.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.service.backend.dto.ParametriiInterogare;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.RanDoc;

/**
 * Created by bogdan.ardeleanu on 10/19/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                InterogareDateServiceTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
@PropertySource({"classpath:application.properties", "classpath:logback.properties"})
public class InterogareDateServiceTest {
    @ContextConfiguration
    @ComponentScan({"ro.uti.ran"})
    static class ImportConfiguration {

    }

    @Autowired
    private InterogareDateService interogareDateService;

    @Autowired
    private InterogareDateCentralizatorService interogareDateCentralizatorService;

    @Autowired
    private DateRegistruXmlParser dateRegistruXmlParser;

    /**
     * Gospodarii (identificator; codSirutaUat):
     * (1234567890; 49625);
     * (1,12345)
     * (gosp23,144152)
     * <p/>
     * (gosp25,144152)
     * <p/>
     * (4461261,12345)
     */

    private String identificatorGospodarie = "7478371";
    private Integer codSirutaUat = 12345;
    private Integer an = 2015;
    private Byte semestru = 2;

    private ParametriiInterogare buildParametriiInterogareCuAnSemestru(TipCapitol tipCapitol) {
        return new ParametriiInterogare
                .ParametriiInterogareBuilder(codSirutaUat, identificatorGospodarie, tipCapitol)
                .an(an)
                .semestru(semestru).build();
    }

    private ParametriiInterogare buildParametriiInterogareCuAn(TipCapitol tipCapitol) {
        return new ParametriiInterogare
                .ParametriiInterogareBuilder(codSirutaUat, identificatorGospodarie, tipCapitol)
                .an(an)
                .build();
    }

    private ParametriiInterogare buildParametriiInterogareFaraAnSemestru(TipCapitol tipCapitol) {
        return new ParametriiInterogare
                .ParametriiInterogareBuilder(codSirutaUat, identificatorGospodarie, tipCapitol)
                .build();
    }

    @Test
    public void testGetDateGospodarie() {
        try {
            testGetDateCapitol_0_12();
            testGetDateCapitol_0_34();
            testGetDateCapitol_1();
            testGetDateCapitol_2a();
            testGetDateCapitol_2b();
            testGetDateCapitol_3();
            testGetDateCapitol_4a();
            testGetDateCapitol_4a1();
            testGetDateCapitol_4b1();
            testGetDateCapitol_4b2();
            testGetDateCapitol_4c();
            testGetDateCapitol_5a();
            testGetDateCapitol_5b();
            testGetDateCapitol_5c();
            testGetDateCapitol_5d();
            testGetDateCapitol_6();
            testGetDateCapitol_7();
            testGetDateCapitol_8();
            testGetDateCapitol_9();
            testGetDateCapitol_10a();
            testGetDateCapitol_10b();
            testGetDateCapitol_11();
            testGetDateCapitol_12();
            testGetDateCapitol_13();
            testGetDateCapitol_14();
            testGetDateCapitol_15a();
            testGetDateCapitol_15b();
            testGetDateCapitol_16();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDateCapitolCentralizator() {
        try {
            testCapitolCentralizator_12a();
            testCapitolCentralizator_12a1();
            testCapitolCentralizator_12b1();
            testCapitolCentralizator_12b2();
            testCapitolCentralizator_12c();
            testCapitolCentralizator_12d();
            testCapitolCentralizator_12e();
            testCapitolCentralizator_12f();
            testCapitolCentralizator_13cent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testCapitolCentralizator_12a() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP12a;
            Integer codSiruta = 49625;
            //
            RanDoc ranDoc = interogareDateCentralizatorService.getDateCapitol(codSiruta, an, tipCapitol);
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    public void testCapitolCentralizator_12a1() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP12a1;
            Integer codSiruta = 49625;
            //
            RanDoc ranDoc = interogareDateCentralizatorService.getDateCapitol(codSiruta, an, tipCapitol);
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    public void testCapitolCentralizator_12b1() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP12b1;
            Integer codSiruta = 49625;
            //
            RanDoc ranDoc = interogareDateCentralizatorService.getDateCapitol(codSiruta, an, tipCapitol);
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    public void testCapitolCentralizator_12b2() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP12b2;
            Integer codSiruta = 49625;
            //
            RanDoc ranDoc = interogareDateCentralizatorService.getDateCapitol(codSiruta, an, tipCapitol);
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    public void testCapitolCentralizator_12c() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP12c;
            Integer codSiruta = 49625;
            //
            RanDoc ranDoc = interogareDateCentralizatorService.getDateCapitol(codSiruta, an, tipCapitol);
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    public void testCapitolCentralizator_12d() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP12d;
            Integer codSiruta = 49625;
            //
            RanDoc ranDoc = interogareDateCentralizatorService.getDateCapitol(codSiruta, an, tipCapitol);
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    public void testCapitolCentralizator_12e() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP12e;
            Integer codSiruta = 49625;
            //
            RanDoc ranDoc = interogareDateCentralizatorService.getDateCapitol(codSiruta, an, tipCapitol);
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);
            System.out.println(xml);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    public void testCapitolCentralizator_12f() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP12f;
            Integer codSiruta = 49625;
            //
            RanDoc ranDoc = interogareDateCentralizatorService.getDateCapitol(codSiruta, an, tipCapitol);
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    public void testCapitolCentralizator_13cent() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP13cent;
            Integer codSiruta = 49625;
            //
            RanDoc ranDoc = interogareDateCentralizatorService.getDateCapitol(codSiruta, an, tipCapitol);
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }


    //@Test
    public void testGetDateCapitol_0_12() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP0_12;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareFaraAnSemestru(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_0_34() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP0_34;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareFaraAnSemestru(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_1() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP1;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareFaraAnSemestru(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_2a() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP2a;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_2b() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP2b;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);
            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_3() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP3;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_4a() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP4a;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_4a1() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP4a1;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_4b1() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP4b1;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_4b2() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP4b2;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_4c() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP4c;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_5a() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP5a;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_5b() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP5b;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_5c() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP5c;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_5d() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP5d;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_6() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP6;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_7() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP7;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAnSemestru(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_8() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP8;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAnSemestru(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_9() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP9;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_10a() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP10a;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_10b() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP10b;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_11() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP11;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareCuAn(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_12() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP12;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareFaraAnSemestru(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_13() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP13;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareFaraAnSemestru(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_14() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP14;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareFaraAnSemestru(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_15a() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP15a;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareFaraAnSemestru(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_15b() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP15b;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareFaraAnSemestru(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

    //@Test
    public void testGetDateCapitol_16() {
        String xml = null;
        try {
            TipCapitol tipCapitol = TipCapitol.CAP16;
            //
            RanDoc ranDoc = interogareDateService.getDateCapitol(buildParametriiInterogareFaraAnSemestru(tipCapitol));
            xml = dateRegistruXmlParser.getXMLFromPojo(ranDoc);

            //FileUtils.write(new File("..\\ran-xml\\src\\main\\resources\\samples\\2015-10-28\\" + tipCapitol + ".xml"), xml);
        } catch (RanBusinessException e) {
            System.out.println(e.getCode() + " " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xml != null) {
                System.out.println(xml);
            }
        }
    }

}
