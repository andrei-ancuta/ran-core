package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.MentiuneSpeciala;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_16;

/**
 * Mențiuni speciale
 * Created by Dan on 02-Nov-15.
 */
public class Capitol_16ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_16 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_16 capitol_16 = null;
        if (gospodarieDTO.getMentiuneSpecialas() != null && !gospodarieDTO.getMentiuneSpecialas().isEmpty()) {
            MentiuneSpeciala mentiuneSpeciala = gospodarieDTO.getMentiuneSpecialas().get(0);
            capitol_16 = new Capitol_16();
            capitol_16.setRandCapitol(RandCapitol_16ConversionHelper.toSchemaType(mentiuneSpeciala));
        }
        return capitol_16;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_16 jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_16 capitol_16, Gospodarie gospodarie) throws RequestValidationException {
        if (capitol_16 == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_16");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (capitol_16.getRandCapitol() != null) {
            RandCapitol_16ConversionHelper.populeazaFromSchemaType(capitol_16.getRandCapitol(), gospodarie);
        }
    }
}
