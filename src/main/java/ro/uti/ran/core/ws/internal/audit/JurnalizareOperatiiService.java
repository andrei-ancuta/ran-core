package ro.uti.ran.core.ws.internal.audit;

import ro.uti.ran.core.model.portal.Context;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by adrian.boldisor on 4/20/2016.
 */


@WebService
@javax.jws.soap.SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT, use = javax.jws.soap.SOAPBinding.Use.LITERAL, parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
public interface JurnalizareOperatiiService {


    @WebMethod
    JurnalizareOperatiiList getJurnalOperatii(SortInfo sortInfo, PagingInfo pagingInfo,OperatiiFilter operatiiFilter);

    @WebMethod
    TipOperatiiList getAllTipOperatie();

    @WebMethod
    List<Context> getAppContextNames();

}
