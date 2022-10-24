package ro.uti.ran.core.ws.internal.interogare;

import org.springframework.stereotype.Service;

import javax.jws.WebService;

/**
 * Created by smash on 24/11/15.
 */

@WebService(
        serviceName = "InterogareCentralizatoare",
        endpointInterface = "ro.uti.ran.core.ws.internal.interogare.InterogareDateCentralizatoare",
        targetNamespace = "http://interogare.internal.ws.core.ran.uti.ro",
        portName = "InterogareDateServicePort")
@Service
public class InternalInterogareDateCentralizatoareImpl extends InterogareDateCentralizatoareImpl {

}
