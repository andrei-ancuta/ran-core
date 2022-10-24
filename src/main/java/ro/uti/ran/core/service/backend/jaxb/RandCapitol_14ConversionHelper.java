package ro.uti.ran.core.service.backend.jaxb;

import org.apache.commons.lang.StringUtils;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.codes.RequestCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.TipAct;
import ro.uti.ran.core.model.utils.TipRelPreemptiune;
import ro.uti.ran.core.xml.model.capitol.sectiune.SectiuneCapitol_14;
import ro.uti.ran.core.xml.model.types.INTREG_POZITIV;
import ro.uti.ran.core.xml.model.types.NString;
import ro.uti.ran.core.xml.model.types.PF;
import ro.uti.ran.core.xml.model.types.RC;

/**
 * Created by Dan on 02-Nov-15.
 */
public class RandCapitol_14ConversionHelper {

    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param preemptiune entity pojo
     * @return jaxb pojo
     */
    public static SectiuneCapitol_14 toSchemaType(Preemptiune preemptiune) {
        if (preemptiune == null) {
            throw new IllegalArgumentException("Preemptiune nedefinit!");
        }
        SectiuneCapitol_14 sectiuneCapitol_14 = new SectiuneCapitol_14();
        /*nrOfertaVanzare*/
        sectiuneCapitol_14.setNrOfertaVanzare(new NString(preemptiune.getNrOfertaVanzare()));
        /*dataOfertaVanzare*/
        sectiuneCapitol_14.setDataOfertaVanzare(preemptiune.getDataOfertaVanzare());
        /*suprafataHA*/
        if (preemptiune.getSuprafata() != null) {
            INTREG_POZITIV intregPozitiv = new INTREG_POZITIV();
            intregPozitiv.setValue(preemptiune.getSuprafata());
            sectiuneCapitol_14.setSuprafataHA(intregPozitiv);
        }
        /*nrCarteFunciara*/
        sectiuneCapitol_14.setNrCarteFunciara(new NString(preemptiune.getNrCarteFunciara()));
        if (preemptiune.getActAvizMadrDadr() != null) {
            /*nrAvizMADR_DADR*/
            sectiuneCapitol_14.setNrAvizMADR_DADR(new NString(preemptiune.getActAvizMadrDadr().getNumarAct()));
            /*dataAvizMADR_DADR*/
            sectiuneCapitol_14.setDataAvizMADR_DADR(preemptiune.getActAvizMadrDadr().getDataAct());
        }
        if (preemptiune.getActAdevVanzareLib() != null) {
            /*nrAdeverintaVanzare*/
            sectiuneCapitol_14.setNrAdeverintaVanzare(new NString(preemptiune.getActAdevVanzareLib().getNumarAct()));
            /*dataAdeverintaVanzare*/
            sectiuneCapitol_14.setDataAdeverintaVanzare(preemptiune.getActAdevVanzareLib().getDataAct());
        }
        /*preemptor + cumparator + vanzator*/
        if (preemptiune.getPersoanaPreemptiunes() != null) {
            for (PersoanaPreemptiune persoanaPreemptiune : preemptiune.getPersoanaPreemptiunes()) {
                if (TipRelPreemptiune.PREEMPTOR.getCod().equalsIgnoreCase(persoanaPreemptiune.getNomTipRelPreemptiune().getCod())) {
                    if (persoanaPreemptiune.getPersoanaRc() != null) {
                        sectiuneCapitol_14.getPreemptor().add(RCConversionHelper.toSchemaType(persoanaPreemptiune.getPersoanaRc()));
                    } else if (persoanaPreemptiune.getPersoanaFizica() != null) {
                        sectiuneCapitol_14.getPreemptor().add(PFConversionHelper.toSchemaType(persoanaPreemptiune.getPersoanaFizica()));
                    }
                }
                if (TipRelPreemptiune.VANZATOR.getCod().equalsIgnoreCase(persoanaPreemptiune.getNomTipRelPreemptiune().getCod())) {
                    if (persoanaPreemptiune.getPersoanaRc() != null) {
                        sectiuneCapitol_14.getVanzator().add(RCConversionHelper.toSchemaType(persoanaPreemptiune.getPersoanaRc()));
                    } else if (persoanaPreemptiune.getPersoanaFizica() != null) {
                        sectiuneCapitol_14.getVanzator().add(PFConversionHelper.toSchemaType(persoanaPreemptiune.getPersoanaFizica()));
                    }
                }
                if (TipRelPreemptiune.CUMPARATOR.getCod().equalsIgnoreCase(persoanaPreemptiune.getNomTipRelPreemptiune().getCod())) {
                    if (persoanaPreemptiune.getPersoanaRc() != null) {
                        sectiuneCapitol_14.setCumparator(RCConversionHelper.toSchemaType(persoanaPreemptiune.getPersoanaRc()));
                    } else if (persoanaPreemptiune.getPersoanaFizica() != null) {
                        sectiuneCapitol_14.setCumparator(PFConversionHelper.toSchemaType(persoanaPreemptiune.getPersoanaFizica()));
                    }
                }
            }
        }
        /*pretRON*/
        if (preemptiune.getPretOfertaVanzare() != null) {
            INTREG_POZITIV intregPozitiv = new INTREG_POZITIV();
            intregPozitiv.setValue(preemptiune.getPretOfertaVanzare());
            sectiuneCapitol_14.setPretRON(intregPozitiv);
        }
        return sectiuneCapitol_14;
    }

    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param sectiuneCapitol_14 jaxb pojo
     * @param gospodarie         entity pojo
     */
    public static void populeazaFromSchemaType(SectiuneCapitol_14 sectiuneCapitol_14, Gospodarie gospodarie) throws RequestValidationException {
        if (sectiuneCapitol_14 == null) {

            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "sectiuneCapitol_14");
        }
        if (gospodarie == null) {
            throw new RequestValidationException(RequestCodes.MISSING_ELEMENT, "gospodarie");
        }
        Preemptiune preemptiune = new Preemptiune();
        /*nrOfertaVanzare*/
        preemptiune.setNrOfertaVanzare(sectiuneCapitol_14.getNrOfertaVanzare().getValue());
        /*dataOfertaVanzare*/
        preemptiune.setDataOfertaVanzare(sectiuneCapitol_14.getDataOfertaVanzare());
        /*suprafataHA*/
        if (sectiuneCapitol_14.getSuprafataHA() != null) {
            preemptiune.setSuprafata(sectiuneCapitol_14.getSuprafataHA().getValue());
        }
        /*nrCarteFunciara*/
        preemptiune.setNrCarteFunciara(sectiuneCapitol_14.getNrCarteFunciara().getValue());
        Act actAvizMadrDadr = new Act();
        preemptiune.setActAvizMadrDadr(actAvizMadrDadr);
        /*nrAvizMADR_DADR*/
        actAvizMadrDadr.setNumarAct(sectiuneCapitol_14.getNrAvizMADR_DADR().getValue());
        /*dataAvizMADR_DADR*/
        actAvizMadrDadr.setDataAct(sectiuneCapitol_14.getDataAvizMADR_DADR());
        //
        NomTipAct nomTipAct = new NomTipAct();
        nomTipAct.setCod(TipAct.AVIZ_MADR_DADR.getCod());
        actAvizMadrDadr.setNomTipAct(nomTipAct);
        if ((sectiuneCapitol_14.getNrAdeverintaVanzare() != null && StringUtils.isNotEmpty(sectiuneCapitol_14.getNrAdeverintaVanzare().getValue())) ||
                        sectiuneCapitol_14.getDataAdeverintaVanzare() != null) {
            Act actAdevVanzareLib = new Act();
            preemptiune.setActAdevVanzareLib(actAdevVanzareLib);
            /*nrAdeverintaVanzare*/
            if (sectiuneCapitol_14.getNrAdeverintaVanzare() != null) {
                actAdevVanzareLib.setNumarAct(sectiuneCapitol_14.getNrAdeverintaVanzare().getValue());
            }
            /*dataAdeverintaVanzare*/
            actAdevVanzareLib.setDataAct(sectiuneCapitol_14.getDataAdeverintaVanzare());
            //
            nomTipAct = new NomTipAct();
            nomTipAct.setCod(TipAct.ADEVERINTA_VANZARE.getCod());
            actAdevVanzareLib.setNomTipAct(nomTipAct);
        }
        /*cumparator*/
        if (sectiuneCapitol_14.getCumparator() instanceof PF) {
            PF pf = (PF) sectiuneCapitol_14.getCumparator();
            PersoanaPreemptiune persoanaPreemptiune = new PersoanaPreemptiune();
            /*FK_NOM_TIP_REL_PREEMPTIUNE*/
            NomTipRelPreemptiune nomTipRelPreemptiune = new NomTipRelPreemptiune();
            nomTipRelPreemptiune.setCod(TipRelPreemptiune.CUMPARATOR.getCod());
            persoanaPreemptiune.setNomTipRelPreemptiune(nomTipRelPreemptiune);
            /*restul campuri*/
            PFConversionHelper.populeazaFromSchemaType(pf, persoanaPreemptiune);
            //
            preemptiune.addPersoanaPreemptiune(persoanaPreemptiune);
        }
        if (sectiuneCapitol_14.getCumparator() instanceof RC) {
            RC RC = (RC) sectiuneCapitol_14.getCumparator();
            PersoanaPreemptiune persoanaPreemptiune = new PersoanaPreemptiune();
            /*FK_NOM_TIP_REL_PREEMPTIUNE*/
            NomTipRelPreemptiune nomTipRelPreemptiune = new NomTipRelPreemptiune();
            nomTipRelPreemptiune.setCod(TipRelPreemptiune.CUMPARATOR.getCod());
            persoanaPreemptiune.setNomTipRelPreemptiune(nomTipRelPreemptiune);
            /*restul campuri*/
            RCConversionHelper.populeazaFromSchemaType(RC, persoanaPreemptiune);
            //
            preemptiune.addPersoanaPreemptiune(persoanaPreemptiune);
        }
        /*vanzator*/
        if (sectiuneCapitol_14.getVanzator() != null) {
            for (Object vanzator : sectiuneCapitol_14.getVanzator()) {
                if (vanzator instanceof PF) {
                    PF pf = (PF) vanzator;
                    PersoanaPreemptiune persoanaPreemptiune = new PersoanaPreemptiune();
                    /*FK_NOM_TIP_REL_PREEMPTIUNE*/
                    NomTipRelPreemptiune nomTipRelPreemptiune = new NomTipRelPreemptiune();
                    nomTipRelPreemptiune.setCod(TipRelPreemptiune.VANZATOR.getCod());
                    persoanaPreemptiune.setNomTipRelPreemptiune(nomTipRelPreemptiune);
                    /*restul campuri*/
                    PFConversionHelper.populeazaFromSchemaType(pf, persoanaPreemptiune);
                    //
                    preemptiune.addPersoanaPreemptiune(persoanaPreemptiune);
                }
                if (vanzator instanceof RC) {
                    RC RC = (RC) vanzator;
                    PersoanaPreemptiune persoanaPreemptiune = new PersoanaPreemptiune();
                    /*FK_NOM_TIP_REL_PREEMPTIUNE*/
                    NomTipRelPreemptiune nomTipRelPreemptiune = new NomTipRelPreemptiune();
                    nomTipRelPreemptiune.setCod(TipRelPreemptiune.VANZATOR.getCod());
                    persoanaPreemptiune.setNomTipRelPreemptiune(nomTipRelPreemptiune);
                    /*restul campuri*/
                    RCConversionHelper.populeazaFromSchemaType(RC, persoanaPreemptiune);
                    //
                    preemptiune.addPersoanaPreemptiune(persoanaPreemptiune);
                }
            }
        }
        /*preemptor*/
        if (sectiuneCapitol_14.getPreemptor() != null) {
            for (Object preemptor : sectiuneCapitol_14.getPreemptor()) {
                if (preemptor instanceof PF) {
                    PF pf = (PF) preemptor;
                    PersoanaPreemptiune persoanaPreemptiune = new PersoanaPreemptiune();
                    /*FK_NOM_TIP_REL_PREEMPTIUNE*/
                    NomTipRelPreemptiune nomTipRelPreemptiune = new NomTipRelPreemptiune();
                    nomTipRelPreemptiune.setCod(TipRelPreemptiune.PREEMPTOR.getCod());
                    persoanaPreemptiune.setNomTipRelPreemptiune(nomTipRelPreemptiune);
                    /*restul campuri*/
                    PFConversionHelper.populeazaFromSchemaType(pf, persoanaPreemptiune);
                    //
                    preemptiune.addPersoanaPreemptiune(persoanaPreemptiune);
                }
                if (preemptor instanceof RC) {
                    RC RC = (RC) preemptor;
                    PersoanaPreemptiune persoanaPreemptiune = new PersoanaPreemptiune();
                    /*FK_NOM_TIP_REL_PREEMPTIUNE*/
                    NomTipRelPreemptiune nomTipRelPreemptiune = new NomTipRelPreemptiune();
                    nomTipRelPreemptiune.setCod(TipRelPreemptiune.PREEMPTOR.getCod());
                    persoanaPreemptiune.setNomTipRelPreemptiune(nomTipRelPreemptiune);
                    /*restul campuri*/
                    RCConversionHelper.populeazaFromSchemaType(RC, persoanaPreemptiune);
                    //
                    preemptiune.addPersoanaPreemptiune(persoanaPreemptiune);
                }
            }
        }
        /*pretRON*/
        if (sectiuneCapitol_14.getPretRON() != null) {
            preemptiune.setPretOfertaVanzare(sectiuneCapitol_14.getPretRON().getValue());
        }
        /*populare entity pojo*/
        gospodarie.addPreemptiune(preemptiune);
    }
}
