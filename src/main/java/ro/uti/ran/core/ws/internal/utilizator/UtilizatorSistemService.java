package ro.uti.ran.core.ws.internal.utilizator;

import java.math.BigDecimal;

import ro.uti.ran.core.model.portal.ContextSistem;
import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.model.portal.UATConfig;
import ro.uti.ran.core.service.registru.NumarSecventaNotFoundException;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.internal.UatRanAuthorization;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-15 17:02
 */
@WebService
public interface UtilizatorSistemService {


    @WebMethod
    Sistem getUtilizatorSistem(
            @WebParam(name = "searchFilter") UtilizatorSistemSearchFilter searchFilter
    ) throws RanException, RanRuntimeException;

    @WebMethod
    Sistem getUtilizatorSistemById(
            @WebParam(name = "idUtilizatorSistem") Long idUtilizatorSistem
    ) throws RanException, RanRuntimeException;

    @WebMethod
    UtilizatoriSistemListResult getUtilizatoriSistem(
            @WebParam(name = "searchFilter") UtilizatorSistemSearchFilter searchFilter,
            @WebParam(name = "pagingInfo") PagingInfo pagingInfo,
            @WebParam(name = "sortInfo") SortInfo sortInfo
    ) throws RanException, RanRuntimeException;

    @WebMethod
    String genereazaToken() throws RanException, RanRuntimeException;

    @WebMethod
    Sistem salveazaTokenUtilizatorSistem(
            @WebParam(name = "idEntity") Long idEntity,
            @WebParam(name = "context") ContextSistem context,
            @WebParam(name = "token") String token
    ) throws RanException, RanRuntimeException;


    @WebMethod
    UatConfig getUatConfig(
            @WebParam(name = "idUat") Long idUat
    ) throws RanException, RanRuntimeException;

    @WebMethod
    void saveUatConfig(
            @WebParam(name = "idUat") Long idUat,
            @WebParam(name = "uatConfig") UatConfig uatConfig
    ) throws RanException, RanRuntimeException;

    @WebMethod
    boolean canSwitchToManualTransmision(
            @WebParam(name = "idUat") Long idUat
    ) throws RanException, RanRuntimeException;
    
    @WebMethod
	Long getNumarCerere(
			@WebParam(name = "ranAuthorization", targetNamespace = "core.ran.uti.ro", header = true) UatRanAuthorization ranAuthorization
	) throws RanException, RanRuntimeException;
    
    @WebMethod
	Long vizualizeazaNumarCerere(
			@WebParam(name = "ranAuthorization", targetNamespace = "core.ran.uti.ro", header = true) UatRanAuthorization ranAuthorization
	) throws RanException, RanRuntimeException;
    
    @WebMethod
	void seteazaSecventa(
			@WebParam(name = "ranAuthorization", targetNamespace = "core.ran.uti.ro", header = true) UatRanAuthorization ranAuthorization, 
			@WebParam(name = "value") BigDecimal value
	) throws RanException, RanRuntimeException;

    @WebMethod
	void reseteazaSecventa(
			@WebParam(name = "ranAuthorization", targetNamespace = "core.ran.uti.ro", header = true) UatRanAuthorization ranAuthorization
	) throws RanException, RanRuntimeException;
    
}
