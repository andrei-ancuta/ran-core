package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.CapFructProd;
import ro.uti.ran.core.repository.registru.CapFructProdRepository;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNom;
import ro.uti.ran.core.service.exportNomenclatoare.NomeclatorConversionHelper;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportValue;

/**
 * Created by Stanciu Neculai on 11.Feb.2016.
 */
@Service
@Transactional(value = "registruTransactionManager")
public class CapFructProdService extends ComplexExportableNomGenericService<CapFructProd> implements ComplexExportableNom {

    @Autowired
    private CapFructProdRepository capFructProdRepository;

    @Override
    public CapitolExportableNom<CapFructProd> getCapExportableNom(ExportableNomenclator exportableNomenclator) {
        return capFructProdRepository;
    }

    @Override
    public NomenclatorExportValue from(CapFructProd exportableNom) {
        return NomeclatorConversionHelper.from(exportableNom);
    }
}
