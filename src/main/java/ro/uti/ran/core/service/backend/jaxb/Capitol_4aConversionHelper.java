package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Cultura;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_4a;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_4a;

import java.util.ArrayList;
import java.util.List;

/**
 * Suprafața arabilă situată pe raza localității - culturi în câmp
 * Created by Dan on 28-Oct-15.
 */
public class Capitol_4aConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_4a toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_4a capitol_4a = null;
        List<SectiuneCapitol_4a> lstSectiuneCapitol_4A = new ArrayList<SectiuneCapitol_4a>();
        if (gospodarieDTO.getCulturas() != null) {
            for (Cultura cultura : gospodarieDTO.getCulturas()) {
                lstSectiuneCapitol_4A.add(RandCapitol_4aConversionHelper.toSchemaType(cultura));
            }
        }
        if (!lstSectiuneCapitol_4A.isEmpty()) {
            capitol_4a = new Capitol_4a();
            capitol_4a.setRandCapitol(lstSectiuneCapitol_4A);
        }
        return capitol_4a;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_4a jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_4a capitol_4a, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_4a == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_4a");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        if (capitol_4a.getRandCapitol() != null) {
            for (SectiuneCapitol_4a sectiuneCapitol_4A : capitol_4a.getRandCapitol()) {
                RandCapitol_4aConversionHelper.populeazaFromSchemaType(sectiuneCapitol_4A, gospodarie, anRaportare);
            }
        }
    }
}
