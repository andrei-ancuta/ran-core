package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.CapSistemTehnic;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.SistemTehnic;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_9;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;

/**
 * Created by Dan on 30-Oct-15.
 */
public class RandCapitol_9ConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param sistemTehnic entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_9 toSchemaType(SistemTehnic sistemTehnic) {
        if (sistemTehnic == null) {
            throw new IllegalArgumentException("SistemTehnic nedefinit!");
        }
        SectiuneCapitol_9 sectiuneCapitol_9 = new SectiuneCapitol_9();
        /*codNomenclator + codRand + denumire*/
        sectiuneCapitol_9.setCodNomenclator(sistemTehnic.getCapSistemTehnic().getCod());
        sectiuneCapitol_9.setCodRand(sistemTehnic.getCapSistemTehnic().getCodRand());
        sectiuneCapitol_9.setDenumire(sistemTehnic.getCapSistemTehnic().getDenumire());
        /*nrBucati*/
        if (sistemTehnic.getNumar() != null) {
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(sistemTehnic.getNumar());
            sectiuneCapitol_9.setNrBucati(intreg_pozitiv);
        }
        return sectiuneCapitol_9;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_9 jaxb pojo
     * @param gospodarie    entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_9 sectiuneCapitol_9, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_9 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_9");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        SistemTehnic sistemTehnic = new SistemTehnic();
        /*an*/
        sistemTehnic.setAn(anRaportare);
        /*codNomenclator + codRand + denumire*/
        CapSistemTehnic capSistemTehnic = new CapSistemTehnic();
        capSistemTehnic.setCod(sectiuneCapitol_9.getCodNomenclator());
        capSistemTehnic.setCodRand(sectiuneCapitol_9.getCodRand());
        capSistemTehnic.setDenumire(sectiuneCapitol_9.getDenumire());
        sistemTehnic.setCapSistemTehnic(capSistemTehnic);
        /*nrBucati*/
        if (sectiuneCapitol_9.getNrBucati() != null) {
            sistemTehnic.setNumar(sectiuneCapitol_9.getNrBucati().getValue());
        }
         /*populare entity pojo*/
        gospodarie.addSistemTehnic(sistemTehnic);
    }
}
