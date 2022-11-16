package ro.uti.ran.core.service.backend.capitol;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.service.backend.GeometrieService;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.utils.CnpValidator;
import ro.uti.ran.core.xml.model.capitol.Capitol_2b;

import java.util.ArrayList;
import java.util.List;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.*;

/**
 * Created by Dan on 06-Nov-15.
 */
@Service("capitol_2bService")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_2bServiceImpl extends AbstractCapitolFara0XXService {


    @Autowired
    private GeometrieService geometrieService;

    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }


    /**
     * - unicitate dupa an + PARCELA_TEREN.COD_RAND
     *
     * @param ranDocDTO datele ce trebuiesc validate (din mesajul XML)
     * @throws DateRegistruValidationException
     */
    public void valideazaDateRegistruLaEditare(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        /*gospodaria trebuie sa existe in baza*/
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        if (oldGospodarie == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.GOSPODARIE_NOT_FOUND, ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        }
        Gospodarie gospodarie = ranDocDTO.getGospodarie();
        if (gospodarie.getParcelaTerens() != null && !gospodarie.getParcelaTerens().isEmpty()) {
            List<Integer> xmlParcelaTerensCodRand = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
            NomUat nomUatHeader = nomSrv.getNomenclatorForStringParam(NomUat, ranDocDTO.getSirutaUAT(), dataRaportare);
            for (ParcelaTeren parcelaTeren : gospodarie.getParcelaTerens()) {
                /* Validari pentru campurile care au constrangere in baza de date, dar nu si in schema xml si genereaza erori ORA-12899*/
                if (null != parcelaTeren.getDenumire() && parcelaTeren.getDenumire().length() > 100) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.CONSTRANGERE_LUNGIME_CAMP_DEPASITA, "denumire", parcelaTeren.getDenumire().length(), 100);
                }
                if (null != parcelaTeren.getNrBlocFizic() && parcelaTeren.getNrBlocFizic().length() > 32) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.CONSTRANGERE_LUNGIME_CAMP_DEPASITA, "nrBlocFizic", parcelaTeren.getNrBlocFizic().length(), 32);
                }
                if (null != parcelaTeren.getMentiune() && parcelaTeren.getMentiune().length() > 500) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.CONSTRANGERE_LUNGIME_CAMP_DEPASITA, "mentiuni", parcelaTeren.getMentiune().length(), 500);
                }


                GeometrieParcelaTeren geometrieParcelaTeren = parcelaTeren.getGeometrieParcelaTeren();
                if (geometrieParcelaTeren != null && (geometrieParcelaTeren.getGeometrieGML().contains("Polygon") || geometrieParcelaTeren.getGeometrieGML().contains("Point"))) {
                    geometrieService.validateGeometrie(geometrieParcelaTeren.getGeometrieGML());
                    geometrieService.validateGeometriePoligonUatLimit(ranDocDTO.getSirutaUAT(), geometrieParcelaTeren.getGeometrieGML());
                }

                //Validari pentru lungimea codului de rand
                if(parcelaTeren.getCodRand().toString().length() > 2) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_RAND_ERONAT, parcelaTeren.getCodRand().toString(), 2);
                }
                /*unicitate*/
                if (xmlParcelaTerensCodRand.contains(parcelaTeren.getCodRand())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, "identificare_teren", "COD_RAND", parcelaTeren.getCodRand());
                } else {
                    xmlParcelaTerensCodRand.add(parcelaTeren.getCodRand());
                }
                /*PARCELA_TEREN.FK_ACT_INSTRAINARE*/
                if (parcelaTeren.getActInstrainare() != null && parcelaTeren.getActInstrainare().getNomTipAct() != null) {
                    /* Validari pentru campurile care au constrangere in baza de date, dar nu si in schema xml si genereaza erori ORA-12899*/
                    if (parcelaTeren.getActInstrainare().getNumarAct().length() > 20) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.CONSTRANGERE_LUNGIME_CAMP_DEPASITA, "nrAct", parcelaTeren.getActInstrainare().getNumarAct().length(), 20);
                    }

                    String codValue = parcelaTeren.getActInstrainare().getNomTipAct().getCod();
                    ro.uti.ran.core.model.registru.NomTipAct nomTipAct = nomSrv.getNomenclatorForStringParam(NomTipAct, codValue, dataRaportare);
                    if (nomTipAct == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, "TIP_ACT_INSTRAINARE", NomTipAct.getCodeColumn(), codValue, dataRaportare);
                    }
                    parcelaTeren.getActInstrainare().setNomTipAct(nomTipAct);
                    /*fkNomJudet*/
                    parcelaTeren.getActInstrainare().setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                }
                /*PARCELA_TEREN.FK_CAP_CATEGORIE_FOLOSINTA*/
                if (parcelaTeren.getCapCategorieFolosinta() != null) {
                    Integer codValue = parcelaTeren.getCapCategorieFolosinta().getCodRand();
                    if (codValue.toString().length() > 2) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_RAND_ERONAT, codValue.toString(), 2);
                    }
                    ro.uti.ran.core.model.registru.CapCategorieFolosinta capCategorieFolosinta = nomSrv.getNomenclatorForStringParam(CapCategorieFolosinta, codValue, dataRaportare, TipCapitol.CAP2a.name());
                    if (capCategorieFolosinta == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, "CATEGORIE_FOLOSINTA", CapCategorieFolosinta.getCodeColumn(), codValue, dataRaportare);
                    }
                    parcelaTeren.setCapCategorieFolosinta(capCategorieFolosinta);
                }
                /*PARCELA_TEREN.FK_NOM_MODALITATE_DETINERE*/
                if (parcelaTeren.getNomModalitateDetinere() != null) {
                    String codValue = parcelaTeren.getNomModalitateDetinere().getCod();
                    ro.uti.ran.core.model.registru.NomModalitateDetinere nomModalitateDetinere = nomSrv.getNomenclatorForStringParam(NomModalitateDetinere, codValue, dataRaportare);
                    if (nomModalitateDetinere == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomModalitateDetinere.getLabel(), NomModalitateDetinere.getCodeColumn(), codValue, dataRaportare);
                    }
                    parcelaTeren.setNomModalitateDetinere(nomModalitateDetinere);
                }
                /*PARCELA_LOCALIZARE*/
                if (parcelaTeren.getParcelaLocalizares() != null) {
                    List<String> xmlSubRand = new ArrayList<>();
                    for (ParcelaLocalizare parcelaLocalizare : parcelaTeren.getParcelaLocalizares()) {
                        /* Validari pentru campurile care au constrangere in baza de date, dar nu si in schema xml si genereaza erori ORA-12899*/
                        if ( null != parcelaLocalizare.getValoare() && parcelaLocalizare.getValoare().length() > 100) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.CONSTRANGERE_LUNGIME_CAMP_DEPASITA, "valoare", parcelaLocalizare.getValoare().length(), 100);
                        }

                        /*unicitate la localizare*/
                        String subRand = parcelaLocalizare.getNomTipLocalizare().getCod();
                        if (subRand.length() > 1) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_RAND_ERONAT, subRand, 1);
                        }
                        if (xmlSubRand.contains(subRand)) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.PARCELA_TEREN_LOCALIZARE_DUBLICATE,
                                    parcelaTeren.getCodRand(),
                                    subRand);
                        } else {
                            xmlSubRand.add(subRand);
                        }
                        /*PARCELA_LOCALIZARE.FK_NOM_TIP_LOCALIZARE*/
                        if (parcelaLocalizare.getNomTipLocalizare() != null) {
                            ro.uti.ran.core.model.registru.NomTipLocalizare nomTipLocalizare = nomSrv.getNomenclatorForStringParam(NomTipLocalizare, subRand, dataRaportare);
                            if (nomTipLocalizare == null) {
                                throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomTipLocalizare.getLabel(), NomTipLocalizare.getCodeColumn(), subRand, dataRaportare);
                            }
                            parcelaLocalizare.setNomTipLocalizare(nomTipLocalizare);
                        }
                    }
                }
                /*ACT_DETINERE*/
                if (parcelaTeren.getActDetineres() != null) {
                    for (ActDetinere actDetinere : parcelaTeren.getActDetineres()) {
                        /*ACT_DETINERE.FK_NOM_TIP_ACT_DETINERE*/
                        if (actDetinere.getAct() != null) {
                            /* Validari pentru campurile care au constrangere in baza de date, dar nu si in schema xml si genereaza erori ORA-12899*/
                            if (null != actDetinere.getAct().getNumarAct() && actDetinere.getAct().getNumarAct().length() > 20) {
                                throw new DateRegistruValidationException(DateRegistruValidationCodes.CONSTRANGERE_LUNGIME_CAMP_DEPASITA, "nrAct", parcelaTeren.getActInstrainare().getNumarAct().length(), 20);
                            }
                            String codValue = actDetinere.getAct().getNomTipAct().getCod();
                            ro.uti.ran.core.model.registru.NomTipAct nomTipAct = nomSrv.getNomenclatorForStringParam(NomTipAct, codValue, dataRaportare);
                            if (nomTipAct == null) {
                                throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, "TIP_ACT_DETINERE", NomTipAct.getCodeColumn(), codValue, dataRaportare);
                            }
                            actDetinere.getAct().setNomTipAct(nomTipAct);
                            /*fkNomJudet*/
                            actDetinere.getAct().setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                            actDetinere.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                        }
                    }
                }

                for (Integer codRand : xmlParcelaTerensCodRand) {
                    if (codRand.toString().length() > 2) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_RAND_ERONAT, codRand.toString());
                    }
                }
                /*ACT_INSTRAINARE*/
                if (parcelaTeren.getActInstrainare() != null) {
                    String codValue = parcelaTeren.getActInstrainare().getNomTipAct().getCod();
                    ro.uti.ran.core.model.registru.NomTipAct nomTipAct = nomSrv.getNomenclatorForStringParam(NomTipAct, codValue, dataRaportare);
                    if (nomTipAct == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, "TIP_ACT_DETINERE", NomTipAct.getCodeColumn(), codValue, dataRaportare);
                    }
                    parcelaTeren.getActInstrainare().setNomTipAct(nomTipAct);
                    /*fkNomJudet*/
                    parcelaTeren.getActInstrainare().setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                }
                /*fkNomJudet*/
                parcelaTeren.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                //PersoanaFizica - Proprietar
                validareReutilizareProprietar(parcelaTeren, oldGospodarie, nomUatHeader);
            }
        }
    }

    /**
     * @param parcelaTeren
     * @param oldGospodarie
     * @param nomUatHeader
     * @throws DateRegistruValidationException
     */
    private void validareReutilizareProprietar(ParcelaTeren parcelaTeren, Gospodarie oldGospodarie, NomUat nomUatHeader) throws DateRegistruValidationException {
        List<DetinatorPf> lstPf = detinatorPfRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        if (lstPf != null && !lstPf.isEmpty() && (parcelaTeren.getProprietarParcelas() == null || parcelaTeren.getProprietarParcelas().isEmpty())) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.PROPRIETAR_PARCELA_OBLIGATORIU, parcelaTeren.getDenumire(), parcelaTeren.getCodRand());
        }
        if (parcelaTeren.getProprietarParcelas() != null && !parcelaTeren.getProprietarParcelas().isEmpty()) {
            for (ProprietarParcela proprietarParcela : parcelaTeren.getProprietarParcelas()) {
                String identificator = (proprietarParcela.getPersoanaFizica().getCnp() != null ? proprietarParcela.getPersoanaFizica().getCnp() : proprietarParcela.getPersoanaFizica().getNif());
                boolean isNotMembru = true;
                if (lstPf != null && !lstPf.isEmpty()) {
                    List<MembruPf> lstMembruPfs = lstPf.get(0).getMembruPfs();
                    if (lstMembruPfs != null) {
                        for (MembruPf membruPf : lstMembruPfs) {
                            if ((identificator.equals(membruPf.getPersoanaFizica().getCnp()) || identificator.equals(membruPf.getPersoanaFizica().getNif()))
                                    && proprietarParcela.getPersoanaFizica().getNume().equalsIgnoreCase(membruPf.getPersoanaFizica().getNume())
                                    && proprietarParcela.getPersoanaFizica().getPrenume().equalsIgnoreCase(membruPf.getPersoanaFizica().getPrenume())
                                    && proprietarParcela.getPersoanaFizica().getInitialaTata().equalsIgnoreCase(membruPf.getPersoanaFizica().getInitialaTata())) {
                                isNotMembru = false;
                                break;
                            }
                        }
                    }

                }
                if (isNotMembru) {
                    throw new DateRegistruValidationException(
                            DateRegistruValidationCodes.PROPRIETAR_PARCELA_NU_ESTE_MEMBRU_GOSPODARIE,
                            identificator,proprietarParcela.getPersoanaFizica().getNume(),proprietarParcela.getPersoanaFizica().getInitialaTata(),proprietarParcela.getPersoanaFizica().getPrenume(),
                            parcelaTeren.getDenumire(), parcelaTeren.getCodRand());
                }
            }
        }
        if (parcelaTeren.getProprietarParcelas() != null) {
            for (ProprietarParcela proprietarParcela : parcelaTeren.getProprietarParcelas()) {
                //validare CNP
                if (proprietarParcela.getPersoanaFizica().getCnp() != null &&
                        !CnpValidator.isValid(proprietarParcela.getPersoanaFizica().getCnp())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.CNP_INVALID, proprietarParcela.getPersoanaFizica().getCnp());
                }
                //reutilizare PersoanaFizica
                PersoanaFizica dbPersoanaFizica = new PersoanaFizica();
                dbPersoanaFizica.setIdPersoanaFizica(reutilizareService.reutilizarePersoanaFizica(proprietarParcela.getPersoanaFizica(), nomUatHeader.getId()));
                proprietarParcela.setPersoanaFizica(dbPersoanaFizica);
                /*fkNomJudet*/
                proprietarParcela.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }

    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<ParcelaTeren> oldParcelaTerensDinAn = parcelaTerenRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP2a, oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getParcelaTerens() != null) {
            /*stergere*/
            if (oldParcelaTerensDinAn != null) {
                for (ParcelaTeren oldParcelaTeren : oldParcelaTerensDinAn) {
                    parcelaTerenRepository.delete(oldParcelaTeren);
                }
            }
            for (ParcelaTeren xmlParcelaTeren : ranDocDTO.getGospodarie().getParcelaTerens()) {
                /*adaugare*/
                GeometrieParcelaTeren xmlGeometrie = xmlParcelaTeren.getGeometrieParcelaTeren();
                //act
                if (xmlParcelaTeren.getActDetineres() != null) {
                    for (ActDetinere actDetinere : xmlParcelaTeren.getActDetineres()) {
                        actRepository.save(actDetinere.getAct());
                    }
                }
                if (xmlParcelaTeren.getActInstrainare() != null) {
                    actRepository.save(xmlParcelaTeren.getActInstrainare());
                }
                //
                xmlParcelaTeren.setGospodarie(oldGospodarie);
                xmlParcelaTeren.setGeometrieParcelaTeren(null);
                parcelaTerenRepository.save(xmlParcelaTeren);
                //geometrie
                if (xmlGeometrie != null && (xmlGeometrie.getGeometrieGML().contains("Polygon") || xmlGeometrie.getGeometrieGML().contains("Point"))) {
                    geometrieService.insertParcelaTerenGIS(xmlParcelaTeren.getIdParcelaTeren(), xmlGeometrie.getGeometrieGML(), oldGospodarie.getNomUat().getNomJudet().getId(), xmlGeometrie.getIsFolosinta());
                }
            }
        }
    }

    /**
     * - unicitate dupa an + PARCELA_TEREN.COD_RAND
     * Identific info din XML cu cele din baza dupa anumite chei simple sau compuse (cnp, an, semestru, cod nomenclator, codRand, etc).
     * Info din XML care apare si in DB - delete
     *
     * @param ranDocDTO info trimse prin xml
     */
    public void anuleazaDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<ParcelaTeren> oldParcelaTerensDinAn = parcelaTerenRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP2a, oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldParcelaTerensDinAn != null) {
            for (ParcelaTeren oldParcelaTeren : oldParcelaTerensDinAn) {
                parcelaTerenRepository.delete(oldParcelaTeren);
            }
        }

    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*PARCELA_TEREN - din an*/
        gospodarieDTO.setParcelaTerens(parcelaTerenRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(an, gospodarieDTO.getGospodarie().getIdGospodarie(), TipCapitol.CAP2a, gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
        /*Geometrie*/
        if (gospodarieDTO.getParcelaTerens() != null) {
            for (ParcelaTeren parcelaTeren : gospodarieDTO.getParcelaTerens()) {
                if (parcelaTeren.getGeometrieParcelaTeren() != null) {
                    String geometrieGML = geometrieService.getParcelaTerenGIS(parcelaTeren.getGeometrieParcelaTeren().getIdGeometrieParcelaTeren());
                    if (StringUtils.isNotEmpty(geometrieGML)) {
                        parcelaTeren.getGeometrieParcelaTeren().setGeometrieGML(geometrieGML);
                    }
                }
            }
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_2b.class;
    }
}
