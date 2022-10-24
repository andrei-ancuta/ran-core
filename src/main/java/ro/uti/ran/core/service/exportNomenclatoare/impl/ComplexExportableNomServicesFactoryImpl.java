package ro.uti.ran.core.service.exportNomenclatoare.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.service.backend.nomenclator.*;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNom;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNomServicesFactory;

/**
 * Created by Stanciu Neculai on 19.Nov.2015.
 */
@Service
public class ComplexExportableNomServicesFactoryImpl implements ComplexExportableNomServicesFactory {

    @Autowired
    private NomUnitateMasuraService nomUnitateMasuraService;
    @Autowired
    private CapAnimalProdService capAnimalProdService;
    @Autowired
    private CapCulturaService capCulturaService;
    @Autowired
    private CapCulturaProdService capCulturaProdService;
    @Autowired
    private CapPlantatieService capPlantatieService;
    @Autowired
    private CapPlantatieProdService capPlantatieProdService;
    @Autowired
    private CapSistemTehnicService capSistemTehnicService;
    @Autowired
    private CapModUtilizareService capModUtilizareService;
    @Autowired
    private CapFructProdService capFructProdService;
    @Autowired
    private NomenclatorCapitolExportService<CapCategorieAnimal> capCategorieAnimalService;
    @Autowired
    private NomenclatorCapitolExportService<CapCategorieFolosinta> capCategorieFolosintaService;
    @Autowired
    private NomenclatorCapitolExportService<CapPomRazlet> capPomRazletService;
    @Autowired
    private NomenclatorCapitolExportService<CapSubstantaChimica> capSubstantaChimicaService;
    @Autowired
    private NomenclatorCapitolExportService<CapAplicareIngrasamant> capAplicareIngrasamantService;
    @Autowired
    private NomenclatorCapitolExportService<CapTerenIrigat> capTerenIrigatService;

    @Override
    public ComplexExportableNom getComplexExportableNomenclator(ExportableNomenclator exportableNomenclator) {
        TipNomenclator tipNomenclator = exportableNomenclator.getNomType();
        if (tipNomenclator.equals(TipNomenclator.NomUnitateMasura)) {
            return nomUnitateMasuraService;
        } else if (tipNomenclator.equals(TipNomenclator.CapAnimalProd)) {
            return capAnimalProdService;
        } else if (tipNomenclator.equals(TipNomenclator.CapCultura)) {
            return capCulturaService;
        } else if (tipNomenclator.equals(TipNomenclator.CapCulturaProd)) {
            return capCulturaProdService;
        } else if (tipNomenclator.equals(TipNomenclator.CapPlantatie)) {
            return capPlantatieService;
        } else if (tipNomenclator.equals(TipNomenclator.CapPlantatieProd)) {
            return capPlantatieProdService;
        } else if (tipNomenclator.equals(TipNomenclator.CapSistemTehnic)) {
            return capSistemTehnicService;
        } else if (tipNomenclator.equals(TipNomenclator.CapModUtilizare)) {
            return capModUtilizareService;
        } else if (tipNomenclator.equals(TipNomenclator.CapFructProd)) {
            return capFructProdService;
        } else if (tipNomenclator.equals(TipNomenclator.CapCategorieAnimal)) {
            return capCategorieAnimalService;
        } else if (tipNomenclator.equals(TipNomenclator.CapCategorieFolosinta)) {
            return capCategorieFolosintaService;
        } else if (tipNomenclator.equals(TipNomenclator.CapPomRazlet)) {
            return capPomRazletService;
        } else if (tipNomenclator.equals(TipNomenclator.CapSubstantaChimica)) {
            return capSubstantaChimicaService;
        } else if (tipNomenclator.equals(TipNomenclator.CapAplicareIngrasamant)) {
            return capAplicareIngrasamantService;
        } else if (tipNomenclator.equals(TipNomenclator.CapTerenIrigat)) {
            return capTerenIrigatService;
        } else {
            throw new RuntimeException("" + tipNomenclator.getClazz().getSimpleName() + " not implemented in ComplexExportableNomServicesFactoryImpl");
        }
    }

}
