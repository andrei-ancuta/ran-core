package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.CapModUtilizare;
import ro.uti.ran.core.model.registru.GeometrieSuprafataUtiliz;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.SuprafataUtilizare;
import ro.uti.ran.core.service.backend.utils.SuprafataHelper;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_3;
import ro.uti.ran.core.xml.model.types.FRACTIE_DOUA_ZECIMALE;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dan on 15-Oct-15.
 */
public class RandCapitol_3ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param suprafataUtilizare entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_3 toSchemaType(SuprafataUtilizare suprafataUtilizare) {
        if (suprafataUtilizare == null) {
            throw new IllegalArgumentException("SuprafataUtilizare nedefinit!");
        }
        SectiuneCapitol_3 sectiuneCapitol_3 = new SectiuneCapitol_3();
        /*codNomenclator + codRand + denumire*/
        sectiuneCapitol_3.setCodNomenclator(suprafataUtilizare.getCapModUtilizare().getCod());
        sectiuneCapitol_3.setCodRand(suprafataUtilizare.getCapModUtilizare().getCodRand());
        sectiuneCapitol_3.setDenumire(suprafataUtilizare.getCapModUtilizare().getDenumire());
        /*nrHA + nrARI*/
        if (suprafataUtilizare.getSuprafata() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            FRACTIE_DOUA_ZECIMALE fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
            Map<String, Object> tmp = SuprafataHelper.extractHAAndARIFromMP(BigInteger.valueOf(suprafataUtilizare.getSuprafata()));
            if (tmp.get(SuprafataHelper.KEY_HA) != null) {
                BigInteger value = (BigInteger) tmp.get(SuprafataHelper.KEY_HA);
                intreg_pozitiv.setValue(value.intValue());
                sectiuneCapitol_3.setNrHA(intreg_pozitiv);
            } else {
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(0);
                sectiuneCapitol_3.setNrHA(intreg_pozitiv);
            }
            if (tmp.get(SuprafataHelper.KEY_ARI) != null) {
                BigDecimal value = (BigDecimal) tmp.get(SuprafataHelper.KEY_ARI);
                fractie_doua_zecimale.setValue(value);
                sectiuneCapitol_3.setNrARI(fractie_doua_zecimale);
            } else {
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(BigDecimal.ZERO);
                sectiuneCapitol_3.setNrARI(fractie_doua_zecimale);
            }
        }
         /*referintaGeoXml*/
        if (suprafataUtilizare.getGeometrieSuprafataUtiliz() != null) {
            List<String> geometrie = new ArrayList<>();
            for (GeometrieSuprafataUtiliz geometrieSuprafataUtiliz : suprafataUtilizare.getGeometrieSuprafataUtiliz()) {
                geometrie.add(geometrieSuprafataUtiliz.getGeometrieGML());
            }
            sectiuneCapitol_3.setReferintaGeoXml(geometrie);
        }
        return sectiuneCapitol_3;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_3 jaxb pojo
     * @param gospodarie        entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_3 sectiuneCapitol_3, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_3 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "sectiuneCapitol_3");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "gospodarie");
        }
        if (anRaportare == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "anRaportare");
        }
        SuprafataUtilizare suprafataUtilizare = new SuprafataUtilizare();
        /*an*/
        suprafataUtilizare.setAn(anRaportare);
        /*codNomenclator + codRand + denumire*/
        CapModUtilizare capModUtilizare = new CapModUtilizare();
        capModUtilizare.setCod(sectiuneCapitol_3.getCodNomenclator());
        capModUtilizare.setCodRand(sectiuneCapitol_3.getCodRand());
        capModUtilizare.setDenumire(sectiuneCapitol_3.getDenumire());
        suprafataUtilizare.setCapModUtilizare(capModUtilizare);
        /*nrHA + nrARI*/
        suprafataUtilizare.setSuprafata(SuprafataHelper.transformToMP(
                sectiuneCapitol_3.getNrARI() != null ? sectiuneCapitol_3.getNrARI().getValue() : null,
                sectiuneCapitol_3.getNrHA() != null ? sectiuneCapitol_3.getNrHA().getValue() : null
        ));
         /*referintaGeoXml*/
        List<GeometrieSuprafataUtiliz> listaGeo = new ArrayList<>();
        List<String> geoInfo = sectiuneCapitol_3.getReferintaGeoXml();
        if (geoInfo != null) {
            for (String element : geoInfo) {
                GeometrieSuprafataUtiliz geometrieSuprafataUtiliz = new GeometrieSuprafataUtiliz();
                geometrieSuprafataUtiliz.setGeometrieGML(element);
                listaGeo.add(geometrieSuprafataUtiliz);
            }
        }
        suprafataUtilizare.setGeometrieSuprafataUtiliz(listaGeo);
        /*populare entity pojo*/
        gospodarie.addSuprafataUtilizare(suprafataUtilizare);
    }
}
