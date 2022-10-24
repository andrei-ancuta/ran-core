package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.data.domain.Pageable;
import ro.uti.ran.core.model.registru.CapPlantatie;
import ro.uti.ran.core.model.registru.NomCapitol;

import java.util.List;

/**
 * Created by Stanciu Neculai on 14.Jan.2016.
 */
public interface CapitolExportableNom<T> {
    List<Long> getDistinctNomCapitol();

    List<T> getAllByNomCapitol(NomCapitol capitol,Pageable pageable);
}
