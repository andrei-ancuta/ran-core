package ro.uti.ran.core.ws.client.renns;

import org.springframework.beans.factory.InitializingBean;
import ro.uti.ran.core.ws.client.renns.roads.GetRoadWSRequest;
import ro.uti.ran.core.ws.client.renns.roads.GetRoadWSResponse;

import java.net.MalformedURLException;

/**
 * Created by bogdan.ardeleanu on 11/16/2016.
 */
public interface RennsRoadsService {
    GetRoadWSResponse getRoads(GetRoadWSRequest request) throws MalformedURLException;
}
