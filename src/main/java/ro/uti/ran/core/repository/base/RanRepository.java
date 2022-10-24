package ro.uti.ran.core.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import ro.uti.ran.core.model.registru.Registru;
import ro.uti.ran.core.model.registru.view.ViewRegistruNomStare;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;
import ro.uti.ran.core.repository.criteria.RepositoryCriteria;

import java.io.Serializable;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-18 14:23
 */
@NoRepositoryBean
public interface RanRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    GenericListResult<T> getListResult(RepositoryCriteria criteria, PagingInfo pagingInfo, SortInfo sortInfo);


    GenericListResult<T> getListResult(RepositoryFilter filter, PagingInfo pagingInfo, SortInfo sortInfo);


    GenericListResult<T> getListResult(RepositoryWorker<T> worker, PagingInfo pagingInfo, SortInfo sortInfo);

    GenericListResult<T> getListResultTransmisii(RepositoryFilter filter, PagingInfo pagingInfo, SortInfo sortInfo);

    ViewRegistruNomStare findByIndexRegistruNew(String indexRegistru);

}
