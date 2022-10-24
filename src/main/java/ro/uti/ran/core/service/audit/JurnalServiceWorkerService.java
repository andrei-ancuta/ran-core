package ro.uti.ran.core.service.audit;

import ro.uti.ran.core.model.portal.Context;
import ro.uti.ran.core.model.portal.OperatieSesiune;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.internal.audit.JurnalizareOperatiiList;
import ro.uti.ran.core.ws.internal.audit.OperatiiFilter;
import ro.uti.ran.core.ws.internal.audit.TipOperatiiList;

import java.util.List;

/**
 * Created by adrian.boldisor on 4/20/2016.
 */
public interface JurnalServiceWorkerService {

    JurnalizareOperatiiList getJurnalOperatii(SortInfo sortInfo, PagingInfo pagingInfo, OperatiiFilter operatiiFilter);

    TipOperatiiList getAllTipOperatie(SortInfo sortInfo);

    List<Context> getAppContextNames();

}
