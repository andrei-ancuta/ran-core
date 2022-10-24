package ro.uti.ran.core.service.sistem;

import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.ContextSistem;
import ro.uti.ran.core.model.portal.Sistem;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-15 16:32
 */
public interface SistemService {

    /**
     * Generare token
     * @return
     */
    String genereazaToken();


    /**
     *
     * @param idEntity
     * @param context
     * @param token
     * @return
     * @throws RanBusinessException
     */
    Sistem salveazaToken(Long idEntity, ContextSistem context, String token) throws RanBusinessException;


    /**
     * Salvare date utilizator sistem.
     * @param sistem
     * @return
     */
    Sistem salveaza(Sistem sistem);

    /**
     * Cautare
     * @return
     */
    Sistem getUtilizatorSistem(Long idEntity, ContextSistem context) throws RanBusinessException;


    Sistem getUtilizatorSistem(Long idUtilizatorSistem) throws RanBusinessException;


    GenericListResult<Sistem> getUtilizatoriSistem(SistemSearchFilter sistemSearchFilter, PagingInfo pagingInfo, SortInfo sortInfo) throws RanBusinessException;
}
