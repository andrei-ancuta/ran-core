package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Preemptiune;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_14;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_14;

import java.util.ArrayList;
import java.util.List;

/**
 * Înregistrări privind exercitarea dreptului de preemțiune
 * Created by Dan on 02-Nov-15.
 */
public class Capitol_14ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_14 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_14 capitol_14 = null;
        List<SectiuneCapitol_14> lstSectiuneCapitol_14 = new ArrayList<SectiuneCapitol_14>();
        if (gospodarieDTO.getPreemptiunes() != null) {
            for (Preemptiune preemptiune : gospodarieDTO.getPreemptiunes()) {
                lstSectiuneCapitol_14.add(RandCapitol_14ConversionHelper.toSchemaType(preemptiune));
            }
        }
        if (!lstSectiuneCapitol_14.isEmpty()) {
            capitol_14 = new Capitol_14();
            capitol_14.setRandCapitol(lstSectiuneCapitol_14);
        }
        return capitol_14;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_14 jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_14 capitol_14, Gospodarie gospodarie) throws RequestValidationException {
        if (capitol_14 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_14");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (capitol_14.getRandCapitol() != null) {
            for (SectiuneCapitol_14 sectiuneCapitol_14 : capitol_14.getRandCapitol()) {
                RandCapitol_14ConversionHelper.populeazaFromSchemaType(sectiuneCapitol_14, gospodarie);
            }
        }
    }
}
