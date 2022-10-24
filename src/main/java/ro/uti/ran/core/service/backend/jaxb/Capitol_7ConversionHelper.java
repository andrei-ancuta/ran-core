package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.CategorieAnimal;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_7;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_7;

import java.util.ArrayList;
import java.util.List;

/**
 * Animale domestice și/sau animale sălbatice crescute în captivitate, în condițiile legii - Situația la începutul semestrului
 * Created by Dan on 29-Oct-15.
 */
public class Capitol_7ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_7 toSchemaType(GospodarieDTO gospodarieDTO, Integer semestru) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        if (semestru == null) {
            throw new IllegalArgumentException("Semestru nedefinit!");
        }
        Capitol_7 capitol_7 = null;
        /*randCapitol*/
        List<SectiuneCapitol_7> lstSectiuneCapitol_7 = new ArrayList<SectiuneCapitol_7>();
        if (gospodarieDTO.getCategorieAnimals() != null) {
            for (CategorieAnimal categorieAnimal : gospodarieDTO.getCategorieAnimals()) {
                lstSectiuneCapitol_7.add(RandCapitol_7ConversionHelper.toSchemaType(categorieAnimal));
            }
        }
        if (!lstSectiuneCapitol_7.isEmpty()) {
            capitol_7 = new Capitol_7();
            /*semestru*/
            capitol_7.setSemestru(semestru);
            capitol_7.setRandCapitol(lstSectiuneCapitol_7);
        }
        return capitol_7;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_7  jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_7 capitol_7, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_7 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_7");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"AnRaportare");
        }
        /*semestru*/
        Integer semestru = capitol_7.getSemestru();
        /*randCapitol*/
        if (capitol_7.getRandCapitol() != null) {
            for (SectiuneCapitol_7 sectiuneCapitol_7 : capitol_7.getRandCapitol()) {
                RandCapitol_7ConversionHelper.populeazaFromSchemaType(sectiuneCapitol_7, gospodarie, anRaportare, semestru);
            }
        }
    }
}
