package ro.uti.ran.core.service.backend.capitol;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ro.uti.ran.core.service.backend.ProcesareDateRegistruServiceImpl;
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

    private static final Logger logger = LoggerFactory.getLogger(Capitol_2bServiceImpl.class);

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
        logger.info("S-a facut validarea oldGospodarie pentru registrul cu indexul: " + ranDocDTO.getCodXml());
        Gospodarie gospodarie = ranDocDTO.getGospodarie();
        if (gospodarie.getParcelaTerens() != null && !gospodarie.getParcelaTerens().isEmpty()) {
            List<Integer> xmlParcelaTerensCodRand = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
            NomUat nomUatHeader = nomSrv.getNomenclatorForStringParam(NomUat, ranDocDTO.getSirutaUAT(), dataRaportare);
            logger.info("S-a facut validarea nomUatHeader pentru registrul cu indexul: " + ranDocDTO.getCodXml());
            for (ParcelaTeren parcelaTeren : gospodarie.getParcelaTerens()) {
                GeometrieParcelaTeren geometrieParcelaTeren = parcelaTeren.getGeometrieParcelaTeren();
                if (geometrieParcelaTeren != null) {
                    geometrieService.validateGeometrie(geometrieParcelaTeren.getGeometrieGML());
                    geometrieService.validateGeometriePoligonUatLimit(ranDocDTO.getSirutaUAT(), geometrieParcelaTeren.getGeometrieGML());
                }
                logger.info("S-a facut validarea geometrie pentru registrul cu indexul: " + ranDocDTO.getCodXml());

                /*unicitate*/
                if (xmlParcelaTerensCodRand.contains(parcelaTeren.getCodRand())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, "identificare_teren", "COD_RAND", parcelaTeren.getCodRand());
                } else {
                    xmlParcelaTerensCodRand.add(parcelaTeren.getCodRand());
                }
                logger.info("S-a facut validarea cod rand unic la teren pentru registrul cu indexul: " + ranDocDTO.getCodXml());
                /*PARCELA_TEREN.FK_ACT_INSTRAINARE*/
                if (parcelaTeren.getActInstrainare() != null && parcelaTeren.getActInstrainare().getNomTipAct() != null) {
                    String codValue = parcelaTeren.getActInstrainare().getNomTipAct().getCod();
                    ro.uti.ran.core.model.registru.NomTipAct nomTipAct = nomSrv.getNomenclatorForStringParam(NomTipAct, codValue, dataRaportare);
                    if (nomTipAct == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, "TIP_ACT_INSTRAINARE", NomTipAct.getCodeColumn(), codValue, dataRaportare);
                    }
                    parcelaTeren.getActInstrainare().setNomTipAct(nomTipAct);
                    /*fkNomJudet*/
                    parcelaTeren.getActInstrainare().setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                }
                logger.info("S-a facut validarea act detinere pentru registrul cu indexul: " + ranDocDTO.getCodXml());
                /*PARCELA_TEREN.FK_CAP_CATEGORIE_FOLOSINTA*/
                if (parcelaTeren.getCapCategorieFolosinta() != null) {
                    Integer codValue = parcelaTeren.getCapCategorieFolosinta().getCodRand();
                    ro.uti.ran.core.model.registru.CapCategorieFolosinta capCategorieFolosinta = nomSrv.getNomenclatorForStringParam(CapCategorieFolosinta, codValue, dataRaportare, TipCapitol.CAP2a.name());
                    if (capCategorieFolosinta == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, "CATEGORIE_FOLOSINTA", CapCategorieFolosinta.getCodeColumn(), codValue, dataRaportare);
                    }
                    parcelaTeren.setCapCategorieFolosinta(capCategorieFolosinta);
                }
                logger.info("S-a facut validarea capCategorieFolosinta pentru registrul cu indexul: " + ranDocDTO.getCodXml());
                /*PARCELA_TEREN.FK_NOM_MODALITATE_DETINERE*/
                if (parcelaTeren.getNomModalitateDetinere() != null) {
                    String codValue = parcelaTeren.getNomModalitateDetinere().getCod();
                    ro.uti.ran.core.model.registru.NomModalitateDetinere nomModalitateDetinere = nomSrv.getNomenclatorForStringParam(NomModalitateDetinere, codValue, dataRaportare);
                    if (nomModalitateDetinere == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomModalitateDetinere.getLabel(), NomModalitateDetinere.getCodeColumn(), codValue, dataRaportare);
                    }
                    parcelaTeren.setNomModalitateDetinere(nomModalitateDetinere);
                }
                logger.info("S-a facut validarea nomModalitateDetinere pentru registrul cu indexul: " + ranDocDTO.getCodXml());
                /*PARCELA_LOCALIZARE*/
                if (parcelaTeren.getParcelaLocalizares() != null) {
                    List<String> xmlSubRand = new ArrayList<>();
                    for (ParcelaLocalizare parcelaLocalizare : parcelaTeren.getParcelaLocalizares()) {
                        /*unicitate la localizare*/
                        String subRand = parcelaLocalizare.getNomTipLocalizare().getCod();
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
                logger.info("S-a facut validarea parcelaLocalizare pentru registrul cu indexul: " + ranDocDTO.getCodXml());

                /*ACT_DETINERE*/
                if (parcelaTeren.getActDetineres() != null) {
                    for (ActDetinere actDetinere : parcelaTeren.getActDetineres()) {
                        /*ACT_DETINERE.FK_NOM_TIP_ACT_DETINERE*/
                        if (actDetinere.getAct() != null) {
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
                logger.info("S-a facut validarea actDetinere pentru registrul cu indexul: " + ranDocDTO.getCodXml());
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
                logger.info("S-a facut validarea getActInstrainare pentru registrul cu indexul: " + ranDocDTO.getCodXml());
                /*fkNomJudet*/
                parcelaTeren.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                logger.info("S-a setat setFkNomJudet pentru registrul cu indexul: " + ranDocDTO.getCodXml());

                //PersoanaFizica - Proprietar
                validareReutilizareProprietar(parcelaTeren, oldGospodarie, nomUatHeader);
                logger.info("S-a facut validarea pentru validareReutilizareProprietar pentru registrul cu indexul: " + ranDocDTO.getCodXml());
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
                if (xmlGeometrie != null) {
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
