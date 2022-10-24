package ro.uti.ran.core.repository.base;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-28 16:58
 */
public interface RepositoryFilter<T> {

    Predicate[] buildFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> from);
}
