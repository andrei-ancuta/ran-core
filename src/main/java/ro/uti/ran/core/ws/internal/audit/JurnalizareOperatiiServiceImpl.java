package ro.uti.ran.core.ws.internal.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.portal.Context;
import ro.uti.ran.core.model.portal.TipOperatie;
import ro.uti.ran.core.service.audit.JurnalServiceWorkerService;
import ro.uti.ran.core.utils.Order;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortCriteria;
import ro.uti.ran.core.utils.SortInfo;

import javax.jws.WebService;
import java.util.List;

/**
 * Created by smash on 04/01/16.
 */

@WebService(
        serviceName = "JurnalizareOperatiiService",
        endpointInterface = "ro.uti.ran.core.ws.internal.audit.JurnalizareOperatiiService",
        targetNamespace = "http://info.internal.ws.core.ran.uti.ro",
        portName = "JurnalizareOperatiiServicePort")
@Service("JurnalizareOperatiiService")
public class JurnalizareOperatiiServiceImpl implements JurnalizareOperatiiService {

    @Autowired
    private JurnalServiceWorkerService jurnalServiceWorkerService;

    @Override
    public JurnalizareOperatiiList getJurnalOperatii(SortInfo sortInfo, PagingInfo pagingInfo, OperatiiFilter operatiiFilter) {

        if (sortInfo == null) {
            sortInfo = new SortInfo();
            SortCriteria sortCriteria = new SortCriteria("dataOperatie", Order.desc);
            sortInfo.getCriterias().add(sortCriteria);
        }
        JurnalizareOperatiiList operatieSesiune = jurnalServiceWorkerService.getJurnalOperatii(sortInfo, pagingInfo, operatiiFilter);

        return operatieSesiune;
    }


    @Override
    public TipOperatiiList getAllTipOperatie() {
        return jurnalServiceWorkerService.getAllTipOperatie(null);
    }

    @Override
    public List<Context> getAppContextNames() {
        return jurnalServiceWorkerService.getAppContextNames();
    }

}

