package ro.uti.ran.core.service.audit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.portal.Context;
import ro.uti.ran.core.model.portal.OperatieSesiune;
import ro.uti.ran.core.model.portal.TipOperatie;
import ro.uti.ran.core.repository.base.AbstractRepositoryFilter;
import ro.uti.ran.core.repository.portal.ContextRepository;
import ro.uti.ran.core.repository.portal.OperatieSesiuneRepository;
import ro.uti.ran.core.repository.portal.TipOperatieRepository;
import ro.uti.ran.core.utils.*;
import ro.uti.ran.core.ws.internal.audit.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by adrian.boldisor on 4/20/2016.
 */


@Service
@Transactional
public class JurnalServiceWorkerServiceImpl implements JurnalServiceWorkerService {


    @Autowired
    private OperatieSesiuneRepository operatieSesiuneRepository;

    @Autowired
    private TipOperatieRepository tipOperatieRepository;

    @Autowired
    private ContextRepository contextRepository;


    public JurnalizareOperatiiList getJurnalOperatii(SortInfo sortInfo, PagingInfo pagingInfo, OperatiiFilter operatiiFilter) {
        GenericListResult<OperatieSesiune> operatieSesiune = getLista(sortInfo, pagingInfo, operatiiFilter);

        return ListResultHelper.build(JurnalizareOperatiiList.class, operatieSesiune,
                new ListResultHelper.Mapper<OperatieSesiune, JurnalizareOperatieDetalii>() {
                    @Override
                    public JurnalizareOperatieDetalii map(OperatieSesiune source) {
                        JurnalizareOperatieDetalii jurnalizareOperatieDetalii = new JurnalizareOperatieDetalii(source);
                        BeanUtils.copyProperties(jurnalizareOperatieDetalii, source);
                        return jurnalizareOperatieDetalii;
                    }
                });
    }


    public TipOperatiiList getAllTipOperatie(SortInfo sortInfo) {

        if (sortInfo == null) {
            sortInfo = new SortInfo();
            SortCriteria sortCriteria = new SortCriteria("denumire", Order.asc);
            sortInfo.getCriterias().add(sortCriteria);
        }

        GenericListResult<TipOperatie> tipOperatieList = tipOperatieRepository.getListResult(
                new AbstractRepositoryFilter<TipOperatie>() {
                    @Override
                    public Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery cq, Root from) {
                        addPredicate(cb.equal(from.get("isAutomata"), 1));
                        return predicatesArray();
                    }
                }, null, sortInfo

        );

        return ListResultHelper.build(TipOperatiiList.class, tipOperatieList,
                new ListResultHelper.Mapper<TipOperatie, TipOperatieDetalii>() {
                    @Override
                    public TipOperatieDetalii map(TipOperatie source) {
                        TipOperatieDetalii tipOperatieDetalii = new TipOperatieDetalii(source);
                        BeanUtils.copyProperties(tipOperatieDetalii, source);
                        return tipOperatieDetalii;
                    }
                });
    }


    public List<Context> getAppContextNames() {
        return contextRepository.findAll();
    }


    private GenericListResult<OperatieSesiune> getLista(final SortInfo sortInfo, final PagingInfo pagingInfo, final OperatiiFilter operatiiFilter) {

        return operatieSesiuneRepository.getListResult(
                new AbstractRepositoryFilter<OperatieSesiune>() {
                    public Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery cq, Root from) {

                        if (StringUtils.isNotEmpty(operatiiFilter.getUtilizator())) {
                            addLikePredicate(cb, from.get("sesiune").get("utilizator").get("numeUtilizator"), operatiiFilter.getUtilizator());
                        }

                        if (StringUtils.isNotEmpty(operatiiFilter.getUidSesiuneHttp())) {
                            addPredicate(cb.equal(from.get("sesiune").get("uidSesiuneHttp"),operatiiFilter.getUidSesiuneHttp()));
                        }

                        if (StringUtils.isNotEmpty(operatiiFilter.getDenumireContext())) {
                            addLikePredicate(cb, from.get("sesiune").get("context").get("denumire"), operatiiFilter.getDenumireContext());
                        }

                        if (StringUtils.isNotEmpty(operatiiFilter.getAdresaIp())) {
                            addLikePredicate(cb, from.get("sesiune").get("adresaIp"), operatiiFilter.getAdresaIp());
                        }


                        if (operatiiFilter.getTipOperatie() != null) {
                            addPredicate(cb.equal(from.get("tipOperatie").get("id"), operatiiFilter.getTipOperatie()));
                        }

                        if (operatiiFilter.getStartDataOperatie() != null && operatiiFilter.getEndtDataOperatie() != null) {
                            addPredicate(cb.between(from.get("dataOperatie"), operatiiFilter.getStartDataOperatie(), operatiiFilter.getEndtDataOperatie()));
                        }

                        return predicatesArray();
                    }
                }, pagingInfo, sortInfo);
    }


}
