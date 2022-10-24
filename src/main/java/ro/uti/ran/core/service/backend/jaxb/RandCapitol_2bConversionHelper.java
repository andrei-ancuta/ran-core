package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.utils.SuprafataHelper;
import ro.uti.ran.core.xml.model.capitol.nested.Act;
import ro.uti.ran.core.xml.model.capitol.nested.Extravilan;
import ro.uti.ran.core.xml.model.capitol.nested.Intravilan;
import ro.uti.ran.core.xml.model.capitol.nested.Localizare;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_2b;
import ro.uti.ran.core.xml.model.types.FRACTIE_DOUA_ZECIMALE;
import ro.uti.ran.core.xml.model.types.GEOMETRIE_PARCELA_TEREN;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;
import ro.uti.ran.core.xml.model.types.PF;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dan on 15-Oct-15.
 */
public class RandCapitol_2bConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param parcelaTeren entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_2b toSchemaType(ParcelaTeren parcelaTeren) {
        if (parcelaTeren == null) {
            throw new IllegalArgumentException("ParcelaTeren nedefinit!");
        }
        SectiuneCapitol_2b sectiuneCapitol_2B = new SectiuneCapitol_2b();
        /*codRand*/
        sectiuneCapitol_2B.setCodRand(parcelaTeren.getCodRand());
         /*denumire*/
        sectiuneCapitol_2B.setDenumire(parcelaTeren.getDenumire());
        /*intravilanHA + intravilanARI*/
        if (RanConstants.PARCELA_TEREN_INTRAVILAN.equals(parcelaTeren.getIntravilanExtravilan()) &&
                parcelaTeren.getSuprafata() != null) {
            Intravilan suprafata = new Intravilan();
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            FRACTIE_DOUA_ZECIMALE fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
            Map<String, Object> tmp = SuprafataHelper.extractHAAndARIFromMP(BigInteger.valueOf(parcelaTeren.getSuprafata()));
            if (tmp.get(SuprafataHelper.KEY_HA) != null) {
                BigInteger value = (BigInteger) tmp.get(SuprafataHelper.KEY_HA);
                intreg_pozitiv.setValue(value.intValue());
                suprafata.setIntravilanHA(intreg_pozitiv);
            } else {
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(0);
                suprafata.setIntravilanHA(intreg_pozitiv);
            }
            if (tmp.get(SuprafataHelper.KEY_ARI) != null) {
                BigDecimal value = (BigDecimal) tmp.get(SuprafataHelper.KEY_ARI);
                fractie_doua_zecimale.setValue(value);
                suprafata.setIntravilanARI(fractie_doua_zecimale);
            } else {
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(BigDecimal.ZERO);
                suprafata.setIntravilanARI(fractie_doua_zecimale);
            }
            sectiuneCapitol_2B.setSuprafata(suprafata);
        }
        /*extravilanHA + extravilanARI*/
        if (RanConstants.PARCELA_TEREN_EXTRAVILAN.equals(parcelaTeren.getIntravilanExtravilan()) &&
                parcelaTeren.getSuprafata() != null) {
            Extravilan suprafata = new Extravilan();
            INTREG_POZITIV intreg_pozitiv = new INTREG_POZITIV();
            FRACTIE_DOUA_ZECIMALE fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
            Map<String, Object> tmp = SuprafataHelper.extractHAAndARIFromMP(BigInteger.valueOf(parcelaTeren.getSuprafata()));
            if (tmp.get(SuprafataHelper.KEY_HA) != null) {
                BigInteger value = (BigInteger) tmp.get(SuprafataHelper.KEY_HA);
                intreg_pozitiv.setValue(value.intValue());
                suprafata.setExtravilanHA(intreg_pozitiv);
            } else {
                intreg_pozitiv = new INTREG_POZITIV();
                intreg_pozitiv.setValue(0);
                suprafata.setExtravilanHA(intreg_pozitiv);
            }
            if (tmp.get(SuprafataHelper.KEY_ARI) != null) {
                BigDecimal value = (BigDecimal) tmp.get(SuprafataHelper.KEY_ARI);
                fractie_doua_zecimale.setValue(value);
                suprafata.setExtravilanARI(fractie_doua_zecimale);
            } else {
                fractie_doua_zecimale = new FRACTIE_DOUA_ZECIMALE();
                fractie_doua_zecimale.setValue(BigDecimal.ZERO);
                suprafata.setExtravilanARI(fractie_doua_zecimale);
            }
            sectiuneCapitol_2B.setSuprafata(suprafata);
        }
        /*nrBlocFizic*/
        sectiuneCapitol_2B.setNrBlocFizic(parcelaTeren.getNrBlocFizic());
        /*proprietar*/
        if (parcelaTeren.getProprietarParcelas() != null && !parcelaTeren.getProprietarParcelas().isEmpty()) {
            List<PF> listJaxb = new ArrayList<>();
            for (ProprietarParcela proprietarParcela : parcelaTeren.getProprietarParcelas()) {
                listJaxb.add(PFConversionHelper.toSchemaType(proprietarParcela.getPersoanaFizica()));
            }
            sectiuneCapitol_2B.setProprietar(listJaxb);
        }
        /*codCatFolosinta*/
        if (parcelaTeren.getCapCategorieFolosinta() != null) {
            sectiuneCapitol_2B.setCodCatFolosinta(parcelaTeren.getCapCategorieFolosinta().getCodRand());
        }
        /*mentiuni*/
        sectiuneCapitol_2B.setMentiuni(parcelaTeren.getMentiune());
        /*codModalitateDetinere*/
        if (parcelaTeren.getNomModalitateDetinere() != null) {
            sectiuneCapitol_2B.setCodModalitateDetinere(parcelaTeren.getNomModalitateDetinere().getCod());
        }
        /*act detinere*/
        if (parcelaTeren.getActDetineres() != null && !parcelaTeren.getActDetineres().isEmpty()) {
            List<Act> listActJaxb = new ArrayList<Act>();
            for (ActDetinere actDetinere : parcelaTeren.getActDetineres()) {
                listActJaxb.add(ActConversionHelper.toSchemaType(actDetinere));
            }
            sectiuneCapitol_2B.setActDetinere(listActJaxb);
        }
        /*actInstrainare*/
        if (parcelaTeren.getActInstrainare() != null) {
            sectiuneCapitol_2B.setActInstrainare(ActConversionHelper.toSchemaType(parcelaTeren));
        }
        /*localizare*/
        if (parcelaTeren.getParcelaLocalizares() != null && !parcelaTeren.getParcelaLocalizares().isEmpty()) {
            List<Localizare> listLocalizareJaxb = new ArrayList<Localizare>();
            for (ParcelaLocalizare parcelaLocalizare : parcelaTeren.getParcelaLocalizares()) {
                listLocalizareJaxb.add(LocalizareConversionHelper.toSchemaType(parcelaLocalizare));
            }
            sectiuneCapitol_2B.setLocalizare(listLocalizareJaxb);
        }
        /*referintaGeoXml*/
        if (parcelaTeren.getGeometrieParcelaTeren() != null) {
            GeometrieParcelaTeren geometrieParcelaTeren = parcelaTeren.getGeometrieParcelaTeren();
            GEOMETRIE_PARCELA_TEREN geometrie_parcela_teren = new GEOMETRIE_PARCELA_TEREN();
            geometrie_parcela_teren.setReferintaGeoXml(geometrieParcelaTeren.getGeometrieGML());
            geometrie_parcela_teren.setIsFolosinta(1 == geometrieParcelaTeren.getIsFolosinta());
            sectiuneCapitol_2B.setGeometrieParcelaTeren(geometrie_parcela_teren);
        }
        return sectiuneCapitol_2B;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_2B jaxb pojo
     * @param gospodarie         entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_2b sectiuneCapitol_2B, Gospodarie gospodarie, Integer anRaportare) throws RequestValidationException {
        if (sectiuneCapitol_2B == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "sectiuneCapitol_2B");
        }
        if (gospodarie == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "gospodarie");
        }
        if (anRaportare == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "anRaportare");
        }
        ParcelaTeren parcelaTeren = new ParcelaTeren();
        /*anRaportare*/
        parcelaTeren.setAn(anRaportare);
        /*codRand*/
        parcelaTeren.setCodRand(sectiuneCapitol_2B.getCodRand());
        /*denumire*/
        parcelaTeren.setDenumire(sectiuneCapitol_2B.getDenumire());
        /*intravilanHA + intravilanARI*/
        if (sectiuneCapitol_2B.getSuprafata() instanceof Intravilan) {
            Intravilan suprafata = (Intravilan) sectiuneCapitol_2B.getSuprafata();
            if (suprafata.getIntravilanARI() != null || suprafata.getIntravilanHA() != null) {
                parcelaTeren.setSuprafata(SuprafataHelper.transformToMP(
                        (suprafata.getIntravilanARI() != null) ? suprafata.getIntravilanARI().getValue() : null,
                        (suprafata.getIntravilanHA() != null) ? suprafata.getIntravilanHA().getValue() : null
                ));
            }
            parcelaTeren.setIntravilanExtravilan(RanConstants.PARCELA_TEREN_INTRAVILAN);
        }
        /*extravilanHA + extravilanARI*/
        if (sectiuneCapitol_2B.getSuprafata() instanceof Extravilan) {
            Extravilan suprafata = (Extravilan) sectiuneCapitol_2B.getSuprafata();
            if (suprafata.getExtravilanARI() != null || suprafata.getExtravilanHA() != null) {
                parcelaTeren.setSuprafata(
                        SuprafataHelper.transformToMP(
                                (suprafata.getExtravilanARI() != null) ? suprafata.getExtravilanARI().getValue() : null,
                                (suprafata.getExtravilanHA() != null) ? suprafata.getExtravilanHA().getValue() : null
                        ));
            }
            parcelaTeren.setIntravilanExtravilan(RanConstants.PARCELA_TEREN_EXTRAVILAN);
        }
        /*nrBlocFizic*/
        parcelaTeren.setNrBlocFizic(sectiuneCapitol_2B.getNrBlocFizic());

        /*proprietar*/
        if (sectiuneCapitol_2B.getProprietar() != null) {
            for (PF pf : sectiuneCapitol_2B.getProprietar()) {
                ProprietarParcela proprietarParcela = new ProprietarParcela();
                proprietarParcela.setPersoanaFizica(PFConversionHelper.transformaFromSchemaTypeToPersoanaFizica(pf));
                parcelaTeren.addProprietarParcela(proprietarParcela);
            }
        }


        /*codCatFolosinta*/
        if (null != sectiuneCapitol_2B.getCodCatFolosinta()) {
            CapCategorieFolosinta capCategorieFolosinta = new CapCategorieFolosinta();
            capCategorieFolosinta.setCodRand(sectiuneCapitol_2B.getCodCatFolosinta());
            parcelaTeren.setCapCategorieFolosinta(capCategorieFolosinta);
        }
        /*mentiuni*/
        parcelaTeren.setMentiune(sectiuneCapitol_2B.getMentiuni());
        /*codModalitateDetinere*/
        if (StringUtils.isNotEmpty(sectiuneCapitol_2B.getCodModalitateDetinere())) {
            NomModalitateDetinere nomModalitateDetinere = new NomModalitateDetinere();
            nomModalitateDetinere.setCod(sectiuneCapitol_2B.getCodModalitateDetinere());
            parcelaTeren.setNomModalitateDetinere(nomModalitateDetinere);
        }
        /*act detinere*/
        if (sectiuneCapitol_2B.getActDetinere() != null) {
            for (Act act : sectiuneCapitol_2B.getActDetinere()) {
                ActDetinere actDetinere = new ActDetinere();
                ActConversionHelper.populeazaFromSchemaType(act, actDetinere);
                parcelaTeren.addActDetinere(actDetinere);
            }
        }
        /*act instrainare*/
        if (sectiuneCapitol_2B.getActInstrainare() != null) {
            ActConversionHelper.populeazaFromSchemaType(sectiuneCapitol_2B.getActInstrainare(), parcelaTeren);
        }

        /*List<Localizare> localizare*/
        if (sectiuneCapitol_2B.getLocalizare() != null) {
            for (Localizare localizare : sectiuneCapitol_2B.getLocalizare()) {
                LocalizareConversionHelper.populeazaFromSchemaType(localizare, parcelaTeren);
            }
        }
         /*referintaGeoXml*/
        GEOMETRIE_PARCELA_TEREN geoInfo = sectiuneCapitol_2B.getGeometrieParcelaTeren();
        if (geoInfo != null) {
            GeometrieParcelaTeren geometrieParcelaTeren = new GeometrieParcelaTeren();
            geometrieParcelaTeren.setGeometrieGML(geoInfo.getReferintaGeoXml());
            geometrieParcelaTeren.setIsFolosinta(geoInfo.getIsFolosinta() ? 1 : 0);
            parcelaTeren.setGeometrieParcelaTeren(geometrieParcelaTeren);
        }

        /*populare entity pojo*/
        gospodarie.addParcelaTeren(parcelaTeren);
    }


}
