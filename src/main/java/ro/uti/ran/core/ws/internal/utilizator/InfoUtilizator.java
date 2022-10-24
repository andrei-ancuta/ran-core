package ro.uti.ran.core.ws.internal.utilizator;

import ro.uti.ran.core.model.portal.Context;
import ro.uti.ran.core.model.portal.RolUtilizator;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 15:37
 */
@WebService
public interface InfoUtilizator {

    /**
     * Preluare roluri utilizator
     * @param numeUtilizator
     * @param codContext
     * @return
     * @throws Exception
     */
//    @WebMethod
//    List<RolUtilizator> getRoluriUtilizator(
//            @WebParam(name = "numeUtilizator") String numeUtilizator,
//            @WebParam(name = "codContext") String codContext
//    ) throws RanException, RanRuntimeException;

    @WebMethod
    List<Rol> getRoluriUtilizator(
            @WebParam(name = "numeUtilizator") String numeUtilizator,
            @WebParam(name = "codContext") String codContext
    ) throws RanException, RanRuntimeException;

    /**
     * Preluare detalii utilizator
     *
     * @param numeUtilizator
     * @return
     * @throws Exception
     */
    @WebMethod
    DetaliiUtilizator getDetaliiUtilizator(
            @WebParam(name = "numeUtilizator") String numeUtilizator
    ) throws RanException, RanRuntimeException;

    /**
     * Preluare contexte utilizator la care are acces functie de rolurile asignate
     * @param numeUtilizator
     * @return
     * @throws Exception
     */
    @WebMethod
    List<Context> getContexteUtilizator(
            @WebParam(name = "numeUtilizator") String numeUtilizator
    ) throws RanException, RanRuntimeException;
}
