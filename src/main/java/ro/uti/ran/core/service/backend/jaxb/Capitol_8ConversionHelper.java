package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.CategorieAnimal;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_8;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_8;

import java.util.ArrayList;
import java.util.List;

/**
 * Evoluția efectivelor de animale, în cursul anului, aflate în proprietate
 * Created by Dan on 29-Oct-15.
 */
public class Capitol_8ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_8 toSchemaType(GospodarieDTO gospodarieDTO, Integer semestru) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        if (semestru == null) {
            throw new IllegalArgumentException("Semestru nedefinit!");
        }
        Capitol_8 capitol_8 = null;
        /*randCapitol*/
        List<SectiuneCapitol_8> lstSectiuneCapitol_8 = new ArrayList<SectiuneCapitol_8>();
        if (gospodarieDTO.getCategorieAnimals() != null) {
            for (CategorieAnimal categorieAnimal : gospodarieDTO.getCategorieAnimals()) {
                lstSectiuneCapitol_8.add(RandCapitol_8ConversionHelper.toSchemaType(categorieAnimal));
            }
        }
        if (!lstSectiuneCapitol_8.isEmpty()) {
            capitol_8 = new Capitol_8();
            /*semestru*/
            capitol_8.setSemestru(semestru);
            capitol_8.setRandCapitol(lstSectiuneCapitol_8);
        }
        return capitol_8;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_8  jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_8 capitol_8, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_8 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_8");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        /*semestru*/
        Integer semestru = capitol_8.getSemestru();
        /*randCapitol*/
        if (capitol_8.getRandCapitol() != null) {
            for (SectiuneCapitol_8 sectiuneCapitol_8 : capitol_8.getRandCapitol()) {
                RandCapitol_8ConversionHelper.populeazaFromSchemaType(sectiuneCapitol_8, gospodarie, anRaportare, semestru);
            }
        }
    }
}
