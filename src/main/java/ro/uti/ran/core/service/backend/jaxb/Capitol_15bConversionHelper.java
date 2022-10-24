package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Contract;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_15b;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_15b;

import java.util.ArrayList;
import java.util.List;

/**
 * Înregistrări privind contractele de concesiune
 * Created by Dan on 02-Nov-15.
 */
public class Capitol_15bConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_15b toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_15b capitol_15b = null;
        List<SectiuneCapitol_15b> lstSectiuneCapitol_15B = new ArrayList<SectiuneCapitol_15b>();
        if (gospodarieDTO.getContracts() != null) {
            for (Contract contract : gospodarieDTO.getContracts()) {
                lstSectiuneCapitol_15B.add(RandCapitol_15bConversionHelper.toSchemaType(contract));
            }
        }
        if (!lstSectiuneCapitol_15B.isEmpty()) {
            capitol_15b = new Capitol_15b();
            capitol_15b.setRandCapitol(lstSectiuneCapitol_15B);
        }
        return capitol_15b;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_15b jaxb pojo
     * @param gospodarie  entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_15b capitol_15b, Gospodarie gospodarie) throws RequestValidationException {
        if (capitol_15b == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_15b");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (capitol_15b.getRandCapitol() != null) {
            for (SectiuneCapitol_15b sectiuneCapitol_15B : capitol_15b.getRandCapitol()) {
                RandCapitol_15bConversionHelper.populeazaFromSchemaType(sectiuneCapitol_15B, gospodarie);
            }
        }
    }
}
