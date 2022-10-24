package ro.uti.ran.core.service.gospodarii;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.audit.Audit;
import ro.uti.ran.core.model.registru.InventarGospUat;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.repository.base.AbstractRepositoryFilter;
import ro.uti.ran.core.repository.registru.IInventarGospUatRepository;
import ro.uti.ran.core.repository.registru.NomUatRepository;
import ro.uti.ran.core.utils.*;
import ro.uti.ran.core.ws.internal.gospodarii.InfoInventarGospUat;
import ro.uti.ran.core.ws.internal.gospodarii.InventarGospUatComparator;
import ro.uti.ran.core.ws.internal.gospodarii.InventarGospUatList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

import static ro.uti.ran.core.audit.AuditOpType.ACTUALIZARE_INVENTAR_GOSPODARII;

/**
 * Created by Anastasia cea micuta on 1/19/2016.
 */
@Service
@Transactional("registruTransactionManager")
public class InventarGospodariiUATServiceImpl implements InventarGospodariiUATService {
    @Autowired
    private IInventarGospUatRepository iInventarGospUatRepository;

    @Autowired
    private NomUatRepository nomUatRepository;

    @Override
    public InventarGospUatList getInventarGospodariiUat(SortInfo sortInfo, Integer codSiruta) {
        if (sortInfo == null) {
            sortInfo = new SortInfo();
            SortCriteria sortCriteria = new SortCriteria("an", Order.desc);
            sortInfo.getCriterias().add(sortCriteria);
        }
        GenericListResult<InventarGospUat> inventarGospUat = getLista(sortInfo, codSiruta);
        List<InventarGospUat> copyList = new ArrayList<>(inventarGospUat.getItems());
        Map<Integer, Boolean> isInList = new HashMap<>();
        //Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Integer currentYear = 2026;
        if (currentYear - 2021 >= 0) {
            for (int i = currentYear; i >= 2021; i--) {
                isInList.put(i, false);
                for (InventarGospUat inventarGospUat1 : inventarGospUat.getItems()) {
                    if (inventarGospUat1.getAn() == i) {
                        isInList.put(i, true);
                    }
                }
                if (isInList.get(i).equals(Boolean.FALSE)) {
                    InventarGospUat inventarGospUat1 = new InventarGospUat();
                    NomUat nomUat = new NomUat();
                    inventarGospUat1.setAn(i);
                    nomUat.setCodSiruta(codSiruta);
                    inventarGospUat1.setNomUat(nomUat);
                    copyList.add(inventarGospUat1);
                }
            }
        }
        Collections.sort(copyList, Collections.reverseOrder(new InventarGospUatComparator()));
        inventarGospUat.setItems(copyList);

        return ListResultHelper.build(InventarGospUatList.class, inventarGospUat,
                new ListResultHelper.Mapper<InventarGospUat, InfoInventarGospUat>() {
                    @Override
                    public InfoInventarGospUat map(InventarGospUat source) {
                        InfoInventarGospUat infoInventarGospUat = new InfoInventarGospUat(source);
                        BeanUtils.copyProperties(infoInventarGospUat, source);
                        return infoInventarGospUat;
                    }
                });
    }


    @Override
    public InventarGospUat getByAnAndSiruta(Integer an, Integer codSiruta) {
        return iInventarGospUatRepository.findByAnAndNomUatCodSiruta(an, codSiruta);
    }

    @Audit(opType = ACTUALIZARE_INVENTAR_GOSPODARII)
    @Override
    public void updateOrCreate(InfoInventarGospUat infoInventarGospUat) {
        if (infoInventarGospUat.getId() != null) {
            /**
             * is update
             */
            Calendar now = Calendar.getInstance();
            now.set(Calendar.SECOND, 0);

            InventarGospUat inventarGospUat = iInventarGospUatRepository.findOne(infoInventarGospUat.getId());
            inventarGospUat.setValoare(infoInventarGospUat.getValoare());
            inventarGospUat.setLastModifiedDate(now.getTime());
            iInventarGospUatRepository.save(inventarGospUat);
        } else {
            /**
             * is create
             */
            InventarGospUat inventarGospUat = new InventarGospUat();
            inventarGospUat.setAn(infoInventarGospUat.getAn());
            inventarGospUat.setNomUat(nomUatRepository.findByCodSiruta(infoInventarGospUat.getCodSiruta()));
            inventarGospUat.setValoare(infoInventarGospUat.getValoare());
            inventarGospUat.setLastModifiedDate(new Date());
            iInventarGospUatRepository.save(inventarGospUat);
        }
    }

    private GenericListResult<InventarGospUat> getLista(final SortInfo sortInfo, final Integer codSiruta) {
        return iInventarGospUatRepository.getListResult(
                new AbstractRepositoryFilter<InventarGospUat>() {
                    public Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery cq, Root from) {
                        if (codSiruta != null) {
                            addPredicate(cb.equal(from.get("nomUat").get("codSiruta"), codSiruta));
                        }
                        return predicatesArray();
                    }
                }, null, sortInfo);
    }

}
