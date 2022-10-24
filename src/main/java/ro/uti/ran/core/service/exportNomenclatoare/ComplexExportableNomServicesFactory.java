package ro.uti.ran.core.service.exportNomenclatoare;

import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;

/**
 * Created by Stanciu Neculai on 19.Nov.2015.
 */

public interface ComplexExportableNomServicesFactory {

    ComplexExportableNom getComplexExportableNomenclator(ExportableNomenclator exportableNomenclator);
}
