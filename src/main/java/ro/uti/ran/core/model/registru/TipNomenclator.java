package ro.uti.ran.core.model.registru;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;

import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Tipurile de nomenclator,
 * Metadate nomenclator.
 * <p/>
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-02 15:00
 */
@XmlSeeAlso({
        CapAnimalProd.class,
        NomCapitol.class,
        CapCategorieAnimal.class,
        NomCategorieFolosinta.class,
        CapCultura.class,
        CapCulturaProd.class,
        NomDestinatieCladire.class,
        Nomenclator.class,
        NomFormaOrganizareRc.class,
        CapFructProd.class,
        NomIndicativXml.class,
        NomJudet.class,
        NomLegaturaRudenie.class,
        NomLocalitate.class,
        NomModalitateDetinere.class,
        CapModUtilizare.class,
        CapPomRazlet.class,
        NomRenn.class,
//        NomRezolutieCerere.class,
        CapSistemTehnic.class,
        NomStareCerere.class,
        NomStareRegistru.class,
        CapPlantatieProd.class,
        CapSubstantaChimica.class,
        NomSursaRegistru.class,
        NomTara.class,
        CapTerenIrigat.class,
        NomTipAct.class,
//        NomTipActInstrainare.class,
        NomTipAdresa.class,
        NomTipCladire.class,
        NomTipContract.class,
        NomTipDetinator.class,
        NomTipExploatatie.class,
        NomTipLocalizare.class,
        NomTipSpatiuProt.class,
        NomTipUnitateMasura.class,
        NomUat.class,
        NomUnitateMasura.class,
        CapAplicareIngrasamant.class,
        NomCategorieFolosinta.class,
        NomPlantaCultura.class,
        NomPomArbust.class,
        NomSpecieAnimal.class
})
public enum TipNomenclator {
    CapAnimalProd(CapAnimalProd.class),
    NomCapitol(NomCapitol.class),
    CapCategorieAnimal(CapCategorieAnimal.class),
    CapCategorieFolosinta(CapCategorieFolosinta.class),
    CapCultura(CapCultura.class),
    CapCulturaProd(CapCulturaProd.class),
    NomDestinatieCladire(NomDestinatieCladire.class),
    Nomenclator(Nomenclator.class),
    NomFormaOrganizareRc(NomFormaOrganizareRc.class),
    CapFructProd(CapFructProd.class),
    NomIndicativXml(NomIndicativXml.class),
    NomJudet(NomJudet.class),
    NomLegaturaRudenie(NomLegaturaRudenie.class),
    NomLocalitate(NomLocalitate.class),
    NomModalitateDetinere(NomModalitateDetinere.class),
    CapModUtilizare(CapModUtilizare.class),
    CapPlantatie(CapPlantatie.class),
    CapPomRazlet(CapPomRazlet.class),
    NomRenn(NomRenn.class),
//    NomRezolutieCerere(NomRezolutieCerere.class),
    CapSistemTehnic(CapSistemTehnic.class),
    NomStareCerere(NomStareCerere.class),
    NomStareRegistru(NomStareRegistru.class),
    CapPlantatieProd(CapPlantatieProd.class),
    CapSubstantaChimica(CapSubstantaChimica.class),
    NomSursaRegistru(NomSursaRegistru.class),
    NomTara(NomTara.class),
    CapTerenIrigat(CapTerenIrigat.class),
    NomTipAct(NomTipAct.class),
    NomTipAdresa(NomTipAdresa.class),
    NomTipCladire(NomTipCladire.class),
    NomTipContract(NomTipContract.class),
    NomTipDetinator(NomTipDetinator.class),
    NomTipExploatatie(NomTipExploatatie.class),
    NomTipLocalizare(NomTipLocalizare.class),
    NomTipSpatiuProt(NomTipSpatiuProt.class),
    NomTipUnitateMasura(NomTipUnitateMasura.class),
    NomUat(NomUat.class),
    NomUnitateMasura(NomUnitateMasura.class),
    CapAplicareIngrasamant(CapAplicareIngrasamant.class),
    NomTipRelPreemptiune(NomTipRelPreemptiune.class),
    NomCategorieFolosinta(NomCategorieFolosinta.class),
    NomPlantaCultura(NomPlantaCultura.class),
    NomPomArbust(NomPomArbust.class),
    NomSpecieAnimal(NomSpecieAnimal.class)
    ;


    private Class<? extends Nomenclator> clazz;

    TipNomenclator() {
    }

    TipNomenclator(Class<? extends Nomenclator> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Nomenclator> getClazz() {
        return clazz;
    }

    public void setClazz(Class<Nomenclator> clazz) {
        this.clazz = clazz;
    }

    public static TipNomenclator checkCodNomenclator(String codNomenclator) throws RequestValidationException {
        for(TipNomenclator tipNomanclator : values()) {
            if(tipNomanclator.name().equals(codNomenclator)) {
                return tipNomanclator;
            }
        }

        throw new RequestValidationException(RequestCodes.INVALID_ELEMENT, "tipNomanclator");
    }
}
