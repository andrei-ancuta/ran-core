package ro.uti.ran.core.repository.registru;



import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ro.uti.ran.core.model.registru.CapFructProd;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.nomenclator.CapitolExportableNom;

import java.util.List;

/**
 * Created by Stanciu Neculai on 11.Feb.2016.
 */
public interface CapFructProdRepository extends RanRepository<CapFructProd,Long>,CapitolExportableNom<CapFructProd> {
    @Query(value = "select distinct FK_NOM_CAPITOL from CAP_FRUCT_PROD",nativeQuery = true)
    List<Long> getDistinctNomCapitol();

    @Override
    List<CapFructProd> getAllByNomCapitol(NomCapitol capitol, Pageable pageable);
}
