package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.DetinatorPf;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_0_12;

/**
 * Created by Dan on 13-Oct-15.
 */
public class RandCapitol_0_12ConversionHelper {


    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_0_12 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        SectiuneCapitol_0_12 sectiuneCapitol_0_12 = new SectiuneCapitol_0_12();
        /*DateIdentificareGospodarie*/
        DateIdentificareGospodarieConversionHelper.toSchemaType(gospodarieDTO, sectiuneCapitol_0_12);
        /*PF - gospodar*/
        if (gospodarieDTO.getDetinatorPfs() != null && !gospodarieDTO.getDetinatorPfs().isEmpty() && gospodarieDTO.getDetinatorPfs().get(0).getPersoanaFizica() != null) {
            sectiuneCapitol_0_12.setGospodar(PFConversionHelper.toSchemaType(gospodarieDTO.getDetinatorPfs().get(0).getPersoanaFizica()));
        }

        /*elementeJuridice*/
        if (gospodarieDTO.getDetinatorPfs() != null && !gospodarieDTO.getDetinatorPfs().isEmpty()) {
            DetinatorPf detinatorPf = gospodarieDTO.getDetinatorPfs().get(0);
            if (detinatorPf.getPersoanaRc() != null && StringUtils.isNotEmpty(detinatorPf.getPersoanaRc().getCui())) {
                sectiuneCapitol_0_12.setElementeJuridice(RCConversionHelper.toSchemaType(detinatorPf.getPersoanaRc()));
            }
        }

        return sectiuneCapitol_0_12;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_0_12 jaxb pojo
     * @param gospodarie           entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_0_12 sectiuneCapitol_0_12, Gospodarie gospodarie) throws RequestValidationException {
        if (sectiuneCapitol_0_12 == null) {
               throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_0_12");
        }
        if (gospodarie == null) {
             throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"Gospodarie");
        }
        /*dateIdentificareGospodarie*/
        DateIdentificareGospodarieConversionHelper.populeazaFromSchemaType(sectiuneCapitol_0_12, gospodarie);
        /*gospodar - PF*/
        DetinatorPf detinatorPf = new DetinatorPf();
        PFConversionHelper.populeazaFromSchemaType(sectiuneCapitol_0_12.getGospodar(), detinatorPf);
        gospodarie.addDetinatorPf(detinatorPf);
        /*elementeJuridice - RC*/
        if (sectiuneCapitol_0_12.getElementeJuridice() != null) {
            detinatorPf.setPersoanaRc(RCConversionHelper.transformaFromSchemaTypeToPersoanaRc(sectiuneCapitol_0_12.getElementeJuridice()));
        }
    }
}
