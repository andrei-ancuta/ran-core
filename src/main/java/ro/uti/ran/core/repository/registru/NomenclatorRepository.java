package ro.uti.ran.core.repository.registru;

import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.service.nomenclator.NomenclatorSearchFilter;
import ro.uti.ran.core.utils.GenericListResult;
import ro.uti.ran.core.utils.PagingInfo;
import ro.uti.ran.core.utils.SortInfo;

import java.util.Date;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-02 15:28
 */
public interface NomenclatorRepository {

    Nomenclator findOne(Class<? extends Nomenclator> clazz, Long id);

    Nomenclator findOne(Class<? extends Nomenclator> clazz, Long id, boolean detached);

    Nomenclator save(Nomenclator entry);

    GenericListResult<Nomenclator> getList(
            NomenclatorSearchFilter searchFilter,
            PagingInfo pagingInfo,
            SortInfo sortInfo);

    void delete(Nomenclator entry);

    void alterSessionNlsSortComp();

    Nomenclator getLastVersion(Class<? extends Nomenclator> clazz, Long baseID);

    boolean checkNewVersion(Class<? extends Nomenclator> clazz,Long baseID,Date dataStart);

    boolean chekLatestVersion(Class<? extends Nomenclator> clazz,Long baseID,Date dataStop);

    String checkInsert(Class<? extends Nomenclator> clazz,Nomenclator inputNomenclator);
}
