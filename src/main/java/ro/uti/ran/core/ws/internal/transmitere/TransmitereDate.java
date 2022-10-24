package ro.uti.ran.core.ws.internal.transmitere;

import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.model.transmitere.InformatiiTransmisie;
import ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere;

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
public interface TransmitereDate {


    /**
     * @param xmlCDATA
     * @param modalitateTransmitere
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    void transmitere(
            @WebParam(name = "xmlCDATA") String xmlCDATA,
            @WebParam(name = "modalitateTransmitere") ModalitateTransmitere modalitateTransmitere,
            @WebParam(name = "ranAuthorization", targetNamespace = "core.ran.uti.ro", header = true) RanAuthorization ranAuthorization
    ) throws RanException, RanRuntimeException;

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
            @WebParam(name = "ranAuthorization", targetNamespace = "core.ran.uti.ro", header = true) RanAuthorization ranAuthorization
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
            @WebParam(name = "ranAuthorization", targetNamespace = "core.ran.uti.ro", header = true) RanAuthorization ranAuthorization
    ) throws RanException, RanRuntimeException;
}
