package ro.uti.ran.core.service.backend.nomenclator.model;


import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.service.backend.nomenclator.type.SqlConditionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: mala
 */
public enum NomenclatorCodeType implements ValidityNomenclator, ExportableNomenclator {

    // @formatter:off
    /**
     * Nom simple
     */
    NomFormaOrganizareRc(TipNomenclator.NomFormaOrganizareRc, "NOM_FORMA_ORGANIZARE_RC", "COD", "DENUMIRE", "formaOrganizareRC", SqlConditionType.SIMPLE, false),
    NomIndicativXml(TipNomenclator.NomIndicativXml, "NOM_INDICATIV_XML", "COD", "DENUMIRE", "", SqlConditionType.SIMPLE,true,false,false),
    //todo: nu apare in xls
    NomTipContract(TipNomenclator.NomTipContract, "NOM_TIP_CONTRACT", "COD", "DENUMIRE", "TIP_CONTRACT", SqlConditionType.SIMPLE,true,false, false),
    NomTipRelPreemptiune(TipNomenclator.NomTipRelPreemptiune, "NOM_TIP_REL_PREEMPTIUNE", "COD", "DENUMIRE", "TIP_REL_PREEMPTIUNE", SqlConditionType.SIMPLE, false),
    NomTipSpatiuProt(TipNomenclator.NomTipSpatiuProt, "NOM_TIP_SPATIU_PROT", "COD", "DENUMIRE", "TIP_SPATIU_PROT", SqlConditionType.SIMPLE, false),
    NomTipUnitateMasura(TipNomenclator.NomTipUnitateMasura, "NOM_TIP_UNITATE_MASURA", "COD", "DENUMIRE", "", SqlConditionType.SIMPLE,true,false, false),

    /**
     * Nom versionate
     */
    NomCapitol(TipNomenclator.NomCapitol, "NOM_CAPITOL", "COD", "DENUMIRE", "DESCRIERE", SqlConditionType.WITH_DATE_BETWEEN, true, true, false),
    NomDestinatieCladire(TipNomenclator.NomDestinatieCladire, "NOM_DESTINATIE_CLADIRE", "COD", "DENUMIRE", "DESTINATIE_CLADIRE",SqlConditionType.WITH_DATE_BETWEEN,true,true,true),
    NomLegaturaRudenie(TipNomenclator.NomLegaturaRudenie, "NOM_LEGATURA_RUDENIE", "COD", "DENUMIRE", "LEGATURA_RUDENIE",SqlConditionType.WITH_DATE_BETWEEN,true,true,false),
    NomModalitateDetinere(TipNomenclator.NomModalitateDetinere, "NOM_MODALITATE_DETINERE", "COD", "DENUMIRE", "MODALITATE_DETINERE",SqlConditionType.WITH_DATE_BETWEEN,true,true,true),
    NomTipAct(TipNomenclator.NomTipAct, "NOM_TIP_ACT", "COD", "DENUMIRE", "",SqlConditionType.WITH_DATE_BETWEEN,true,true,true),
    NomTipCladire(TipNomenclator.NomTipCladire, "NOM_TIP_CLADIRE", "COD", "DENUMIRE", "TIP_CLADIRE",SqlConditionType.WITH_DATE_BETWEEN,true,true,true),
    NomTipDetinator(TipNomenclator.NomTipDetinator, "NOM_TIP_DETINATOR", "COD", "DENUMIRE", "TIP_DETINATOR",SqlConditionType.WITH_DATE_BETWEEN,true,true,true),
    NomTipExploatatie(TipNomenclator.NomTipExploatatie, "NOM_TIP_EXPLOATATIE", "COD", "DENUMIRE", "TIP_EXPLOATATIE",SqlConditionType.WITH_DATE_BETWEEN,true,true,true),
    NomTipLocalizare(TipNomenclator.NomTipLocalizare, "NOM_TIP_LOCALIZARE", "COD", "DENUMIRE", "TIP_LOCALIZARE",SqlConditionType.WITH_DATE_BETWEEN,true,true,true),
    /**
     * Nom neexportabile
     */

