package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.CapCultura;
import ro.uti.ran.core.model.registru.Cultura;
import ro.uti.ran.core.model.registru.GeometrieCultura;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.utils.SuprafataHelper;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_4a1;
import ro.uti.ran.core.xml.model.types.FRACTIE_DOUA_ZECIMALE;
import ro.uti.ran.core.xml.model.types.GEOMETRIE_CULTURA;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dan on 28-Oct-15.
 */
public class RandCapitol_4a1ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param cultura entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_4a1 toSchemaType(Cultura cultura) {
        if (cultura == null) {
            throw new IllegalArgumentException("Cultura nedefinit!");
        }
        SectiuneCapitol_4a1 sectiuneCapitol_4A1 = new SectiuneCapitol_4a1();
        /*codNomenclator + codRand + denumire*/
        sectiuneCapitol_4A1.setCodNomenclator(cultura.getCapCultura().getCod());
        sectiuneCapitol_4A1.setCodRand(cultura.getCapCultura().getCodRand());
        sectiuneCapitol_4A1.setDenumire(cultura.getCapCultura().getDenumire());
        /*nrHA + nrARI*/
        if (cultura.getSuprafata() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            FRACTIE_DOUA_ZECIMALE fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
            Map<String, Object> tmp = SuprafataHelper.extractHAAndARIFromMP(BigInteger.valueOf(cultura.getSuprafata()));
            if (tmp.get(SuprafataHelper.KEY_HA) != null) {
                BigInteger value = (BigInteger) tmp.get(SuprafataHelper.KEY_HA);
                intreg_pozitiv.setValue(value.intValue());
                sectiuneCapitol_4A1.setNrHA(intreg_pozitiv);
            } else {
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(0);
                sectiuneCapitol_4A1.setNrHA(intreg_pozitiv);
            }
            if (tmp.get(SuprafataHelper.KEY_ARI) != null) {
                BigDecimal value = (BigDecimal) tmp.get(SuprafataHelper.KEY_ARI);
                fractie_doua_zecimale.setValue(value);
                sectiuneCapitol_4A1.setNrARI(fractie_doua_zecimale);
            } else {
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(BigDecimal.ZERO);
                sectiuneCapitol_4A1.setNrARI(fractie_doua_zecimale);
            }
        }
        /*referintaGeoXml*/
        if (cultura.getGeometrieCulturi() != null) {
            List<GEOMETRIE_CULTURA> geometrie = new ArrayList<>();
            for (GeometrieCultura geometrieGultura : cultura.getGeometrieCulturi()) {
                GEOMETRIE_CULTURA geometrie_cultura = new GEOMETRIE_CULTURA();
                geometrie_cultura.setReferintaGeoXml(geometrieGultura.getGeometrieGML());
                geometrie_cultura.setIsPrincipala(1 == geometrieGultura.getIsPrincipala());
                geometrie.add(geometrie_cultura);
            }
            sectiuneCapitol_4A1.setGeometrieCultura(geometrie);
        }
        return sectiuneCapitol_4A1;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_4A1 jaxb pojo
     * @param gospodarie          entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_4a1 sectiuneCapitol_4A1, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_4A1 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "sectiuneCapitol_4A1");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "anRaportare");
        }
        Cultura cultura = new Cultura();
        /*an*/
        cultura.setAn(anRaportare);
        /*codNomenclator + codRand + denumire*/
        CapCultura capCultura = new CapCultura();
        capCultura.setCod(sectiuneCapitol_4A1.getCodNomenclator());
        capCultura.setCodRand(sectiuneCapitol_4A1.getCodRand());
        capCultura.setDenumire(sectiuneCapitol_4A1.getDenumire());
        cultura.setCapCultura(capCultura);
        /*nrHA + nrARI*/
        cultura.setSuprafata(SuprafataHelper.transformToMP(
                sectiuneCapitol_4A1.getNrARI() != null ? sectiuneCapitol_4A1.getNrARI().getValue() : null,
                sectiuneCapitol_4A1.getNrHA() != null ? sectiuneCapitol_4A1.getNrHA().getValue() : null
        ));
        /*referintaGeoXml*/
        List<GeometrieCultura> listaGeo = new ArrayList<GeometrieCultura>();
        List<GEOMETRIE_CULTURA> geoInfo = sectiuneCapitol_4A1.getGeometrieCultura();
        if (geoInfo != null) {
            for (GEOMETRIE_CULTURA element : geoInfo) {
                GeometrieCultura geometrieCultura = new GeometrieCultura();
                geometrieCultura.setGeometrieGML(element.getReferintaGeoXml());
                geometrieCultura.setIsPrincipala(element.getIsPrincipala() ? 1 : 0);
                listaGeo.add(geometrieCultura);
            }
        }
        cultura.setGeometrieCulturi(listaGeo);
         /*populare entity pojo*/
        gospodarie.addCultura(cultura);
    }
}
