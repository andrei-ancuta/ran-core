package ro.uti.ran.core.repository.registru;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ro.uti.ran.core.model.registru.CapPlantatieProd;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.nomenclator.CapitolExportableNom;

import java.util.List;

/**
 * Created by Stanciu Neculai on 14.Jan.2016.
 */
public interface CapPlantatieProdRepository extends RanRepository<CapPlantatieProd,Long>,CapitolExportableNom<CapPlantatieProd> {

    @Query(value = "select distinct FK_NOM_CAPITOL from CAP_PLANTATIE_PROD",nativeQuery = true)
    List<Long> getDistinctNomCapitol();

    List<CapPlantatieProd> getAllByNomCapitol(NomCapitol capitol,Pageable pageable);
}
