package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.service.backend.utils.SuprafataHelper;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_6;
import ro.uti.ran.core.xml.model.types.FRACTIE_DOUA_ZECIMALE;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dan on 29-Oct-15.
 */
public class RandCapitol_6ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param terenIrigat entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_6 toSchemaType(TerenIrigat terenIrigat) {
        if (terenIrigat == null) {
            throw new IllegalArgumentException("TerenIrigat nedefinit!");
        }
        SectiuneCapitol_6 sectiuneCapitol_6 = new SectiuneCapitol_6();
        /*codNomenclator + codRand + denumire*/
        sectiuneCapitol_6.setCodNomenclator(terenIrigat.getCapTerenIrigat().getCod());
        sectiuneCapitol_6.setCodRand(terenIrigat.getCapTerenIrigat().getCodRand());
        sectiuneCapitol_6.setDenumire(terenIrigat.getCapTerenIrigat().getDenumire());
        /*nrHA + nrARI*/
        if (terenIrigat.getSuprafata() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            FRACTIE_DOUA_ZECIMALE fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
            Map<String, Object> tmp = SuprafataHelper.extractHAAndARIFromMP(BigInteger.valueOf(terenIrigat.getSuprafata()));
            if (tmp.get(SuprafataHelper.KEY_HA) != null) {
                BigInteger value = (BigInteger) tmp.get(SuprafataHelper.KEY_HA);
                intreg_pozitiv.setValue(value.intValue());
                sectiuneCapitol_6.setNrHA(intreg_pozitiv);
            } else {
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(0);
                sectiuneCapitol_6.setNrHA(intreg_pozitiv);
            }
            if (tmp.get(SuprafataHelper.KEY_ARI) != null) {
                BigDecimal value = (BigDecimal) tmp.get(SuprafataHelper.KEY_ARI);
                fractie_doua_zecimale.setValue(value);
                sectiuneCapitol_6.setNrARI(fractie_doua_zecimale);
            }else {
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(BigDecimal.ZERO);
                sectiuneCapitol_6.setNrARI(fractie_doua_zecimale);
            }
        }
        List<String> geometrie = new ArrayList<>();
        for(GeometrieTerenIrigat geometrieTerenIrigat:terenIrigat.getGeometrieTerenIrigats()){
            geometrie.add(geometrieTerenIrigat.getGeometrieGML());
        }
        sectiuneCapitol_6.setReferintaGeoXml(geometrie);
        return sectiuneCapitol_6;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_6 jaxb pojo
     * @param gospodarie    entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_6 sectiuneCapitol_6, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_6 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_6");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        TerenIrigat terenIrigat = new TerenIrigat();
        /*an*/
        terenIrigat.setAn(anRaportare);
        /*codNomenclator + codRand + denumire*/
        CapTerenIrigat capTerenIrigat = new CapTerenIrigat();
        capTerenIrigat.setCod(sectiuneCapitol_6.getCodNomenclator());
        capTerenIrigat.setCodRand(sectiuneCapitol_6.getCodRand());
        capTerenIrigat.setDenumire(sectiuneCapitol_6.getDenumire());
        terenIrigat.setCapTerenIrigat(capTerenIrigat);
        /*nrHA + nrARI*/
        terenIrigat.setSuprafata(SuprafataHelper.transformToMP(
                sectiuneCapitol_6.getNrARI() != null ? sectiuneCapitol_6.getNrARI().getValue() : null,
                sectiuneCapitol_6.getNrHA() != null ? sectiuneCapitol_6.getNrHA().getValue() : null
        ));
         /*populare entity pojo*/
        List<GeometrieTerenIrigat> listaGeo = new ArrayList<>();

        List<String> geoInfo = sectiuneCapitol_6.getReferintaGeoXml();
        if(geoInfo !=null) {
            for ( String element : geoInfo){
                GeometrieTerenIrigat geometrieTerenIrigat = new GeometrieTerenIrigat();
                geometrieTerenIrigat.setGeometrieGML(element);
                listaGeo.add(geometrieTerenIrigat);
            }
        }
        terenIrigat.setGeometrieTerenIrigats(listaGeo);
        gospodarie.addTerenIrigat(terenIrigat);
    }
}
