package ro.uti.ran.core.ws.internal.nomenclator;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.service.nomenclator.NomenclatorSearchFilter;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 11:18
 */
@WebService
public interface NomenclatorWS {

    @WebMethod
    Institutie getInstitutieByCod(@WebParam(name = "codInstitutie") String codInstitutie);

    @WebMethod
    List<Institutie> getInstitutieByTipInstitutie(@WebParam(name = "fkNomTipInstitutie") Long fkNomTipInstitutie);


    @WebMethod
    InstitutieListResult getListaInstitii(
            @WebParam(name = "searchFilter") NomenclatorSearchFilter searchFilter,
            @WebParam(name = "pagingInfo") PagingInfo pagingInfo,
            @WebParam(name = "sortInfo") SortInfo sortInfo
    );

    @WebMethod
    UatListResult getListaUat(
            @WebParam(name = "searchFilter") NomenclatorSearchFilter searchFilter,
            @WebParam(name = "pagingInfo") PagingInfo pagingInfo,
            @WebParam(name = "sortInfo") SortInfo sortInfo
    );

    @WebMethod
    JudetListResult getListaJudete(
            @WebParam(name = "searchFilter") NomenclatorSearchFilter searchFilter,
            @WebParam(name = "pagingInfo") PagingInfo pagingInfo,
            @WebParam(name = "sortInfo") SortInfo sortInfo
    );


    @WebMethod
    Nomenclator getIntrareNomenclatorByIdOrNull(
            @WebParam(name = "tipNomenclator") TipNomenclator tipNomenclator,
            @WebParam(name = "idIntrareNomenclator") Long idIntrareNomenclator
    ) throws RanException, RanRuntimeException;

    @WebMethod
    Nomenclator getIntrareNomenclatorByIdOrThrow(
            @WebParam(name = "tipNomenclator") TipNomenclator tipNomenclator,
            @WebParam(name = "idIntrareNomenclator") Long idIntrareNomenclator
    ) throws RanException, RanRuntimeException;

    @WebMethod
    Nomenclator getIntrareNomenclatorByCodeOrNull(
            @WebParam(name = "tipNomenclator") TipNomenclator tipNomenclator,
            @WebParam(name = "codIntrareNomenclator") String codIntrareNomenclator
    ) throws RanException, RanRuntimeException;


    @WebMethod
    Nomenclator getIntrareNomenclatorByCodeOrThrow(
            @WebParam(name = "tipNomenclator") TipNomenclator tipNomenclator,
            @WebParam(name = "codIntrareNomenclator") String codIntrareNomenclator
    ) throws RanException, RanRuntimeException;

    /**
     * @param searchFilter
     * @param pagingInfo
     * @param sortInfo
     * @return
     */
    @WebMethod
    NomenclatorListResult getListaNomenclator(
            @WebParam(name = "searchFilter") NomenclatorSearchFilter searchFilter,
            @WebParam(name = "pagingInfo") PagingInfo pagingInfo,
            @WebParam(name = "sortInfo") SortInfo sortInfo
    ) throws RanException, RanRuntimeException;


    /**
     * Salvare intrare nomenclator
     *
     * @param intrareNomenclator
     * @return
     * @throws Exception
     */
    @WebMethod
    Nomenclator salveazaIntrareNomenclator(
            @WebParam(name = "intrareNomenclator") Nomenclator intrareNomenclator
    ) throws RanException, RanRuntimeException;



    /**
     * Salvare intrare nomenclator
     *
     * @param intrareNomenclator
     * @return
     * @throws Exception
     */
    @WebMethod
    Nomenclator inchidereVersiune(
            @WebParam(name = "intrareNomenclator")Nomenclator intrareNomenclator

    ) throws RanException, RanRuntimeException;


    /**
     * Logica verificare si pregatire intrare nomenclator pentru versionare.
     * Se returneaza instanta modificata (pregatita) pentru versiune noua, in baza de date nu se aplica modificari.
     *
     * @param tipNomenclator
     * @param idIntrareNomenclator
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    Nomenclator pregatireVersiuneIntrareNomenclator(
            @WebParam(name = "tipNomenclator") TipNomenclator tipNomenclator,
            @WebParam(name = "idIntrareNomenclator") Long idIntrareNomenclator
    ) throws RanException, RanRuntimeException;

    /**
     * Salveaza versiune noua nomenclator,
     * ID-ul entitatii reprezinta id-ul inregistrarii de vesionat (pe baza careia se va inregistra versiune noua).
     * Restul de campuri completate in entitate, reprezinta campurile versiunii noi.
     *
     * Se va realiza relationarea inregistrarilor (BASE_ID)
     *
     * @param intrareNomenclator
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    @WebMethod
    Nomenclator salveazaVersiuneIntrareNomenclator(
            @WebParam(name = "intrareNomenclator") Nomenclator intrareNomenclator
    ) throws RanException, RanRuntimeException;

    /**
     * Stergere intrare nomenclator
     *
     * @param idIntrareNomenclator
     * @param tipNomenclator
     * @throws Exception
     */
    @WebMethod
    void stergeIntrareNomenclator(
            @WebParam(name = "tipNomenclator") TipNomenclator tipNomenclator,
            @WebParam(name = "idIntrareNomenclator") Long idIntrareNomenclator
    ) throws RanException, RanRuntimeException;
}
