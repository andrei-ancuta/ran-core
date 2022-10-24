package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.service.backend.utils.SuprafataHelper;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_5b;
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
public class RandCapitol_5bConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param plantatie entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_5b toSchemaType(Plantatie plantatie) {
        if (plantatie == null) {
            throw new IllegalArgumentException("Plantatie nedefinit!");
        }
        SectiuneCapitol_5b sectiuneCapitol_5B = new SectiuneCapitol_5b();
        /*codNomenclator + codRand + denumire*/
        sectiuneCapitol_5B.setCodNomenclator(plantatie.getCapPlantatie().getCod());
        sectiuneCapitol_5B.setCodRand(plantatie.getCapPlantatie().getCodRand());
        sectiuneCapitol_5B.setDenumire(plantatie.getCapPlantatie().getDenumire());
        /*nrHA + nrARI*/
        if (plantatie.getSuprafata() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            FRACTIE_DOUA_ZECIMALE fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
            Map<String, Object> tmp = SuprafataHelper.extractHAAndARIFromMP(BigInteger.valueOf(plantatie.getSuprafata()));
            if (tmp.get(SuprafataHelper.KEY_HA) != null) {
                BigInteger value = (BigInteger) tmp.get(SuprafataHelper.KEY_HA);
                intreg_pozitiv.setValue(value.intValue());
                sectiuneCapitol_5B.setNrHA(intreg_pozitiv);
            } else {
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(0);
                sectiuneCapitol_5B.setNrHA(intreg_pozitiv);
            }
            if (tmp.get(SuprafataHelper.KEY_ARI) != null) {
                BigDecimal value = (BigDecimal) tmp.get(SuprafataHelper.KEY_ARI);
                fractie_doua_zecimale.setValue(value);
                sectiuneCapitol_5B.setNrARI(fractie_doua_zecimale);
            }else {
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(BigDecimal.ZERO);
                sectiuneCapitol_5B.setNrARI(fractie_doua_zecimale);
            }
        }
        /*nrPomiPeRod*/
        if (plantatie.getNrPomRod() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(plantatie.getNrPomRod());
            sectiuneCapitol_5B.setNrPomiPeRod(intreg_pozitiv);
        }
        List<String> geometrie = new ArrayList<>();
        for(GeometriePlantatie geometriePlantatie:plantatie.getGeometriePlantatii()){
            geometrie.add(geometriePlantatie.getGeometrieGML());
        }
        sectiuneCapitol_5B.setReferintaGeoXml(geometrie);
        return sectiuneCapitol_5B;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_5B jaxb pojo
     * @param gospodarie     entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_5b sectiuneCapitol_5B, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_5B == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_5B");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        Plantatie plantatie = new Plantatie();
        /*an*/
        plantatie.setAn(anRaportare);
        /*codNomenclator + codRand + denumire*/
        CapPlantatie capPlantatie = new CapPlantatie();
        capPlantatie.setCod(sectiuneCapitol_5B.getCodNomenclator());
        capPlantatie.setCodRand(sectiuneCapitol_5B.getCodRand());
        capPlantatie.setDenumire(sectiuneCapitol_5B.getDenumire());
        plantatie.setCapPlantatie(capPlantatie);
        /*nrHA + nrARI*/
        plantatie.setSuprafata(SuprafataHelper.transformToMP(
                sectiuneCapitol_5B.getNrARI() != null ? sectiuneCapitol_5B.getNrARI().getValue() : null,
                sectiuneCapitol_5B.getNrHA() != null ? sectiuneCapitol_5B.getNrHA().getValue() : null
        ));
        /*nrPomiPeRod*/
        if (sectiuneCapitol_5B.getNrPomiPeRod() != null) {
            plantatie.setNrPomRod(sectiuneCapitol_5B.getNrPomiPeRod().getValue());
        }
         /*populare entity pojo*/
        List<GeometriePlantatie> listaGeo = new ArrayList<>();

        List<String> geoInfo = sectiuneCapitol_5B.getReferintaGeoXml();
        if(geoInfo !=null) {
            for ( String element : geoInfo){
                GeometriePlantatie geometriePlantatie = new GeometriePlantatie();
                geometriePlantatie.setGeometrieGML(element);
                listaGeo.add(geometriePlantatie);
            }
        }
        plantatie.setGeometriePlantatii(listaGeo);
        gospodarie.addPlantaty(plantatie);
    }
}
