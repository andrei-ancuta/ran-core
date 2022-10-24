package ro.uti.ran.core.ws.internal.renns;

import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Serviciu de lucru cu IDM (OpenAM)
 * <p/>
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 15:40
 */
@WebService
public interface Renns {

    @WebMethod
    AdministratifAddress findAddressByCUA(
            @WebParam(name = "cua") String cua,
            @WebParam(name = "sirutaJudet") String sirutaJudet,
            @WebParam(name = "sirutaLocalitate") String sirutaLocalitate
    ) throws RanException, RanRuntimeException;
}
