package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.CapPlantatie;
import ro.uti.ran.core.model.registru.GeometriePlantatie;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Plantatie;
import ro.uti.ran.core.service.backend.utils.SuprafataHelper;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_5c;
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
public class RandCapitol_5cConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param plantatie entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_5c toSchemaType(Plantatie plantatie) {
        if (plantatie == null) {
            throw new IllegalArgumentException("Plantatie nedefinit!");
        }
        SectiuneCapitol_5c sectiuneCapitol_5C = new SectiuneCapitol_5c();
        /*codNomenclator + codRand + denumire*/
        sectiuneCapitol_5C.setCodNomenclator(plantatie.getCapPlantatie().getCod());
        sectiuneCapitol_5C.setCodRand(plantatie.getCapPlantatie().getCodRand());
        sectiuneCapitol_5C.setDenumire(plantatie.getCapPlantatie().getDenumire());
        /*nrHA + nrARI*/
        if (plantatie.getSuprafata() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            FRACTIE_DOUA_ZECIMALE fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
            Map<String, Object> tmp = SuprafataHelper.extractHAAndARIFromMP(BigInteger.valueOf(plantatie.getSuprafata()));
            if (tmp.get(SuprafataHelper.KEY_HA) != null) {
                BigInteger value = (BigInteger) tmp.get(SuprafataHelper.KEY_HA);
                intreg_pozitiv.setValue(value.intValue());
                sectiuneCapitol_5C.setNrHA(intreg_pozitiv);
            } else {
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(0);
                sectiuneCapitol_5C.setNrHA(intreg_pozitiv);
            }
            if (tmp.get(SuprafataHelper.KEY_ARI) != null) {
                BigDecimal value = (BigDecimal) tmp.get(SuprafataHelper.KEY_ARI);
                fractie_doua_zecimale.setValue(value);
                sectiuneCapitol_5C.setNrARI(fractie_doua_zecimale);
            }else {
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(BigDecimal.ZERO);
                sectiuneCapitol_5C.setNrARI(fractie_doua_zecimale);
            }
        }
        List<String> geometrie = new ArrayList<>();
        for(GeometriePlantatie geometriePlantatie:plantatie.getGeometriePlantatii()){
            geometrie.add(geometriePlantatie.getGeometrieGML());
        }
        sectiuneCapitol_5C.setReferintaGeoXml(geometrie);
        return sectiuneCapitol_5C;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_5C jaxb pojo
     * @param gospodarie     entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_5c sectiuneCapitol_5C, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_5C == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_5C");
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
        capPlantatie.setCod(sectiuneCapitol_5C.getCodNomenclator());
        capPlantatie.setCodRand(sectiuneCapitol_5C.getCodRand());
        capPlantatie.setDenumire(sectiuneCapitol_5C.getDenumire());
        plantatie.setCapPlantatie(capPlantatie);
        /*nrHA + nrARI*/
        plantatie.setSuprafata(SuprafataHelper.transformToMP(
                sectiuneCapitol_5C.getNrARI() != null ? sectiuneCapitol_5C.getNrARI().getValue() : null,
                sectiuneCapitol_5C.getNrHA() != null ? sectiuneCapitol_5C.getNrHA().getValue() : null
        ));
         /*populare entity pojo*/
        List<GeometriePlantatie> listaGeo = new ArrayList<>();

        List<String> geoInfo = sectiuneCapitol_5C.getReferintaGeoXml();
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
