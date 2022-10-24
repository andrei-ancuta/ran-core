package ro.ancpi.ran.core.ws.external.transmitere;

import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.ancpi.ran.core.ws.fault.RanException;
import ro.ancpi.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.model.transmitere.InformatiiTransmisie;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Serviciu de transmitere date in RAN.
 * <p/>
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-29 17:05
 */
@WebService
public interface TransmitereDateExtern {


    /**
     * @param xmlCompresat
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    void transmitere(
            @WebParam(name = "xmlCompresat") byte[] xmlCompresat,
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication) throws RanException, RanRuntimeException;

    /**
     * UTI obs: serviciul este intern, rezultatul schemei XSD poate fi un simplu String.
     * In exterior poate fi expus si compresat
     *
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    String getTransmitereXsdSchema(
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication
    ) throws RanException, RanRuntimeException;


    /**
     * @param uuidTransmisie
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    InformatiiTransmisie getStatusTransmisie(
            @WebParam(name = "uuidTransmisie") String uuidTransmisie,
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication
    ) throws RanException, RanRuntimeException;
}
