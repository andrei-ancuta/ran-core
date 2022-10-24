package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_11;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;
import ro.uti.ran.core.xml.model.types.NString;

/**
 * Created by Dan on 02-Nov-15.
 */
public class RandCapitol_11ConversionHelper {


    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param cladire entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_11 toSchemaType(Cladire cladire) {
        if (cladire == null) {
            throw new IllegalArgumentException("Cladire nedefinit!");
        }
        SectiuneCapitol_11 sectiuneCapitol_11 = new SectiuneCapitol_11();
        /*identificator*/
        sectiuneCapitol_11.setIdentificator(new NString(cladire.getIdentificator()));
        /*adresa*/
        if (cladire.getAdresa() != null) {
            sectiuneCapitol_11.setAdresa(AdresaConversionHelper.toSchemaType(cladire.getAdresa()));
        }
        /*zona*/
        sectiuneCapitol_11.setZona(new NString(cladire.getZona()));
        INTREG_POZITIV intreg_pozitiv;
        /*suprafataConstruitaDesfasurataMP*/
        if (cladire.getSuprafataDesfasurata() != null) {
            intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(cladire.getSuprafataDesfasurata());
            sectiuneCapitol_11.setSuprafataConstruitaDesfasurataMP(intreg_pozitiv);
        }
        /*suprafataConstruitaLaSolMP*/
        if (cladire.getSuprafataSol() != null) {
            intreg_pozitiv = new INTREG_POZITIV();
            intreg_pozitiv.setValue(cladire.getSuprafataSol());
            sectiuneCapitol_11.setSuprafataConstruitaLaSolMP(intreg_pozitiv);
        }
        /*identificatorCadastralElectronic*/
        sectiuneCapitol_11.setIdentificatorCadastralElectronic(cladire.getIdentificatorCadastral());
        /*codTipCladire*/
        if (cladire.getNomTipCladire() != null) {
            sectiuneCapitol_11.setCodTipCladire(new NString(cladire.getNomTipCladire().getCod()));
        }
        /*codDestinatieCladire*/
        if (cladire.getNomDestinatieCladire() != null) {
            sectiuneCapitol_11.setCodDestinatieCladire(new NString(cladire.getNomDestinatieCladire().getCod()));
        }
        /*anulTerminarii*/
        sectiuneCapitol_11.setAnulTerminarii(cladire.getAnTerminare());
        /*referintaGeoXml*/
        if (StringUtils.isNotEmpty(cladire.getGeometrieGML())) {
            sectiuneCapitol_11.setReferintaGeoXml(cladire.getGeometrieGML());
        }
        //
        return sectiuneCapitol_11;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_11 jaxb pojo
     * @param gospodarie         entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_11 sectiuneCapitol_11, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_11 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_11");

        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"anRaportare");
        }
        Cladire cladire = new Cladire();
        /*an*/
        cladire.setAn(anRaportare);
        /*identificator*/
        cladire.setIdentificator(sectiuneCapitol_11.getIdentificator().getValue());
        /*adresa*/
        if (sectiuneCapitol_11.getAdresa() != null) {
            Adresa adresa = new Adresa();
            AdresaConversionHelper.populeazaFromSchemaType(sectiuneCapitol_11.getAdresa(), adresa);
            cladire.setAdresa(adresa);
        }
        /*zona*/
        cladire.setZona(sectiuneCapitol_11.getZona().getValue());
        /*suprafataConstruitaDesfasurataMP*/
        if (sectiuneCapitol_11.getSuprafataConstruitaDesfasurataMP() != null) {
            cladire.setSuprafataDesfasurata(sectiuneCapitol_11.getSuprafataConstruitaDesfasurataMP().getValue());
        }
        /*suprafataConstruitaLaSolMP*/
        if (sectiuneCapitol_11.getSuprafataConstruitaLaSolMP() != null) {
            cladire.setSuprafataSol(sectiuneCapitol_11.getSuprafataConstruitaLaSolMP().getValue());
        }
        /*identificatorCadastralElectronic*/
        cladire.setIdentificatorCadastral(sectiuneCapitol_11.getIdentificatorCadastralElectronic());
        /*codTipCladire*/
        if (StringUtils.isNotEmpty(sectiuneCapitol_11.getCodTipCladire().getValue())) {
            NomTipCladire nomTipCladire = new NomTipCladire();
            nomTipCladire.setCod(sectiuneCapitol_11.getCodTipCladire().getValue());
            cladire.setNomTipCladire(nomTipCladire);
        }
        /*codDestinatieCladire*/
        if (StringUtils.isNotEmpty(sectiuneCapitol_11.getCodDestinatieCladire().getValue())) {
            NomDestinatieCladire nomDestinatieCladire = new NomDestinatieCladire();
            nomDestinatieCladire.setCod(sectiuneCapitol_11.getCodDestinatieCladire().getValue());
            cladire.setNomDestinatieCladire(nomDestinatieCladire);
        }
        /*anulTerminarii*/
        cladire.setAnTerminare(sectiuneCapitol_11.getAnulTerminarii());
        /*referintaGeoXml*/
        if (StringUtils.isNotEmpty(sectiuneCapitol_11.getReferintaGeoXml())) {
            cladire.setGeometrieGML(sectiuneCapitol_11.getReferintaGeoXml());
        }
        /*populare entity pojo*/
        gospodarie.addCladire(cladire);

    }
}
