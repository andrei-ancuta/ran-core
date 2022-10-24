package ro.uti.ran.core.ws.internal.interogare;

import org.springframework.stereotype.Service;

import javax.jws.WebService;

/**
 * Created by Stanciu Neculai on 28.Oct.2015.
 */
@WebService(
        serviceName = "InterogareDateService",
        endpointInterface = "ro.uti.ran.core.ws.internal.interogare.InterogareDate",
        targetNamespace = "http://interogare.internal.ws.core.ran.uti.ro",
        portName = "InterogareDateServicePort")
@Service
public class InternalInterogareDateImpl extends InterogareDateImpl {
}
