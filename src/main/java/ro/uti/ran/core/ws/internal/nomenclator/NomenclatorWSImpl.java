package ro.uti.ran.core.ws.internal.nomenclator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.model.portal.Judet;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.service.nomenclator.NomenclatorException;
import ro.uti.ran.core.service.nomenclator.NomenclatorExceptionCodes;
import ro.uti.ran.core.service.nomenclator.NomenclatorSearchFilter;
import ro.uti.ran.core.service.nomenclator.NomenclatorService;
import ro.uti.ran.core.service.portal.NomInstitutieService;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.ListResultHelper;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Arrays;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-16 11:19
 */
@WebService(
        serviceName = "NomenclatorService",
        endpointInterface = "ro.uti.ran.core.ws.internal.nomenclator.NomenclatorWS",
        targetNamespace = "http://nomenclator.internal.ws.core.ran.uti.ro",
        portName = "NomenclatorServicePort")
@Service("nomenclatorWS")
public class NomenclatorWSImpl implements NomenclatorWS {

    @Autowired
    NomenclatorService nomenclatorService;

    @Autowired
    NomInstitutieService nomInstitutieService;

    @Autowired
    private ExceptionUtil exceptionUtil;


    @Override
    public Institutie getInstitutieByCod(@WebParam(name = "codInstitutie") String codInstitutie) {
        return nomInstitutieService.getInstitutieByCod(codInstitutie);
    }

    @Override
    public List<Institutie> getInstitutieByTipInstitutie(@WebParam(name = "fkNomTipInstitutie") Long fkNomTipInstitutie) {
        return nomInstitutieService.getInstitutieByTipInstitutie(fkNomTipInstitutie);
    }

    @Override
    public InstitutieListResult getListaInstitii(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo) {
        GenericListResult<Institutie> institutii = nomenclatorService.getListaInstitii(searchFilter, pagingInfo, sortInfo);

        return ListResultHelper.build(InstitutieListResult.class, institutii);
    }

    @Override
    public UatListResult getListaUat(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo) {
        GenericListResult<UAT> uat = nomenclatorService.getListaUat(searchFilter, pagingInfo, sortInfo);

        return ListResultHelper.build(UatListResult.class, uat);
    }

    @Override
    public JudetListResult getListaJudete(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo) {
        GenericListResult<Judet> uat = nomenclatorService.getListaJudete(searchFilter, pagingInfo, sortInfo);

        return ListResultHelper.build(JudetListResult.class, uat);
    }

    @Override
    public Nomenclator getIntrareNomenclatorByIdOrNull(final TipNomenclator tipNomenclator, final Long idIntrareNomenclator) throws RanException, RanRuntimeException {
        return nomenclatorService.getNomenclator(new NomenclatorSearchFilter() {
            {
                setType(tipNomenclator);
                setOnlyFromIds(Arrays.asList(idIntrareNomenclator));
            }
        });
    }

    @Override
    public Nomenclator getIntrareNomenclatorByIdOrThrow(final TipNomenclator tipNomenclator, final Long idIntrareNomenclator) throws RanException, RanRuntimeException {
        Nomenclator nomenclator = nomenclatorService.getNomenclator(new NomenclatorSearchFilter() {
            {
                setType(tipNomenclator);
                setOnlyFromIds(Arrays.asList(idIntrareNomenclator));
                setShowHistoryRecords(true);
            }
        });

        //todo: definire exceptie de business
        if (nomenclator == null) {
            throw exceptionUtil.buildException(new RanRuntimeException("Not found"));
        }

        return nomenclator;
    }

    @Override
    public Nomenclator getIntrareNomenclatorByCodeOrNull(final TipNomenclator tipNomenclator, final String codIntrareNomenclator) throws RanException, RanRuntimeException {
        return nomenclatorService.getNomenclator(new NomenclatorSearchFilter() {
            {
                setType(tipNomenclator);
                setCod(codIntrareNomenclator);
            }
        });
    }

    @Override
    public Nomenclator getIntrareNomenclatorByCodeOrThrow(final TipNomenclator tipNomenclator, final String codIntrareNomenclator) throws RanException, RanRuntimeException {
        Nomenclator nomenclator = nomenclatorService.getNomenclator(new NomenclatorSearchFilter() {
            {
                setType(tipNomenclator);
                setCod(codIntrareNomenclator);
            }
        });

        //todo: definire exceptie de business
        if (nomenclator == null) {
            throw exceptionUtil.buildException(new RanRuntimeException("Not found"));
        }
        return nomenclator;
    }

    @Override
    public NomenclatorListResult getListaNomenclator(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo) {
        GenericListResult<Nomenclator> nomenclator = nomenclatorService.getListaNomenclator(
                searchFilter,
                pagingInfo,
                sortInfo
        );
        return ListResultHelper.build(NomenclatorListResult.class, nomenclator);
    }

    @Override
    public Nomenclator salveazaIntrareNomenclator(
            Nomenclator intrareNomenclator
    ) throws RanException, RanRuntimeException {
        try {
            return nomenclatorService.saveNomenclator(intrareNomenclator);
        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }


    @Override
    public Nomenclator inchidereVersiune(Nomenclator intrareNomenclator)
            throws RanException, RanRuntimeException {
        try {

            return nomenclatorService.inchidereVersiune(intrareNomenclator);
        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public Nomenclator pregatireVersiuneIntrareNomenclator(
            TipNomenclator tipNomenclator,
            Long idIntrareNomenclator
    ) throws RanException, RanRuntimeException {

        //todo: verificare existenta intrare nomenclator dupa id
        //todo: verificare intrare daca este versionabil
        //todo: verificare intrare daca indeplineste conditiile de versinoare (daca data stop este completata)
        //todo: pregatire versiune: data stop trece in data start, data stop devine null

        try {
            return nomenclatorService.pregatireVersiuneIntrareNomenclator(tipNomenclator, idIntrareNomenclator);
        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    //NU A FOST TESTATA
    @Override
    public Nomenclator salveazaVersiuneIntrareNomenclator(
            Nomenclator intrareNomenclator
    ) throws RanException, RanRuntimeException {

        //todo: identificare intrare noua de versionat dupa id intrareNomenclator
        //todo: creare instanta noua de acelasi tip
        //todo: copiere proprietati din parametru intrareNomenclator in instanta noua, exclusiv ID, BASE_ID
        //todo: pentru id se va seta null, pentru inserare, pentru base_id se va seta base_id al inregistrarii de versionat
        //todo:

        try {
            return nomenclatorService.salveazaVersiuneIntrareNomenclator(intrareNomenclator);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }

    }

    @Override
    public void stergeIntrareNomenclator(
            TipNomenclator tipNomenclator,
            Long idIntrareNomenclator
    ) throws RanException, RanRuntimeException {
        try {
            nomenclatorService.deleteNomenclator(tipNomenclator, idIntrareNomenclator);
        } catch (RanBusinessException rbe) {
            throw exceptionUtil.buildException(new RanException(rbe));
        } catch (org.springframework.dao.DataIntegrityViolationException dive) {
            throw exceptionUtil.buildException(new RanException(new NomenclatorException(NomenclatorExceptionCodes.NOMENCLATOR_REFERENCED_EXCEPTION)));
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }
}
