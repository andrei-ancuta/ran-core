package ro.uti.ran.core.ws.internal.registru;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.repository.base.AbstractRepositoryFilter;
import ro.uti.ran.core.repository.registru.RegistruRepository;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorService;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.utils.*;
import ro.uti.ran.core.utils.Order;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import javax.jws.WebService;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Andreea on 11/3/2015.
 */
@WebService(
        serviceName = "TransmisiiService",
        endpointInterface = "ro.uti.ran.core.ws.internal.registru.ITransmisiiService",
        targetNamespace = "http://info.internal.ws.core.ran.uti.ro",
        portName = "TransmisiiServicePort")
@Service("transmisiiService")
public class TransmisiiServiceImpl implements ITransmisiiService {

    private Logger logger = LoggerFactory.getLogger(TransmisiiServiceImpl.class);

    @Autowired
    private RegistruRepository registruRepository;

    @Autowired
    private NomenclatorService nomSrv;

    @Autowired
    private ExceptionUtil exceptionUtil;

    @Override
    public TransmisieList getListaIncarcari(FiltruTransmisii searchFilter, PagingInfo pagingInfo, SortInfo sortInfo) throws RanException, RanRuntimeException {
        final TransmisieList rezult = new TransmisieList();
        final List<Transmisie> items = new ArrayList<Transmisie>();
        try {
            /*implicit ordonat dupa data_registru descrescator*/
            if (sortInfo == null) {
                sortInfo = new SortInfo();
                SortCriteria sortCriteria = new SortCriteria("idRegistru", Order.desc);
                sortInfo.getCriterias().add(sortCriteria);
            }
            GenericListResult<Registru> incarcari = getLista(searchFilter, pagingInfo, sortInfo);

            return ListResultHelper.build(
                    TransmisieList.class,
                    incarcari,
                    new ListResultHelper.Mapper<Registru, Transmisie>() {
                        @Override
                        public Transmisie map(Registru source) {
                            Transmisie incarcare = new Transmisie(source);
                            BeanUtils.copyProperties(source, incarcare);
                            return incarcare;
                        }
                    });

        } catch (Throwable th) {
            logger.error(th.getMessage(), th);
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    private GenericListResult<Registru> getLista(final FiltruTransmisii filtruTransmisii, PagingInfo pagingInfo, SortInfo sortInfo) {

        final FiltruTransmisii searchFilter = (filtruTransmisii == null) ? new FiltruTransmisii() : filtruTransmisii;

        return registruRepository.getListResultTransmisii(
                new AbstractRepositoryFilter<Registru>() {
                    @Override
                    public Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery cq, Root from) {

                        if (searchFilter.getIdUatUtilizatorLogat() != null) {
                            NomUat nomUatUtilizatorLogat = nomSrv.getNomenlatorForId(NomenclatorCodeType.NomUat, searchFilter.getIdUatUtilizatorLogat());
                            addPredicate(cb.equal(from.get("nomUat").get("baseId"), nomUatUtilizatorLogat.getBaseId()));
                        }

                        if (searchFilter.getBaseIdUat() != null) {
                            addPredicate(cb.equal(from.get("nomUat").get("baseId"), searchFilter.getBaseIdUat()));
                        }

                        if (searchFilter.getCodSirutaUat() != null) {
                            addPredicate(cb.equal(from.get("nomUat").get("codSiruta"), searchFilter.getCodSirutaUat()));
                        }

                        if (searchFilter.getModalitateTransmitere() != null) {
                            addPredicate(cb.equal(from.get("modalitateTransmitere"), searchFilter.getModalitateTransmitere()));
                        }

                        if (searchFilter.getFkNomJudet() != null) {
                            addPredicate(cb.equal(from.get("fkNomJudet"), searchFilter.getFkNomJudet()));
                        }

                        if (StringUtils.isNotEmpty(searchFilter.getCodStareRegistru())) {
                            addPredicate(cb.equal(from.get("nomStareRegistru").get("cod"), searchFilter.getCodStareRegistru()));
                        }
                        if (StringUtils.isNotEmpty(searchFilter.getIndexRegistru())) {
                            addPredicate(cb.equal(from.get("indexRegistru"), searchFilter.getIndexRegistru().toLowerCase()));
                        }
                        if (StringUtils.isNotEmpty(searchFilter.getIdentificatorGospodarie())) {
                            addPredicate(cb.equal(cb.lower(from.get("identificatorGospodarie")), searchFilter.getIdentificatorGospodarie().toLowerCase()));
                        }
                        if (searchFilter.getCodCapitol() != null && !searchFilter.getCodCapitol().isEmpty()) {
                            List<TipCapitol> tip = new ArrayList<TipCapitol>();
                            for (String cod : searchFilter.getCodCapitol()) {
                                try {
                                    tip.add(TipCapitol.checkTipCapitol(cod));
                                } catch (RequestValidationException e) {
                                    logger.error(e.getMessage(), e);
                                }
                            }
                            addPredicate(from.join("nomCapitol").get("cod").in(tip));
                        }
                        if (searchFilter.getDataRegistruDeLa() != null) {
                            addPredicate(cb.greaterThanOrEqualTo(from.get("dataRegistru"), searchFilter.getDataRegistruDeLa()));
                        }
                        if (searchFilter.getDataRegistruPanaLa() != null) {
                            addPredicate(cb.lessThanOrEqualTo(from.get("dataRegistru"), searchFilter.getDataRegistruPanaLa()));
                        }

                        if (StringUtils.isNotEmpty(searchFilter.getCodNomIndicativXml())) {
                            addPredicate(cb.equal(from.get("nomIndicativXml").get("cod"), searchFilter.getCodNomIndicativXml()));
                        }
                        return predicatesArray();
                    }
                },
                pagingInfo, sortInfo
        );
    }
}
