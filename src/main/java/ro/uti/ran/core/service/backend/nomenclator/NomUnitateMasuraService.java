package ro.uti.ran.core.service.backend.nomenclator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.registru.NomUnitateMasura;
import ro.uti.ran.core.repository.registru.NomUnitateMasuraRepository;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNom;
import ro.uti.ran.core.service.exportNomenclatoare.NomeclatorConversionHelper;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stanciu Neculai on 19.Nov.2015.
 */
@Service
@Transactional(value = "registruTransactionManager")
public class NomUnitateMasuraService implements ComplexExportableNom {

    @Autowired
    private NomUnitateMasuraRepository nomUnitateMasuraRepository;

    @Override
    public List<NomenclatorExportValue> getNomValues(int page,int size, ExportableNomenclator exportableNomenclator,Map<ExportableParamsEnum,Object> additionalParam) {
        Pageable pageable = new PageRequest(page,size);
        Page<NomUnitateMasura> nomUnitateMasuraPage = nomUnitateMasuraRepository.findAll(pageable);
        List<NomenclatorExportValue> nomenclatorExportValueList = new ArrayList<NomenclatorExportValue>();
        for(NomUnitateMasura nomUnitateMasura : nomUnitateMasuraPage){
           NomenclatorExportValue exportValue =  NomeclatorConversionHelper.from(nomUnitateMasura);
           if(exportValue != null){
               nomenclatorExportValueList.add(exportValue);
           }
        }
        return nomenclatorExportValueList;
    }

    @Override
    public Map<ExportableParamsEnum,Object> getSubcatNameCodes(ExportableNomenclator nomenclator) {
        return null;
    }

}
