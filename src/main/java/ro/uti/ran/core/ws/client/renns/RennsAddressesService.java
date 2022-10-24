package ro.uti.ran.core.ws.client.renns;

import ro.uti.ran.core.ws.client.renns.addresses.GetAddressWSRequest;
import ro.uti.ran.core.ws.client.renns.addresses.GetAddressWSResponse;

import java.net.MalformedURLException;

/**
 * Created by bogdan.ardeleanu on 11/16/2016.
 */
public interface RennsAddressesService {
    GetAddressWSResponse findAddressByCua(GetAddressWSRequest request) throws MalformedURLException;
}
