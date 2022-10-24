package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.model.registru.NomTipLocalizare;
import ro.uti.ran.core.model.registru.ParcelaLocalizare;
import ro.uti.ran.core.model.registru.ParcelaTeren;
import ro.uti.ran.core.xml.model.capitol.nested.Localizare;

/**
 * Created by Dan on 30-Oct-15.
 */
public class LocalizareConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param parcelaLocalizare entity pojo
     * @return jaxb pojo
     */
    public static Localizare toSchemaType(ParcelaLocalizare parcelaLocalizare) {
        if (parcelaLocalizare == null) {
            throw new IllegalArgumentException("ParcelaLocalizare nedefinit!");
        }
        Localizare localizare = new Localizare();
        /*codTip*/
        if (parcelaLocalizare.getNomTipLocalizare() != null) {
            localizare.setCodTip(parcelaLocalizare.getNomTipLocalizare().getCod());
             /*denumire*/
            localizare.setDenumire(parcelaLocalizare.getNomTipLocalizare().getDenumire());
        }
        /*valoare*/
        localizare.setValoare(parcelaLocalizare.getValoare());
        return localizare;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param localizare   jaxb pojo
     * @param parcelaTeren entity pojo
     */
    public static void populeazaFromSchemaType(Localizare localizare, ParcelaTeren parcelaTeren) {
        if (localizare == null) {
            throw new IllegalArgumentException("Localizare nedefinit!");
        }
        if (parcelaTeren == null) {
            throw new IllegalArgumentException("ParcelaTeren nedefinit!");
        }
        ParcelaLocalizare parcelaLocalizare = new ParcelaLocalizare();
        /*codTip*/
        if (StringUtils.isNotEmpty(localizare.getCodTip())) {
            NomTipLocalizare nomTipLocalizare = new NomTipLocalizare();
            nomTipLocalizare.setCod(localizare.getCodTip());
            /*denumire*/
            nomTipLocalizare.setDenumire(localizare.getDenumire());
            parcelaLocalizare.setNomTipLocalizare(nomTipLocalizare);
        }
        /*valoare*/
        parcelaLocalizare.setValoare(localizare.getValoare());
        //
        parcelaTeren.addParcelaLocalizare(parcelaLocalizare);
    }
}
