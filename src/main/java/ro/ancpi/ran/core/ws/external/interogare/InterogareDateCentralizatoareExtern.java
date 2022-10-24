package ro.ancpi.ran.core.ws.external.interogare;

import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.ancpi.ran.core.ws.fault.RanException;
import ro.ancpi.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Created by smash on 24/11/15.
 */

@WebService
public interface InterogareDateCentralizatoareExtern {


    @WebMethod
    @WebResult(name = "xmlCompresat")
    byte[] getDateCapitolCentralizator(@WebParam(name = "an") Integer an,
                                       @WebParam(name = "codCapitol") String codCapitol,
                                       @WebParam(name = "sirutaUAT") Integer sirutaUAT,
                                       @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication)
            throws RanException, RanRuntimeException;



}
