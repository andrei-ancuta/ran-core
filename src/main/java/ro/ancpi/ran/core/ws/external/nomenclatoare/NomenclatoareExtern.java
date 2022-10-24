package ro.ancpi.ran.core.ws.external.nomenclatoare;

import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.ancpi.ran.core.ws.fault.RanException;
import ro.ancpi.ran.core.ws.fault.RanRuntimeException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.handler.MessageContext;

/**
 * Created by Anastasia cea micuta on 11/30/2015.
 */
@WebService
public interface NomenclatoareExtern {

    @WebMethod
    @WebResult(name = "xmlCompresat")
    byte[] getListaNomenclatoare(
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication
    ) throws RanException, RanRuntimeException;

    @WebMethod
    @WebResult(name = "xmlCompresat")
    byte[] getNomenclator(
            @WebParam(name = "codNomenclator") String codNomenclator,
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication
    ) throws RanException, RanRuntimeException;

    @WebMethod
    String getListaNomenclatoareXsdSchema(
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication
    ) throws RanException, RanRuntimeException;


    @WebMethod
    String getNomenclatorXsdSchema(
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication
    ) throws RanException, RanRuntimeException;

}
