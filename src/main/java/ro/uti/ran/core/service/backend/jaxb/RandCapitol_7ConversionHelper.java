package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.CategorieAnimal;
import ro.uti.ran.core.model.registru.Crotalie;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.CapCategorieAnimal;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_7;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 29-Oct-15.
 */
public class RandCapitol_7ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param categorieAnimal entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_7 toSchemaType(CategorieAnimal categorieAnimal) {
        if (categorieAnimal == null) {
            throw new IllegalArgumentException("CategorieAnimal nedefinit!");
        }
        SectiuneCapitol_7 sectiuneCapitol_7 = new SectiuneCapitol_7();
        /*codNomenclator + codRand + denumire*/
        sectiuneCapitol_7.setCodNomenclator(categorieAnimal.getCapCategorieAnimal().getCod());
        sectiuneCapitol_7.setCodRand(categorieAnimal.getCapCategorieAnimal().getCodRand());
        sectiuneCapitol_7.setDenumire(categorieAnimal.getCapCategorieAnimal().getDenumire());
        /*nrCapete*/
        if (categorieAnimal.getNrCap() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(categorieAnimal.getNrCap());
            sectiuneCapitol_7.setNrCapete(intreg_pozitiv);
        }
        /*crotalie*/
        if (categorieAnimal.getCrotalies() != null) {
            List<ro.uti.ran.core.xml.model.capitol.nested.Crotalie> lstCrotalie = new ArrayList<ro.uti.ran.core.xml.model.capitol.nested.Crotalie>();
            for (Crotalie crotalieEntity : categorieAnimal.getCrotalies()) {
                ro.uti.ran.core.xml.model.capitol.nested.Crotalie crotalieJaxb = new ro.uti.ran.core.xml.model.capitol.nested.Crotalie();
                crotalieJaxb.setCodIdentificare(crotalieEntity.getCodIdentificare());
                lstCrotalie.add(crotalieJaxb);
            }
            sectiuneCapitol_7.setCrotalie(lstCrotalie);
        }
        return sectiuneCapitol_7;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_7 jaxb pojo
     * @param gospodarie    entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_7 sectiuneCapitol_7, Gospodarie gospodarie, Integer anRaportare, Integer semestruRaportare) throws RequestValidationException {
        if (sectiuneCapitol_7 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_7");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        if (semestruRaportare == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"semestruRaportare");
        }
        CategorieAnimal categorieAnimal = new CategorieAnimal();
        /*an*/
        categorieAnimal.setAn(anRaportare);
        /*semestru*/
        categorieAnimal.setSemestru(semestruRaportare);
        /*codNomenclator + codRand + denumire*/
        CapCategorieAnimal capCategorieAnimal = new CapCategorieAnimal();
        capCategorieAnimal.setCod(sectiuneCapitol_7.getCodNomenclator());
        capCategorieAnimal.setCodRand(sectiuneCapitol_7.getCodRand());
        capCategorieAnimal.setDenumire(sectiuneCapitol_7.getDenumire());
        categorieAnimal.setCapCategorieAnimal(capCategorieAnimal);
        /*nrCapete*/
        if (sectiuneCapitol_7.getNrCapete() != null) {
            categorieAnimal.setNrCap(sectiuneCapitol_7.getNrCapete().getValue());
        }
       /*crotalie*/
        if (sectiuneCapitol_7.getCrotalie() != null) {
            for (ro.uti.ran.core.xml.model.capitol.nested.Crotalie crotalieJaxb : sectiuneCapitol_7.getCrotalie()) {
                Crotalie crotalieEntity = new Crotalie();
                crotalieEntity.setCodIdentificare(crotalieJaxb.getCodIdentificare());
                categorieAnimal.addCrotaly(crotalieEntity);
            }
        }
         /*populare entity pojo*/
        gospodarie.addCategorieAnimal(categorieAnimal);
    }
}
