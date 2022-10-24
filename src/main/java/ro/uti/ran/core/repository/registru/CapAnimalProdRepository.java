package ro.uti.ran.core.repository.registru;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ro.uti.ran.core.model.registru.CapAnimalProd;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.repository.base.RanRepository;
import ro.uti.ran.core.service.backend.nomenclator.CapitolExportableNom;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;

import java.util.List;

/**
 * Created by Stanciu Neculai on 20.Nov.2015.
 */
public interface CapAnimalProdRepository extends RanRepository<CapAnimalProd,Long>,CapitolExportableNom<CapAnimalProd> {
    @Query(value = "select distinct FK_NOM_CAPITOL from CAP_ANIMAL_PROD",nativeQuery = true)
    List<Long> getDistinctNomCapitol();

    List<CapAnimalProd> getAllByNomCapitol(NomCapitol capitol, Pageable pageable);
}
