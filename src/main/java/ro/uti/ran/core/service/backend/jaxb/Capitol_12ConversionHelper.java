package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Atestat;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_12;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_12;

import java.util.ArrayList;
import java.util.List;

/**
 * Atestatele de producător și carnetele de comercializare eliberate/vizate
 * Created by Dan on 02-Nov-15.
 */
public class Capitol_12ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_12 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_12 capitol_12 = null;
        List<SectiuneCapitol_12> lstSectiuneCapitol_12 = new ArrayList<SectiuneCapitol_12>();
        if (gospodarieDTO.getAtestats() != null) {
            for (Atestat atestat : gospodarieDTO.getAtestats()) {
                lstSectiuneCapitol_12.add(RandCapitol_12ConversionHelper.toSchemaType(atestat));
            }
        }
        if (!lstSectiuneCapitol_12.isEmpty()) {
            capitol_12 = new Capitol_12();
            capitol_12.setRandCapitol(lstSectiuneCapitol_12);
        }
        return capitol_12;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_12 jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_12 capitol_12, Gospodarie gospodarie) throws RequestValidationException {
        if (capitol_12 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_12");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (capitol_12.getRandCapitol() != null) {
            for (SectiuneCapitol_12 sectiuneCapitol_12 : capitol_12.getRandCapitol()) {
                RandCapitol_12ConversionHelper.populeazaFromSchemaType(sectiuneCapitol_12, gospodarie);
            }
        }
    }
}
