package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.SuprafataUtilizare;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_3;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_3;

import java.util.ArrayList;
import java.util.List;

/**
 * Modul de utilizare a suprafețelor agricole situate pe raza localității
 * Created by Dan on 15-Oct-15.
 */
public class Capitol_3ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_3 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_3 capitol_3 = null;
        List<SectiuneCapitol_3> lstSectiuneCapitol_3 = new ArrayList<SectiuneCapitol_3>();
        if (gospodarieDTO.getSuprafataUtilizares() != null) {
            for (SuprafataUtilizare suprafataUtilizare : gospodarieDTO.getSuprafataUtilizares()) {
                lstSectiuneCapitol_3.add(RandCapitol_3ConversionHelper.toSchemaType(suprafataUtilizare));
            }
        }
        if (!lstSectiuneCapitol_3.isEmpty()) {
            capitol_3 = new Capitol_3();
            capitol_3.setRandCapitol(lstSectiuneCapitol_3);
        }
        return capitol_3;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_3  jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_3 capitol_3, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_3 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_3");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (anRaportare == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        if (capitol_3.getRandCapitol() != null) {
            for (SectiuneCapitol_3 sectiuneCapitol_3 : capitol_3.getRandCapitol()) {
                RandCapitol_3ConversionHelper.populeazaFromSchemaType(sectiuneCapitol_3, gospodarie, anRaportare);
            }
        }
    }
}
