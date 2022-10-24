package ro.uti.ran.core.service.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.DevDataSourceConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.RegistruPersistenceLayerConfig;

/**
 * Created by bogdan.ardeleanu on 10/19/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                DevDataSourceConfig.class,
                RegistruPersistenceLayerConfig.class,
                GeometrieServiceImpl.class,
                GeometrieServiceTest.ImportConfiguration.class
        })
@ActiveProfiles(profiles = {Profiles.DEV})
public class GeometrieServiceTest {


    @ContextConfiguration
    @PropertySource({"classpath:application.properties", "classpath:logback.properties", "classpath:gisserver.properties"})
    static class ImportConfiguration {

    }

    @Autowired
    private GeometrieService geometrieService;

    @Test
    public void testValidareGeometrie() throws Exception {
        try {
//            geometrieService.validateGeometrie("<gml:Polygon srsName=\"EPSG:31700\" xmlns:gml=\"http://www.opengis.net/gml\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">646821.944 399471.284 647288.603 399824.501 647328.391 399772.073 646843.807 399410.656 646821.944 399471.284</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>");
            geometrieService.validateGeometrie("<gml:Polygon srsName=\"EPSG:31700\" xmlns:gml=\"http://www.opengis.net/gml\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">368262.178380236 491502.758369315 368046.822169682 491734.549823896 368033.062774871 491721.321491896 368247.889938617 491491.117542551 368262.178380236 491502.758369315 </gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testValidareGeometrieUatLimit() throws Exception {
        try {
//            geometrieService.validateGeometrie("<gml:Polygon srsName=\"EPSG:31700\" xmlns:gml=\"http://www.opengis.net/gml\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">646821.944 399471.284 647288.603 399824.501 647328.391 399772.073 646843.807 399410.656 646821.944 399471.284</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>");
            geometrieService.validateGeometriePoligonUatLimit(12345, "<gml:Polygon srsName=\"EPSG:31700\" xmlns:gml=\"http://www.opengis.net/gml\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\">368262.178380236 491502.758369315 368046.822169682 491734.549823896 368033.062774871 491721.321491896 368247.889938617 491491.117542551 368262.178380236 491502.758369315 </gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
