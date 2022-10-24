package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.CapPomRazlet;
import ro.uti.ran.core.model.registru.PomRazlet;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_5a;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

/**
 * Created by Dan on 28-Oct-15.
 */
public class RandCapitol_5aConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param pomRazlet entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_5a toSchemaType(PomRazlet pomRazlet) {
        if (pomRazlet == null) {
            throw new IllegalArgumentException("PomRazlet nedefinit!");
        }
        SectiuneCapitol_5a sectiuneCapitol_5A = new SectiuneCapitol_5a();
        /*codNomenclator + codRand + denumire*/
        sectiuneCapitol_5A.setCodNomenclator(pomRazlet.getCapPomRazlet().getCod());
        sectiuneCapitol_5A.setCodRand(pomRazlet.getCapPomRazlet().getCodRand());
        sectiuneCapitol_5A.setDenumire(pomRazlet.getCapPomRazlet().getDenumire());
        /*nrPomiPeRod*/
        if (pomRazlet.getNrPomRod() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(pomRazlet.getNrPomRod());
            sectiuneCapitol_5A.setNrPomiPeRod(intreg_pozitiv);
        }
        /*nrPomiTineri*/
        if (pomRazlet.getNrPomTanar() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(pomRazlet.getNrPomTanar());
            sectiuneCapitol_5A.setNrPomiTineri(intreg_pozitiv);
        }
        return sectiuneCapitol_5A;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_5A jaxb pojo
     * @param gospodarie     entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_5a sectiuneCapitol_5A, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_5A == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_5A");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        PomRazlet pomRazlet = new PomRazlet();
        /*an*/
        pomRazlet.setAn(anRaportare);
        /*codNomenclator + codRand + denumire*/
        CapPomRazlet capPomRazlet = new CapPomRazlet();
        capPomRazlet.setCod(sectiuneCapitol_5A.getCodNomenclator());
        capPomRazlet.setCodRand(sectiuneCapitol_5A.getCodRand());
        capPomRazlet.setDenumire(sectiuneCapitol_5A.getDenumire());
        pomRazlet.setCapPomRazlet(capPomRazlet);
        /*nrPomiPeRod*/
        if (sectiuneCapitol_5A.getNrPomiPeRod() != null) {
            pomRazlet.setNrPomRod(sectiuneCapitol_5A.getNrPomiPeRod().getValue());
        }
        /*nrPomiTineri*/
        if (sectiuneCapitol_5A.getNrPomiTineri() != null) {
            pomRazlet.setNrPomTanar(sectiuneCapitol_5A.getNrPomiTineri().getValue());
        }
         /*populare entity pojo*/
        gospodarie.addPomRazlet(pomRazlet);
    }
}
