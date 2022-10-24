package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.CapCulturaProd;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.repository.registru.CapCulturaProdRepository;
import ro.uti.ran.core.repository.registru.NomCapitolRepository;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNom;
import ro.uti.ran.core.service.exportNomenclatoare.NomeclatorConversionHelper;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stanciu Neculai on 14.Jan.2016.
 */

@Service
@Transactional(value = "registruTransactionManager")
public class CapCulturaProdService extends ComplexExportableNomGenericService<CapCulturaProd> implements ComplexExportableNom {
    @Autowired
    private CapCulturaProdRepository capCulturaProdRepository;

    @Override
    public CapitolExportableNom<CapCulturaProd> getCapExportableNom(ExportableNomenclator exportableNomenclator) {
        return capCulturaProdRepository;
    }

    @Override
    public NomenclatorExportValue from(CapCulturaProd exportableNom) {
        return NomeclatorConversionHelper.from(exportableNom);
    }
}
