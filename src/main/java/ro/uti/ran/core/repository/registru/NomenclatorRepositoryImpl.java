package ro.uti.ran.core.repository.registru;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Repository;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.model.Editable;
import ro.uti.ran.core.model.Versioned;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.repository.criteria.Operation;
import ro.uti.ran.core.repository.criteria.OperationNotSupportedException;
import ro.uti.ran.core.repository.criteria.ValueTypeNotSupportedException;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.service.nomenclator.NomenclatorSearchCriteria;
import ro.uti.ran.core.service.nomenclator.NomenclatorSearchFilter;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortCriteria;
import ro.uti.ran.core.utils.SortInfo;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-02 15:30
 */
@Repository
public class NomenclatorRepositoryImpl implements NomenclatorRepository {

    @PersistenceContext(unitName = "ran-registru")
    private EntityManager em;

    @Override
    public void alterSessionNlsSortComp() {
        em.createNativeQuery("ALTER SESSION SET NLS_SORT=GENERIC_M_AI").executeUpdate();
        em.createNativeQuery("ALTER SESSION SET NLS_COMP=LINGUISTIC").executeUpdate();
    }

    @Override
    public Nomenclator getLastVersion(Class<? extends Nomenclator> clazz, Long baseID) {
        return (Nomenclator) em.createQuery(
                "select n from " + clazz.getName() + " n where n.baseId = :baseId order by n.dataStart desc"
        ).setParameter("baseId", baseID)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Override
    public boolean checkNewVersion(Class<? extends Nomenclator> clazz, Long baseID, Date dataStart) {
        String dataString = DateFormatUtils.format(dataStart, "yyyy-MM-dd");
        Metamodel meta = em.getMetamodel();
        EntityType<?> entityType = meta.entity(clazz);
        Table t = clazz.getAnnotation(Table.class);
        String tableName = (t == null)
                ? entityType.getName().toUpperCase()
                : t.name();
        List check = em.createNativeQuery(
                "select * from " + tableName + " n where " +
                        "n.BASE_ID = " + baseID.toString() + " and " +
                        "(n.DATA_STOP is null " +
                        "or to_date(to_char(n.DATA_START,'YYYY-MM-DD'),'YYYY-MM-DD') " +
                        ">=to_date ('" + dataString + "','YYYY-MM-DD') " +
                        ")"
        ).getResultList();
        if (check.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean chekLatestVersion(Class<? extends Nomenclator> clazz, Long baseID, Date dataStop) {
        List haveNullDataStop = em.createQuery("select n from " + clazz.getName() + " n where n.dataStop is null and n.baseId=:baseId")
                .setParameter("baseId", baseID)
                .getResultList();
        if (haveNullDataStop.size() > 0) {
            return false;
        } else {
            Object maxDataStop = em.createQuery("select max(n.dataStop) from " + clazz.getName() + " n  where n.baseId = :baseId")
                    .setParameter("baseId", baseID)
                    .getSingleResult();
            return dataStop.getTime() >= ((Date) maxDataStop).getTime();
        }
    }

    @Override
    public String checkInsert(Class<? extends Nomenclator> clazz, Nomenclator inputNomenclator) {
        String queryS = "";
        if (inputNomenclator instanceof Editable) {
            //nomenclatoare fara data start se cauta existenta
            Editable nE = (Editable) inputNomenclator;
            if (nE.getStarting() == null) {
                queryS = "select n from " + clazz.getName() + " n where n." + (nE.getCodePrimName() != null ? nE.getCodePrimName() + " = :codP" : "") + (nE.getCodePrimName() == null ? nE.getCodeSecName() + " = :codS" : (nE.getCodeSecName() != null ? " and n." + nE.getCodeSecName() + " = :codS" : ""));
                Query queryExec = em.createQuery(queryS);
                if (nE.getCodePrimName() != null) {
                    queryExec.setParameter("codP", nE.getCodePrim());
                }
                if (nE.getCodeSecName() != null) {
                    queryExec.setParameter("codS", nE.getCodeSec());
                }
                List<Nomenclator> foundNomenclator = queryExec.getResultList();
                if (foundNomenclator.size() > 0) {
                    return "Nu se respecta conditia unicitate pentru nomenclator";
                } else {
                    return "Ok";
                }
            } else {
                //Nomenclatoare restrictionate de dataStop
                //e nomenclator nou success si iesire
                queryS = "select n from " + clazz.getName() + " n where n." + (nE.getCodePrimName() != null ? nE.getCodePrimName() + " = :codP" : "") + (nE.getCodePrimName() == null ? nE.getCodeSecName() + " = :codS" : (nE.getCodeSecName() != null ? " and n." + nE.getCodeSecName() + " = :codS" : ""));
                Query queryExec = em.createQuery(queryS);
                if (nE.getCodePrimName() != null) {
                    queryExec.setParameter("codP", nE.getCodePrim());
                }
                if (nE.getCodeSecName() != null) {
                    queryExec.setParameter("codS", nE.getCodeSec());
                }
                List<Nomenclator> foundNomenclator = queryExec.getResultList();
                if (foundNomenclator.size() == 0) {
                    return "Ok";
                } else {
                    //daca exista nomenclator cu DataStop necomplectat nu se permite adaugarea
                    //pentru instanta versionabila setam baseId cu BaseId din primul element lista returnata
                    if (inputNomenclator instanceof Versioned) {
                        ((Versioned) inputNomenclator).setBaseId(((Versioned) foundNomenclator.get(0)).getBaseId());
                    }
                    queryS = "select n from " + clazz.getName() + " n where n." + (nE.getCodePrimName() != null ? nE.getCodePrimName() + " = :codP" : "") + (nE.getCodePrimName() == null ? nE.getCodeSecName() + " = :codS" : (nE.getCodeSecName() != null ? " and n." + nE.getCodeSecName() + " = :codS" : "")) + " and n.dataStop is null";
                    queryExec = em.createQuery(queryS);
                    if (nE.getCodePrimName() != null) {
                        queryExec.setParameter("codP", nE.getCodePrim());
                    }
                    if (nE.getCodeSecName() != null) {
                        queryExec.setParameter("codS", nE.getCodeSec());
                    }
                    foundNomenclator = queryExec.getResultList();
                    if (foundNomenclator.size() > 0) {
                        return "Exista nomenclator valabil, nu se permite adaucare nomenclator cu asemenea parametri";
                    } else {
                        //Cautam dataStart maximala
                        queryS = "select max(n.dataStop) from " + clazz.getName() + " n where n." + (nE.getCodePrimName() != null ? nE.getCodePrimName() + " = :codP" : "") + (nE.getCodePrimName() == null ? nE.getCodeSecName() + " = :codS" : (nE.getCodeSecName() != null ? " and n." + nE.getCodeSecName() + " = :codS" : ""));
                        queryExec = em.createQuery(queryS);
                        if (nE.getCodePrimName() != null) {
                            queryExec.setParameter("codP", nE.getCodePrim());
                        }
                        if (nE.getCodeSecName() != null) {
                            queryExec.setParameter("codS", nE.getCodeSec());
                        }
                        Object maxDataStop = queryExec.getSingleResult();

                        if (inputNomenclator instanceof Versioned) {
                            if (((Versioned) inputNomenclator).getDataStart().getTime() <= ((Date) maxDataStop).getTime()) {
                                return "Versiunea nomenclator se intersecteaza cu o alta versiune";
                            } else {
                                return "Ok";
                            }
                        } else {
                            Long time = new Date().getTime();
                            Date today = new Date(time - time % (24 * 60 * 60 * 1000));

                            if (((Date) maxDataStop).getTime() >= today.getTime()) {
                                return "Exista instanta activa pentru asemenea conditii ";
                            } else {
                                return "Ok";
                            }
                        }
                    }
                }
            }
        } else {
            //Pentru needitabile nu se verifica nimic
            return "Ok";
        }

//        String query ="select n from "+clazz.getName()+"n where n."+(fillConditions.isNomOrgTer()?fillConditions.getNomOrgTerCodName():"cod")+
//                " = :cod +"+(fillConditions.isNomOrgTer()||!fillConditions.getHaveCodRand()?"":" and n.codRand=:codRand")+" and n.datStop=null";
//
//        return null;
    }

    @Override
    public Nomenclator findOne(Class<? extends Nomenclator> clazz, Long id) {
        return em.find(clazz, id);
    }

    @Override
    public Nomenclator findOne(Class<? extends Nomenclator> clazz, Long id, boolean detached) {
        Nomenclator entry = findOne(clazz, id);
        if (entry != null && detached) {
            em.detach(entry);
        }
        return entry;
    }

    @Override
    public Nomenclator save(Nomenclator entry) {

        if (entry.getId() == null) {
            em.persist(entry);
        } else {
            em.merge(entry);
        }
        return entry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public GenericListResult<Nomenclator> getList(NomenclatorSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo) {

        Class<? extends Nomenclator> type = searchFilter.getType().getClazz();

        GenericListResult<Nomenclator> result = new GenericListResult<Nomenclator>();


        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<? extends Nomenclator> cq = cb.createQuery(type);
        Root<? extends Nomenclator> from = cq.from(type);

        List<Predicate> predicates = new LinkedList<Predicate>();

        //
        // Predicate
        //
        if (StringUtils.isNotEmpty(searchFilter.getCod())) {
            //
            //Todo: De vazut o metoda mai eleganta
            if (type == UAT.class) {
                predicates.add(cb.equal(from.get("codSiruta"), Long.parseLong(searchFilter.getCod())));
            } else if (type == NomCapitol.class) {
                try {
                    predicates.add(cb.equal(from.get("cod"), TipCapitol.checkTipCapitol(searchFilter.getCod())));
                } catch (RequestValidationException e) {
                    e.printStackTrace();
                }
            } else {
                predicates.add(cb.equal(from.get("cod"), searchFilter.getCod()));
            }
        }

        if (StringUtils.isNotEmpty(searchFilter.getDenumire())) {
            predicates.add(cb.equal(from.get("denumire"), searchFilter.getDenumire()));
        }

        if (searchFilter.getOnlyFromIds() != null && searchFilter.getOnlyFromIds().size() > 0) {
            predicates.add(from.get("id").in(searchFilter.getOnlyFromIds()));
        }

        if (searchFilter.getExcludedIds() != null && searchFilter.getExcludedIds().size() > 0) {
            predicates.add(cb.not(from.get("id").in(searchFilter.getExcludedIds())));
        }


        if (Versioned.class.isAssignableFrom(type)) {

            boolean showActiveRecords = searchFilter.getShowActiveRecords() != null ? searchFilter.getShowActiveRecords() : true;
            boolean showHistoryRecords = searchFilter.getShowHistoryRecords() != null ? searchFilter.getShowHistoryRecords() : false;

            if (showActiveRecords && showHistoryRecords) {

                //
                // Nu se mai aplica filtru pe data
                //

            } else if (!showActiveRecords && !showHistoryRecords) {
                //
                // Nu se afiseaza nimik
                //
                predicates.add(from.get("id").isNull());
            } else if (showActiveRecords) {
                //
                // Filtru inregistrari active
                //
                Date now = new Date();

                Expression<Date> dataStart = from.get("dataStart");
                Expression<Date> dataStop = from.get("dataStop");

                predicates.add(
                        cb.or(
                                dataStop.isNull(),
                                cb.and(
                                        dataStop.isNotNull(),
                                        cb.lessThanOrEqualTo(dataStart, now),
                                        cb.greaterThanOrEqualTo(dataStop, now)
                                )
                        )
                );
            } else {
                /*am introdus dataHistoryRecords; pentru compatibilitate las codul vechi nemodificat pe else (Dan)*/
                if (searchFilter.getDataHistoryRecords() != null) {
                    Date dataValabilitate = searchFilter.getDataHistoryRecords();
                    Expression<Date> dataStart = from.get("dataStart");
                    Expression<Date> dataStop = from.get("dataStop");

                    predicates.add(
                            cb.or(
                                    cb.and(dataStop.isNull(), cb.lessThanOrEqualTo(dataStart, dataValabilitate))
                                    ,
                                    cb.and(
                                            dataStop.isNotNull(),
                                            cb.lessThanOrEqualTo(dataStart, dataValabilitate),
                                            cb.greaterThanOrEqualTo(dataStop, dataValabilitate)
                                    )
                            )
                    );
                } else {
                    //
                    // Filtru inregistrari istoric
                    //
                    Date now = new Date();

                    Expression<Date> dataStart = from.get("dataStart");
                    Expression<Date> dataStop = from.get("dataStop");

                    predicates.add(
                            cb.and(
                                    dataStop.isNotNull(),
                                    cb.greaterThanOrEqualTo(dataStart, now),
                                    cb.lessThanOrEqualTo(dataStop, now)
                            )
                    );
                }
            }
        }


        if (searchFilter.getCriterias() != null) {
            for (NomenclatorSearchCriteria nomenclatorSearchCriteria : searchFilter.getCriterias()) {

                //
                // todo: tratare operatie searchCriteria
                // am facut intr-un prim pas copy/paste dupa  RanRepositoryImpl (Dan)
                //
                if (nomenclatorSearchCriteria.getOperation() != null) {

                    Predicate predicate = null;
                    String path = nomenclatorSearchCriteria.getPath();
                    Expression expression = buildExpression(from, path);
                    Object value = null;
                    if ("nomCapitol.cod".equals(path)) {
                        try {
                            value = TipCapitol.checkTipCapitol(String.valueOf(nomenclatorSearchCriteria.getValue()));
                        } catch (RequestValidationException e) {
                            e.printStackTrace();
                        }
                    } else {
                        value = nomenclatorSearchCriteria.getValue();
                    }


                    if (nomenclatorSearchCriteria.getOperation() == Operation.IS_NOT_NULL) {

                        predicate = cb.isNotNull(expression);

                    } else if (nomenclatorSearchCriteria.getOperation() == Operation.IS_NULL) {

                        predicate = cb.isNull(expression);

                    } else if (nomenclatorSearchCriteria.getOperation() == Operation.EQ) {

                        predicate = cb.equal(expression, value);

                    } else if (nomenclatorSearchCriteria.getOperation() == Operation.LT) {

                        predicate = cb.lessThan(expression, (Comparable) value);

                    } else if (nomenclatorSearchCriteria.getOperation() == Operation.LTE) {

                        predicate = cb.lessThanOrEqualTo(expression, (Comparable) value);

                    } else if (nomenclatorSearchCriteria.getOperation() == Operation.GT) {

                        predicate = cb.greaterThan(expression, (Comparable) value);

                    } else if (nomenclatorSearchCriteria.getOperation() == Operation.GTE) {

                        predicate = cb.greaterThanOrEqualTo(expression, (Comparable) value);

                    } else if (nomenclatorSearchCriteria.getOperation() == Operation.LIKE) {

                        String stringToFind = "%" + StringUtils.trim((String) value).replaceAll(" ", "%") + "%";
                        predicate = cb.like(expression, stringToFind);
                    } else if (nomenclatorSearchCriteria.getOperation() == Operation.ILIKE) {
                        String stringToFind = "%" + StringUtils.trim((String) value).toUpperCase().replaceAll(" ", "%") + "%";
                        predicate = cb.like(cb.upper(expression), stringToFind);
                    } else if (nomenclatorSearchCriteria.getOperation() == Operation.IN) {
                        if (value instanceof List) {
                            predicate = expression.in((Collection<?>) value);
                        } else if (value instanceof Object[]) {
                            predicate = expression.in((Object[]) value);
                        } else {
                            throw new ValueTypeNotSupportedException("Type " + value.getClass() + " not supported for operation IN");
                        }
                    }

                    if (predicate == null) {
                        throw new OperationNotSupportedException("Unknown operation " + nomenclatorSearchCriteria.getOperation());
                    }

                    predicates.add(predicate);

                } else if (StringUtils.isNotEmpty(nomenclatorSearchCriteria.getPath()) && nomenclatorSearchCriteria.getValue() != null &&
                        StringUtils.isNotEmpty(nomenclatorSearchCriteria.getValue().toString())
                        ) {

                    String path = nomenclatorSearchCriteria.getPath();

                    Expression expression = buildExpression(from, path);

                    String value = nomenclatorSearchCriteria.getValue().toString().trim();

                    String stringToFind = "%" + value.toUpperCase().replaceAll(" ", "%") + "%";


                    Predicate predicate = cb.like(cb.upper(expression), stringToFind);

                    predicates.add(predicate);
                }
            }
        }

        if (predicates.size() > 0) {
            cq.where(predicates.toArray(new Predicate[predicates.size()]));
        }

        //
        // Sortare
        //
        if (sortInfo != null) {

            List<SortCriteria> sortCriterias = sortInfo.getCriterias();
            if (sortCriterias != null) {

                List<Order> orders = new LinkedList<Order>();

                for (SortCriteria sortCriteria : sortCriterias) {
                    if (sortCriteria.getPath() == null) {
                        throw new IllegalArgumentException("Criteriu sortare nedefinit");
                    }

                    if (sortCriteria.getOrder() == null) {
                        throw new IllegalArgumentException("Directie sortare nedefinita");
                    }

                    if (sortCriteria.getOrder() == ro.uti.ran.core.utils.Order.asc) {
                        orders.add(cb.asc(from.get(sortCriteria.getPath().toString())));
                    } else if (sortCriteria.getOrder() == ro.uti.ran.core.utils.Order.desc) {
                        orders.add(cb.desc(from.get(sortCriteria.getPath().toString())));
                    } else {
                        throw new RuntimeException("Directie de sortare necunoscuta: " + sortCriteria.getOrder());
                    }
                }

                cq.orderBy(orders);
            }
        }


        //
        // Paginare
        //
        PagingInfo _pagingInfo = pagingInfo != null ? pagingInfo : new PagingInfo(0, 1000);

        TypedQuery<? extends Nomenclator> typedQuery = em.createQuery(cq)
                .setFirstResult(_pagingInfo.getFirstResult())
                .setMaxResults(_pagingInfo.getMaxResults() > 0 ? _pagingInfo.getMaxResults() : 1000);


        //
        // Total count
        //
        CriteriaQuery<Long> cqCount = cb.createQuery(Long.class);
        Root<? extends Nomenclator> fromCount = cqCount.from(type);
        cqCount.select(cb.count(fromCount));

        if (predicates.size() > 0) {
            cqCount.where(predicates.toArray(new Predicate[predicates.size()]));
        }

        Long totalCount = em.createQuery(cqCount).getSingleResult();

        List<Nomenclator> list = (List<Nomenclator>) typedQuery.getResultList();

        result.setItems(list);
        result.setFirstResult(_pagingInfo.getFirstResult());
        result.setRecordsPerPage(_pagingInfo.getMaxResults());
        result.setTotalRecordCount(totalCount);

        return result;
    }

    @Override
    //@Transactional(value = "registruTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public void delete(Nomenclator entry) {
        em.remove(entry);
    }

    private Expression buildExpression(Root<? extends Nomenclator> from, String path) {
        if (path.contains(".")) {
            String[] paths = path.split("\\.");
            Join join = from.join(paths[0]);
            for (int i = 1; i < paths.length - 1; i++) {
                join = join.join(paths[i]);
            }
            return join.get(paths[paths.length - 1]);
        }
        return from.get(path);
    }

//    private NomenclatorInsertChecked buildInsertInfo(Class<? extends Nomenclator> clazz, Nomenclator inputNomenclator)  {
//        NomenclatorInsertChecked result = new NomenclatorInsertChecked();
//        result.setHaveCodRand(false);
//        result.setNomOrgTer(false);
//        Metamodel meta = em.getMetamodel();
//        EntityType<?> entityType = meta.entity(clazz);
//        //check codRand
//        if (!( inputNomenclator instanceof NomOrganizareTeritoriala)) {
//
//            for (Attribute<?, ?> t : entityType.getAttributes()) {
//                if (t.getName().equals("codRand")) {
//                    result.setHaveCodRand(true);
//                    break;
//                }
//            }
//            try {
//                Field fieldCod = inputNomenclator.getClass().getDeclaredField("cod");
//                fieldCod.setAccessible(true);
//                Method codMeth = fieldCod.get(inputNomenclator).getClass().getMethod("getCod");
//                result.setCod((String) codMeth.invoke(fieldCod.get(inputNomenclator)));
//                Field fieldDataStart = inputNomenclator.getClass().getDeclaredField("dataStart");
//                fieldDataStart.setAccessible(true);
//                Method codDataStart = fieldDataStart.get(inputNomenclator).getClass().getMethod("getDataStart");
//                result.setDataStart((Date) codDataStart.invoke(fieldCod.get(inputNomenclator)));
//                if(result.getHaveCodRand()) {
//                    Field fieldCodRand = inputNomenclator.getClass().getDeclaredField("codRand");
//                    fieldCodRand.setAccessible(true);
//                    Method codRandMeth = fieldCodRand.get(inputNomenclator).getClass().getMethod("getCodRand");
//                    result.setCodRand((Integer) codRandMeth.invoke(fieldCodRand.get(inputNomenclator)));
//                }
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }else{
//            result.setNomOrgTer(true);
//            result.setDataStart(((Versioned)inputNomenclator).getDataStart());
//            if(inputNomenclator instanceof NomTara){
//                result.setCod(((NomTara) inputNomenclator).getCodAlfa2());
//                result.setNomOrgTerCodName("codAlfa2");
//                result.setAlpha(true);
//            }
//            if(inputNomenclator instanceof NomJudet){
//                result.setCod(((NomJudet)inputNomenclator).getCodAlfa());
//                result.setNomOrgTerCodName("codAlfa");
//                result.setAlpha(true);
//            }
//            if(inputNomenclator instanceof NomLocalitate){
//                result.setCodRand(((NomLocalitate) inputNomenclator).getCodSiruta());
//                result.setNomOrgTerCodName("codSiruta");
//                result.setAlpha(false);
//            }
//            if(inputNomenclator instanceof  NomUat){
//                result.setCodRand(((NomUat)inputNomenclator).getCodSiruta());
//                result.setNomOrgTerCodName("codSiruta");
//                result.setAlpha(false);
//            }
//
//        }
//
//        return result;
//    }
}
