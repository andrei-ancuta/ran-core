package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.service.backend.dto.AnulareDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.AnulareCapitol;
import ro.uti.ran.core.xml.model.capitol.*;

/**
 * Created by Dan on 16-Nov-15.
 */
public class AnulareConversionHelper {


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param anulareCapitolJaxb jaxb pojo
     * @param ranDocDTO   dto
     */
    public static void populeazaFromSchemaType(AnulareCapitol anulareCapitolJaxb, RanDocDTO ranDocDTO) {
        populeazaFromSchemaType(anulareCapitolJaxb, ranDocDTO, null);
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param anulareCapitolJaxb jaxb pojo
     * @param ranDocDTO   dto
     * @param an          an raportare
     */
    public static void populeazaFromSchemaType(AnulareCapitol anulareCapitolJaxb, RanDocDTO ranDocDTO, Integer an) {
        if (anulareCapitolJaxb == null) {
            throw new IllegalArgumentException("Anulare jaxb nedefinit!");
        }
        if (ranDocDTO == null) {
            throw new IllegalArgumentException("RanDocDTO nedefinit!");
        }
        AnulareDTO anulareDTO = new AnulareDTO();
        /*an*/
        anulareDTO.setAnRaportare(an);
        ranDocDTO.setAnRaportare(an);
        /*codCapitol*/
        anulareDTO.setCodCapitol(anulareCapitolJaxb.getCodCapitol());
        /*denumire*/
        anulareDTO.setDenumire(anulareCapitolJaxb.getDenumire());
        /*semestru*/
        anulareDTO.setSemestruRaportare(anulareCapitolJaxb.getSemestru());
        ranDocDTO.setSemestruRaportare(anulareCapitolJaxb.getSemestru());
        //populare DTO parinte
        ranDocDTO.setAnulareDTO(anulareDTO);
        /*Capitol_0_12*/
        if (TipCapitol.CAP0_12.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_0_12.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP0_12);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_0_34*/
        if (TipCapitol.CAP0_34.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_0_34.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP0_34);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        if (TipCapitol.CAP1.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_1.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP1);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_1*/
        if (TipCapitol.CAP1.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_1.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP1);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_2a*/
        if (TipCapitol.CAP2a.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_2a.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP2a);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_2b*/
        if (TipCapitol.CAP2b.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_2b.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP2b);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_3*/
        if (TipCapitol.CAP3.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_3.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP3);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_4a*/
        if (TipCapitol.CAP4a.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_4a.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP4a);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_4a1*/
        if (TipCapitol.CAP4a1.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_4a1.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP4a1);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_4b1*/
        if (TipCapitol.CAP4b1.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_4b1.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP4b1);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
         /*Capitol_4b2*/
        if (TipCapitol.CAP4b2.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_4b2.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP4b2);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_4c*/
        if (TipCapitol.CAP4c.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_4c.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP4c);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_5a*/
        if (TipCapitol.CAP5a.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_5a.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP5a);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
         /*Capitol_5b*/
        if (TipCapitol.CAP5b.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_5b.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP5b);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_5c*/
        if (TipCapitol.CAP5c.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_5c.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP5c);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_5d*/
        if (TipCapitol.CAP5d.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_5d.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP5d);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_6*/
        if (TipCapitol.CAP6.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_6.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP6);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_7*/
        if (TipCapitol.CAP7.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_7.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP7);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_8*/
        if (TipCapitol.CAP8.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_8.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP8);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_9*/
        if (TipCapitol.CAP9.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_9.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP9);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
         /*Capitol_10a*/
        if (TipCapitol.CAP10a.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_10a.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP10a);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_10b*/
        if (TipCapitol.CAP10b.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_10b.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP10b);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_11*/
        if (TipCapitol.CAP11.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_11.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP11);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
         /*Capitol_12*/
        if (TipCapitol.CAP12.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_12.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP12);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_13*/
        if (TipCapitol.CAP13.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_13.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP13);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_14*/
        if (TipCapitol.CAP14.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_14.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP14);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_15a*/
        if (TipCapitol.CAP15a.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_15a.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP15a);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_15b*/
        if (TipCapitol.CAP15b.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_15b.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP15b);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
        /*Capitol_16*/
        if (TipCapitol.CAP16.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_16.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP16);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }
    }
}
