package ro.uti.ran.core.repository.registru;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ro.uti.ran.core.model.registru.CapSistemTehnic;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.nomenclator.CapitolExportableNom;

import java.util.List;

/**
 * Created by Stanciu Neculai on 15.Jan.2016.
 */
public interface CapSistemTehnicRepository extends RanRepository<CapSistemTehnic,Long>,CapitolExportableNom<CapSistemTehnic> {

    @Query(value = "select distinct FK_NOM_CAPITOL from CAP_SISTEM_TEHNIC",nativeQuery = true)
    List<Long> getDistinctNomCapitol();

    List<CapSistemTehnic> getAllByNomCapitol(NomCapitol capitol,Pageable pageable);
}
