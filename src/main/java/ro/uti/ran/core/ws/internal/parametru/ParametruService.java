package ro.uti.ran.core.ws.internal.parametru;

import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

/**
 * Serviciu acces parametri sistem
 * <p/>
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-08 17:00
 */
@WebService
public interface ParametruService {

    /**
     * Preluare parametru dupa cod.
     *
     * @param codParametru
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    Parametru getParametru(
            @WebParam(name = "codParametru") String codParametru
    ) throws RanException, RanRuntimeException;

    /**
     * Preluare lista parametri.
     *
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    List<Parametru> getListaParametri() throws RanException, RanRuntimeException;


    /**
     * Actualizare parametru.
     *
     * @param parametru
     * @param ranAuthorization
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    Parametru salveazaParametru(
            @WebParam(name = "parametru") Parametru parametru,
            @WebParam(name = "ranAuthorisation") RanAuthorization ranAuthorization
    ) throws RanException, RanRuntimeException;
}
