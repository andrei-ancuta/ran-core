package ro.uti.ran.core.ws.internal.nomenclatoare;

import ro.uti.ran.core.service.exportNomenclatoare.data.summary.NomenclatorsSummary;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Created by Stanciu Neculai on 03.Dec.2015.
 */
@WebService
public interface NomenclatoareIntern {

    @WebMethod
    @WebResult(name = "nomenclatorsList")
    NomenclatorsSummary getListaNomenclatoare() throws RanException, RanRuntimeException;

    @WebMethod
    @WebResult(name = "nomenclator")
    byte[] getNomenclator(
            @WebParam(name = "codNomenclator") String codNomenclator
    ) throws RanException, RanRuntimeException;

}
