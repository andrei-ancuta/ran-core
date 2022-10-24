package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.MentiuneCerereSuc;
import ro.uti.ran.core.model.registru.PersoanaFizica;
import ro.uti.ran.core.xml.model.capitol.nested.Succesibil;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_13;
import ro.uti.ran.core.xml.model.types.NString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 02-Nov-15.
 */
public class RandCapitol_13ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param mentiuneCerereSuc entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_13 toSchemaType(MentiuneCerereSuc mentiuneCerereSuc) {
        if (mentiuneCerereSuc == null) {
            throw new IllegalArgumentException("MentiuneCerereSuc nedefinit!");
        }
        SectiuneCapitol_13 sectiuneCapitol_13 = new SectiuneCapitol_13();
        /*nr*/
        sectiuneCapitol_13.setNr(new NString(mentiuneCerereSuc.getNrInregistrare()));
        /*dataInregistrare*/
        sectiuneCapitol_13.setDataInregistrare(mentiuneCerereSuc.getDataInregistrare());
        /*persoana*/
        sectiuneCapitol_13.setDefunct(PFConversionHelper.toSchemaType(mentiuneCerereSuc.getPersoanaFizica()));
        /*dataDeces*/
        sectiuneCapitol_13.setDataDeces(mentiuneCerereSuc.getDataDeces());
        /*denumireSPB_BIN*/
        sectiuneCapitol_13.setDenumireSPB_BIN(new NString(mentiuneCerereSuc.getSpnBin()));
        /*succesibil*/
        if (mentiuneCerereSuc.getSuccesibils() != null && !mentiuneCerereSuc.getSuccesibils().isEmpty()) {
            List<Succesibil> listSuccesibils = new ArrayList<Succesibil>();
            for (ro.uti.ran.core.model.registru.Succesibil succesibil : mentiuneCerereSuc.getSuccesibils()) {
                listSuccesibils.add(SuccesibilConversionHelper.toSchemaType(succesibil));
            }
            sectiuneCapitol_13.setSuccesibil(listSuccesibils);
        }
        return sectiuneCapitol_13;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_13 jaxb pojo
     * @param gospodarie         entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_13 sectiuneCapitol_13, Gospodarie gospodarie) throws RequestValidationException {
        if (sectiuneCapitol_13 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_13");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        MentiuneCerereSuc mentiuneCerereSuc = new MentiuneCerereSuc();
        /*nr*/
        mentiuneCerereSuc.setNrInregistrare(sectiuneCapitol_13.getNr().getValue());
        /*dataInregistrare*/
        mentiuneCerereSuc.setDataInregistrare(sectiuneCapitol_13.getDataInregistrare());
        /*numeDefunct*/
        mentiuneCerereSuc.setPersoanaFizica(PFConversionHelper.transformaFromSchemaTypeToPersoanaFizica(sectiuneCapitol_13.getDefunct()));

        /*dataDeces*/
        mentiuneCerereSuc.setDataDeces(sectiuneCapitol_13.getDataDeces());
        /*denumireSPB_BIN*/
        mentiuneCerereSuc.setSpnBin(sectiuneCapitol_13.getDenumireSPB_BIN().getValue());
        /*succesibil*/
        if (sectiuneCapitol_13.getSuccesibil() != null) {
            for (Succesibil succesibil : sectiuneCapitol_13.getSuccesibil()) {
                ro.uti.ran.core.model.registru.Succesibil succesibilEntity = new ro.uti.ran.core.model.registru.Succesibil();
                SuccesibilConversionHelper.populeazaFromSchemaType(succesibil, succesibilEntity);
                mentiuneCerereSuc.addSuccesibil(succesibilEntity);
            }
        }
        /*populare entity pojo*/
        gospodarie.addMentiuneCerereSuc(mentiuneCerereSuc);

    }
}
