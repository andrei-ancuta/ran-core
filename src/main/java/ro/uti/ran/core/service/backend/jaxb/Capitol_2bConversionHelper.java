package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.ParcelaTeren;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_2b;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_2b;

import java.util.ArrayList;
import java.util.List;

/**
 * Identificarea pe parcele a terenurilor aflate in proprietate
 * Created by Dan on 15-Oct-15.
 */
public class Capitol_2bConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_2b toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_2b capitol_2b = new Capitol_2b();
        List<SectiuneCapitol_2b> listSectiuneCapitol_2B = new ArrayList<SectiuneCapitol_2b>();
        if (gospodarieDTO.getParcelaTerens() != null) {
            for (ParcelaTeren parcelaTeren : gospodarieDTO.getParcelaTerens()) {
                listSectiuneCapitol_2B.add(RandCapitol_2bConversionHelper.toSchemaType(parcelaTeren));
            }
        }
        if (!listSectiuneCapitol_2B.isEmpty()) {
            capitol_2b = new Capitol_2b();
            capitol_2b.setRandCapitol(listSectiuneCapitol_2B);
        }
        return capitol_2b;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_2b jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_2b capitol_2b, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_2b == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_2b");

        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"AnRaportare");
        }
        if (capitol_2b.getRandCapitol() != null) {
            for (SectiuneCapitol_2b sectiuneCapitol_2B : capitol_2b.getRandCapitol()) {
                RandCapitol_2bConversionHelper.populeazaFromSchemaType(sectiuneCapitol_2B, gospodarie, anRaportare);
            }
        }
    }
}
