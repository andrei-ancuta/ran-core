package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Cladire;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_11;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_11;

import java.util.ArrayList;
import java.util.List;

/**
 * Clădiri existente la începutul anului pe raza localității
 * Created by Dan on 02-Nov-15.
 */
public class Capitol_11ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_11 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_11 capitol_11 = null;
        List<SectiuneCapitol_11> lstSectiuneCapitol_11 = new ArrayList<SectiuneCapitol_11>();
        if (gospodarieDTO.getCladires() != null) {
            for (Cladire cladire : gospodarieDTO.getCladires()) {
                lstSectiuneCapitol_11.add(RandCapitol_11ConversionHelper.toSchemaType(cladire));
            }
        }
        if (!lstSectiuneCapitol_11.isEmpty()) {
            capitol_11 = new Capitol_11();
            capitol_11.setRandCapitol(lstSectiuneCapitol_11);
        }
        return capitol_11;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_11 jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_11 capitol_11, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_11 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_11");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (anRaportare == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        if (capitol_11.getRandCapitol() != null) {
            for (SectiuneCapitol_11 sectiuneCapitol_11 : capitol_11.getRandCapitol()) {
                RandCapitol_11ConversionHelper.populeazaFromSchemaType(sectiuneCapitol_11, gospodarie, anRaportare);
            }
        }
    }
}
