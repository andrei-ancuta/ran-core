package ro.uti.ran.core.repository.base;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ro.uti.ran.core.model.registru.view.ViewRegistruNomStare;
import ro.uti.ran.core.repository.criteria.*;
import ro.uti.ran.core.utils.*;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.criteria.Order;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * Criteria Builder examples
 *
 * http://docs.oracle.com/javaee/6/tutorial/doc/gjivm.html
 * http://www.altuure.com/2010/09/23/jpa-criteria-api-by-samples-%E2%80%93-part-ii/
 *
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-18 14:30
 */
public class RanRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements RanRepository<T, ID> {

    private EntityManager entityManager;

    // There are two constructors to choose from, either can be used.
    public RanRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);

        // This is the recommended method for accessing inherited class dependencies.
        this.entityManager = entityManager;
    }

    @Override
    public GenericListResult<T> getListResult(RepositoryCriteria criteria, PagingInfo pagingInfo, SortInfo sortInfo) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getDomainClass());
        Root<T> from = cq.from(getDomainClass());

        return getListResult(
                cq,
                cb,
                from,
                processRepositoryCriteria(criteria, cb, from),
                pagingInfo,
                sortInfo
        );
    }

    @Override
    public GenericListResult<T> getListResult(RepositoryFilter filter, PagingInfo pagingInfo, SortInfo sortInfo) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getDomainClass());
        Root<T> from = cq.from(getDomainClass());
        cq.select(from);

        return getListResult(
                cq,
                cb,
                from,
                filter.buildFilter(cb, cq, from),
                pagingInfo,
                sortInfo
        );
    }


    @Override
    public GenericListResult<T> getListResultTransmisii(RepositoryFilter filter, PagingInfo pagingInfo, SortInfo sortInfo) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getDomainClass());
        Root<T> from = cq.from(getDomainClass());
        cq.select(from);

        return getListResultTransmisii(
                cq,
                cb,
                from,
                filter.buildFilter(cb, cq, from),
                pagingInfo,
                sortInfo
        );
    }

    @Override
    public ViewRegistruNomStare findByIndexRegistruNew(String indexRegistru) {
        String sql = "select r from ViewRegistruNomStare r where r.indexRegistru = ?1";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, indexRegistru);
        if (query.getResultList().isEmpty())
            return null;
        else if (query.getResultList().size() == 1)
            return (ViewRegistruNomStare) query.getSingleResult();

        throw new NonUniqueResultException();
    }


    @Override
    public GenericListResult<T> getListResult(RepositoryWorker<T> worker, PagingInfo pagingInfo, SortInfo sortInfo) {

        PagingInfo _pagingInfo = pagingInfo != null ? pagingInfo : new PagingInfo(0, 1000);

        List<T> list = worker.getList(entityManager, _pagingInfo, sortInfo);

        Long totalCount = worker.getCount(entityManager);



        //
        // Construct result
        //
        GenericListResult<T> result = new GenericListResult<T>();
        result.setItems(list);
        result.setFirstResult(_pagingInfo.getFirstResult());
        result.setRecordsPerPage(_pagingInfo.getMaxResults());
        result.setTotalRecordCount(totalCount);

        return result;
    }

    private GenericListResult<T> getListResult(CriteriaQuery<T> cq, CriteriaBuilder cb, Root<T> from, Predicate[] predicates, PagingInfo pagingInfo, SortInfo sortInfo) {

        //
        // Procesare filtre
        //
        if (predicates != null && predicates.length > 0) {
            cq.where(predicates);
        }

        //
        // Procesare sort info
        //
        if (sortInfo != null) {

            List<SortCriteria> sortCriterias = sortInfo.getCriterias();
            if (sortCriterias != null) {

                List<Order> orders = new LinkedList<Order>();

                for (SortCriteria sortCriteria : sortCriterias) {
                    if(sortCriteria.getPath() == null || sortCriteria.getOrder() == null) continue;

                    Expression expression = buildExpression(from, sortCriteria.getPath());

                    if( sortCriteria.getOrder() == ro.uti.ran.core.utils.Order.asc){
                        orders.add(cb.asc(expression));
                    }else if( sortCriteria.getOrder() == ro.uti.ran.core.utils.Order.desc){
                        orders.add(cb.desc(expression));
                    }else{
                        throw new RuntimeException("Directie de sortare necunoscuta: " + sortCriteria.getOrder());
                    }
                }

                cq.orderBy(orders);
            }
        }


        //
        // Interpretare paging info
        //
        PagingInfo _pagingInfo = pagingInfo != null ? pagingInfo : new PagingInfo(0, 1000);

        TypedQuery<T> typedQuery = entityManager.createQuery(cq)
                .setFirstResult(_pagingInfo.getFirstResult())
                .setMaxResults(_pagingInfo.getMaxResults());

        List<T> list = typedQuery.getResultList();


        //
        // Total count
        //
        CriteriaQuery<Long> cqCount = cb.createQuery(Long.class);
        Root<T> fromCount = cqCount.from(getDomainClass());



        CriteriaQuery<Long> select = cqCount.select(cb.count(fromCount));

        if( predicates != null && predicates.length > 0){
            select.where(predicates);
        }

        TypedQuery<Long> typedQueryCount = entityManager.createQuery(select);
        typedQueryCount.setFirstResult(0);
//typedQuery.setMaxResults(pageSize);
// here is the size of your query
        Long totalCount = typedQueryCount.getSingleResult();

//        Long totalCount = entityManager.createQuery(cqCount).getSingleResult();
//        Long totalCount = 100L;


        //
        // Construct result
        //
        GenericListResult<T> result = new GenericListResult<T>();
        result.setItems(list);
        result.setFirstResult(_pagingInfo.getFirstResult());
        result.setRecordsPerPage(_pagingInfo.getMaxResults());
        result.setTotalRecordCount(totalCount);

        return result;
    }


    private GenericListResult<T> getListResultTransmisii(CriteriaQuery<T> cq, CriteriaBuilder cb, Root<T> from, Predicate[] predicates, PagingInfo pagingInfo, SortInfo sortInfo) {

        if (predicates != null && predicates.length > 0) {
            cq.where(predicates);
        }

        if (sortInfo != null) {

            List<SortCriteria> sortCriterias = sortInfo.getCriterias();
            if (sortCriterias != null) {

                List<Order> orders = new LinkedList<Order>();

                for (SortCriteria sortCriteria : sortCriterias) {
                    if(sortCriteria.getPath() == null || sortCriteria.getOrder() == null) continue;

                    Expression expression = buildExpression(from, sortCriteria.getPath());

                    if( sortCriteria.getOrder() == ro.uti.ran.core.utils.Order.asc){
                        orders.add(cb.asc(expression));
                    }else if( sortCriteria.getOrder() == ro.uti.ran.core.utils.Order.desc){
                        orders.add(cb.desc(expression));
                    }else{
                        throw new RuntimeException("Directie de sortare necunoscuta: " + sortCriteria.getOrder());
                    }
                }

                cq.orderBy(orders);
            }
        }


        PagingInfo _pagingInfo = pagingInfo != null ? pagingInfo : new PagingInfo(0, 1000);

        TypedQuery<T> typedQuery = entityManager.createQuery(cq)
                .setFirstResult(_pagingInfo.getFirstResult())
                .setMaxResults(_pagingInfo.getMaxResults());

        List<T> list = typedQuery.getResultList();
        GenericListResult<T> result = new GenericListResult<>();
        result.setItems(list);
        result.setFirstResult(_pagingInfo.getFirstResult());
        result.setRecordsPerPage(_pagingInfo.getMaxResults());

        return result;
    }

    private Expression buildExpression(Root<T> from, String path){
        if(path.contains(".")){
            String []paths = path.split("\\.");
            Join join = from.join(paths[0]);
            for (int i = 1; i < paths.length - 1; i++){
                join = join.join( paths[i]);
            }
            return join.get(paths[paths.length-1]);
        }
        return from.get(path);
    }

    private Predicate[] processRepositoryCriteria(RepositoryCriteria criteria, CriteriaBuilder cb, Root<T> from ) {

        List<Predicate> list = new LinkedList<Predicate>();

        processRepositoryCriteria(criteria, cb, list, from);

        return list.toArray(new Predicate[list.size()]);
    }

    private void processRepositoryCriteria(RepositoryCriteria criteria, CriteriaBuilder cb, List<Predicate> predicates, Root<T> from ){

        if( criteria == null){
            return;
        }

        if( criteria instanceof AndRepositoryCriteria){

            List<RepositoryCriteria> criterias = ((AndRepositoryCriteria)criteria).getCriterias();

            List<Predicate> andPredicates = new LinkedList<Predicate>();

            for (RepositoryCriteria _criteria : criterias) {
                processRepositoryCriteria(_criteria, cb, andPredicates, from);
            }

            if( andPredicates.size() > 0) {
                Predicate andPredicate = cb.and(andPredicates.toArray(new Predicate[andPredicates.size()]));

                predicates.add(andPredicate);
            }

        }else if( criteria instanceof OrRepositoryCriteria){

            List<RepositoryCriteria> criterias = ((OrRepositoryCriteria)criteria).getCriterias();

            List<Predicate> orPredicates = new LinkedList<Predicate>();

            for (RepositoryCriteria _criteria : criterias) {
                processRepositoryCriteria(_criteria, cb, orPredicates, from);
            }

            if( orPredicates.size() > 0) {
                Predicate orPredicate = cb.or(orPredicates.toArray(new Predicate[orPredicates.size()]));

                predicates.add(orPredicate);
            }

        }else{

            //
            // Simple criteria
            //

            //
            // Validations ...
            //
            String field = criteria.getPath();
            Operation operation = criteria.getOperation();
            Object value = criteria.getValue();

            if(StringUtils.isEmpty(field)){
                throw new InvalidCriteriaException("Field is not defined");
            }

            if(operation == null){
                throw new InvalidCriteriaException("Operation is not defined");
            }

            Expression expression = buildExpression(from, criteria.getPath());

            //
            // Check null value  ...
            //
            if( value == null){
                cb.isNull(expression);
            }else {

                //
                // Check operation ...
                //
                Predicate predicate = null;
                if (criteria.getOperation() == Operation.EQ) {

                    predicate = cb.equal(expression, value);

                } else if (criteria.getOperation() == Operation.LT) {

                    predicate = cb.lessThan(expression, (Comparable)value);

                } else if (criteria.getOperation() == Operation.LTE) {

                    predicate = cb.lessThanOrEqualTo(expression, (Comparable)value);

                } else if (criteria.getOperation() == Operation.GT) {

                    predicate = cb.greaterThan(expression, (Comparable)value);

                } else if (criteria.getOperation() == Operation.GTE) {

                    predicate = cb.greaterThanOrEqualTo(expression,(Comparable) value);

                } else if (criteria.getOperation() == Operation.LIKE) {

                    String stringToFind = "%" + StringUtils.trim((String) value).replaceAll(" ", "%") + "%";


                    predicate = cb.like(expression, stringToFind);
                } else if (criteria.getOperation() == Operation.ILIKE) {

                    String stringToFind = "%" + StringUtils.trim((String) value).toUpperCase().replaceAll(" ", "%") + "%";


                    predicate = cb.like(cb.upper(expression), stringToFind);
                } else if(criteria.getOperation() == Operation.IN) {

                    if( value instanceof List){
                        predicate = expression.in((Collection<?>) value);
                    }else if( value instanceof Object[]){
                        predicate = expression.in((Object[]) value);
                    }else{
                        throw new ValueTypeNotSupportedException("Type " + value.getClass() + " not supported for operation IN");
                    }
                }

                if (predicate == null) {
                    throw new OperationNotSupportedException("Unknown operation " + criteria.getOperation());
                }

                predicates.add(predicate);
            }
        }
    }
}
