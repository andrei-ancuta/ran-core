package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_10a;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 30-Oct-15.
 */
public class RandCapitol_10aConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param aplicareIngrasamant entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_10a toSchemaType(AplicareIngrasamant aplicareIngrasamant) {
        if (aplicareIngrasamant == null) {
            throw new IllegalArgumentException("AplicareIngrasamant nedefinit!");
        }
        SectiuneCapitol_10a sectiuneCapitol_10A = new SectiuneCapitol_10a();
        /*codNomenclator + codRand + denumire*/
        sectiuneCapitol_10A.setCodNomenclator(aplicareIngrasamant.getCapAplicareIngrasamant().getCod());
        sectiuneCapitol_10A.setCodRand(aplicareIngrasamant.getCapAplicareIngrasamant().getCodRand());
        sectiuneCapitol_10A.setDenumire(aplicareIngrasamant.getCapAplicareIngrasamant().getDenumire());
        INTREG_POZITIV intreg_pozitiv;
        /*nrHA*/
        if (aplicareIngrasamant.getSuprafata() != null) {
            intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(aplicareIngrasamant.getSuprafata());
            sectiuneCapitol_10A.setNrHA(intreg_pozitiv);
        }
         /*nrKG*/
        if (aplicareIngrasamant.getCantitate() != null) {
            intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(aplicareIngrasamant.getCantitate());
            sectiuneCapitol_10A.setNrKG(intreg_pozitiv);
        }
        List<String> geometrie = new ArrayList<>();
        for(GeometrieAplicareIngras geometrieAplicareIngras:aplicareIngrasamant.getGeometrieAplicareIngrases()){
            geometrie.add(geometrieAplicareIngras.getGeometrieGML());
        }
        sectiuneCapitol_10A.setReferintaGeoXml(geometrie);
        return sectiuneCapitol_10A;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_10A jaxb pojo
     * @param gospodarie      entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_10a sectiuneCapitol_10A, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_10A == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_10A");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        AplicareIngrasamant aplicareIngrasamant = new AplicareIngrasamant();
        /*an*/
        aplicareIngrasamant.setAn(anRaportare);
        /*codNomenclator + codRand + denumire*/
        CapAplicareIngrasamant capAplicareIngrasamant = new CapAplicareIngrasamant();
        capAplicareIngrasamant.setCod(sectiuneCapitol_10A.getCodNomenclator());
        capAplicareIngrasamant.setCodRand(sectiuneCapitol_10A.getCodRand());
        capAplicareIngrasamant.setDenumire(sectiuneCapitol_10A.getDenumire());
        aplicareIngrasamant.setCapAplicareIngrasamant(capAplicareIngrasamant);
        /*nrHA*/
        if (sectiuneCapitol_10A.getNrHA() != null) {
            aplicareIngrasamant.setSuprafata(sectiuneCapitol_10A.getNrHA().getValue());
        }
        /*nrKG*/
        if (sectiuneCapitol_10A.getNrKG() != null) {
            aplicareIngrasamant.setCantitate(sectiuneCapitol_10A.getNrKG().getValue());
        }
         /*populare entity pojo*/
        List<GeometrieAplicareIngras> listaGeo = new ArrayList<>();

        List<String> geoInfo = sectiuneCapitol_10A.getReferintaGeoXml();
        if(geoInfo !=null) {
            for ( String element : geoInfo){
                GeometrieAplicareIngras geometrieAplicareIngras = new GeometrieAplicareIngras();
                geometrieAplicareIngras.setGeometrieGML(element);
                listaGeo.add(geometrieAplicareIngras);
            }
        }
        aplicareIngrasamant.setGeometrieAplicareIngrases(listaGeo);
        gospodarie.addAplicareIngrasamant(aplicareIngrasamant);
    }
}
