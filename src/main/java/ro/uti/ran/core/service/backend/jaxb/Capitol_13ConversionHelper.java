package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.MentiuneCerereSuc;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_13;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_13;

import java.util.ArrayList;
import java.util.List;

/**
 * Mențiuni cu privire la sesizările/cererile pentru deschiderea procedurilor succesorale înaintate notarilor publici
 * Created by Dan on 02-Nov-15.
 */
public class Capitol_13ConversionHelper {


    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static Capitol_13 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        Capitol_13 capitol_13 = null;
        List<SectiuneCapitol_13> lstSectiuneCapitol_13 = new ArrayList<SectiuneCapitol_13>();
        if (gospodarieDTO.getMentiuneCerereSucs() != null) {
            for (MentiuneCerereSuc mentiuneCerereSuc : gospodarieDTO.getMentiuneCerereSucs()) {
                lstSectiuneCapitol_13.add(RandCapitol_13ConversionHelper.toSchemaType(mentiuneCerereSuc));
            }
        }
        if (!lstSectiuneCapitol_13.isEmpty()) {
            capitol_13 = new Capitol_13();
            capitol_13.setRandCapitol(lstSectiuneCapitol_13);
        }
        return capitol_13;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param capitol_13 jaxb pojo
     * @param gospodarie entity pojo
     */
    public static void populeazaFromSchemaType(Capitol_13 capitol_13, Gospodarie gospodarie) throws RequestValidationException {
        if (capitol_13 == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"capitol_13");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (capitol_13.getRandCapitol() != null) {
            for (SectiuneCapitol_13 sectiuneCapitol_13 : capitol_13.getRandCapitol()) {
                RandCapitol_13ConversionHelper.populeazaFromSchemaType(sectiuneCapitol_13, gospodarie);
            }
        }
    }

}
