package ro.uti.ran.core.ws.internal.parametru;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.handlers.annotation.NoMessageStore;
import ro.uti.ran.core.ws.internal.RanAuthorization;

import javax.jws.WebService;

import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-08 17:11
 */
@WebService(
        serviceName = "ParametruWebService",
        endpointInterface = "ro.uti.ran.core.ws.internal.parametru.ParametruService",
        targetNamespace = "http://parametru.internal.ws.core.ran.uti.ro",
        portName = "ParametruServicePort")
@Service("parametruService")
@NoMessageStore
public class ParametruServiceImpl implements ParametruService {

    @Autowired
    ro.uti.ran.core.service.parametru.ParametruService parametruService;

    @Override
    public Parametru getParametru(String codParametru) throws RanException, RanRuntimeException {
        return parametruService.getParametru(codParametru);
    }

    @Override
    public List<Parametru> getListaParametri() throws RanException, RanRuntimeException {
        return parametruService.getListaParametri();
    }

    @Override
    public Parametru salveazaParametru(Parametru parametru, RanAuthorization ranAuthorization) throws RanException, RanRuntimeException {

        //
        // todo: verificare authorizare
        //

        return parametruService.salveazaParametru(parametru);
    }
}
