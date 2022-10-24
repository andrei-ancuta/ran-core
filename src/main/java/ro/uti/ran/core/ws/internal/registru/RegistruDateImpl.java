package ro.uti.ran.core.ws.internal.registru;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.service.registru.RegistruSearchFilter;
import ro.uti.ran.core.service.registru.RegistruService;
import ro.uti.ran.core.utils.ListResultHelper;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import javax.jws.WebService;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 16:11
 */
@WebService(
        serviceName = "RegistruDateService",
        endpointInterface = "ro.uti.ran.core.ws.internal.registru.RegistruDate",
        targetNamespace = "http://registru.internal.ws.core.ran.uti.ro",
        portName = "RegistruDateServicePort")
@Service("registruDateService")
public class RegistruDateImpl implements RegistruDate {

    @Autowired
    private RegistruService registruService;


//    @Override
//    public Registru transmite(byte[] continut) throws RanException, RanRuntimeException {
//
//        Registru registru = new Registru();
//
//        //todo:
//        //registru.setContinut();
//
//        registru.setUat(null);
//
//
//        return registruService.saveRegistru(registru);
//    }

    @Override
    public RegistruListResult getListaRegistru(
            RegistruSearchFilter searchFilter,
            PagingInfo pagingInfo,
            SortInfo sortInfo) throws RanException, RanRuntimeException {

        return ListResultHelper.build(
                RegistruListResult.class,
                registruService.getListaRegistru(searchFilter, pagingInfo, sortInfo)
        );
    }
}
