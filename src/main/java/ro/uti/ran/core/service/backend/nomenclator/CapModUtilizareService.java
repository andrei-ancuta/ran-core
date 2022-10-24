package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.CapModUtilizare;
import ro.uti.ran.core.repository.registru.CapModUtilizareRepository;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNom;
import ro.uti.ran.core.service.exportNomenclatoare.NomeclatorConversionHelper;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportValue;

/**
 * Created by Stanciu Neculai on 15.Jan.2016.
 */
@Service
@Transactional(value = "registruTransactionManager")
public class CapModUtilizareService extends ComplexExportableNomGenericService<CapModUtilizare> implements ComplexExportableNom {

    @Autowired
    private CapModUtilizareRepository repository;

    @Override
    public CapitolExportableNom<CapModUtilizare> getCapExportableNom(ExportableNomenclator exportableNomenclator) {
        return repository;
    }

    @Override
    public NomenclatorExportValue from(CapModUtilizare exportableNom) {
        return NomeclatorConversionHelper.from(exportableNom);
    }
}
