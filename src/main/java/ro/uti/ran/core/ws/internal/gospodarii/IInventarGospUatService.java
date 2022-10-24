package ro.uti.ran.core.ws.internal.gospodarii;

import ro.uti.ran.core.model.registru.InventarGospUat;
import ro.uti.ran.core.utils.SortInfo;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
@javax.jws.soap.SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.DOCUMENT, use = javax.jws.soap.SOAPBinding.Use.LITERAL, parameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.WRAPPED)
public interface IInventarGospUatService {

    @WebMethod
    InventarGospUatList getInventarGospodariiUat(final SortInfo sortInfo, Integer codSiruta);

    @WebMethod
    void updateOrCreate(InfoInventarGospUat infoInventarGospUat);

    @WebMethod
    InventarGospUat getByAnAndSiruta(Integer an, Integer codSiruta);

}
