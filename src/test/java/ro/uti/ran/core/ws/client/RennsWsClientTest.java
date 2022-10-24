package ro.uti.ran.core.ws.client;

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
import ro.uti.ran.core.config.Profiles;
import ro.uti.ran.core.config.WsLayerConfig;

import ro.uti.ran.core.ws.client.renns.RennsRoadsService;
import ro.uti.ran.core.ws.client.renns.roads.GetRoadWSRequest;
import ro.uti.ran.core.ws.client.renns.roads.GetRoadWSResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                WsLayerConfig.class,
                RennsWsClientTest.ImportConfig.class
        })

@ActiveProfiles(profiles = {Profiles.DEV})
public class RennsWsClientTest {
    @Configuration
    @PropertySource("classpath:application.properties")
    public static class ImportConfig {

    }

    @Autowired
    private RennsRoadsService rennsRoadsService;

    @Autowired
    private Environment env;

    @Test
    public void testRennsRoads() throws Exception {
        GetRoadWSRequest request = new GetRoadWSRequest();
        request.setCounty("Alba Iulia");
        request.setLocalityName("Aiud");
        GetRoadWSResponse response = rennsRoadsService.getRoads(request);


    }
}

