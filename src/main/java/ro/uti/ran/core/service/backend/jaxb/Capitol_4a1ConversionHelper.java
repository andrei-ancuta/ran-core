package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Cultura;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_4a1;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_4a1;

import java.util.ArrayList;
import java.util.List;

/**
 * a1) Culturi succesive în câmp, culturi intercalate, culturi modificate genetic pe raza localității
 * Created by Dan on 28-Oct-15.
 */
public class Capitol_4a1ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_4a1 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_4a1 capitol_4a1 = null;
        List<SectiuneCapitol_4a1> lstSectiuneCapitol_4A1 = new ArrayList<SectiuneCapitol_4a1>();
        if (gospodarieDTO.getCulturas() != null) {
            for (Cultura cultura : gospodarieDTO.getCulturas()) {
                lstSectiuneCapitol_4A1.add(RandCapitol_4a1ConversionHelper.toSchemaType(cultura));
            }
        }
        if (!lstSectiuneCapitol_4A1.isEmpty()) {
            capitol_4a1 = new Capitol_4a1();
            capitol_4a1.setRandCapitol(lstSectiuneCapitol_4A1);
        }
        return capitol_4a1;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_4a1 jaxb pojo
     * @param gospodarie  entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_4a1 capitol_4a1, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_4a1 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_4a1");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        if (capitol_4a1.getRandCapitol() != null) {
            for (SectiuneCapitol_4a1 sectiuneCapitol_4A1 : capitol_4a1.getRandCapitol()) {
                RandCapitol_4a1ConversionHelper.populeazaFromSchemaType(sectiuneCapitol_4A1, gospodarie, anRaportare);
            }
        }
    }
}
