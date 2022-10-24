package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.CapPlantatie;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.repository.registru.CapPlantatieRepository;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNom;
import ro.uti.ran.core.service.exportNomenclatoare.NomeclatorConversionHelper;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportValue;

import java.util.List;
import java.util.Map;

/**
 * Created by Stanciu Neculai on 14.Jan.2016.
 */
@Service
@Transactional(value = "registruTransactionManager")
public class CapPlantatieService extends ComplexExportableNomGenericService<CapPlantatie> implements ComplexExportableNom {
    @Autowired
    private CapPlantatieRepository capPlantatieRepository;

    @Override
    public CapitolExportableNom<CapPlantatie> getCapExportableNom(ExportableNomenclator exportableNomenclator) {
        return capPlantatieRepository;
    }

    @Override
    public NomenclatorExportValue from(CapPlantatie exportableNom) {
        return NomeclatorConversionHelper.from(exportableNom);
    }
}
