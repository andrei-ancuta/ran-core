package ro.uti.ran.core.service.backend.jaxb;

import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.service.backend.dto.*;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.*;
import ro.uti.ran.core.xml.model.capitol.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 13-Oct-15.
 */
public class RanDocConversionHelper {


    /**
     * Metoda statica pentru crearea unei entitati RanDoc pe baza unor informatii de capitol.
     *
     * @param capitolCentralizatorDTO - DTO-ul care contine date despre capitolul centralizator
     * @return
     */
    public static RanDoc toSchemaType(CapitolCentralizatorDTO capitolCentralizatorDTO) {
        if (capitolCentralizatorDTO == null) {
            throw new IllegalArgumentException("Capitolul centralizator nu este definit!");
        }
        if (capitolCentralizatorDTO.getAn() == null) {
            throw new IllegalArgumentException("Anul capitolului centralizator nu este definit!");
        }
        RanDoc ranDoc = new RanDoc();
        /*header*/
        ranDoc.setHeader(HeaderConversionHelper.toSchemaType(capitolCentralizatorDTO));
        /*body*/
        Body body = new Body();
        AnRaportareCentralizator anRaportareCentralizator = new AnRaportareCentralizator();
        anRaportareCentralizator.setAn(capitolCentralizatorDTO.getAn());

        switch (capitolCentralizatorDTO.getTipCapitol()) {
            case CAP12a:
                anRaportareCentralizator.setCapitolCentralizatorCuAnRaportare(Capitol_12aConversionHelper.toSchemaType(capitolCentralizatorDTO));
                break;
            case CAP12a1:
                anRaportareCentralizator.setCapitolCentralizatorCuAnRaportare(Capitol_12a1ConversionHelper.toSchemaType(capitolCentralizatorDTO));
                break;
            case CAP12b1:
                anRaportareCentralizator.setCapitolCentralizatorCuAnRaportare(Capitol_12b1ConversionHelper.toSchemaType(capitolCentralizatorDTO));
                break;
            case CAP12b2:
                anRaportareCentralizator.setCapitolCentralizatorCuAnRaportare(Capitol_12b2ConversionHelper.toSchemaType(capitolCentralizatorDTO));
                break;
            case CAP12c:
                anRaportareCentralizator.setCapitolCentralizatorCuAnRaportare(Capitol_12cConversionHelper.toSchemaType(capitolCentralizatorDTO));
                break;
            case CAP12d:
                anRaportareCentralizator.setCapitolCentralizatorCuAnRaportare(Capitol_12dConversionHelper.toSchemaType(capitolCentralizatorDTO));
                break;
            case CAP12e:
                anRaportareCentralizator.setCapitolCentralizatorCuAnRaportare(Capitol_12eConversionHelper.toSchemaType(capitolCentralizatorDTO));
                break;
            case CAP12f:
                anRaportareCentralizator.setCapitolCentralizatorCuAnRaportare(Capitol_12fConversionHelper.toSchemaType(capitolCentralizatorDTO));
                break;
            case CAP13cent:
                anRaportareCentralizator.setCapitolCentralizatorCuAnRaportare(Capitol_13centConversionHelper.toSchemaType(capitolCentralizatorDTO));
                break;
            default:
                throw new UnsupportedOperationException("CAPITOL centralizator neimplementat");
        }

        body.setGospodarieSauRaportare(anRaportareCentralizator);
        ranDoc.setBody(body);

        return ranDoc;
    }


