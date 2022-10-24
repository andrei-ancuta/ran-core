package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.CapCultura;
import ro.uti.ran.core.model.registru.Cultura;
import ro.uti.ran.core.model.registru.GeometrieCultura;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_4c;
import ro.uti.ran.core.xml.model.types.GEOMETRIE_CULTURA;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 28-Oct-15.
 */
public class RandCapitol_4cConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param cultura entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_4c toSchemaType(Cultura cultura) {
        if (cultura == null) {
            throw new IllegalArgumentException("Cultura nedefinit!");
        }
        SectiuneCapitol_4c sectiuneCapitol_4C = new SectiuneCapitol_4c();
        /*codNomenclator + codRand + denumire*/
        sectiuneCapitol_4C.setCodNomenclator(cultura.getCapCultura().getCod());
        sectiuneCapitol_4C.setCodRand(cultura.getCapCultura().getCodRand());
        sectiuneCapitol_4C.setDenumire(cultura.getCapCultura().getDenumire());
         /*nrMP*/
        if (cultura.getSuprafata() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(cultura.getSuprafata());
            sectiuneCapitol_4C.setNrMP(intreg_pozitiv);
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
            sectiuneCapitol_4C.setGeometrieCultura(geometrie);
        }
        return sectiuneCapitol_4C;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_4C jaxb pojo
     * @param gospodarie         entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_4c sectiuneCapitol_4C, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_4C == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "sectiuneCapitol_4C");
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
        capCultura.setCod(sectiuneCapitol_4C.getCodNomenclator());
        capCultura.setCodRand(sectiuneCapitol_4C.getCodRand());
        capCultura.setDenumire(sectiuneCapitol_4C.getDenumire());
        cultura.setCapCultura(capCultura);
         /*nrMP*/
        if (sectiuneCapitol_4C.getNrMP() != null) {
            cultura.setSuprafata(sectiuneCapitol_4C.getNrMP().getValue());
        }
        /*referintaGeoXml*/
        List<GeometrieCultura> listaGeo = new ArrayList<GeometrieCultura>();
        List<GEOMETRIE_CULTURA> geoInfo = sectiuneCapitol_4C.getGeometrieCultura();
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
