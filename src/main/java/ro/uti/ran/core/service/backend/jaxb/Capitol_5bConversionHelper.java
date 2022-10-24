package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Plantatie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_5b;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_5b;

import java.util.ArrayList;
import java.util.List;

/**
 * b) Suprafața plantațiilor pomicole și numărul pomilor pe raza localității
 * Created by Dan on 29-Oct-15.
 */
public class Capitol_5bConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_5b toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_5b capitol_5b = null;
        List<SectiuneCapitol_5b> lstSectiuneCapitol_5B = new ArrayList<SectiuneCapitol_5b>();
        if (gospodarieDTO.getPlantaties() != null) {
            for (Plantatie plantatie : gospodarieDTO.getPlantaties()) {
                lstSectiuneCapitol_5B.add(RandCapitol_5bConversionHelper.toSchemaType(plantatie));
            }
        }
        if (!lstSectiuneCapitol_5B.isEmpty()) {
            capitol_5b = new Capitol_5b();
            capitol_5b.setRandCapitol(lstSectiuneCapitol_5B);
        }
        return capitol_5b;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_5b jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_5b capitol_5b, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_5b == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Capitol_5b");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (anRaportare == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        if (capitol_5b.getRandCapitol() != null) {
            for (SectiuneCapitol_5b sectiuneCapitol_5B : capitol_5b.getRandCapitol()) {
                RandCapitol_5bConversionHelper.populeazaFromSchemaType(sectiuneCapitol_5B, gospodarie, anRaportare);
            }
        }
    }
}
