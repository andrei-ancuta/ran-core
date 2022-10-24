package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.TipAdresa;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_0_34;

/**
 * Created by Dan on 14-Oct-15.
 */
public class RandCapitol_0_34ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_0_34 toSchemaType(GospodarieDTO gospodarieDTO) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        SectiuneCapitol_0_34 sectiuneCapitol_0_34 = new SectiuneCapitol_0_34();
        /*DateIdentificareGospodarie*/
        DateIdentificareGospodarieConversionHelper.toSchemaType(gospodarieDTO, sectiuneCapitol_0_34);
        /*persoanaJuridica*/
        if (gospodarieDTO.getDetinatorPjs() != null && !gospodarieDTO.getDetinatorPjs().isEmpty()) {
            DetinatorPj detinatorPj = gospodarieDTO.getDetinatorPjs().get(0);
            if (detinatorPj.getPersoanaRc() != null) {
                sectiuneCapitol_0_34.setPersoanaJuridica(RCConversionHelper.toSchemaType(detinatorPj.getPersoanaRc()));
            }
        }
        /*reprezentantLegal + denumireSubdiviziuneEntitate*/
        if (gospodarieDTO.getDetinatorPjs() != null && !gospodarieDTO.getDetinatorPjs().isEmpty()) {
            DetinatorPj detinatorPj = gospodarieDTO.getDetinatorPjs().get(0);
            if (detinatorPj.getPersoanaFizica() != null) {
                sectiuneCapitol_0_34.setReprezentantLegal(PFConversionHelper.toSchemaType(detinatorPj.getPersoanaFizica()));
            }
            if (StringUtils.isNotEmpty(detinatorPj.getDenumireSubdiviziune())) {
                sectiuneCapitol_0_34.setDenumireSubdiviziuneEntitate(detinatorPj.getDenumireSubdiviziune());
            }
        }
        /*adresaReprezentantLegal + adresaSubdiviziuneEntitate*/
        if (gospodarieDTO.getAdresaGospodaries() != null && !gospodarieDTO.getAdresaGospodaries().isEmpty()) {
            for (AdresaGospodarie adresaEntity : gospodarieDTO.getAdresaGospodaries()) {
                if (TipAdresa.DOMICILIU_REPREZENTANT_LEGAL.equals(TipAdresa.getTipAdresaByCod(adresaEntity.getNomTipAdresa().getCod()))) {
                    sectiuneCapitol_0_34.setAdresaReprezentantLegal(AdresaConversionHelper.toSchemaType(adresaEntity.getAdresa()));
                }
                if (TipAdresa.SEDIU_PCT_LUCRU.equals(TipAdresa.getTipAdresaByCod(adresaEntity.getNomTipAdresa().getCod()))) {
                    sectiuneCapitol_0_34.setAdresaSubdiviziuneEntitate(AdresaConversionHelper.toSchemaType(adresaEntity.getAdresa()));
                }
            }
        }

        return sectiuneCapitol_0_34;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_0_34 jaxb pojo
     * @param gospodarie           entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_0_34 sectiuneCapitol_0_34, Gospodarie gospodarie) throws RequestValidationException {
        if (sectiuneCapitol_0_34 == null) {
           throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"sectiuneCapitol_0_34");
        }
        if (gospodarie == null) {
           throw new RequestValidationException(RequestCodes.MISSING_ELEMENT,"gospodarie");
        }
        /*DateIdentificareGospodarie*/
        DateIdentificareGospodarieConversionHelper.populeazaFromSchemaType(sectiuneCapitol_0_34, gospodarie);
        //
        DetinatorPj detinatorPj = new DetinatorPj();
        /*persoanaJuridica*/
        if (sectiuneCapitol_0_34.getPersoanaJuridica() != null) {
            RCConversionHelper.populeazaFromSchemaType(sectiuneCapitol_0_34.getPersoanaJuridica(), detinatorPj);
        }
        /*reprezentantLegal*/
        if (sectiuneCapitol_0_34.getReprezentantLegal() != null) {
            PFConversionHelper.populeazaFromSchemaType(sectiuneCapitol_0_34.getReprezentantLegal(), detinatorPj);
        }
        /*adresaReprezentantLegal*/
        if (sectiuneCapitol_0_34.getAdresaReprezentantLegal() != null) {
            AdresaGospodarie adresaGospodarie = new AdresaGospodarie();
            NomTipAdresa nomTipAdresa = new NomTipAdresa();
            nomTipAdresa.setCod(TipAdresa.DOMICILIU_REPREZENTANT_LEGAL.getCod());
            adresaGospodarie.setNomTipAdresa(nomTipAdresa);
            Adresa adresa = new Adresa();
            adresaGospodarie.setAdresa(adresa);
            AdresaConversionHelper.populeazaFromSchemaType(sectiuneCapitol_0_34.getAdresaReprezentantLegal(), adresa);
            gospodarie.addAdresaGospodary(adresaGospodarie);
        }
        /*denumireSubdiviziuneEntitate; codNomenclaturaStradalaSubdiviziuneEntitate; referintaGeoXmlSubdiviziuneEntitate*/
        detinatorPj.setDenumireSubdiviziune(sectiuneCapitol_0_34.getDenumireSubdiviziuneEntitate());
        /*adresaSubdiviziuneEntitate*/
        if (sectiuneCapitol_0_34.getAdresaSubdiviziuneEntitate() != null) {
            AdresaGospodarie adresaGospodarie = new AdresaGospodarie();
            NomTipAdresa nomTipAdresa = new NomTipAdresa();
            nomTipAdresa.setCod(TipAdresa.SEDIU_PCT_LUCRU.getCod());
            adresaGospodarie.setNomTipAdresa(nomTipAdresa);
            Adresa adresa = new Adresa();
            adresaGospodarie.setAdresa(adresa);
            AdresaConversionHelper.populeazaFromSchemaType(sectiuneCapitol_0_34.getAdresaSubdiviziuneEntitate(), adresa);
            gospodarie.addAdresaGospodary(adresaGospodarie);
        }
        /*populare entity pojo*/
        gospodarie.addDetinatorPj(detinatorPj);
    }
}
