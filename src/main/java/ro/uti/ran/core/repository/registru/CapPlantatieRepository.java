package ro.uti.ran.core.repository.registru;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ro.uti.ran.core.model.registru.CapPlantatie;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.nomenclator.CapitolExportableNom;

import java.util.List;

/**
 * Created by Stanciu Neculai on 14.Jan.2016.
 */
public interface CapPlantatieRepository extends RanRepository<CapPlantatie,Long>,CapitolExportableNom<CapPlantatie> {

    @Query(value = "select distinct FK_NOM_CAPITOL from CAP_PLANTATIE",nativeQuery = true)
    List<Long> getDistinctNomCapitol();

    List<CapPlantatie> getAllByNomCapitol(NomCapitol capitol,Pageable pageable);
}
