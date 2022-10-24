package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.CapCultura;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.repository.registru.CapCulturaRepository;
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
 * Created by Stanciu Neculai on 23.Nov.2015.
 */
@Service
@Transactional(value = "registruTransactionManager")
public class CapCulturaService extends ComplexExportableNomGenericService<CapCultura> implements ComplexExportableNom {
    @Autowired
    private CapCulturaRepository capCulturaRepository;

    @Override
    public CapitolExportableNom<CapCultura> getCapExportableNom(ExportableNomenclator exportableNomenclator) {
        return capCulturaRepository;
    }

    @Override
    public NomenclatorExportValue from(CapCultura exportableNom) {
        return NomeclatorConversionHelper.from(exportableNom);
    }
}
