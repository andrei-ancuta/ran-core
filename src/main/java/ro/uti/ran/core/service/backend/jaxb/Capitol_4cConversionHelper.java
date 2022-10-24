package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Cultura;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_4c;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_4c;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 28-Oct-15.
 */
public class Capitol_4cConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_4c toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_4c capitol_4c = null;
        List<SectiuneCapitol_4c> lstSectiuneCapitol_4C = new ArrayList<SectiuneCapitol_4c>();
        if (gospodarieDTO.getCulturas() != null) {
            for (Cultura cultura : gospodarieDTO.getCulturas()) {
                lstSectiuneCapitol_4C.add(RandCapitol_4cConversionHelper.toSchemaType(cultura));
            }
        }
        if (!lstSectiuneCapitol_4C.isEmpty()) {
            capitol_4c = new Capitol_4c();
            capitol_4c.setRandCapitol(lstSectiuneCapitol_4C);
        }
        return capitol_4c;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_4c jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_4c capitol_4c, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_4c == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_4c");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"AnRaportare");
        }
        if (capitol_4c.getRandCapitol() != null) {
            for (SectiuneCapitol_4c sectiuneCapitol_4C : capitol_4c.getRandCapitol()) {
                RandCapitol_4cConversionHelper.populeazaFromSchemaType(sectiuneCapitol_4C, gospodarie, anRaportare);
            }
        }
    }
}
