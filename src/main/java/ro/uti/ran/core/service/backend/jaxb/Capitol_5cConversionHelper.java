package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Plantatie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_5c;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_5c;

import java.util.ArrayList;
import java.util.List;

/**
 * c) Alte plantații pomicole aflate în teren agricol, pe raza localității
 * Created by Dan on 29-Oct-15.
 */
public class Capitol_5cConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_5c toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_5c capitol_5c = null;
        List<SectiuneCapitol_5c> lstSectiuneCapitol_5C = new ArrayList<SectiuneCapitol_5c>();
        if (gospodarieDTO.getPlantaties() != null) {
            for (Plantatie plantatie : gospodarieDTO.getPlantaties()) {
                lstSectiuneCapitol_5C.add(RandCapitol_5cConversionHelper.toSchemaType(plantatie));
            }
        }
        if (!lstSectiuneCapitol_5C.isEmpty()) {
            capitol_5c = new Capitol_5c();
            capitol_5c.setRandCapitol(lstSectiuneCapitol_5C);
        }
        return capitol_5c;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_5c jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_5c capitol_5c, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_5c == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_5c");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"AnRaportare");
        }
        if (capitol_5c.getRandCapitol() != null) {
            for (SectiuneCapitol_5c sectiuneCapitol_5C : capitol_5c.getRandCapitol()) {
                RandCapitol_5cConversionHelper.populeazaFromSchemaType(sectiuneCapitol_5C, gospodarie, anRaportare);
            }
        }
    }
}
