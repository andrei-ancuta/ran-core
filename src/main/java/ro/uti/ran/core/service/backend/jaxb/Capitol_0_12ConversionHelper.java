package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_0_12;

/**
 * Created by Dan on 13-Oct-15.
 */
public class Capitol_0_12ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_0_12 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_0_12 capitol_0_12 = new Capitol_0_12();
        capitol_0_12.setRandCapitol(RandCapitol_0_12ConversionHelper.toSchemaType(gospodarieDTO));
        return capitol_0_12;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_0_12 jaxb pojo
     * @param gospodarie   entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_0_12 capitol_0_12, Gospodarie gospodarie) throws RequestValidationException {
        if (capitol_0_12 == null) {
           throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_0_12");
        }
        if (gospodarie == null) {
           throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (capitol_0_12.getRandCapitol() != null) {
            RandCapitol_0_12ConversionHelper.populeazaFromSchemaType(capitol_0_12.getRandCapitol(), gospodarie);
        }
    }
}
