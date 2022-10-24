package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.CapCategorieFolosinta;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.SuprafataCategorie;
import ro.uti.ran.core.service.backend.utils.SuprafataHelper;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_2a;
import ro.uti.ran.core.xml.model.types.FRACTIE_DOUA_ZECIMALE;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * Created by Dan on 15-Oct-15.
 */
public class RandCapitol_2aConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param suprafataCategorie entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_2a toSchemaType(SuprafataCategorie suprafataCategorie) {
        if (suprafataCategorie == null) {
            throw new IllegalArgumentException("SuprafataCategorie nedefinit!");
        }
        SectiuneCapitol_2a sectiuneCapitol_2A = new SectiuneCapitol_2a();
        /*codNomenclator*/
        sectiuneCapitol_2A.setCodNomenclator(suprafataCategorie.getCapCategorieFolosinta().getCod());
        /*codRand*/
        sectiuneCapitol_2A.setCodRand(suprafataCategorie.getCapCategorieFolosinta().getCodRand());
        /*denumire*/
        sectiuneCapitol_2A.setDenumire(suprafataCategorie.getCapCategorieFolosinta().getDenumire());
        //
        INTREG_POZITIV intreg_pozitiv;
        FRACTIE_DOUA_ZECIMALE fractie_doua_zecimale;
        /*totalHA + totalARI*/
        if (suprafataCategorie.getSuprafataTotal() != null) {
            Map<String, Object> tmp = SuprafataHelper.extractHAAndARIFromMP(BigInteger.valueOf(suprafataCategorie.getSuprafataTotal()));
            if (tmp.get(SuprafataHelper.KEY_HA) != null) {
                BigInteger value = (BigInteger) tmp.get(SuprafataHelper.KEY_HA);
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(value.intValue());
                sectiuneCapitol_2A.setTotalHA(intreg_pozitiv);
            } else {
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(0);
                sectiuneCapitol_2A.setTotalHA(intreg_pozitiv);
            }
            if (tmp.get(SuprafataHelper.KEY_ARI) != null) {
                BigDecimal value = (BigDecimal) tmp.get(SuprafataHelper.KEY_ARI);
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(value);
                sectiuneCapitol_2A.setTotalARI(fractie_doua_zecimale);
            } else {
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(BigDecimal.ZERO);
                sectiuneCapitol_2A.setTotalARI(fractie_doua_zecimale);
            }
        }
        /*localHA + localARI*/
        if (suprafataCategorie.getSuprafataLocal() != null) {
            Map<String, Object> tmp = SuprafataHelper.extractHAAndARIFromMP(BigInteger.valueOf(suprafataCategorie.getSuprafataLocal()));
            if (tmp.get(SuprafataHelper.KEY_HA) != null) {
                BigInteger value = (BigInteger) tmp.get(SuprafataHelper.KEY_HA);
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(value.intValue());
                sectiuneCapitol_2A.setLocalHA(intreg_pozitiv);
            } else {
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(0);
                sectiuneCapitol_2A.setLocalHA(intreg_pozitiv);
            }
            if (tmp.get(SuprafataHelper.KEY_ARI) != null) {
                BigDecimal value = (BigDecimal) tmp.get(SuprafataHelper.KEY_ARI);
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(value);
                sectiuneCapitol_2A.setLocalARI(fractie_doua_zecimale);
            } else {
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(BigDecimal.ZERO);
                sectiuneCapitol_2A.setLocalARI(fractie_doua_zecimale);
            }
        }
        /*altelocHA + altelocARI*/
        if (suprafataCategorie.getSuprafataAlt() != null) {
            Map<String, Object> tmp = SuprafataHelper.extractHAAndARIFromMP(BigInteger.valueOf(suprafataCategorie.getSuprafataAlt()));
            if (tmp.get(SuprafataHelper.KEY_HA) != null) {
                BigInteger value = (BigInteger) tmp.get(SuprafataHelper.KEY_HA);
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(value.intValue());
                sectiuneCapitol_2A.setAltelocHA(intreg_pozitiv);
            } else {
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(0);
                sectiuneCapitol_2A.setAltelocHA(intreg_pozitiv);
            }
            if (tmp.get(SuprafataHelper.KEY_ARI) != null) {
                BigDecimal value = (BigDecimal) tmp.get(SuprafataHelper.KEY_ARI);
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(value);
                sectiuneCapitol_2A.setAltelocARI(fractie_doua_zecimale);
            } else {
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(BigDecimal.ZERO);
                sectiuneCapitol_2A.setAltelocARI(fractie_doua_zecimale);
            }
        }

        return sectiuneCapitol_2A;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_2A jaxb pojo
     * @param gospodarie         entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_2a sectiuneCapitol_2A, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_2A == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "sectiuneCapitol_2A");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "anRaportare");
        }
        SuprafataCategorie suprafataCategorie = new SuprafataCategorie();
        CapCategorieFolosinta capCategorieFolosinta = new CapCategorieFolosinta();
        /*codNomenclator*/
        capCategorieFolosinta.setCod(sectiuneCapitol_2A.getCodNomenclator());
        /*codRand*/
        capCategorieFolosinta.setCodRand(sectiuneCapitol_2A.getCodRand());
        /*denumire*/
        capCategorieFolosinta.setDenumire(sectiuneCapitol_2A.getDenumire());
        //
        suprafataCategorie.setCapCategorieFolosinta(capCategorieFolosinta);
        /*anRaportare*/
        suprafataCategorie.setAn(anRaportare);
        /*totalHA + totalARI*/
        suprafataCategorie.setSuprafataTotal(
                SuprafataHelper.transformToMP(
                        (sectiuneCapitol_2A.getTotalARI() != null) ? sectiuneCapitol_2A.getTotalARI().getValue() : null,
                        (sectiuneCapitol_2A.getTotalHA() != null) ? sectiuneCapitol_2A.getTotalHA().getValue() : null
                ));
        /*localHA + localARI*/
        suprafataCategorie.setSuprafataLocal(
                SuprafataHelper.transformToMP(
                        (sectiuneCapitol_2A.getLocalARI() != null) ? sectiuneCapitol_2A.getLocalARI().getValue() : null,
                        (sectiuneCapitol_2A.getLocalHA() != null) ? sectiuneCapitol_2A.getLocalHA().getValue() : null
                ));
        /*altelocHA + altelocARI*/
        suprafataCategorie.setSuprafataAlt(
                SuprafataHelper.transformToMP(
                        (sectiuneCapitol_2A.getAltelocARI() != null) ? sectiuneCapitol_2A.getAltelocARI().getValue() : null,
                        (sectiuneCapitol_2A.getAltelocHA() != null) ? sectiuneCapitol_2A.getAltelocHA().getValue() : null
                ));
         /*populare entity pojo*/
        gospodarie.addSuprafataCategory(suprafataCategorie);
    }
}
