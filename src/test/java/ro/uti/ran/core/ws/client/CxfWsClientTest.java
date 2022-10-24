package ro.uti.ran.core.ws.client;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ro.uti.ran.core.config.CxfLayerConfig;
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.WsLayerConfig;
import ro.uti.ran.core.ws.client.renns.RennsRoadsService;
import ro.uti.ran.core.ws.client.renns.addresses.AddressWS1Port;
import ro.uti.ran.core.ws.client.renns.addresses.GetAddressWSRequest;
import ro.uti.ran.core.ws.client.renns.addresses.GetAddressWSResponse;
import ro.uti.ran.core.ws.client.renns.roads.GetRoadWSRequest;
import ro.uti.ran.core.ws.client.renns.roads.GetRoadWSResponse;
import ro.uti.ran.core.ws.client.renns.roads.RoadsPort;
import ro.uti.ran.core.ws.client.renns.roads.RoadsPortService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                CxfLayerConfig.class,
                CxfWsClientTest.ImportConfig.class
        })

@ActiveProfiles(profiles = {Profiles.DEV})
public class CxfWsClientTest {
    @Configuration
    @PropertySource("classpath:application.properties")
    public static class ImportConfig {

    }

    @Autowired
    private RoadsPort roadsPort;

    @Autowired
    private AddressWS1Port addressesPort;


    @Autowired
    private Environment env;

    @Test
    public void testRennsRoads() throws Exception {
        GetRoadWSRequest request = new GetRoadWSRequest();
        request.setCounty("Alba Iulia");
        request.setLocalityName("Aiud");
        GetRoadWSResponse response = roadsPort.getRoadWS(request);
    }

    @Test
    public void testRennsAddresses() throws Exception {
        GetAddressWSRequest request = new GetAddressWSRequest();
        request.setCua("12345");
        request.setCounty("Alba Iulia");
        GetAddressWSResponse response = addressesPort.getAddressWS(request);
    }
}

