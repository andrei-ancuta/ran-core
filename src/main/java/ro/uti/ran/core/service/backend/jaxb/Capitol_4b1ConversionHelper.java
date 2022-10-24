package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Cultura;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_4b1;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_4b1;

import java.util.ArrayList;
import java.util.List;

/**
 * b1) Suprafața cultivată în sere pe raza localității
 * Created by Dan on 28-Oct-15.
 */
public class Capitol_4b1ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_4b1 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_4b1 capitol_4b1 = null;
        List<SectiuneCapitol_4b1> lstSectiuneCapitol_4B1 = new ArrayList<SectiuneCapitol_4b1>();
        if (gospodarieDTO.getCulturas() != null) {
            for (Cultura cultura : gospodarieDTO.getCulturas()) {
                lstSectiuneCapitol_4B1.add(RandCapitol_4b1ConversionHelper.toSchemaType(cultura));
            }
        }
        if (!lstSectiuneCapitol_4B1.isEmpty()) {
            capitol_4b1 = new Capitol_4b1();
            capitol_4b1.setRandCapitol(lstSectiuneCapitol_4B1);
        }
        return capitol_4b1;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_4b1 jaxb pojo
     * @param gospodarie  entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_4b1 capitol_4b1, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_4b1 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_4b1");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"AnRaportare");

        }
        if (capitol_4b1.getRandCapitol() != null) {
            for (SectiuneCapitol_4b1 sectiuneCapitol_4B1 : capitol_4b1.getRandCapitol()) {
                RandCapitol_4b1ConversionHelper.populeazaFromSchemaType(sectiuneCapitol_4B1, gospodarie, anRaportare);
            }
        }
    }
}
