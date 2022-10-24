package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_4b2;
import ro.uti.ran.core.xml.model.types.GEOMETRIE_CULTURA;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 28-Oct-15.
 */
public class RandCapitol_4b2ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param cultura entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_4b2 toSchemaType(Cultura cultura) {
        if (cultura == null) {
            throw new IllegalArgumentException("Cultura nedefinit!");
        }
        SectiuneCapitol_4b2 sectiuneCapitol_4B2 = new SectiuneCapitol_4b2();
        /*codNomenclator + codRand + denumire*/
        if (cultura.getCapCultura() != null) {
            sectiuneCapitol_4B2.setCodNomenclator(cultura.getCapCultura().getCod());
            sectiuneCapitol_4B2.setCodRand(cultura.getCapCultura().getCodRand());
            sectiuneCapitol_4B2.setDenumire(cultura.getCapCultura().getDenumire());
        }
        /*nrMP*/
        if (cultura.getSuprafata() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(cultura.getSuprafata());
            sectiuneCapitol_4B2.setNrMP(intreg_pozitiv);
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
            sectiuneCapitol_4B2.setGeometrieCultura(geometrie);
        }
        return sectiuneCapitol_4B2;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_4B2 jaxb pojo
     * @param gospodarie          entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_4b2 sectiuneCapitol_4B2, Gospodarie gospodarie, Integer anRaportare) {
        if (sectiuneCapitol_4B2 == null) {
            throw new IllegalArgumentException("RandCapitol_4b2 nedefinit!");
        }
        if (gospodarie == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        if (anRaportare == null) {
            throw new IllegalArgumentException("AnRaportare nedefinit!");
        }
        Cultura cultura = new Cultura();
        /*an*/
        cultura.setAn(anRaportare);
        /*codNomenclator + codRand + denumire*/
        CapCultura capCultura = new CapCultura();
        capCultura.setCod(sectiuneCapitol_4B2.getCodNomenclator());
        capCultura.setCodRand(sectiuneCapitol_4B2.getCodRand());
        capCultura.setDenumire(sectiuneCapitol_4B2.getDenumire());
        cultura.setCapCultura(capCultura);
        /*nrMP*/
        if (sectiuneCapitol_4B2.getNrMP() != null) {
            cultura.setSuprafata(sectiuneCapitol_4B2.getNrMP().getValue());
        }
        /*NOM_TIP_SPATIU_PROT*/
        NomTipSpatiuProt nomTipSpatiuProt = new NomTipSpatiuProt();
        nomTipSpatiuProt.setCod(RanConstants.NOM_TIP_SPATIU_PROT_COD_SOLAR);
        //
        cultura.setNomTipSpatiuProt(nomTipSpatiuProt);
        /*referintaGeoXml*/
        List<GeometrieCultura> listaGeo = new ArrayList<GeometrieCultura>();
        List<GEOMETRIE_CULTURA> geoInfo = sectiuneCapitol_4B2.getGeometrieCultura();
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
