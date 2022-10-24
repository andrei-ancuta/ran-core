package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.PomRazlet;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_5a;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_5a;

import java.util.ArrayList;
import java.util.List;

/**
 * a) Numărul pomilor răzleți pe raza localității
 * Created by Dan on 28-Oct-15.
 */
public class Capitol_5aConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_5a toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_5a capitol_5a = null;
        List<SectiuneCapitol_5a> lstSectiuneCapitol_5A = new ArrayList<SectiuneCapitol_5a>();
        if (gospodarieDTO.getPomRazlets() != null) {
            for (PomRazlet pomRazlet : gospodarieDTO.getPomRazlets()) {
                lstSectiuneCapitol_5A.add(RandCapitol_5aConversionHelper.toSchemaType(pomRazlet));
            }
        }
        if (!lstSectiuneCapitol_5A.isEmpty()) {
            capitol_5a = new Capitol_5a();
            capitol_5a.setRandCapitol(lstSectiuneCapitol_5A);
        }
        return capitol_5a;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_5a jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_5a capitol_5a, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_5a == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_5a");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"AnRaportare");
        }
        if (capitol_5a.getRandCapitol() != null) {
            for (SectiuneCapitol_5a sectiuneCapitol_5A : capitol_5a.getRandCapitol()) {
                RandCapitol_5aConversionHelper.populeazaFromSchemaType(sectiuneCapitol_5A, gospodarie, anRaportare);
            }
        }
    }
}
