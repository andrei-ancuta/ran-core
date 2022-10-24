package ro.uti.ran.core.repository.portal;

import org.springframework.stereotype.Repository;
import ro.uti.ran.core.model.portal.RolUtilizator;
import ro.uti.ran.core.model.portal.Utilizator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-11 09:12
 */
@Repository
public class TestRepository {

    @PersistenceContext(unitName = "ran-portal")
    private EntityManager entityManager;

    public void getList(){
        //
        // JPA Query Language
        //

        String jpql = "select u from Utilizator u where u.id in (select ru.utilizator.id from RolUtilizator ru where ru.uat.id=1)";
        String jpqlCount = "select count(u.id) from Utilizator u where u.id in (select ru.utilizator.id from RolUtilizator ru where ru.uat.id=1)";

        Query query = entityManager.createQuery(jpql)
                .setFirstResult(0)
                .setMaxResults(4)
                ;

        List<Utilizator> list = query.getResultList();

        System.out.println("list = " + list);


        Query querycount = entityManager.createQuery(jpqlCount);

        Object count = querycount.getSingleResult();
        System.out.println("count = " + count);


        if( 1==1) return;


        //
        // JPA Criteria API
        //

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Utilizator> cq = cb.createQuery(Utilizator.class);
        Root<Utilizator> from = cq.from(Utilizator.class);
        cq.select(from);

        // where building
        Subquery<RolUtilizator> subquery = cq.subquery(RolUtilizator.class);
        Root fromRolUtilizator = subquery.from(RolUtilizator.class);
        subquery.select(fromRolUtilizator.join("utilizator").get("id"));


        List<Predicate> rolUtilizatorPredicates = new LinkedList<Predicate>();

        //
        // Daca are roluri pentru UAT dat
        //

        rolUtilizatorPredicates.add(
                cb.in(fromRolUtilizator .join("uat").get("id")).value(1L)
        );

        Predicate[] predicates = new Predicate[]{
                cb.in(from.get("id")).value(subquery)
        };

        cq.where(
//                cb.equal(from.get("numeUtilizator"), "test")
                predicates
        );


        TypedQuery<Utilizator> typedQuery = entityManager.createQuery(cq)
                .setFirstResult(0)
                .setMaxResults(25);

        List<Utilizator> list2 = typedQuery.getResultList();

        System.out.println("list2 = " + list2);



        CriteriaQuery<Long> cqCount = cb.createQuery(Long.class);
        Root<Utilizator> fromCount = cqCount.from(Utilizator.class);


        CriteriaQuery<Long> select = cqCount.select(cb.count(fromCount));

        select.where(predicates);

        TypedQuery<Long> typedQueryCount = entityManager.createQuery(select);
        typedQueryCount.setFirstResult(0);
        //typedQuery.setMaxResults(pageSize);
        // here is the size of your query

        Long totalCount = typedQueryCount.getSingleResult();

        System.out.println("totalCount = " + totalCount);



    }
}
