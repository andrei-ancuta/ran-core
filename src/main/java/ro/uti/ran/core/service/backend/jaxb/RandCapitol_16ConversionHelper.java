package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.MentiuneSpeciala;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_16;

/**
 * Created by Dan on 02-Nov-15.
 */
public class RandCapitol_16ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param mentiuneSpeciala entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_16 toSchemaType(MentiuneSpeciala mentiuneSpeciala) {
        if (mentiuneSpeciala == null) {
            throw new IllegalArgumentException("MentiuneSpeciala nedefinit!");
        }
        SectiuneCapitol_16 sectiuneCapitol_16 = new SectiuneCapitol_16();
        sectiuneCapitol_16.setText(mentiuneSpeciala.getMentiune());
        return sectiuneCapitol_16;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_16 jaxb pojo
     * @param gospodarie     entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_16 sectiuneCapitol_16, Gospodarie gospodarie) throws RequestValidationException {
        if (sectiuneCapitol_16 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_16");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        MentiuneSpeciala mentiuneSpeciala = new MentiuneSpeciala();
        mentiuneSpeciala.setMentiune(sectiuneCapitol_16.getText());
        /*populare entity pojo*/
        gospodarie.addMentiuneSpeciala(mentiuneSpeciala);
    }
}
