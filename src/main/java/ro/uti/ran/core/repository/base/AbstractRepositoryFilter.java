package ro.uti.ran.core.repository.base;

import org.apache.commons.lang.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-10 21:21
 */
public abstract class AbstractRepositoryFilter<T> implements RepositoryFilter<T> {

    private List<Predicate> predicates;

    protected Predicate[] predicatesArray(){
        if( predicates == null) return null;

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    protected void addPredicate(Predicate predicate){
        if( predicates == null){
            predicates = new LinkedList<Predicate>();
        }

        predicates.add(predicate);
    }

    protected void addLikePredicate(CriteriaBuilder cb, Path field, String search){
        String stringToFind = "%" + StringUtils.trim(search).toUpperCase().replaceAll(" ", "%") + "%";

        addPredicate(cb.like(cb.upper(field), stringToFind));
    }
}
