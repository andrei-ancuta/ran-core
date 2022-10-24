package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.TerenIrigat;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_6;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_6;

import java.util.ArrayList;
import java.util.List;

/**
 * Suprafețele efectiv irigate în câmp, situate pe raza localității
 * Created by Dan on 29-Oct-15.
 */
public class Capitol_6ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_6 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_6 capitol_6 = null;
        List<SectiuneCapitol_6> lstSectiuneCapitol_6 = new ArrayList<SectiuneCapitol_6>();
        if (gospodarieDTO.getTerenIrigats() != null) {
            for (TerenIrigat terenIrigat : gospodarieDTO.getTerenIrigats()) {
                lstSectiuneCapitol_6.add(RandCapitol_6ConversionHelper.toSchemaType(terenIrigat));
            }
        }
        if (!lstSectiuneCapitol_6.isEmpty()) {
            capitol_6 = new Capitol_6();
            capitol_6.setRandCapitol(lstSectiuneCapitol_6);
        }
        return capitol_6;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_6  jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_6 capitol_6, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_6 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_6");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"AnRaportare");
        }
        if (capitol_6.getRandCapitol() != null) {
            for (SectiuneCapitol_6 sectiuneCapitol_6 : capitol_6.getRandCapitol()) {
                RandCapitol_6ConversionHelper.populeazaFromSchemaType(sectiuneCapitol_6, gospodarie, anRaportare);
            }
        }
    }
}
