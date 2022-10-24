package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.DetinatorPf;
import ro.uti.ran.core.model.registru.MembruPf;
import ro.uti.ran.core.model.registru.NomLegaturaRudenie;
import ro.uti.ran.core.model.registru.PersoanaFizica;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_1;
import ro.uti.ran.core.xml.model.types.CNP;
import ro.uti.ran.core.xml.model.types.NIF;
import ro.uti.ran.core.xml.model.types.NString;

/**
 * membru_gospodarie
 * Created by Dan on 14-Oct-15.
 */
public class RandCapitol_1ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param membruPf entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_1 toSchemaType(MembruPf membruPf) {
        if (membruPf == null) {
            throw new IllegalArgumentException("MembruPf nedefinit!");
        }
        SectiuneCapitol_1 sectiuneCapitol_1 = new SectiuneCapitol_1();


        /*persoana fizica*/
        if (membruPf.getPersoanaFizica() != null) {
            sectiuneCapitol_1.setDateMembruGospodarie(PFConversionHelper.toSchemaType(membruPf.getPersoanaFizica()));
        }
        /*codRand*/
        sectiuneCapitol_1.setCodRand(membruPf.getCodRand());
        /*codLegaturaRudenie; denumireLegaturaRudenie*/
        sectiuneCapitol_1.setCodLegaturaRudenie(new NString(membruPf.getNomLegaturaRudenie().getCod()));
        sectiuneCapitol_1.setDenumireLegaturaRudenie(new NString(membruPf.getNomLegaturaRudenie().getDenumire()));
        /*mentiuni*/
        sectiuneCapitol_1.setMentiuni(membruPf.getMentiune());
        return sectiuneCapitol_1;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_1 jaxb pojo
     * @param detinatorPf       entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_1 sectiuneCapitol_1, DetinatorPf detinatorPf) throws RequestValidationException {
        if (sectiuneCapitol_1 == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_1");
        }
        if (detinatorPf == null) {
             throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"detinatorPf");
        }
        MembruPf membruPf = new MembruPf();
        PersoanaFizica persoanaFizica = new PersoanaFizica();
        membruPf.setPersoanaFizica(persoanaFizica);
        /*persoana fizica*/
        Object identificatorPersoana = sectiuneCapitol_1.getDateMembruGospodarie().getIdentificator();
        if (identificatorPersoana instanceof CNP) {
            membruPf.getPersoanaFizica().setCnp(((CNP) sectiuneCapitol_1.getDateMembruGospodarie().getIdentificator()).getValue().toUpperCase());
        } else {
            membruPf.getPersoanaFizica().setNif(((NIF) sectiuneCapitol_1.getDateMembruGospodarie().getIdentificator()).getValue().toUpperCase());
        }

        /*nume*/
        membruPf.getPersoanaFizica().setNume(sectiuneCapitol_1.getDateMembruGospodarie().getNume().getValue().toUpperCase());
        /*prenume*/
        membruPf.getPersoanaFizica().setPrenume(sectiuneCapitol_1.getDateMembruGospodarie().getPrenume().getValue().toUpperCase());
        /*initialaTata*/
        membruPf.getPersoanaFizica().setInitialaTata(sectiuneCapitol_1.getDateMembruGospodarie().getInitialaTata().getValue().toUpperCase());
        /*codRand*/
        membruPf.setCodRand(sectiuneCapitol_1.getCodRand());
        /*codLegaturaRudenie; denumireLegaturaRudenie*/
        if (StringUtils.isNotEmpty(sectiuneCapitol_1.getCodLegaturaRudenie().getValue())) {
            NomLegaturaRudenie nomLegaturaRudenie = new NomLegaturaRudenie();
            nomLegaturaRudenie.setCod(sectiuneCapitol_1.getCodLegaturaRudenie().getValue());
            nomLegaturaRudenie.setDenumire(sectiuneCapitol_1.getDenumireLegaturaRudenie().getValue());
            membruPf.setNomLegaturaRudenie(nomLegaturaRudenie);
        }
        /*mentiuni*/
        membruPf.setMentiune(sectiuneCapitol_1.getMentiuni());
        /*populare entity pojo*/
        detinatorPf.addMembruPf(membruPf);
    }
}
