package ro.uti.ran.core.service.registru;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Incarcare;
import ro.uti.ran.core.model.portal.RegistruPortal;
import ro.uti.ran.core.model.portal.RegistruPortalDetails;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;

import java.io.InputStream;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 15:58
 */
public interface IncarcareService {

    Incarcare getIncarcareById(Long id);

    Incarcare saveIncarcare(Incarcare incarcare) throws RanBusinessException;

    GenericListResult<Incarcare> getListaIncarcari(IncarcariSearchFilter searchFilter, PagingInfo pagingInfo, SortInfo sortInfo);

    GenericListResult<RegistruPortal> getListaRegistruPortal(Long idIncarcare, PagingInfo pagingInfo, SortInfo sortInfo);

    GenericListResult<RegistruPortalDetails> getDetaliiRegistruPortal(Long idIncarcare, PagingInfo pagingInfo, SortInfo sortInfo);

    InputStream genereazaPachetRaspuns(Incarcare incarcare) throws Exception;
}
