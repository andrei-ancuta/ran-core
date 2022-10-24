package ro.uti.ran.core.ws.internal.incarcare;

import com.sun.xml.ws.developer.StreamingAttachment;
import ro.uti.ran.core.service.registru.IncarcariSearchFilter;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.utilizator.UatConfig;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.ws.soap.MTOM;

/**
 * Serviciu de incarcare date bulk din portal prin intermediul portletului de incarcare date.
 * Datele sint transmise arhivat in format ZIP ce contine fisiere in format XML.
 *
 * Large Attachments:
 *  http://docs.oracle.com/cd/E14571_01/web.1111/e13734/mtom.htm#WSADV141
 *  https://metro.java.net/nonav/1.2/guide/Large_Attachments.html
 *
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 15:50
 */
// Configure such that whole MIME message is parsed eagerly,
// Attachments under 4MB are kept in memory
@MTOM
@StreamingAttachment(parseEagerly=true, memoryThreshold=4000000L)
@WebService
public interface IncarcareDate {

    /**
     * Operatie pentru incarcare date bulk in format ZIP.
     *
     * @param denumireFisier
     * @param continutFisier
     * @return
     * @throws Exception
     */
    @WebMethod
    RezultatIncarcare incarca(
            @WebParam(name="denumireFisier") String denumireFisier,
            @WebParam(name="continutFisier")
            @XmlMimeType("application/octet-stream") DataHandler continutFisier,
            @WebParam(name="numeUtilizator") String numeUtilizator,
            @WebParam(name="idUat") Long idUat
    ) throws RanException, RanRuntimeException;


    /**
     * Preluare lista incarcari
     *
     * @param searchFilter
     * @param pagingInfo
     * @param sortInfo
     * @return
     * @throws Exception
     */
    @WebMethod
    IncarcariListResult getListaIncarcari(
            @WebParam(name="searchFilter") IncarcariSearchFilter searchFilter,
            @WebParam(name="pagingInfo") PagingInfo pagingInfo,
            @WebParam(name="sortInfo") IncarcariSortInfo sortInfo
    ) throws RanException, RanRuntimeException;

    /**
     * Preluare Detalii incarcare
     *
     * @param idIncarcare
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    DetaliiIncarcare getDetaliiIncarcare(
            @WebParam(name = "idIncarcare") Long idIncarcare
    ) throws RanException, RanRuntimeException;


    /**
     * Preluare lista fisiere XML ce au fost incarcate cu un zip.
     *
     * @param idIncarcare
     * @param pagingInfo
     * @param sortInfo
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    RegistruPortalListResult getListaRegistruIncarcare(
            @WebParam(name="idIncarcare") Long idIncarcare,
            @WebParam(name="pagingInfo") PagingInfo pagingInfo,
            @WebParam(name="sortInfo") RegistruPortalSortInfo sortInfo
    ) throws RanException, RanRuntimeException;


    /**
     * Descarcare arhiva zip incarcata.
     *
     * @param idIncarcare
     * @return
     */
    @XmlMimeType("application/octet-stream")
    @WebMethod
    DataHandler descarcaIncarcare(
            @WebParam(name="idIncarcare") Long idIncarcare
    ) throws RanException, RanRuntimeException;


    /**
     * Descarcare continut fisier xml.
     *
     * @param idRegistru
     * @return
     */
    @XmlMimeType("application/octet-stream")
    @WebMethod
    DataHandler descarcaFisierXml(
            @WebParam(name="idRegistru") Long idRegistru
    ) throws RanException, RanRuntimeException;


    /**
     * Descarcare recipisa.
     *
     * @param idRegistru
     * @return
     */
    @XmlMimeType("application/octet-stream")
    @WebMethod
    DataHandler descarcaRecipisa(
            @WebParam(name="idRegistru") Long idRegistru
    ) throws RanException, RanRuntimeException;



    /**
     * Descarcare pachet raspuns incarcare.
     *
     * @param idIncarcare
     * @return
     */
    @XmlMimeType("application/octet-stream")
    @WebMethod
    DataHandler descarcaPachetRaspuns(
            @WebParam(name="idIncarcare") Long idIncarcare
    ) throws RanException, RanRuntimeException;


    @WebMethod
    UatConfig getUatConfig(
            @WebParam(name = "idUat") Long idUat
    ) throws RanException, RanRuntimeException;

}
