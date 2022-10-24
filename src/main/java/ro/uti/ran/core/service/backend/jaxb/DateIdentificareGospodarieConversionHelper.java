package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.model.utils.TipAdresa;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.xml.model.capitol.nested.AdresaExterna;
import ro.uti.ran.core.xml.model.capitol.nested.DateIdentificareGospodarie;
import ro.uti.ran.core.xml.model.types.NString;

/**
 * Created by Dan on 14-Oct-15.
 */
public class DateIdentificareGospodarieConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static DateIdentificareGospodarie toSchemaType(GospodarieDTO gospodarieDTO, DateIdentificareGospodarie dateIdentificareGospodarie) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        /*tipDetinator*/
        dateIdentificareGospodarie.setTipDetinator(Integer.parseInt(gospodarieDTO.getGospodarie().getNomTipDetinator().getCod()));
        /*pozitieGospodarie*/
        dateIdentificareGospodarie.setPozitieGospodarie(PozitieGospodarieConversionHelper.toSchemaType(gospodarieDTO.getGospodarie()));
        /*domiciliuFiscal: domiciliuFiscalRo sau domiciliuFiscalExtern + adresaGospodarie + codNomenclaturaStradala + referintaGeoXml*/
        if (gospodarieDTO.getAdresaGospodaries() != null) {
            for (AdresaGospodarie adresaEntity : gospodarieDTO.getAdresaGospodaries()) {
                if (TipAdresa.DOMICILIU_FISCAL.equals(TipAdresa.getTipAdresaByCod(adresaEntity.getNomTipAdresa().getCod()))) {
                    if (RanConstants.NOM_TARA_COD_NUMERIC_ROMANIA.equals(adresaEntity.getAdresa().getNomTara().getCodNumeric())) {
                        dateIdentificareGospodarie.setDomiciliuFiscal(AdresaConversionHelper.toSchemaType(adresaEntity.getAdresa()));
                    } else {
                        AdresaExterna adresaExterna = new AdresaExterna();
                        adresaExterna.setCodTara(adresaEntity.getAdresa().getNomTara().getCodNumeric());
                        adresaExterna.setAdresaText(new NString(adresaEntity.getAdresa().getExceptieAdresa()));
                        dateIdentificareGospodarie.setDomiciliuFiscal(adresaExterna);
                    }
                }
                if (TipAdresa.GOSPODARIE.equals(TipAdresa.getTipAdresaByCod(adresaEntity.getNomTipAdresa().getCod()))) {
                    dateIdentificareGospodarie.setAdresaGospodarie(AdresaConversionHelper.toSchemaType(adresaEntity.getAdresa()));
                }
            }
        }
         /*codExploatatie*/
        dateIdentificareGospodarie.setCodExploatatie(gospodarieDTO.getGospodarie().getCodExploatatie());
        /*tipExploatatie*/
        if (gospodarieDTO.getGospodarie().getNomTipExploatatie() != null) {
            dateIdentificareGospodarie.setTipExploatatie(Integer.parseInt(gospodarieDTO.getGospodarie().getNomTipExploatatie().getCod()));
        }
        /*nrUnicIdentificare*/
        dateIdentificareGospodarie.setNrUnicIdentificare(gospodarieDTO.getGospodarie().getNrUnicIdentificare());
        return dateIdentificareGospodarie;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param dateIdentificareGospodarie jaxb pojo
     * @param gospodarie                 entity pojo
     */
    public static void populeazaFromSchemaType(DateIdentificareGospodarie dateIdentificareGospodarie, Gospodarie gospodarie) {
        if (dateIdentificareGospodarie == null) {
            throw new IllegalArgumentException("DateIdentificareGospodarie nedefinit!");
        }
        if (gospodarie == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        /*tipDetinator*/
        if (dateIdentificareGospodarie.getTipDetinator() != null) {
            NomTipDetinator nomTipDetinator = new NomTipDetinator();
            nomTipDetinator.setCod(dateIdentificareGospodarie.getTipDetinator().toString());
            gospodarie.setNomTipDetinator(nomTipDetinator);
        }
        /*identVolum*/
        gospodarie.setIdentVolum(dateIdentificareGospodarie.getPozitieGospodarie().getVolumul().getValue());
        /*pozitieGospodarie*/
        if (dateIdentificareGospodarie.getPozitieGospodarie() != null) {
            PozitieGospodarieConversionHelper.populeazaFromSchemaType(dateIdentificareGospodarie.getPozitieGospodarie(), gospodarie);
        }
        /*domiciliuFiscal: domiciliuFiscalRo sau domiciliuFiscalExtern*/
        if (dateIdentificareGospodarie.getDomiciliuFiscal() != null) {
            /*domiciliuFiscalRo*/
            if (dateIdentificareGospodarie.getDomiciliuFiscal() instanceof ro.uti.ran.core.xml.model.capitol.nested.Adresa) {
                AdresaGospodarie adresaGospodarie = new AdresaGospodarie();
                NomTipAdresa nomTipAdresa = new NomTipAdresa();
                nomTipAdresa.setCod(TipAdresa.DOMICILIU_FISCAL.getCod());
                adresaGospodarie.setNomTipAdresa(nomTipAdresa);
                //
                Adresa adresaEntity = new Adresa();
                adresaGospodarie.setAdresa(adresaEntity);
                ro.uti.ran.core.xml.model.capitol.nested.Adresa adresaJaxb = (ro.uti.ran.core.xml.model.capitol.nested.Adresa) dateIdentificareGospodarie.getDomiciliuFiscal();
                //
                AdresaConversionHelper.populeazaFromSchemaType(adresaJaxb, adresaEntity);
                //
                gospodarie.addAdresaGospodary(adresaGospodarie);
            }
            /*domiciliuFiscalExtern*/
            if (dateIdentificareGospodarie.getDomiciliuFiscal() instanceof AdresaExterna) {
                AdresaGospodarie adresaGospodarie = new AdresaGospodarie();
                NomTipAdresa nomTipAdresa = new NomTipAdresa();
                nomTipAdresa.setCod(TipAdresa.DOMICILIU_FISCAL.getCod());
                adresaGospodarie.setNomTipAdresa(nomTipAdresa);
                //
                Adresa adresaEntity = new Adresa();
                adresaGospodarie.setAdresa(adresaEntity);
                AdresaExterna adresaJaxb = (AdresaExterna) dateIdentificareGospodarie.getDomiciliuFiscal();
                AdresaConversionHelper.populeazaFromSchemaType(adresaJaxb, adresaEntity);
                gospodarie.addAdresaGospodary(adresaGospodarie);
            }
        }
        /*adresaGospodarie + codNomenclaturaStradala + referintaGeoXml*/
        if (dateIdentificareGospodarie.getAdresaGospodarie() != null) {
            AdresaGospodarie adresaGospodarie = new AdresaGospodarie();
            NomTipAdresa nomTipAdresa = new NomTipAdresa();
            nomTipAdresa.setCod(TipAdresa.GOSPODARIE.getCod());
            adresaGospodarie.setNomTipAdresa(nomTipAdresa);
            Adresa adresa = new Adresa();
            adresaGospodarie.setAdresa(adresa);
            AdresaConversionHelper.populeazaFromSchemaType(dateIdentificareGospodarie.getAdresaGospodarie(), adresa);
            gospodarie.addAdresaGospodary(adresaGospodarie);
        }
        /*codExploatatie*/
        gospodarie.setCodExploatatie(dateIdentificareGospodarie.getCodExploatatie() != null ? dateIdentificareGospodarie.getCodExploatatie().toUpperCase() : dateIdentificareGospodarie.getCodExploatatie());
        /*tipExploatatie*/
        if (dateIdentificareGospodarie.getTipExploatatie() != null) {
            NomTipExploatatie nomTipExploatatie = new NomTipExploatatie();
            nomTipExploatatie.setCod(dateIdentificareGospodarie.getTipExploatatie().toString());
            gospodarie.setNomTipExploatatie(nomTipExploatatie);
        }
        /*nrUnicIdentificare*/
        gospodarie.setNrUnicIdentificare(dateIdentificareGospodarie.getNrUnicIdentificare() != null ? dateIdentificareGospodarie.getNrUnicIdentificare().toUpperCase() : dateIdentificareGospodarie.getNrUnicIdentificare());
    }
}
