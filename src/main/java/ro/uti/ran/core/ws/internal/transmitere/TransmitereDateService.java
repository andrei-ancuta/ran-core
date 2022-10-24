package ro.uti.ran.core.ws.internal.transmitere;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.model.transmitere.InformatiiTransmisie;
import ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere;

/**
 * Created by Stanciu Neculai on 13.Nov.2015.
 */
public interface TransmitereDateService {

    /**
     * @param xmlCDATA
     * @param modalitateTransmitere
     * @return idRegistru
     * @throws RanException
     * @throws RanRuntimeException
     */
    Long transmitere(String xmlCDATA, ModalitateTransmitere modalitateTransmitere, RanAuthorization ranAuthorization) throws RanBusinessException;

    /**
     * UTI obs: serviciul este intern, rezultatul schemei XSD poate fi un simplu String.
     * In exterior poate fi expus si compresat
     *
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    String getTransmitereXsdSchema(RanAuthorization ranAuthorization) throws RanBusinessException;


    /**
     * @param uuidTransmisie
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    InformatiiTransmisie getStatusTransmisie(String uuidTransmisie, RanAuthorization ranAuthorization) throws RanBusinessException;

}
