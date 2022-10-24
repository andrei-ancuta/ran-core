package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.SistemTehnic;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_9;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_9;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilaje, instalații pentru agricultură, mijloace de transport cu tracțiune animală și mecanică existente la începutul anului
 * Created by Dan on 30-Oct-15.
 */
public class Capitol_9ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_9 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_9 capitol_9 = null;
        List<SectiuneCapitol_9> lstSectiuneCapitol_9 = new ArrayList<SectiuneCapitol_9>();
        if (gospodarieDTO.getSistemTehnics() != null) {
            for (SistemTehnic sistemTehnic : gospodarieDTO.getSistemTehnics()) {
                lstSectiuneCapitol_9.add(RandCapitol_9ConversionHelper.toSchemaType(sistemTehnic));
            }
        }
        if (!lstSectiuneCapitol_9.isEmpty()) {
            capitol_9 = new Capitol_9();
            capitol_9.setRandCapitol(lstSectiuneCapitol_9);
        }
        return capitol_9;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_9  jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_9 capitol_9, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_9 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_9");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"AnRaportare");
        }
        if (capitol_9.getRandCapitol() != null) {
            for (SectiuneCapitol_9 sectiuneCapitol_9 : capitol_9.getRandCapitol()) {
                RandCapitol_9ConversionHelper.populeazaFromSchemaType(sectiuneCapitol_9, gospodarie, anRaportare);
            }
        }
    }
}