    NomRenns(TipNomenclator.NomRenn,"NOM_RENNS","UID_RENNS","","Referinta RENNS",SqlConditionType.SIMPLE, true, false, false),
    NomSursaRegistru(TipNomenclator.NomSursaRegistru, "NOM_SURSA_REGISTRU", "COD", "DENUMIRE", "", SqlConditionType.SIMPLE, true, false, false),
    NomUat(TipNomenclator.NomUat, "NOM_UAT", "COD_SIRUTA", "DENUMIRE", "UAT", SqlConditionType.WITH_DATE_BETWEEN, true, false, true),
    NomJudet(TipNomenclator.NomJudet, "NOM_JUDET", "COD_SIRUTA", "DENUMIRE", "JUDET",SqlConditionType.WITH_DATE_BETWEEN, true, false, true),
    NomLocalitate(TipNomenclator.NomLocalitate, "NOM_LOCALITATE", "COD_SIRUTA", "DENUMIRE", "LOCALITATE", SqlConditionType.WITH_DATE_BETWEEN, true, false, true),
    NomTara(TipNomenclator.NomTara, "NOM_TARA", "COD_NUMERIC", "DENUMIRE", "TARA", SqlConditionType.SIMPLE, true, false, true),
    NomTipAdresa(TipNomenclator.NomTipAdresa, "NOM_TIP_ADRESA", "COD", "DENUMIRE", "TIP_ADRESA", SqlConditionType.SIMPLE, true, false, false),
    NomStareRegistru(TipNomenclator.NomStareRegistru, "NOM_STARE_REGISTRU", "COD", "DENUMIRE", "", SqlConditionType.SIMPLE, true, false, false),
    /**
     * Nom compuse
     */
    /**
     * Nom compuse - simple neversionate dar cu parinte
     */
    NomUnitateDeMasura(TipNomenclator.NomUnitateMasura, "NOM_UNITATE_MASURA", "COD", "DENUMIRE", "", SqlConditionType.COMPLEX, true, false, false),
    NomCategorieFolosinta(TipNomenclator.NomCategorieFolosinta, "NOM_CATEGORIE_FOLOSINTA", "COD", "DENUMIRE", "codCategFolosinta", SqlConditionType.WITH_PARENT, true, false, false),
    NomPlantaCultura(TipNomenclator.NomPlantaCultura,"NOM_PLANTA_CULTURA","COD","DENUMIRE","",SqlConditionType.WITH_PARENT,true,false,true),
    NomPomArbust(TipNomenclator.NomPomArbust,"NOM_POM_ARBUST","COD","DENUMIRE","",SqlConditionType.WITH_PARENT,true,false,false),
    NomSpecieAnimal(TipNomenclator.NomSpecieAnimal,"NOM_SPECIE_ANIMAL","COD","DENUMIRE","",SqlConditionType.WITH_PARENT,true,false,false),
    /**
     * Nom compuse
     */
    CapCategorieAnimal(TipNomenclator.CapCategorieAnimal, "CAP_CATEGORIE_ANIMAL", "COD_RAND", "DENUMIRE", "", SqlConditionType.COMPLEX, true, true, true),
    CapCategorieFolosinta(TipNomenclator.CapCategorieFolosinta, "CAP_CATEGORIE_FOLOSINTA", "COD_RAND", "DENUMIRE", "categorie_teren", SqlConditionType.COMPLEX, true, true, true),
    CapPomRazlet(TipNomenclator.CapPomRazlet, "CAP_POM_RAZLET", "COD_RAND", "DENUMIRE", "pom_razlet", SqlConditionType.COMPLEX, true, true, true),
    CapSubstantaChimica(TipNomenclator.CapSubstantaChimica, "CAP_SUBSTANTA_CHIMICA", "COD_RAND", "DENUMIRE", "culturi_ingrasaminte_chimice", SqlConditionType.COMPLEX, true, true, true),
    CapAplicareIngrasamant(TipNomenclator.CapAplicareIngrasamant, "CAP_APLICARE_INGRASAMANT", "COD_RAND", "DENUMIRE", "substanta_chimica_agricola", SqlConditionType.COMPLEX, true, true, true),
    CapTerenIrigat(TipNomenclator.CapTerenIrigat, "CAP_TEREN_IRIGAT", "COD_RAND", "DENUMIRE", "cultura_irigata", SqlConditionType.COMPLEX, true, true, true),


