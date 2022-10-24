package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.repository.registru.NomCapitolRepository;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNom;
import ro.uti.ran.core.service.exportNomenclatoare.NomenclatorClassificationService;
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
public abstract class ComplexExportableNomGenericService<T> implements ComplexExportableNom {

    @Autowired
    private NomCapitolRepository nomCapitolRepository;
    @Autowired
    private NomenclatorClassificationService nomenclatorClassificationService;


    @Override
    public List<NomenclatorExportValue> getNomValues(int page, int size, ExportableNomenclator nomenclatorCodeType, Map<ExportableParamsEnum, Object> additionalParam) {
        if (additionalParam != null) {
            if (additionalParam.containsKey(ExportableParamsEnum.CAPITOL)) {
                NomCapitol value = (NomCapitol) additionalParam.get(ExportableParamsEnum.CAPITOL);
                List<T> exportableNom = getCapExportableNom(nomenclatorCodeType).getAllByNomCapitol(value, new PageRequest(page, size));
                List<NomenclatorExportValue> exportValues = new ArrayList<NomenclatorExportValue>();
                for (T elem : exportableNom) {
                    exportValues.add(from(elem));
                }
                return exportValues;
            }
        }
        return null;
    }

    @Override
    public Map<ExportableParamsEnum, Object> getSubcatNameCodes(ExportableNomenclator nomenclator) {
        Map<ExportableParamsEnum, Object> categories = new HashMap<ExportableParamsEnum, Object>();
        List<Long> distinctNomCapitol = getCapExportableNom(nomenclator).getDistinctNomCapitol();

        if(null != distinctNomCapitol && !distinctNomCapitol.isEmpty()) {
            List<NomCapitol> nomCapitols = nomCapitolRepository.findByIdList(distinctNomCapitol);
            nomCapitols = filterNomCapitols(nomenclator.getNomType(),nomCapitols);
            categories.put(ExportableParamsEnum.CAPITOL, nomCapitols);
        } else {
            categories.put(ExportableParamsEnum.CAPITOL, new ArrayList<NomCapitol>());
        }

        return categories;
    }

    private List<NomCapitol> filterNomCapitols(TipNomenclator tipNomenclator,List<NomCapitol> storedCapitols){
        List<NomCapitol> availableCapitolList = nomenclatorClassificationService.availableNonCapitolListFor(tipNomenclator);
        List<NomCapitol> filteredCaptitolList = new ArrayList<>();
        for(NomCapitol nomCapitol : storedCapitols){
            for(NomCapitol availableCapitol : availableCapitolList){
                if(nomCapitol.getCod().equals(availableCapitol.getCod())){
                    filteredCaptitolList.add(nomCapitol);
                }
            }
        }
        return filteredCaptitolList;
    }

    public abstract CapitolExportableNom<T> getCapExportableNom(ExportableNomenclator exportableNomenclator);
    public abstract NomenclatorExportValue from(T exportableNom);

}
