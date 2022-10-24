package ro.uti.ran.core.ws.internal.registru;

import ro.uti.ran.core.service.registru.IncarcariSearchFilter;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.incarcare.IncarcariListResult;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by Andreea on 11/3/2015.
 */
@WebService
public interface ITransmisiiService {
    /*
     afișează criteriile de filtrare a transmisiilor
     */

    @WebMethod
    TransmisieList getListaIncarcari(
            @WebParam(name="searchFilter") FiltruTransmisii searchFilter,
            @WebParam(name="pagingInfo") PagingInfo pagingInfo,
            @WebParam(name="sortInfo") SortInfo sortInfo
    ) throws RanException, RanRuntimeException;

}
