package ro.uti.ran.core.ws.internal.registru;

import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.service.registru.RegistruSearchFilter;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Serviciu incarcare date registru.
 *
 * Se transmite continutul fisierului XML.
 *
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 16:01
 */
@WebService
public interface RegistruDate {

    /**
     * Preluare lista registru
     *
     * @param searchFilter
     * @param pagingInfo
     * @param sortInfo
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    RegistruListResult getListaRegistru(
            @WebParam(name = "searchFilter") RegistruSearchFilter searchFilter,
            @WebParam(name = "pagingInfo") PagingInfo pagingInfo,
            @WebParam(name = "sortInfo") SortInfo sortInfo) throws RanException, RanRuntimeException;
}
