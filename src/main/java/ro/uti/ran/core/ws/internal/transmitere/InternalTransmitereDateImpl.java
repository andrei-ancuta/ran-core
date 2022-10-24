package ro.uti.ran.core.ws.internal.transmitere;

import org.springframework.stereotype.Service;

import javax.jws.WebService;

/**
 * Created by Stanciu Neculai on 28.Oct.2015.
 */

@WebService(
        serviceName = "TransmitereDateService",
        endpointInterface = "ro.uti.ran.core.ws.internal.transmitere.TransmitereDate",
        targetNamespace = "http://transmitere.internal.ws.core.ran.uti.ro",
        portName = "TransmitereDateServicePort")
@Service
public class InternalTransmitereDateImpl extends TransmitereDateImpl {
}