    /**
     * reimpachetare informatii din baza de date pentru a fi transmise ca xml
     *
     * @param gospodarieDTO entity pojo
     * @return jaxb pojo
     */
    public static RanDoc toSchemaType(GospodarieDTO gospodarieDTO, TipCapitol tipCapitol, Integer an, Integer semestru) {
        if (gospodarieDTO == null) {
            throw new IllegalArgumentException("Gospodarie nedefinit!");
        }
        RanDoc ranDoc = new RanDoc();
        /*header*/
        ranDoc.setHeader(HeaderConversionHelper.toSchemaType(gospodarieDTO));
        /*body*/
        Body body = new Body();
        ro.uti.ran.core.xml.model.Gospodarie gospodarieJaxb = new ro.uti.ran.core.xml.model.Gospodarie();
        /*identificator*/
        gospodarieJaxb.setIdentificator(gospodarieDTO.getGospodarie().getIdentificator().toUpperCase());
        /*unitateGospodarie*/
        if (an != null) {
            AnRaportare anRaportare = new AnRaportare();
            anRaportare.setAn(an);
            switch (tipCapitol) {
                case CAP2a:
                    anRaportare.setCapitolCuAnRaportare(Capitol_2aConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP2b:
                    anRaportare.setCapitolCuAnRaportare(Capitol_2bConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP3:
                    anRaportare.setCapitolCuAnRaportare(Capitol_3ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP4a:
                    anRaportare.setCapitolCuAnRaportare(Capitol_4aConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP4a1:
                    anRaportare.setCapitolCuAnRaportare(Capitol_4a1ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP4b1:
                    anRaportare.setCapitolCuAnRaportare(Capitol_4b1ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP4b2:
                    anRaportare.setCapitolCuAnRaportare(Capitol_4b2ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP4c:
                    anRaportare.setCapitolCuAnRaportare(Capitol_4cConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP5a:
                    anRaportare.setCapitolCuAnRaportare(Capitol_5aConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP5b:
                    anRaportare.setCapitolCuAnRaportare(Capitol_5bConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP5c:
                    anRaportare.setCapitolCuAnRaportare(Capitol_5cConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP5d:
                    anRaportare.setCapitolCuAnRaportare(Capitol_5dConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP6:
                    anRaportare.setCapitolCuAnRaportare(Capitol_6ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP7:
                    anRaportare.setCapitolCuAnRaportare(Capitol_7ConversionHelper.toSchemaType(gospodarieDTO, semestru));
                    break;
                case CAP8:
                    anRaportare.setCapitolCuAnRaportare(Capitol_8ConversionHelper.toSchemaType(gospodarieDTO, semestru));
                    break;
                case CAP9:
                    anRaportare.setCapitolCuAnRaportare(Capitol_9ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP10a:
                    anRaportare.setCapitolCuAnRaportare(Capitol_10aConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP10b:
                    anRaportare.setCapitolCuAnRaportare(Capitol_10bConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP11:
                    anRaportare.setCapitolCuAnRaportare(Capitol_11ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
            }
            gospodarieJaxb.setUnitateGospodarie(anRaportare);
        } else {
            switch (tipCapitol) {
                case CAP0_12:
                    gospodarieJaxb.setUnitateGospodarie(Capitol_0_12ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP0_34:
                    gospodarieJaxb.setUnitateGospodarie(Capitol_0_34ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP1:
                    gospodarieJaxb.setUnitateGospodarie(Capitol_1ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP12:
                    gospodarieJaxb.setUnitateGospodarie(Capitol_12ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP13:
                    gospodarieJaxb.setUnitateGospodarie(Capitol_13ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP14:
                    gospodarieJaxb.setUnitateGospodarie(Capitol_14ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP15a:
                    gospodarieJaxb.setUnitateGospodarie(Capitol_15aConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP15b:
                    gospodarieJaxb.setUnitateGospodarie(Capitol_15bConversionHelper.toSchemaType(gospodarieDTO));
                    break;
                case CAP16:
                    gospodarieJaxb.setUnitateGospodarie(Capitol_16ConversionHelper.toSchemaType(gospodarieDTO));
                    break;
            }
        }

        switch (tipCapitol) {
            case CAP0_12:
            case CAP0_34:
            case CAP1:
            case CAP2a:
            case CAP2b:
            case CAP3:
            case CAP4a:
            case CAP4a1:
            case CAP4b1:
            case CAP4b2:
            case CAP4c:
            case CAP5a:
            case CAP5b:
            case CAP5c:
            case CAP5d:
            case CAP6:
            case CAP7:
            case CAP8:
            case CAP9:
            case CAP10a:
            case CAP10b:
            case CAP11:
            case CAP12:
            case CAP13:
            case CAP14:
            case CAP15a:
            case CAP15b:
            case CAP16:
                body.setGospodarieSauRaportare(gospodarieJaxb);
                break;

            default:
                throw new UnsupportedOperationException("CAPITOL neimplementat");
        }

        ranDoc.setBody(body);
        return ranDoc;
    }


    /**
     * reimpachetare informatii din XML pentru a fi transmisa in baza de date
     *
     * @param ranDocJaxb jaxb pojo
     * @param ranDocDTO  dto pojo
     */
    public static void populeazaFromSchemaType(RanDoc ranDocJaxb, RanDocDTO ranDocDTO) throws RequestValidationException {
        if (ranDocJaxb == null) {
            throw new IllegalArgumentException("RanDoc jaxb nedefinit!");
        }
        if (ranDocDTO == null) {
            throw new IllegalArgumentException("RanDocDTO nedefinit!");
        }
        /*header*/
        if (ranDocJaxb.getHeader() != null) {
            HeaderConversionHelper.populeazaFromSchemaType(ranDocJaxb.getHeader(), ranDocDTO);
        }

        Object gospodarieSauRaportare = ranDocJaxb.getBody().getGospodarieSauRaportare();
        if (gospodarieSauRaportare instanceof AnRaportareCentralizator) {
            AnRaportareCentralizator raportareCentralizator = (AnRaportareCentralizator) gospodarieSauRaportare;
            Object capitolCentralizator = raportareCentralizator.getCapitolCentralizatorCuAnRaportare();
            ranDocDTO.setAnRaportare(raportareCentralizator.getAn());
            List<? extends RandCapitolCentralizator> randuriCapitolCentralizator = null;
            if (capitolCentralizator instanceof Capitol_12a) {
                ranDocDTO.setCodCapitol(TipCapitol.CAP12a.name());
                ranDocDTO.setTipCapitol(TipCapitol.CAP12a);
                ranDocDTO.setClazz(Capitol_12a.class.getName());
                randuriCapitolCentralizator = new ArrayList<RandCapitol_12aDTO>();
                Capitol_12aConversionHelper.populeazaFromSchemaType((Capitol_12a) capitolCentralizator, randuriCapitolCentralizator);
            } else if (capitolCentralizator instanceof Capitol_12a1) {
                ranDocDTO.setCodCapitol(TipCapitol.CAP12a1.name());
                ranDocDTO.setTipCapitol(TipCapitol.CAP12a1);
                ranDocDTO.setClazz(Capitol_12a1.class.getName());
                randuriCapitolCentralizator = new ArrayList<RandCapitol_12a1DTO>();
                Capitol_12a1ConversionHelper.populeazaFromSchemaType((Capitol_12a1) capitolCentralizator, randuriCapitolCentralizator);
            } else if (capitolCentralizator instanceof Capitol_12b1) {
                ranDocDTO.setCodCapitol(TipCapitol.CAP12b1.name());
                ranDocDTO.setTipCapitol(TipCapitol.CAP12b1);
                ranDocDTO.setClazz(Capitol_12b1.class.getName());
                randuriCapitolCentralizator = new ArrayList<RandCapitol_12b1DTO>();
                Capitol_12b1ConversionHelper.populeazaFromSchemaType((Capitol_12b1) capitolCentralizator, randuriCapitolCentralizator);
            } else if (capitolCentralizator instanceof Capitol_12b2) {
                ranDocDTO.setCodCapitol(TipCapitol.CAP12b2.name());
                ranDocDTO.setTipCapitol(TipCapitol.CAP12b2);
                ranDocDTO.setClazz(Capitol_12b2.class.getName());
                randuriCapitolCentralizator = new ArrayList<RandCapitol_12b2DTO>();
                Capitol_12b2ConversionHelper.populeazaFromSchemaType((Capitol_12b2) capitolCentralizator, randuriCapitolCentralizator);
            } else if (capitolCentralizator instanceof Capitol_12c) {
                ranDocDTO.setCodCapitol(TipCapitol.CAP12c.name());
                ranDocDTO.setTipCapitol(TipCapitol.CAP12c);
                ranDocDTO.setClazz(Capitol_12c.class.getName());
                randuriCapitolCentralizator = new ArrayList<RandCapitol_12cDTO>();
                Capitol_12cConversionHelper.populeazaFromSchemaType((Capitol_12c) capitolCentralizator, randuriCapitolCentralizator);
            } else if (capitolCentralizator instanceof Capitol_12d) {
                ranDocDTO.setCodCapitol(TipCapitol.CAP12d.name());
                ranDocDTO.setTipCapitol(TipCapitol.CAP12d);
                ranDocDTO.setClazz(Capitol_12d.class.getName());
                randuriCapitolCentralizator = new ArrayList<RandCapitol_12dDTO>();
                Capitol_12dConversionHelper.populeazaFromSchemaType((Capitol_12d) capitolCentralizator, randuriCapitolCentralizator);
            } else if (capitolCentralizator instanceof Capitol_12e) {
                ranDocDTO.setCodCapitol(TipCapitol.CAP12e.name());
                ranDocDTO.setTipCapitol(TipCapitol.CAP12e);
                ranDocDTO.setClazz(Capitol_12e.class.getName());
                randuriCapitolCentralizator = new ArrayList<RandCapitol_12eDTO>();
                Capitol_12eConversionHelper.populeazaFromSchemaType((Capitol_12e) capitolCentralizator, randuriCapitolCentralizator);
            } else if (capitolCentralizator instanceof Capitol_12f) {
                ranDocDTO.setCodCapitol(TipCapitol.CAP12f.name());
                ranDocDTO.setTipCapitol(TipCapitol.CAP12f);
                ranDocDTO.setClazz(Capitol_12f.class.getName());
                randuriCapitolCentralizator = new ArrayList<RandCapitol_12fDTO>();
                Capitol_12fConversionHelper.populeazaFromSchemaType((Capitol_12f) capitolCentralizator, randuriCapitolCentralizator);
            } else if (capitolCentralizator instanceof Capitol_13_centralizator) {
                ranDocDTO.setCodCapitol(TipCapitol.CAP13cent.name());
                ranDocDTO.setTipCapitol(TipCapitol.CAP13cent);
                ranDocDTO.setClazz(Capitol_13_centralizator.class.getName());
                randuriCapitolCentralizator = new ArrayList<RandCapitol_13centDTO>();
                Capitol_13centConversionHelper.populeazaFromSchemaType((Capitol_13_centralizator) capitolCentralizator, randuriCapitolCentralizator);
            } else if (capitolCentralizator instanceof AnulareCapitol) {
                AnulareCentralizatorConversionHelper.populeazaFromSchemaType((AnulareCapitol) capitolCentralizator, ranDocDTO, ranDocDTO.getAnRaportare());
            } else {
                throw new IllegalArgumentException("Capitol centralizator nedefinit");
            }

            if (randuriCapitolCentralizator != null) {
                for (RandCapitolCentralizator rand : randuriCapitolCentralizator) {
                    rand.setCodSiruta(ranDocDTO.getSirutaUAT());
                    rand.setAn(ranDocDTO.getAnRaportare());
                }
                ranDocDTO.setRanduriCapitolCentralizator(randuriCapitolCentralizator);
            }

        } else if (gospodarieSauRaportare instanceof Gospodarie) {

            ro.uti.ran.core.xml.model.Gospodarie gospodarieElement = (ro.uti.ran.core.xml.model.Gospodarie) gospodarieSauRaportare;

            /*body*/
            if (gospodarieElement != null && gospodarieElement.getUnitateGospodarie() != null) {
                ranDocDTO.setIdentificatorGospodarie(gospodarieElement.getIdentificator().toUpperCase());
                ranDocDTO.getGospodarie().setIdentificator(gospodarieElement.getIdentificator().toUpperCase());
                Object unitateGospodarie = gospodarieElement.getUnitateGospodarie();
                /*Capitol_0_12*/
                if (unitateGospodarie instanceof Capitol_0_12) {
                    ranDocDTO.setClazz(Capitol_0_12.class.getName());
                    ranDocDTO.setTipCapitol(TipCapitol.CAP0_12);
                    ranDocDTO.setCodCapitol(((Capitol_0_12) unitateGospodarie).getCodCapitol());
                    Capitol_0_12ConversionHelper.populeazaFromSchemaType((Capitol_0_12) unitateGospodarie, ranDocDTO.getGospodarie());
                }
                /*Capitol_0_34*/
                if (unitateGospodarie instanceof Capitol_0_34) {
                    ranDocDTO.setClazz(Capitol_0_34.class.getName());
                    ranDocDTO.setTipCapitol(TipCapitol.CAP0_34);
                    ranDocDTO.setCodCapitol(((Capitol_0_34) unitateGospodarie).getCodCapitol());
                    Capitol_0_34ConversionHelper.populeazaFromSchemaType((Capitol_0_34) unitateGospodarie, ranDocDTO.getGospodarie());
                }
                /*Capitol_1*/
                if (unitateGospodarie instanceof Capitol_1) {
                    ranDocDTO.setClazz(Capitol_1.class.getName());
                    ranDocDTO.setTipCapitol(TipCapitol.CAP1);
                    ranDocDTO.setCodCapitol(((Capitol_1) unitateGospodarie).getCodCapitol());
                    Capitol_1ConversionHelper.populeazaFromSchemaType((Capitol_1) unitateGospodarie, ranDocDTO.getGospodarie());
                }
                /*Capitol_12*/
                if (unitateGospodarie instanceof Capitol_12) {
                    ranDocDTO.setClazz(Capitol_12.class.getName());
                    ranDocDTO.setTipCapitol(TipCapitol.CAP12);
                    ranDocDTO.setCodCapitol(((Capitol_12) unitateGospodarie).getCodCapitol());
                    Capitol_12ConversionHelper.populeazaFromSchemaType((Capitol_12) unitateGospodarie, ranDocDTO.getGospodarie());
                }
                /*Capitol_13*/
                if (unitateGospodarie instanceof Capitol_13) {
                    ranDocDTO.setClazz(Capitol_13.class.getName());
                    ranDocDTO.setTipCapitol(TipCapitol.CAP13);
                    ranDocDTO.setCodCapitol(((Capitol_13) unitateGospodarie).getCodCapitol());
                    Capitol_13ConversionHelper.populeazaFromSchemaType((Capitol_13) unitateGospodarie, ranDocDTO.getGospodarie());
                }
                /*Capitol_14*/
                if (unitateGospodarie instanceof Capitol_14) {
                    ranDocDTO.setClazz(Capitol_14.class.getName());
                    ranDocDTO.setTipCapitol(TipCapitol.CAP14);
                    ranDocDTO.setCodCapitol(((Capitol_14) unitateGospodarie).getCodCapitol());
                    Capitol_14ConversionHelper.populeazaFromSchemaType((Capitol_14) unitateGospodarie, ranDocDTO.getGospodarie());
                }
                /*Capitol_15a*/
                if (unitateGospodarie instanceof Capitol_15a) {
                    ranDocDTO.setClazz(Capitol_15a.class.getName());
                    ranDocDTO.setTipCapitol(TipCapitol.CAP15a);
                    ranDocDTO.setCodCapitol(((Capitol_15a) unitateGospodarie).getCodCapitol());
                    Capitol_15aConversionHelper.populeazaFromSchemaType((Capitol_15a) unitateGospodarie, ranDocDTO.getGospodarie());
                }
                /*Capitol_15b*/
                if (unitateGospodarie instanceof Capitol_15b) {
                    ranDocDTO.setClazz(Capitol_15b.class.getName());
                    ranDocDTO.setTipCapitol(TipCapitol.CAP15b);
                    ranDocDTO.setCodCapitol(((Capitol_15b) unitateGospodarie).getCodCapitol());
                    Capitol_15bConversionHelper.populeazaFromSchemaType((Capitol_15b) unitateGospodarie, ranDocDTO.getGospodarie());
                }
                /*Capitol_16*/
                if (unitateGospodarie instanceof Capitol_16) {
                    ranDocDTO.setClazz(Capitol_16.class.getName());
                    ranDocDTO.setTipCapitol(TipCapitol.CAP16);
                    ranDocDTO.setCodCapitol(((Capitol_16) unitateGospodarie).getCodCapitol());
                    Capitol_16ConversionHelper.populeazaFromSchemaType((Capitol_16) unitateGospodarie, ranDocDTO.getGospodarie());
                }
                /*Anulare*/
                if (unitateGospodarie instanceof AnulareCapitol) {
                    AnulareConversionHelper.populeazaFromSchemaType((AnulareCapitol) unitateGospodarie, ranDocDTO);
                }

                /*Dezactivare*/
                if (unitateGospodarie instanceof DezactivareGospodarie) {
                    ranDocDTO.setIsDezactivare(true);
                    ranDocDTO.setCodCapitol(((DezactivareGospodarie) unitateGospodarie).getCodCapitol());
                }

                /*Reactivare*/
                if (unitateGospodarie instanceof ReactivareGospodarie) {
                    ranDocDTO.setIsReactivare(true);
                    ranDocDTO.setCodCapitol(((ReactivareGospodarie) unitateGospodarie).getCodCapitol());
                }

                /*anRaportare*/
                if (unitateGospodarie instanceof AnRaportare) {
                    AnRaportare anRaportare = (AnRaportare) unitateGospodarie;
                    ranDocDTO.setAnRaportare(anRaportare.getAn());
                    Object capitolCuAnRaportare = anRaportare.getCapitolCuAnRaportare();
                    /*Capitol_2a*/
                    if (capitolCuAnRaportare instanceof Capitol_2a) {
                        ranDocDTO.setClazz(Capitol_2a.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP2a);
                        ranDocDTO.setCodCapitol(((Capitol_2a) capitolCuAnRaportare).getCodCapitol());
                        Capitol_2aConversionHelper.populeazaFromSchemaType((Capitol_2a) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_2b*/
                    if (capitolCuAnRaportare instanceof Capitol_2b) {
                        ranDocDTO.setClazz(Capitol_2b.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP2b);
                        ranDocDTO.setCodCapitol(((Capitol_2b) capitolCuAnRaportare).getCodCapitol());
                        Capitol_2bConversionHelper.populeazaFromSchemaType((Capitol_2b) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_3*/
                    if (capitolCuAnRaportare instanceof Capitol_3) {
                        ranDocDTO.setClazz(Capitol_3.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP3);
                        ranDocDTO.setCodCapitol(((Capitol_3) capitolCuAnRaportare).getCodCapitol());
                        Capitol_3ConversionHelper.populeazaFromSchemaType((Capitol_3) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_4a*/
                    if (capitolCuAnRaportare instanceof Capitol_4a) {
                        ranDocDTO.setClazz(Capitol_4a.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP4a);
                        ranDocDTO.setCodCapitol(((Capitol_4a) capitolCuAnRaportare).getCodCapitol());
                        Capitol_4aConversionHelper.populeazaFromSchemaType((Capitol_4a) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_4a1*/
                    if (capitolCuAnRaportare instanceof Capitol_4a1) {
                        ranDocDTO.setClazz(Capitol_4a1.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP4a1);
                        ranDocDTO.setCodCapitol(((Capitol_4a1) capitolCuAnRaportare).getCodCapitol());
                        Capitol_4a1ConversionHelper.populeazaFromSchemaType((Capitol_4a1) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_4b1*/
                    if (capitolCuAnRaportare instanceof Capitol_4b1) {
                        ranDocDTO.setClazz(Capitol_4b1.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP4b1);
                        ranDocDTO.setCodCapitol(((Capitol_4b1) capitolCuAnRaportare).getCodCapitol());
                        Capitol_4b1ConversionHelper.populeazaFromSchemaType((Capitol_4b1) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_4b2*/
                    if (capitolCuAnRaportare instanceof Capitol_4b2) {
                        ranDocDTO.setClazz(Capitol_4b2.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP4b2);
                        ranDocDTO.setCodCapitol(((Capitol_4b2) capitolCuAnRaportare).getCodCapitol());
                        Capitol_4b2ConversionHelper.populeazaFromSchemaType((Capitol_4b2) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_4c*/
                    if (capitolCuAnRaportare instanceof Capitol_4c) {
                        ranDocDTO.setClazz(Capitol_4c.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP4c);
                        ranDocDTO.setCodCapitol(((Capitol_4c) capitolCuAnRaportare).getCodCapitol());
                        Capitol_4cConversionHelper.populeazaFromSchemaType((Capitol_4c) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_5a*/
                    if (capitolCuAnRaportare instanceof Capitol_5a) {
                        ranDocDTO.setClazz(Capitol_5a.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP5a);
                        ranDocDTO.setCodCapitol(((Capitol_5a) capitolCuAnRaportare).getCodCapitol());
                        Capitol_5aConversionHelper.populeazaFromSchemaType((Capitol_5a) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_5b*/
                    if (capitolCuAnRaportare instanceof Capitol_5b) {
                        ranDocDTO.setClazz(Capitol_5b.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP5b);
                        ranDocDTO.setCodCapitol(((Capitol_5b) capitolCuAnRaportare).getCodCapitol());
                        Capitol_5bConversionHelper.populeazaFromSchemaType((Capitol_5b) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_5c*/
                    if (capitolCuAnRaportare instanceof Capitol_5c) {
                        ranDocDTO.setClazz(Capitol_5c.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP5c);
                        ranDocDTO.setCodCapitol(((Capitol_5c) capitolCuAnRaportare).getCodCapitol());
                        Capitol_5cConversionHelper.populeazaFromSchemaType((Capitol_5c) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_5d*/
                    if (capitolCuAnRaportare instanceof Capitol_5d) {
                        ranDocDTO.setClazz(Capitol_5d.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP5d);
                        ranDocDTO.setCodCapitol(((Capitol_5d) capitolCuAnRaportare).getCodCapitol());
                        Capitol_5dConversionHelper.populeazaFromSchemaType((Capitol_5d) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_6*/
                    if (capitolCuAnRaportare instanceof Capitol_6) {
                        ranDocDTO.setClazz(Capitol_6.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP6);
                        ranDocDTO.setCodCapitol(((Capitol_6) capitolCuAnRaportare).getCodCapitol());
                        Capitol_6ConversionHelper.populeazaFromSchemaType((Capitol_6) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }

                    /*Capitol_7*/
                    if (capitolCuAnRaportare instanceof Capitol_7) {
                        ranDocDTO.setClazz(Capitol_7.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP7);
                        ranDocDTO.setCodCapitol(((Capitol_7) capitolCuAnRaportare).getCodCapitol());
                        ranDocDTO.setSemestruRaportare(((Capitol_7) capitolCuAnRaportare).getSemestru());
                        Capitol_7ConversionHelper.populeazaFromSchemaType((Capitol_7) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }

                    /*Capitol_8*/
                    if (capitolCuAnRaportare instanceof Capitol_8) {
                        ranDocDTO.setClazz(Capitol_8.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP8);
                        ranDocDTO.setCodCapitol(((Capitol_8) capitolCuAnRaportare).getCodCapitol());
                        ranDocDTO.setSemestruRaportare(((Capitol_8) capitolCuAnRaportare).getSemestru());
                        Capitol_8ConversionHelper.populeazaFromSchemaType((Capitol_8) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_9*/
                    if (capitolCuAnRaportare instanceof Capitol_9) {
                        ranDocDTO.setClazz(Capitol_9.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP9);
                        ranDocDTO.setCodCapitol(((Capitol_9) capitolCuAnRaportare).getCodCapitol());
                        Capitol_9ConversionHelper.populeazaFromSchemaType((Capitol_9) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_10a*/
                    if (capitolCuAnRaportare instanceof Capitol_10a) {
                        ranDocDTO.setClazz(Capitol_10a.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP10a);
                        ranDocDTO.setCodCapitol(((Capitol_10a) capitolCuAnRaportare).getCodCapitol());
                        Capitol_10aConversionHelper.populeazaFromSchemaType((Capitol_10a) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_10b*/
                    if (capitolCuAnRaportare instanceof Capitol_10b) {
                        ranDocDTO.setClazz(Capitol_10b.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP10b);
                        ranDocDTO.setCodCapitol(((Capitol_10b) capitolCuAnRaportare).getCodCapitol());
                        Capitol_10bConversionHelper.populeazaFromSchemaType((Capitol_10b) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Capitol_11*/
                    if (capitolCuAnRaportare instanceof Capitol_11) {
                        ranDocDTO.setClazz(Capitol_11.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP11);
                        ranDocDTO.setCodCapitol(((Capitol_11) capitolCuAnRaportare).getCodCapitol());
                        Capitol_11ConversionHelper.populeazaFromSchemaType((Capitol_11) capitolCuAnRaportare, ranDocDTO.getGospodarie(), anRaportare.getAn());
                    }
                    /*Anulare*/
                    if (capitolCuAnRaportare instanceof AnulareCapitol) {
                        AnulareConversionHelper.populeazaFromSchemaType((AnulareCapitol) capitolCuAnRaportare, ranDocDTO, anRaportare.getAn());
                    }
                }
            }
        }
    }
}
