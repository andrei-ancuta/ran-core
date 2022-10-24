package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.AplicareIngrasamant;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_10a;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_10a;

import java.util.ArrayList;
import java.util.List;

/**
 * a) Aplicarea îngrășămintelor, amendamentelor și a pesticidelor pe suprafețe situate pe raza localității
 * Created by Dan on 30-Oct-15.
 */
public class Capitol_10aConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_10a toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_10a capitol_10a = null;
        List<SectiuneCapitol_10a> lstSectiuneCapitol_10A = new ArrayList<SectiuneCapitol_10a>();
        if (gospodarieDTO.getAplicareIngrasamants() != null) {
            for (AplicareIngrasamant aplicareIngrasaminte : gospodarieDTO.getAplicareIngrasamants()) {
                lstSectiuneCapitol_10A.add(RandCapitol_10aConversionHelper.toSchemaType(aplicareIngrasaminte));
            }
        }
        if (!lstSectiuneCapitol_10A.isEmpty()) {
            capitol_10a = new Capitol_10a();
            capitol_10a.setRandCapitol(lstSectiuneCapitol_10A);
        }
        return capitol_10a;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_10a  jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_10a capitol_10a, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_10a == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_10a");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"AnRaportare");
        }
        if (capitol_10a.getRandCapitol() != null) {
            for (SectiuneCapitol_10a sectiuneCapitol_10A : capitol_10a.getRandCapitol()) {
                RandCapitol_10aConversionHelper.populeazaFromSchemaType(sectiuneCapitol_10A, gospodarie, anRaportare);
            }
        }
    }
}
