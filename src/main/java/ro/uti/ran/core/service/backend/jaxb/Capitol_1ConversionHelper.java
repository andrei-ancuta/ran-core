package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.DetinatorPf;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.MembruPf;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_1;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_1;

import java.util.ArrayList;
import java.util.List;

/**
 * membru_gospodarie
 * Created by Dan on 14-Oct-15.
 */
public class Capitol_1ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_1 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_1 capitol_1 = null;
        List<SectiuneCapitol_1> listSectiuneCapitol_1 = new ArrayList<SectiuneCapitol_1>();
        if (gospodarieDTO.getMembruPfs() != null) {
            for (MembruPf membruPf : gospodarieDTO.getMembruPfs()) {
                listSectiuneCapitol_1.add(RandCapitol_1ConversionHelper.toSchemaType(membruPf));
            }
        }
        if (!listSectiuneCapitol_1.isEmpty()) {
            capitol_1 = new Capitol_1();
            capitol_1.setRandCapitol(listSectiuneCapitol_1);
        }
        return capitol_1;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_1  jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_1 capitol_1, Gospodarie gospodarie) throws RequestValidationException {
        if (capitol_1 == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_1");
        }
        if (gospodarie == null) {
              throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        if (capitol_1.getRandCapitol() != null && !capitol_1.getRandCapitol().isEmpty()) {
            DetinatorPf detinatorPf = new DetinatorPf();
            gospodarie.addDetinatorPf(detinatorPf);
            for (SectiuneCapitol_1 sectiuneCapitol_1 : capitol_1.getRandCapitol()) {
                RandCapitol_1ConversionHelper.populeazaFromSchemaType(sectiuneCapitol_1, detinatorPf);
            }
        }
    }
}
