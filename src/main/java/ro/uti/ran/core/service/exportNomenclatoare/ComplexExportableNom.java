package ro.uti.ran.core.service.exportNomenclatoare;

import ro.uti.ran.core.service.backend.nomenclator.ExportableParamsEnum;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportValue;

import java.util.List;
import java.util.Map;

/**
 * Created by Stanciu Neculai on 19.Nov.2015.
 */
public interface ComplexExportableNom {
    List<NomenclatorExportValue> getNomValues(int page,int size, ExportableNomenclator nomenclatorCodeType,Map<ExportableParamsEnum,Object> additionalParam);
    Map<ExportableParamsEnum,Object> getSubcatNameCodes(ExportableNomenclator nomenclator);
}
