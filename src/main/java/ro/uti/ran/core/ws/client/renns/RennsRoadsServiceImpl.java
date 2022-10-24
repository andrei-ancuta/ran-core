package ro.uti.ran.core.ws.client.renns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.ws.client.renns.roads.GetRoadWSRequest;
import ro.uti.ran.core.ws.client.renns.roads.GetRoadWSResponse;
import ro.uti.ran.core.ws.client.renns.roads.RoadsPort;
import ro.uti.ran.core.ws.client.renns.roads.RoadsPortService;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bogdan.ardeleanu on 11/16/2016.
 */
@Service
public class RennsRoadsServiceImpl  implements RennsRoadsService {

    @Autowired
    private ParametruService parametruService;

    @Override
    public GetRoadWSResponse getRoads(GetRoadWSRequest request) throws MalformedURLException {
        //
        StringBuilder url = new StringBuilder(parametruService.getParametru(ParametruService.WS_RENNS_URL_COD).getValoare());
        url.append("roads.wsdl");
        //
        RoadsPortService rennsRoads = new RoadsPortService(new URL(url.toString()));
        RoadsPort rennsRoadsPort = rennsRoads.getRoadsPortSoap11();
        GetRoadWSResponse response = rennsRoadsPort.getRoadWS(request);
        return response;
    }
}
