package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.CapCategorieAnimal;
import ro.uti.ran.core.model.registru.CategorieAnimal;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_8;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

/**
 * Created by Dan on 29-Oct-15.
 */
public class RandCapitol_8ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param categorieAnimal entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_8 toSchemaType(CategorieAnimal categorieAnimal) {
        if (categorieAnimal == null) {
            throw new IllegalArgumentException("CategorieAnimal nedefinit!");
        }
        SectiuneCapitol_8 sectiuneCapitol_8 = new SectiuneCapitol_8();
        /*codNomenclator + codRand + denumire*/
        sectiuneCapitol_8.setCodNomenclator(categorieAnimal.getCapCategorieAnimal().getCod());
        sectiuneCapitol_8.setCodRand(categorieAnimal.getCapCategorieAnimal().getCodRand());
        sectiuneCapitol_8.setDenumire(categorieAnimal.getCapCategorieAnimal().getDenumire());
        /*nrCapete*/
        if (categorieAnimal.getNrCap() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(categorieAnimal.getNrCap());
            sectiuneCapitol_8.setNrCapete(intreg_pozitiv);
        }
        return sectiuneCapitol_8;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_8 jaxb pojo
     * @param gospodarie    entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_8 sectiuneCapitol_8, Gospodarie gospodarie, Integer anRaportare, Integer semestruRaportare) throws RequestValidationException {
        if (sectiuneCapitol_8 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_8");
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
        capCategorieAnimal.setCod(sectiuneCapitol_8.getCodNomenclator());
        capCategorieAnimal.setCodRand(sectiuneCapitol_8.getCodRand());
        capCategorieAnimal.setDenumire(sectiuneCapitol_8.getDenumire());
        categorieAnimal.setCapCategorieAnimal(capCategorieAnimal);
        /*nrCapete*/
        if (sectiuneCapitol_8.getNrCapete() != null) {
            categorieAnimal.setNrCap(sectiuneCapitol_8.getNrCapete().getValue());
        }
         /*populare entity pojo*/
        gospodarie.addCategorieAnimal(categorieAnimal);
    }
}
