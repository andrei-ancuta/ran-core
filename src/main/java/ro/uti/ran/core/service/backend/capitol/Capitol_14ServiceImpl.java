package ro.uti.ran.core.service.backend.capitol;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.utils.CnpValidator;
import ro.uti.ran.core.xml.model.capitol.Capitol_14;

import java.util.ArrayList;
import java.util.List;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.*;

/**
 * Înregistrări privind exercitarea dreptului de preempțiune
 * Created by Dan on 09-Nov-15.
 */
@Service("capitol_14Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_14ServiceImpl extends AbstractCapitolFara0XXService {


    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }


    /**
     * - unicitate dupa: PREEMPTIUNE.NR_OFERTA_VANZARE
     * - unicitate dupa PERSOANA_PREEMPTIUNE.CNP / PERSOANA_PREEMPTIUNE.CUI
     *
     * @param ranDocDTO datele ce trebuiesc validate (din mesajul XML)
     * @throws DateRegistruValidationException semnaleaza date invalide
     */
    public void valideazaDateRegistruLaEditare(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        /*gospodaria trebuie sa existe in baza*/
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        if (oldGospodarie == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.GOSPODARIE_NOT_FOUND, ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        }
        Gospodarie gospodarie = ranDocDTO.getGospodarie();
        if (gospodarie.getPreemptiunes() != null) {
            List<String> xmlRands = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate();
            NomUat nomUatHeader = nomSrv.getNomenclatorForStringParam(NomUat, ranDocDTO.getSirutaUAT(), dataRaportare);
            for (Preemptiune preemptiune : gospodarie.getPreemptiunes()) {
                String rand = preemptiune.getNrOfertaVanzare().toLowerCase();
                /*unicitate la PREEMPTIUNE*/
                if (xmlRands.contains(rand)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.PREEMPTIUNE_DUBLICATE, preemptiune.getNrOfertaVanzare());
                } else {
                    xmlRands.add(rand);
                }
                /*persoanaPreemptiunes*/
                if (preemptiune.getPersoanaPreemptiunes() != null) {
                    for (PersoanaPreemptiune persoanaPreemptiune : preemptiune.getPersoanaPreemptiunes()) {
                        /*PERSOANA_PREEMPTIUNE.FK_NOM_TIP_REL_PREEMPTIUNE*/
                        NomTipRelPreemptiune nomTipRelPreemptiune = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomTipRelPreemptiune, persoanaPreemptiune.getNomTipRelPreemptiune().getCod());
                        if (nomTipRelPreemptiune == null) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND, NomenclatorCodeType.NomTipRelPreemptiune.getLabel(), NomenclatorCodeType.NomTipRelPreemptiune.getCodeColumn(), persoanaPreemptiune.getNomTipRelPreemptiune().getCod());
                        }
                        persoanaPreemptiune.setNomTipRelPreemptiune(nomTipRelPreemptiune);
                        //PersoanaFizica
                        if (persoanaPreemptiune.getPersoanaFizica() != null) {
                            //validare CNP
                            if (persoanaPreemptiune.getPersoanaFizica().getCnp() != null &&
                                    !CnpValidator.isValid(persoanaPreemptiune.getPersoanaFizica().getCnp())) {
                                throw new DateRegistruValidationException(DateRegistruValidationCodes.CNP_INVALID, persoanaPreemptiune.getPersoanaFizica().getCnp());
                            }
                            //reutilizare PersoanaFizica
                            PersoanaFizica dbPersoanaFizica = new PersoanaFizica();
                            dbPersoanaFizica.setIdPersoanaFizica(reutilizareService.reutilizarePersoanaFizica(persoanaPreemptiune.getPersoanaFizica(), nomUatHeader.getId()));
                            persoanaPreemptiune.setPersoanaFizica(dbPersoanaFizica);
                        }
                        if (persoanaPreemptiune.getPersoanaRc() != null) {
                            /*PERSOANA_RC - NOM_FORMA_ORGANIZARE_RC*/
                            ro.uti.ran.core.model.registru.NomFormaOrganizareRc nomFormaOrganizareRc = nomSrv.getNomenclatorForStringParam(NomFormaOrganizareRc, persoanaPreemptiune.getPersoanaRc().getNomFormaOrganizareRc().getCod());
                            if (nomFormaOrganizareRc == null) {
                                throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND, NomFormaOrganizareRc.getLabel(), NomFormaOrganizareRc.getCodeColumn(), persoanaPreemptiune.getPersoanaRc().getNomFormaOrganizareRc().getCod());
                            }
                            persoanaPreemptiune.getPersoanaRc().setNomFormaOrganizareRc(nomFormaOrganizareRc);
                            /*PERSOANA_RC*/
                            PersoanaRc dbPersoanaRc = new PersoanaRc();
                            dbPersoanaRc.setIdPersoanaRc(reutilizareService.reutilizarePersoanaRc(persoanaPreemptiune.getPersoanaRc(), nomFormaOrganizareRc.getId(), nomUatHeader.getId()));
                            persoanaPreemptiune.setPersoanaRc(dbPersoanaRc);
                        }
                    }
                }
                /*fkNomJudet*/
                preemptiune.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                /*acte - NOM_TIP_ACT*/
                if (preemptiune.getActAdevVanzareLib() != null) {
                    if (StringUtils.isEmpty(preemptiune.getActAdevVanzareLib().getNumarAct()) || preemptiune.getActAdevVanzareLib().getDataAct() == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.ADEV_VANZARE_LIBERA);
                    }
                    String codValue = preemptiune.getActAdevVanzareLib().getNomTipAct().getCod();
                    ro.uti.ran.core.model.registru.NomTipAct nomTipAct = nomSrv.getNomenclatorForStringParam(NomTipAct, codValue, dataRaportare);
                    if (nomTipAct == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, "TIP_ACT_ADEVERINTA_VANZARE", NomTipAct.getCodeColumn(), codValue, dataRaportare);
                    }
                    preemptiune.getActAdevVanzareLib().setNomTipAct(nomTipAct);
                    /*fkNomJudet*/
                    preemptiune.getActAdevVanzareLib().setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                }
                if (preemptiune.getActAvizMadrDadr() != null) {
                    String codValue = preemptiune.getActAvizMadrDadr().getNomTipAct().getCod();
                    ro.uti.ran.core.model.registru.NomTipAct nomTipAct = nomSrv.getNomenclatorForStringParam(NomTipAct, codValue, dataRaportare);
                    if (nomTipAct == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, "TIP_ACT_AVIZ_MADR_DADR", NomTipAct.getCodeColumn(), codValue, dataRaportare);
                    }
                    preemptiune.getActAvizMadrDadr().setNomTipAct(nomTipAct);
                    /*fkNomJudet*/
                    preemptiune.getActAvizMadrDadr().setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                }
            }
        }
    }


    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<Preemptiune> oldPreemptiunes = preemptiuneRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getPreemptiunes() != null) {
             /*stergere*/
            if (oldPreemptiunes != null) {
                for (Preemptiune oldPreemptiune : oldPreemptiunes) {
                    preemptiuneRepository.delete(oldPreemptiune);
                }
            }
            for (Preemptiune xmlPreemptiune : ranDocDTO.getGospodarie().getPreemptiunes()) {
                /*acte*/
                if (xmlPreemptiune.getActAdevVanzareLib() != null) {
                    actRepository.save(xmlPreemptiune.getActAdevVanzareLib());
                }
                if (xmlPreemptiune.getActAvizMadrDadr() != null) {
                    actRepository.save(xmlPreemptiune.getActAvizMadrDadr());
                }
                /*adaugare*/
                xmlPreemptiune.setGospodarie(oldGospodarie);
                preemptiuneRepository.save(xmlPreemptiune);
            }
        }
    }


    /**
     * Identific info din XML cu cele din baza dupa anumite chei simple sau compuse (cnp, an, semestru, cod nomenclator, codRand, etc).
     * Info din XML care apare si in DB - delete
     *
     * @param ranDocDTO info trimse prin xml
     */
    public void anuleazaDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<Preemptiune> oldPreemptiunes = preemptiuneRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldPreemptiunes != null) {
            for (Preemptiune oldPreemptiune : oldPreemptiunes) {
                preemptiuneRepository.delete(oldPreemptiune);
            }
        }

    }


    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*PREEMPTIUNE*/
        gospodarieDTO.setPreemptiunes(preemptiuneRepository.findByGospodarieAndFkNomJudet(gospodarieDTO.getGospodarie().getIdGospodarie(), gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_14.class;
    }
}
