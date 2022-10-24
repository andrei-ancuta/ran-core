package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Plantatie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_5d;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_5d;

import java.util.ArrayList;
import java.util.List;

/**
 * d) Vii, pepiniere viticole și hameiști situate pe raza localității
 * Created by Dan on 29-Oct-15.
 */
public class Capitol_5dConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_5d toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_5d capitol_5d = null;
        List<SectiuneCapitol_5d> lstSectiuneCapitol_5D = new ArrayList<SectiuneCapitol_5d>();
        if (gospodarieDTO.getPlantaties() != null) {
            for (Plantatie plantatie : gospodarieDTO.getPlantaties()) {
                lstSectiuneCapitol_5D.add(RandCapitol_5dConversionHelper.toSchemaType(plantatie));
            }
        }
        if (!lstSectiuneCapitol_5D.isEmpty()) {
            capitol_5d = new Capitol_5d();
            capitol_5d.setRandCapitol(lstSectiuneCapitol_5D);
        }
        return capitol_5d;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_5d jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_5d capitol_5d, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (capitol_5d == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_5d");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        if (capitol_5d.getRandCapitol() != null) {
            for (SectiuneCapitol_5d sectiuneCapitol_5D : capitol_5d.getRandCapitol()) {
                RandCapitol_5dConversionHelper.populeazaFromSchemaType(sectiuneCapitol_5D, gospodarie, anRaportare);
            }
        }
    }
}
