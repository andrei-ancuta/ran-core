package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.CapAnimalProd;
import ro.uti.ran.core.repository.registru.CapAnimalProdRepository;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNom;
import ro.uti.ran.core.service.exportNomenclatoare.NomeclatorConversionHelper;
import ro.uti.ran.core.service.exportNomenclatoare.NomenclatorClassificationService;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stanciu Neculai on 20.Nov.2015.
 */
@Service
@Transactional(value = "registruTransactionManager")
public class CapAnimalProdService  extends ComplexExportableNomGenericService<CapAnimalProd> implements ComplexExportableNom {

    @Autowired
    private CapAnimalProdRepository capAnimalProdRepository;

    @Override
    public CapitolExportableNom<CapAnimalProd> getCapExportableNom(ExportableNomenclator exportableNomenclator) {
        return capAnimalProdRepository;
    }

    @Override
    public NomenclatorExportValue from(CapAnimalProd exportableNom) {
        return NomeclatorConversionHelper.from(exportableNom);
    }
}
