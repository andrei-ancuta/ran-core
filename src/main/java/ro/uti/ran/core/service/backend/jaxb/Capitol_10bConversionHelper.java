package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.SubstantaChimica;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_10b;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_10b;

import java.util.ArrayList;
import java.util.List;

/**
 * b) Utilizarea îngrășămintelor chimice (în echivalent substanță activă) la principalele culturi
 * Created by Dan on 30-Oct-15.
 */
public class Capitol_10bConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_10b toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_10b capitol_10b = null;
        List<SectiuneCapitol_10b> lstSectiuneCapitol_10B = new ArrayList<SectiuneCapitol_10b>();
        if (gospodarieDTO.getSubstantaChimicas() != null) {
            for (SubstantaChimica substantaChimica : gospodarieDTO.getSubstantaChimicas()) {
                lstSectiuneCapitol_10B.add(RandCapitol_10bConversionHelper.toSchemaType(substantaChimica));
            }
        }
        if (!lstSectiuneCapitol_10B.isEmpty()) {
            capitol_10b = new Capitol_10b();
            capitol_10b.setRandCapitol(lstSectiuneCapitol_10B);
        }
        return capitol_10b;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_10b jaxb pojo
     * @param gospodarie  entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_10b capitol_10b, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_10b == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_10b");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"AnRaportare");
        }
        if (capitol_10b.getRandCapitol() != null) {
            for (SectiuneCapitol_10b sectiuneCapitol_10B : capitol_10b.getRandCapitol()) {
                RandCapitol_10bConversionHelper.populeazaFromSchemaType(sectiuneCapitol_10B, gospodarie, anRaportare);
            }
        }
    }
}
