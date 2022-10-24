package ro.uti.ran.core.service.exportNomenclatoare;

import org.springframework.stereotype.Component;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.repository.registru.CapAnimalProdRepository;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportValue;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Stanciu Neculai on 20.Nov.2015.
 */
@Component
public class NomeclatorConversionHelper {
    private static final boolean EXPORT_REFERENCES = false;
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static NomenclatorExportValue from(NomUnitateMasura nomUnitateMasura) {
        if (nomUnitateMasura != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(nomUnitateMasura.getCod());
            nomenclatorExportValue.setDescription(nomUnitateMasura.getDenumire());
            NomTipUnitateMasura nomTipUnitateMasura = nomUnitateMasura.getNomTipUnitateMasura();
            if (EXPORT_REFERENCES) {
                List<NomenclatorReference> refs = new ArrayList<NomenclatorReference>();
                if (nomTipUnitateMasura != null) {
                    NomenclatorReference ref = new NomenclatorReference();
                    ref.setReference(NomeclatorConversionHelper.from(nomTipUnitateMasura));
                    ref.setNomRefDescription(TipNomenclator.NomTipUnitateMasura.getClazz().getSimpleName());
                    refs.add(ref);
                }
                nomenclatorExportValue.setReferences(refs);
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }
    public static NomenclatorExportValue from(NomTipUnitateMasura nomTipUnitateMasura) {
        if (nomTipUnitateMasura != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(nomTipUnitateMasura.getCod());
            nomenclatorExportValue.setDescription(nomTipUnitateMasura.getDenumire());
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }

    public static NomenclatorExportValue from(CapAnimalProd nomAnimalProd, CapAnimalProdRepository capAnimalProdRepository) {
        if (nomAnimalProd != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(nomAnimalProd.getCodRand()));
            nomenclatorExportValue.setDescription(nomAnimalProd.getDenumire());

            CapAnimalProd parent = null;
            if (nomAnimalProd.getCapAnimalProd() != null) {
                parent = capAnimalProdRepository.findOne(Long.valueOf(nomAnimalProd.getCapAnimalProd().getId()));
            }

            if (EXPORT_REFERENCES) {
                List<NomenclatorReference> refs = new ArrayList<NomenclatorReference>();

                NomUnitateMasura nomUnitateMasura = nomAnimalProd.getNomUnitateMasura();
                if (nomUnitateMasura != null) {
                    NomenclatorReference ref = new NomenclatorReference();
                    ref.setReference(NomeclatorConversionHelper.from(nomUnitateMasura));
                    ref.setNomRefDescription(TipNomenclator.NomUnitateMasura.getClazz().getSimpleName());
                    refs.add(ref);
                }
                if (parent != null) {
                    NomenclatorReference ref = new NomenclatorReference();
                    ref.setReference(NomeclatorConversionHelper.from(parent, capAnimalProdRepository));
                    ref.setNomRefDescription(TipNomenclator.CapAnimalProd.getClazz().getSimpleName());
                    refs.add(ref);
                }
                nomenclatorExportValue.setReferences(refs);
            }
            if (parent != null) {
                nomenclatorExportValue.setParentCode(String.valueOf(parent.getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }

    public static NomenclatorExportValue from(CapCultura nomCultura) {
        if (nomCultura != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(nomCultura.getCodRand()));
            nomenclatorExportValue.setDescription(nomCultura.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(nomCultura.getDataStart()));
            if (nomCultura.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(nomCultura.getDataStop()));
            }
            if (nomCultura.getCapCultura() != null) {
                nomenclatorExportValue.setParentCode(String.valueOf(nomCultura.getCapCultura().getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }
    public static NomenclatorExportValue from(CapCulturaProd nomCulturaProd) {
        if (nomCulturaProd != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(nomCulturaProd.getCodRand()));
            nomenclatorExportValue.setDescription(nomCulturaProd.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(nomCulturaProd.getDataStart()));
            if (nomCulturaProd.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(nomCulturaProd.getDataStop()));
            }
            if (nomCulturaProd.getCapCulturaProd() != null) {
                nomenclatorExportValue.setParentCode(String.valueOf(nomCulturaProd.getCapCulturaProd().getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }
    public static NomenclatorExportValue from(CapPlantatie capPlantatie) {
        if (capPlantatie != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(capPlantatie.getCodRand()));
            nomenclatorExportValue.setDescription(capPlantatie.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(capPlantatie.getDataStart()));
            if (capPlantatie.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(capPlantatie.getDataStop()));
            }
            if (capPlantatie.getCapPlantatie() != null) {
                nomenclatorExportValue.setParentCode(String.valueOf(capPlantatie.getCapPlantatie().getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }
    public static NomenclatorExportValue from(CapPlantatieProd exportableNom) {
        if (exportableNom != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(exportableNom.getCodRand()));
            nomenclatorExportValue.setDescription(exportableNom.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(exportableNom.getDataStart()));
            if (exportableNom.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(exportableNom.getDataStop()));
            }
            if (exportableNom.getCapPlantatieProd() != null) {
                nomenclatorExportValue.setParentCode(String.valueOf(exportableNom.getCapPlantatieProd().getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }
    public static NomenclatorExportValue from(CapSistemTehnic exportableNom) {
        if (exportableNom != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(exportableNom.getCodRand()));
            nomenclatorExportValue.setDescription(exportableNom.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(exportableNom.getDataStart()));
            if (exportableNom.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(exportableNom.getDataStop()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }
    public static NomenclatorExportValue from(CapModUtilizare exportableNom){
        if (exportableNom != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(exportableNom.getCodRand()));
            nomenclatorExportValue.setDescription(exportableNom.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(exportableNom.getDataStart()));
            if (exportableNom.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(exportableNom.getDataStop()));
            }
            if (exportableNom.getCapModUtilizare()!= null) {
                nomenclatorExportValue.setParentCode(String.valueOf(exportableNom.getCapModUtilizare().getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }
    public static NomenclatorExportValue from(CapFructProd exportableNom) {
        if (exportableNom != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(exportableNom.getCodRand()));
            nomenclatorExportValue.setDescription(exportableNom.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(exportableNom.getDataStart()));
            if (exportableNom.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(exportableNom.getDataStop()));
            }
            if (exportableNom.getCapFructProd()!= null) {
                nomenclatorExportValue.setParentCode(String.valueOf(exportableNom.getCapFructProd().getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }
    // CapTerenIrigat
    public static NomenclatorExportValue from(CapCategorieAnimal exportableNom){
        if (exportableNom != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(exportableNom.getCodRand()));
            nomenclatorExportValue.setDescription(exportableNom.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(exportableNom.getDataStart()));
            if (exportableNom.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(exportableNom.getDataStop()));
            }
            if (exportableNom.getCapCategorieAnimal() != null) {
                nomenclatorExportValue.setParentCode(String.valueOf(exportableNom.getCapCategorieAnimal().getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }
    public static NomenclatorExportValue from(CapCategorieFolosinta exportableNom){
        if (exportableNom != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(exportableNom.getCodRand()));
            nomenclatorExportValue.setDescription(exportableNom.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(exportableNom.getDataStart()));
            if (exportableNom.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(exportableNom.getDataStop()));
            }
            if (exportableNom.getCapCategorieFolosinta() != null) {
                nomenclatorExportValue.setParentCode(String.valueOf(exportableNom.getCapCategorieFolosinta().getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }

    public static NomenclatorExportValue from(CapSubstantaChimica exportableNom){
        if (exportableNom != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(exportableNom.getCodRand()));
            nomenclatorExportValue.setDescription(exportableNom.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(exportableNom.getDataStart()));
            if (exportableNom.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(exportableNom.getDataStop()));
            }
            if (exportableNom.getCapSubstantaChimica() != null) {
                nomenclatorExportValue.setParentCode(String.valueOf(exportableNom.getCapSubstantaChimica().getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }

    public static NomenclatorExportValue from(CapTerenIrigat exportableNom){
        if (exportableNom != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(exportableNom.getCodRand()));
            nomenclatorExportValue.setDescription(exportableNom.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(exportableNom.getDataStart()));
            if (exportableNom.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(exportableNom.getDataStop()));
            }
            if (exportableNom.getCapTerenIrigat() != null) {
                nomenclatorExportValue.setParentCode(String.valueOf(exportableNom.getCapTerenIrigat().getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }


    public static NomenclatorExportValue from(CapPomRazlet exportableNom){
        if (exportableNom != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(exportableNom.getCodRand()));
            nomenclatorExportValue.setDescription(exportableNom.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(exportableNom.getDataStart()));
            if (exportableNom.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(exportableNom.getDataStop()));
            }
            if (exportableNom.getCapPomRazlet() != null) {
                nomenclatorExportValue.setParentCode(String.valueOf(exportableNom.getCapPomRazlet().getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }

    public static NomenclatorExportValue from(CapAplicareIngrasamant exportableNom){
        if (exportableNom != null) {
            NomenclatorExportValue nomenclatorExportValue = new NomenclatorExportValue();
            nomenclatorExportValue.setCode(String.valueOf(exportableNom.getCodRand()));
            nomenclatorExportValue.setDescription(exportableNom.getDenumire());
            nomenclatorExportValue.setValidFrom(DATE_FORMATTER.format(exportableNom.getDataStart()));
            if (exportableNom.getDataStop() != null) {
                nomenclatorExportValue.setValidTo(DATE_FORMATTER.format(exportableNom.getDataStop()));
            }
            if (exportableNom.getCapAplicareIngrasamant() != null) {
                nomenclatorExportValue.setParentCode(String.valueOf(exportableNom.getCapAplicareIngrasamant().getCodRand()));
            }
            return nomenclatorExportValue;
        } else {
            return null;
        }
    }

    public static <T> NomenclatorExportValue from(T exportableNomenclator) {
        T exportableNom = (T) exportableNomenclator;
        if(exportableNom instanceof CapFructProd){
            return from((CapFructProd) exportableNom);
        } else if(exportableNom instanceof CapModUtilizare){
            return from((CapModUtilizare) exportableNom);
        } else if(exportableNom instanceof CapSistemTehnic){
            return from((CapSistemTehnic) exportableNom);
        } else if(exportableNom instanceof CapPlantatieProd){
            return from((CapPlantatieProd) exportableNom);
        } else if(exportableNom instanceof CapPlantatie){
            return from((CapPlantatie) exportableNom);
        } else if(exportableNom instanceof CapCulturaProd){
            return from((CapCulturaProd) exportableNom);
        } else if(exportableNom instanceof CapCultura){
            return from((CapCultura) exportableNom);
        } else if(exportableNom instanceof NomTipUnitateMasura){
            return from((NomTipUnitateMasura) exportableNom);
        } else if(exportableNom instanceof NomUnitateMasura){
            return from((NomUnitateMasura) exportableNom);
        } else if(exportableNom instanceof CapCategorieAnimal){
            return from((CapCategorieAnimal) exportableNom);
        } else if(exportableNom instanceof CapCategorieFolosinta){
            return from((CapCategorieFolosinta) exportableNom);
        } else if(exportableNom instanceof CapPomRazlet){
            return from((CapPomRazlet) exportableNom);
        } else if(exportableNom instanceof CapSubstantaChimica){
            return from((CapSubstantaChimica) exportableNom);
        } else if(exportableNom instanceof CapAplicareIngrasamant){
            return from((CapAplicareIngrasamant) exportableNom);
        } else if(exportableNom instanceof CapTerenIrigat){
            return from((CapTerenIrigat) exportableNom);
        } else {
            throw new IllegalArgumentException("Nu este implementata o conversie pentru tipul:"+exportableNom.getClass().getSimpleName());
        }
    }
}
