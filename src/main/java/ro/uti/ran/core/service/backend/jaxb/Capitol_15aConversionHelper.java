package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Contract;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_15a;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_15a;

import java.util.ArrayList;
import java.util.List;

/**
 * Înregistrări privind contractele de arendare
 * Created by Dan on 02-Nov-15.
 */
public class Capitol_15aConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_15a toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_15a capitol_15a = null;
        List<SectiuneCapitol_15a> lstSectiuneCapitol_15A = new ArrayList<SectiuneCapitol_15a>();
        if (gospodarieDTO.getContracts() != null) {
            for (Contract contract : gospodarieDTO.getContracts()) {
                lstSectiuneCapitol_15A.add(RandCapitol_15aConversionHelper.toSchemaType(contract));
            }
        }
        if (!lstSectiuneCapitol_15A.isEmpty()) {
            capitol_15a = new Capitol_15a();
            capitol_15a.setRandCapitol(lstSectiuneCapitol_15A);
        }
        return capitol_15a;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_15a jaxb pojo
     * @param gospodarie  entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_15a capitol_15a, Gospodarie gospodarie) throws RequestValidationException {
        if (capitol_15a == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_15a");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (capitol_15a.getRandCapitol() != null) {
            for (SectiuneCapitol_15a sectiuneCapitol_15A : capitol_15a.getRandCapitol()) {
                RandCapitol_15aConversionHelper.populeazaFromSchemaType(sectiuneCapitol_15A, gospodarie);
            }
        }
    }
}
