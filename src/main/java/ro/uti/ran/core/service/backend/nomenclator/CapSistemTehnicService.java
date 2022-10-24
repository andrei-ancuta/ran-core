package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.CapSistemTehnic;
import ro.uti.ran.core.repository.registru.CapSistemTehnicRepository;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNom;
import ro.uti.ran.core.service.exportNomenclatoare.NomeclatorConversionHelper;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportValue;

/**
 * Created by Stanciu Neculai on 15.Jan.2016.
 */
@Service
@Transactional(value = "registruTransactionManager")
public class CapSistemTehnicService extends ComplexExportableNomGenericService<CapSistemTehnic> implements ComplexExportableNom {
    @Autowired
    private CapSistemTehnicRepository capSistemTehnicRepository;

    @Override
    public CapitolExportableNom<CapSistemTehnic> getCapExportableNom(ExportableNomenclator exportableNomenclator) {
        return capSistemTehnicRepository;
    }

    @Override
    public NomenclatorExportValue from(CapSistemTehnic exportableNom) {
        return NomeclatorConversionHelper.from(exportableNom);
    }
}
