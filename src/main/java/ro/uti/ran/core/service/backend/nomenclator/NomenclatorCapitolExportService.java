package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNom;
import ro.uti.ran.core.service.exportNomenclatoare.NomeclatorConversionHelper;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportValue;

/**
 * Created by Stanciu Neculai on 12.Feb.2016.
 */
@Service
public class NomenclatorCapitolExportService<T> extends ComplexExportableNomGenericService<T> implements ComplexExportableNom {

    @Autowired
    private GenericCapitolExportableNom<T> capitolExportableNom;

    @Override
    public CapitolExportableNom<T> getCapExportableNom(ExportableNomenclator exportableNomenclator) {
        capitolExportableNom.setExportableNomenclator(exportableNomenclator);
        return capitolExportableNom;
    }

    @Override
    public NomenclatorExportValue from(T exportableNom) {
       return NomeclatorConversionHelper.from(exportableNom);
    }
}
