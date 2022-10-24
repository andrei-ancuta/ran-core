package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.model.registru.NomTipAct;
import ro.uti.ran.core.model.registru.ParcelaTeren;
import ro.uti.ran.core.xml.model.capitol.nested.Act;
import ro.uti.ran.core.xml.model.types.NString;

/**
 * Created by Dan on 15-Oct-15.
 */
public class ActConversionHelper {
    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param actDetinere entity pojo
     * @return jaxb pojo
     */
    public static Act toSchemaType(ro.uti.ran.core.model.registru.ActDetinere actDetinere) {
        if (actDetinere == null) {
            throw new IllegalArgumentException("ActDetinere  nedefinit!");
        }
        Act act = new Act();
        /*codTip*/
        if (actDetinere.getAct().getNomTipAct() != null) {
            act.setCodTip(new NString(actDetinere.getAct().getNomTipAct().getCod()));
        }
        /*nrAct*/
        act.setNrAct(new NString(actDetinere.getAct().getNumarAct()));
        /*dataAct*/
        act.setDataAct(actDetinere.getAct().getDataAct());
        /*emitent*/
        act.setEmitent(new NString(actDetinere.getAct().getEmitent()));
        return act;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param act         jaxb pojo
     * @param actDetinere entity pojo
     */
    public static void populeazaFromSchemaType(Act act, ro.uti.ran.core.model.registru.ActDetinere actDetinere) {
        if (act == null) {
            throw new IllegalArgumentException("Act nedefinit!");
        }
        if (actDetinere == null) {
            throw new IllegalArgumentException("ActDetinere  nedefinit!");
        }
        ro.uti.ran.core.model.registru.Act actEntity = new ro.uti.ran.core.model.registru.Act();
        /*codTip*/
        if (StringUtils.isNotEmpty(act.getCodTip().getValue())) {
            NomTipAct nomTipAct = new NomTipAct();
            nomTipAct.setCod(act.getCodTip().getValue());
            actEntity.setNomTipAct(nomTipAct);
        }
        /*nrAct*/
        actEntity.setNumarAct(act.getNrAct().getValue());
        /*dataAct*/
        actEntity.setDataAct(act.getDataAct());
        /*emitent*/
        actEntity.setEmitent(act.getEmitent().getValue());
        //
        actDetinere.setAct(actEntity);
    }


    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param parcelaTeren entity pojo
     * @return jaxb pojo
     */
    public static Act toSchemaType(ParcelaTeren parcelaTeren) {
        if (parcelaTeren == null) {
            throw new IllegalArgumentException("ParcelaTeren  nedefinit!");
        }
        Act act = new Act();
        /*codTip*/
        if (parcelaTeren.getActInstrainare().getNomTipAct() != null) {
            act.setCodTip(new NString(parcelaTeren.getActInstrainare().getNomTipAct().getCod()));
        }
        /*nrAct*/
        act.setNrAct(new NString(parcelaTeren.getActInstrainare().getNumarAct()));
        /*dataAct*/
        act.setDataAct(parcelaTeren.getActInstrainare().getDataAct());
        /*emitent*/
        act.setEmitent(new NString(parcelaTeren.getActInstrainare().getEmitent()));
        return act;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param act          jaxb pojo
     * @param parcelaTeren entity pojo
     */
    public static void populeazaFromSchemaType(Act act, ParcelaTeren parcelaTeren) {
        if (act == null) {
            throw new IllegalArgumentException("Act nedefinit!");
        }
        if (parcelaTeren == null) {
            throw new IllegalArgumentException("ParcelaTeren  nedefinit!");
        }
        ro.uti.ran.core.model.registru.Act actEntity = new ro.uti.ran.core.model.registru.Act();
        /*codTip*/
        if (StringUtils.isNotEmpty(act.getCodTip().getValue())) {
            NomTipAct nomTipAct = new NomTipAct();
            nomTipAct.setCod(act.getCodTip().getValue());
            actEntity.setNomTipAct(nomTipAct);
        }
        /*nrAct*/
        actEntity.setNumarAct(act.getNrAct().getValue());
        /*dataAct*/
        actEntity.setDataAct(act.getDataAct());
        /*emitent*/
        actEntity.setEmitent(act.getEmitent().getValue());
        //
        parcelaTeren.setActInstrainare(actEntity);
    }

}
