package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.SuprafataCategorie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_2a;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_2a;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 15-Oct-15.
 */
public class Capitol_2aConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_2a toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_2a capitol_2a = null;
        List<SectiuneCapitol_2a> listSectiuneCapitol_2A = new ArrayList<SectiuneCapitol_2a>();
        if (gospodarieDTO.getSuprafataCategories() != null) {
            for (SuprafataCategorie suprafataCategorie : gospodarieDTO.getSuprafataCategories()) {
                listSectiuneCapitol_2A.add(RandCapitol_2aConversionHelper.toSchemaType(suprafataCategorie));
            }
        }
        if (!listSectiuneCapitol_2A.isEmpty()) {
            capitol_2a = new Capitol_2a();
            capitol_2a.setRandCapitol(listSectiuneCapitol_2A);
        }
        return capitol_2a;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_2a jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_2a capitol_2a, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_2a == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_2a");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");

        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"AnRaportare");
        }
        if (capitol_2a.getRandCapitol() != null && !capitol_2a.getRandCapitol().isEmpty()) {
            for (SectiuneCapitol_2a sectiuneCapitol_2A : capitol_2a.getRandCapitol()) {
                if (sectiuneCapitol_2A != null) {
                    RandCapitol_2aConversionHelper.populeazaFromSchemaType(sectiuneCapitol_2A, gospodarie, anRaportare);
                }
            }
        }
    }
}
