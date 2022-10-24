package ro.uti.ran.core.service.nomenclator;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;

import java.util.Date;

/**
 * Nomenclator business service
 *
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-02 14:59
 */
public interface NomenclatorService {

    /**
     * Cautare intrare nomenclator
     *
     * @param searchFilter
     * @return null daca nu exista intrare nomenclator dupa criteriile specificate
     */
    Nomenclator getNomenclator(NomenclatorSearchFilter searchFilter);

    /**
     * Preluare lista nomenclator
     *
     * @param searchFilter
     * @param pagingInfo
     * @param sortInfo
     * @return
     */
    GenericListResult<Nomenclator> getListaNomenclator(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo);

    /**
     * Salvare intrare nomenclator
     *
     * @param nomenclator
     * @return
     */
    Nomenclator saveNomenclator(Nomenclator nomenclator) throws RanBusinessException, RanException, RanRuntimeException;

    /**
     * Stergere intrare nomenclator
     *
     * @param tipNomenclator
     * @param idIntrareNomenclator
     */
    void deleteNomenclator(TipNomenclator tipNomenclator, Long idIntrareNomenclator) throws RanBusinessException, RanRuntimeException, RanException;

    /**
     * Inchidere versiune pentru intrarea de nomenclator
     *
     * @param intrareNomenclator
     * @throws RanBusinessException
     */
    Nomenclator inchidereVersiune(Nomenclator intrareNomenclator) throws RanBusinessException, RanRuntimeException, RanException;

    /**
     * Pregatire versiune pentru intrarea de nomenclator
     *
     * @param tipNomenclator
     * @throws RanBusinessException
     */
    Nomenclator pregatireVersiuneIntrareNomenclator(TipNomenclator tipNomenclator, Long idIntrareNomenclator) throws RanBusinessException, RanException, RanRuntimeException;

    /**
     * Salveaza versiune noua de intrare nomenclator
     *
     * @param intrareNomenclator
     * @return
     * @throws RanException
     * @throws RanRuntimeException
     */
    public Nomenclator salveazaVersiuneIntrareNomenclator( Nomenclator intrareNomenclator ) throws RanBusinessException, RanException, RanRuntimeException;

        /**
         *
         * @param searchFilter
         * @param pagingInfo
         * @param sortInfo
         * @return
         */
    GenericListResult<Institutie> getListaInstitii(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo);


    GenericListResult<UAT> getListaUat(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo);

    GenericListResult<Judet> getListaJudete(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo);

}
