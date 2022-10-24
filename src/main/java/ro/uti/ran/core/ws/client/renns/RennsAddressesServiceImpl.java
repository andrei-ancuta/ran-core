package ro.uti.ran.core.ws.client.renns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.ws.client.renns.addresses.AddressWS1Port;
import ro.uti.ran.core.ws.client.renns.addresses.AddressWS1PortService;
import ro.uti.ran.core.ws.client.renns.addresses.GetAddressWSRequest;
import ro.uti.ran.core.ws.client.renns.addresses.GetAddressWSResponse;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bogdan.ardeleanu on 11/16/2016.
 */
@Service
public class RennsAddressesServiceImpl implements RennsAddressesService {

    @Autowired
    private ParametruService parametruService;

    @Override
    public GetAddressWSResponse findAddressByCua(GetAddressWSRequest request) throws MalformedURLException {
        //
        StringBuilder url = new StringBuilder(parametruService.getParametru(ParametruService.WS_RENNS_URL_COD).getValoare());
        url.append("addressWS1.wsdl");
        //
        AddressWS1PortService rennsAddresses = new AddressWS1PortService((new URL(url.toString())));
        AddressWS1Port rennsAddressesPort = rennsAddresses.getAddressWS1PortSoap11();
        GetAddressWSResponse response = rennsAddressesPort.getAddressWS(request);
        return response;
    }
}
