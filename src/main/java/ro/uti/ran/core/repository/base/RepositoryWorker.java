package ro.uti.ran.core.repository.base;

import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-11-11 09:44
 */
public interface RepositoryWorker<T> {

    List<T> getList(EntityManager entityManager, PagingInfo pagingInfo, SortInfo sortInfo);

    Long getCount(EntityManager entityManager);
}
