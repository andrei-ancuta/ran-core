package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.CapPlantatieProd;
import ro.uti.ran.core.repository.registru.CapPlantatieProdRepository;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.NomeclatorConversionHelper;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportValue;

/**
 * Created by Stanciu Neculai on 14.Jan.2016.
 */

@Service
@Transactional(value = "registruTransactionManager")
public class CapPlantatieProdService extends ComplexExportableNomGenericService<CapPlantatieProd> {
    @Autowired
    private CapPlantatieProdRepository capPlantatieProdRepository;


    @Override
    public CapitolExportableNom<CapPlantatieProd> getCapExportableNom(ExportableNomenclator exportableNomenclator) {
        return capPlantatieProdRepository;
    }

    @Override
    public NomenclatorExportValue from(CapPlantatieProd exportableNom) {
        return NomeclatorConversionHelper.from(exportableNom);
    }
}
