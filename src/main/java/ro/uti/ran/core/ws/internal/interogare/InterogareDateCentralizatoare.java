package ro.uti.ran.core.ws.internal.interogare;

import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Created by smash on 24/11/15.
 */

@WebService
public interface InterogareDateCentralizatoare {


    @WebMethod
    @WebResult(name = "xmlCDATA")
    String getDateCapitolCentralizator(@WebParam(name = "an") Integer an,
                                       @WebParam(name = "codCapitol") String codCapitol,
                                       @WebParam(name = "sirutaUAT") Integer sirutaUAT,
                                       @WebParam(name = "ranAuthorization", targetNamespace = "core.ran.uti.ro", header = true) RanAuthorization ranAuthorization)
            throws RanException, RanRuntimeException;


}
