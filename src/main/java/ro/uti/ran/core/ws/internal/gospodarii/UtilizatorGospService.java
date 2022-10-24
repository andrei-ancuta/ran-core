package ro.uti.ran.core.ws.internal.gospodarii;

import ro.uti.ran.core.service.gospodarii.GospodariiSearchFilter;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
import java.util.Map;

/**
 * Created by adrian.boldisor on 2/8/2016.
 */

@WebService
@javax.jws.soap.SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT, use = javax.jws.soap.SOAPBinding.Use.LITERAL, parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
public interface UtilizatorGospService {


    @WebMethod
    UtilizatoriGospList getUtilizatorGospList(SortInfo sortInfo, PagingInfo pagingInfo, Long idUtilizator, Long idUta);


//
//    @WebMethod
//    UtilizatoriGospList getByIdUserListForGospodariiPj(SortInfo sortInfo,PagingInfo pagingInfo, Long idUtilizator,  GospodariiSearchFilter searchFilter);

    @WebMethod
    UtilizatoriGospList getByIdUatUserListForGospodariiPj(SortInfo sortInfo,PagingInfo pagingInfo,Long idUser ,Long utaId, GospodariiSearchFilter searchFilter);

    @WebMethod
    Boolean deleteAsignedGospodariePj(Long IdUser, Long idGosp, Long utaId);


    @WebMethod
    Integer setGospodariePj(Long idUser, Long idGosp);




}
