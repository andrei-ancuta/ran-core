/*
 * Copyright (c) 2009, Dennis M. Sosnoski. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. Neither the name of
 * JiBX nor the names of its contributors may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ro.uti.ran.core.ws.client.metro;

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
import ro.uti.ran.core.ws.client.renns.roads.*;

import javax.xml.ws.BindingProvider;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {
                WsLayerConfig.class,
                WebServiceClient.ImportConfig.class
        })

@ActiveProfiles(profiles = {Profiles.DEV})
public class WebServiceClient
{

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

        RoadsPortService rennsRoads = new RoadsPortService(new URL("http://renns.ancpi.ro/renns-public-test/soapws/roads.wsdl"));
        RoadsPort rennsRoadsPort = rennsRoads.getRoadsPortSoap11();

        GetRoadWSRequest request = new GetRoadWSRequest();
        request.setCounty("Alba Iulia");
        request.setLocalityName("Aiud");
        GetRoadWSResponse response = rennsRoadsPort.getRoadWS(request);
    }
}