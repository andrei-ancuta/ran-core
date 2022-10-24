package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.CapSubstantaChimica;
import ro.uti.ran.core.model.registru.SubstantaChimica;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_10b;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

/**
 * Created by Dan on 30-Oct-15.
 */
public class RandCapitol_10bConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param substantaChimica entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_10b toSchemaType(SubstantaChimica substantaChimica) {
        if (substantaChimica == null) {
            throw new IllegalArgumentException("SubstantaChimica nedefinit!");
        }
        SectiuneCapitol_10b sectiuneCapitol_10B = new SectiuneCapitol_10b();
        /*codNomenclator + codRand + denumire*/
        sectiuneCapitol_10B.setCodNomenclator(substantaChimica.getCapSubstantaChimica().getCod());
        sectiuneCapitol_10B.setCodRand(substantaChimica.getCapSubstantaChimica().getCodRand());
        sectiuneCapitol_10B.setDenumire(substantaChimica.getCapSubstantaChimica().getDenumire());
        INTREG_POZITIV intreg_pozitiv;
        /*totalHA*/
        if (substantaChimica.getSuprafataTotal() != null) {
            intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(substantaChimica.getSuprafataTotal());
            sectiuneCapitol_10B.setTotalHA(intreg_pozitiv);
        }
        /*totalKG*/
        if (substantaChimica.getCantitateTotal() != null) {
            intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(substantaChimica.getCantitateTotal());
            sectiuneCapitol_10B.setTotalKG(intreg_pozitiv);
        }
        /*nrHAazotoase*/
        if (substantaChimica.getSuprafataAzotoase() != null) {
            intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(substantaChimica.getSuprafataAzotoase());
            sectiuneCapitol_10B.setNrHAazotoase(intreg_pozitiv);
        }
        /*nrKGazotoase*/
        if (substantaChimica.getCantitateAzotoase() != null) {
            intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(substantaChimica.getCantitateAzotoase());
            sectiuneCapitol_10B.setNrKGazotoase(intreg_pozitiv);
        }
        /*nrHAfosfatice*/
        if (substantaChimica.getSuprafataFosfatice() != null) {
            intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(substantaChimica.getSuprafataFosfatice());
            sectiuneCapitol_10B.setNrHAfosfatice(intreg_pozitiv);
        }
        /*nrKGfosfatice*/
        if (substantaChimica.getCantitateFosfatice() != null) {
            intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(substantaChimica.getCantitateFosfatice());
            sectiuneCapitol_10B.setNrKGfosfatice(intreg_pozitiv);
        }
        /*nrHApotasice*/
        if (substantaChimica.getSuprafataPotasice() != null) {
            intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(substantaChimica.getSuprafataPotasice());
            sectiuneCapitol_10B.setNrHApotasice(intreg_pozitiv);
        }
        /*nrKGpotasice*/
        if (substantaChimica.getCantitatePotasice() != null) {
            intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(substantaChimica.getCantitatePotasice());
            sectiuneCapitol_10B.setNrKGpotasice(intreg_pozitiv);
        }
        return sectiuneCapitol_10B;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_10B jaxb pojo
     * @param gospodarie      entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_10b sectiuneCapitol_10B, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_10B == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_10B");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        SubstantaChimica substantaChimica = new SubstantaChimica();
        /*an*/
        substantaChimica.setAn(anRaportare);
        /*codNomenclator + codRand + denumire*/
        CapSubstantaChimica capSubstantaChimica = new CapSubstantaChimica();
        capSubstantaChimica.setCod(sectiuneCapitol_10B.getCodNomenclator());
        capSubstantaChimica.setCodRand(sectiuneCapitol_10B.getCodRand());
        capSubstantaChimica.setDenumire(sectiuneCapitol_10B.getDenumire());
        substantaChimica.setCapSubstantaChimica(capSubstantaChimica);
        /*totalHA*/
        if (sectiuneCapitol_10B.getTotalHA() != null) {
            substantaChimica.setSuprafataTotal(sectiuneCapitol_10B.getTotalHA().getValue());
        }
        /*totalKG*/
        if (sectiuneCapitol_10B.getTotalKG() != null) {
            substantaChimica.setCantitateTotal(sectiuneCapitol_10B.getTotalKG().getValue());
        }
        /*nrHAazotoase*/
        if (sectiuneCapitol_10B.getNrHAazotoase() != null) {
            substantaChimica.setSuprafataAzotoase(sectiuneCapitol_10B.getNrHAazotoase().getValue());
        }
        /*nrKGazotoase*/
        if (sectiuneCapitol_10B.getNrKGazotoase() != null) {
            substantaChimica.setCantitateAzotoase(sectiuneCapitol_10B.getNrKGazotoase().getValue());
        }
        /*nrHAfosfatice*/
        if (sectiuneCapitol_10B.getNrHAfosfatice() != null) {
            substantaChimica.setSuprafataFosfatice(sectiuneCapitol_10B.getNrHAfosfatice().getValue());
        }
        /*nrKGfosfatice*/
        if (sectiuneCapitol_10B.getNrKGfosfatice() != null) {
            substantaChimica.setCantitateFosfatice(sectiuneCapitol_10B.getNrKGfosfatice().getValue());
        }
        /*nrHApotasice*/
        if (sectiuneCapitol_10B.getNrHApotasice() != null) {
            substantaChimica.setSuprafataPotasice(sectiuneCapitol_10B.getNrHApotasice().getValue());
        }
        /*nrKGpotasice*/
        if (sectiuneCapitol_10B.getNrKGpotasice() != null) {
            substantaChimica.setCantitatePotasice(sectiuneCapitol_10B.getNrKGpotasice().getValue());
        }
         /*populare entity pojo*/
        gospodarie.addSubstantaChimica(substantaChimica);
    }
}
