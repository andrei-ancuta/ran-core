package ro.ancpi.ran.core.ws.external.interogare;

import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.ancpi.ran.core.ws.fault.RanException;
import ro.ancpi.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.model.interogare.IdentificatorGospodarie;
import ro.uti.ran.core.ws.model.interogare.IdentificatorPF;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anastasia cea micuta on 10/11/2015.
 */
@WebService
public interface InterogareDateExtern {

    /**
     * Preluare lista de gospodarii in proprietatea unei persoane fizice
     *
     * @param identificatorPF
     * @param ranAuthentication
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    ArrayList<IdentificatorGospodarie> getListaGospodariiPF(
            @WebParam(name = "identificatorPF") IdentificatorPF identificatorPF,
            @WebParam(name = "activ") Boolean activ,
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication
    ) throws RanException, RanRuntimeException;

    /**
     * Preluare lista de gospodarii in proprietatea unei persoane juridice
     *
     * @param cui
     * @param ranAuthentication
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    List<IdentificatorGospodarie> getListaGospodariiPJ(
            @WebParam(name = "cui") String cui,
            @WebParam(name = "activ") Boolean activ,
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication
    ) throws RanException, RanRuntimeException;


    /**
     * Interogare capitol RA pe baza de identificator de gospodarie
     *
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    @WebResult(name = "xmlCompresat")
    byte[] getDateCapitol(
            @WebParam(name = "idGospodarie") String idGospodarie,
            @WebParam(name = "sirutaUAT") Integer sirutaUAT,
            @WebParam(name = "codCapitol") String codCapitol,
            @WebParam(name = "an") Integer an,
            @WebParam(name = "semestru") Integer semestru,
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication
    ) throws RanException, RanRuntimeException;

    /**
     * Interogare capitol RA pe baza de identificator UAT si CNP persoana
     *
     * @param sirutaUAT
     * @param ranAuthentication
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    @WebResult(name = "xmlCompresat")
    byte[] getDateCapitolPF(
            @WebParam(name = "identificatorPF") IdentificatorPF identificatorPF,
            @WebParam(name = "sirutaUAT") Integer sirutaUAT,
            @WebParam(name = "codCapitol") String codCapitol,
            @WebParam(name = "an") Integer an,
            @WebParam(name = "semestru") Integer semestru,
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication
    ) throws RanException, RanRuntimeException;

    /**
     * Interogare capitol RA pe baza de identificator UAT si CUI persoana juridica
     *
     * @param cui
     * @param sirutaUAT
     * @param ranAuthentication
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    @WebResult(name = "xmlCompresat")
    byte[] getDateCapitolPJ(
            @WebParam(name = "cui") String cui,
            @WebParam(name = "sirutaUAT") Integer sirutaUAT,
            @WebParam(name = "codCapitol") String codCapitol,
            @WebParam(name = "an") Integer an,
            @WebParam(name = "semestru") Integer semestru,
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication
    ) throws RanException, RanRuntimeException;


    /**
     * UTI obs: serviciul este intern, rezultatul schemei XSD poate fi un simplu String.
     * In exterior poate fi expus si compresat
     *
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     * @param ranAuthentication
     */
    @WebMethod
    String getInterogareXsdSchema(
            @WebParam(name = "ranAuthentication", targetNamespace = "core.ran.ancpi.ro", header = true) RanAuthentication ranAuthentication
    ) throws RanException, RanRuntimeException;

}