    CapFructProd(TipNomenclator.CapFructProd, "CAP_FRUCT_PROD", "COD_RAND", "DENUMIRE", "productie_fructe_in_teren_agricol", SqlConditionType.COMPLEX, true, true, true),
    CapCultura(TipNomenclator.CapCultura, "CAP_CULTURA", "COD_RAND", "DENUMIRE", "", SqlConditionType.COMPLEX, true, true, true),
    CapCulturaProd(TipNomenclator.CapCulturaProd, "CAP_CULTURA_PROD", "COD_RAND", "DENUMIRE", "", SqlConditionType.COMPLEX, true, true, true),
    CapAnimalProd(TipNomenclator.CapAnimalProd, "CAP_ANIMAL_PROD", "COD_RAND", "DENUMIRE", "productie_animala", SqlConditionType.COMPLEX, true, true, true),
    CapPlantatie(TipNomenclator.CapPlantatie, "CAP_PLANTATIE", "COD_RAND", "DENUMIRE", "", SqlConditionType.COMPLEX, true, true, true),
    CapPlantatieProd(TipNomenclator.CapPlantatieProd, "CAP_PLANTATIE_PROD", "COD_RAND", "DENUMIRE", "DESCRIERE", SqlConditionType.COMPLEX, true, true, true),
    CapSistemTehnic(TipNomenclator.CapSistemTehnic, "CAP_SISTEM_TEHNIC", "COD_RAND", "DENUMIRE", "sistem_tehnic_agricol",SqlConditionType.COMPLEX,true,true,true),
    CapModUtilizare(TipNomenclator.CapModUtilizare, "CAP_MOD_UTILIZARE", "COD_RAND", "DENUMIRE", "mod_utilizare_suprafete_agricole", SqlConditionType.COMPLEX, true, true, true),
    ;
    // @formatter:on


    private final TipNomenclator nomType;
    private final String table;
    private final String codeColumn;
    private final String descriptionColumn;
    private final String label;
    private final SqlConditionType conditionType;
    private final boolean isActive;
    private final boolean exportable;
    private final boolean appUpdatable;

    NomenclatorCodeType(TipNomenclator nomType, String table, String codeColumn, String descriptionColumn, String label,
                        SqlConditionType conditionType, boolean active, boolean isExportable, boolean appUpdatable) {
        this.nomType = nomType;
        this.table = table;
        this.codeColumn = codeColumn;
        this.conditionType = conditionType;
        this.descriptionColumn = descriptionColumn;
        this.label = label;
        this.isActive = active;
        this.exportable = isExportable;
        this.appUpdatable = appUpdatable;
    }

    NomenclatorCodeType(TipNomenclator nomType, String table, String codeColumn, String descriptionColumn, String label,
                        SqlConditionType conditionType) {
        this(nomType, table, codeColumn, descriptionColumn, label, conditionType, true, true, true);
    }

    NomenclatorCodeType(TipNomenclator nomType, String table, String codeColumn, String descriptionColumn, String label,
                        SqlConditionType conditionType, boolean appUpdatable) {
        this(nomType, table, codeColumn, descriptionColumn, label, conditionType, true, true, appUpdatable);
    }

    NomenclatorCodeType(TipNomenclator nomType, String table, String codeColumn, String descriptionColumn, String label) {
        this(nomType, table, codeColumn, descriptionColumn, label, SqlConditionType.WITH_DATE_BETWEEN, true, false, true);
    }

    public static NomenclatorCodeType fromNomType(TipNomenclator nomType) {
        if (nomType != null) {
            for (NomenclatorCodeType catalogCodeType : NomenclatorCodeType.values()) {
                if (catalogCodeType.getNomType() == nomType)
                    return catalogCodeType;
            }
        }

        return null;
    }

    public static List<ExportableNomenclator> getAllExportableCatalogCodeTypes() {
        List<ExportableNomenclator> exportableNomenclators = new ArrayList<ExportableNomenclator>();
        for (ExportableNomenclator exportableNomenclator : NomenclatorCodeType.values()) {
            if (exportableNomenclator.isExportable()) {
                    exportableNomenclators.add(exportableNomenclator);
            }
        }

        return exportableNomenclators;
    }
    public String getNumeNomenclator(ro.uti.ran.core.model.registru.NomCapitol nomCapitol){
       return getName() + "-" + nomCapitol.getCod().toString();
    }

    @Override
    public TipNomenclator getNomType() {
        return this.nomType;
    }

    @Override
    public String getName() {
        return this.nomType.getClazz().getSimpleName();
    }

    @Override
    public boolean isExportable() {
        return exportable;
    }

    @Override
    public String getCodeColumn() {
        return codeColumn;
    }

    @Override
    public String getDescriptionColumn() {
        return descriptionColumn;
    }

    @Override
    public String getTableName() {
        return table;
    }

    @Override
    public SqlConditionType getConditionType() {
        return conditionType;
    }

    @Override
    public boolean isAppUpdatable() {
        return appUpdatable;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    public String getLabel() {
        return label;
    }


}
