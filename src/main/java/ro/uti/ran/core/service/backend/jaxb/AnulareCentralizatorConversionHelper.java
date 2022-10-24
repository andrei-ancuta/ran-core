package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.service.backend.dto.AnulareDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.AnulareCapitol;
import ro.uti.ran.core.xml.model.capitol.*;

/**
 * Created by smash on 23/11/15.
 */
public class AnulareCentralizatorConversionHelper {


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
        /*codCapitol*/
        anulareDTO.setCodCapitol(anulareCapitolJaxb.getCodCapitol());
        /*denumire*/
        anulareDTO.setDenumire(anulareCapitolJaxb.getDenumire());
        /*semestru*/
//        anulareDTO.setSemestruRaportare(anulareJaxb.getSemestru());

        //populare DTO parinte
        ranDocDTO.setAnulareDTO(anulareDTO);

        /*Capitol_21a*/
        if (TipCapitol.CAP12a.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_12a.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP12a);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }

        if (TipCapitol.CAP12a1.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_12a1.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP12a1);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }

        if (TipCapitol.CAP12b1.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_12b1.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP12b1);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }

        if (TipCapitol.CAP12b2.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_12b2.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP12b2);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }

        if (TipCapitol.CAP12c.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_12c.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP12c);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }

        if (TipCapitol.CAP12d.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_12d.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP12d);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }

        if (TipCapitol.CAP12e.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_12e.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP12e);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }

        if (TipCapitol.CAP12f.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_12f.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP12f);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }

        if (TipCapitol.CAP13cent.name().equals(anulareDTO.getCodCapitol())) {
            ranDocDTO.setClazz(Capitol_13_centralizator.class.getName());
            ranDocDTO.setTipCapitol(TipCapitol.CAP13cent);
            ranDocDTO.setCodCapitol(anulareDTO.getCodCapitol());
        }

    }

}
