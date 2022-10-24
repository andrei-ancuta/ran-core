package ro.uti.ran.core.service.backend.capitol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.TipContract;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.utils.CnpValidator;
import ro.uti.ran.core.xml.model.capitol.Capitol_15b;

import java.util.ArrayList;
import java.util.List;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomFormaOrganizareRc;
import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomUat;

/**
 * Înregistrări privind contractele de concesiune
 * Created by Dan on 09-Nov-15.
 */
@Service("capitol_15bService")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_15bServiceImpl extends AbstractCapitolFara0XXService {


    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }


    /**
     * - unicitate dupa: CONTRACT.NR_CRT
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
        if (gospodarie.getContracts() != null && !gospodarie.getContracts().isEmpty()) {
            List<Integer> xmlRands = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate();
            NomUat nomUatHeader = nomSrv.getNomenclatorForStringParam(NomUat, ranDocDTO.getSirutaUAT(), dataRaportare);
            for (Contract contract : gospodarie.getContracts()) {
                Integer rand = contract.getNrCrt();
                /*unicitate la CONTRACT*/
                if (xmlRands.contains(rand)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, "contract_concesiune", "nrCrt", contract.getNrCrt());
                } else {
                    xmlRands.add(rand);
                }
                /*CONTRACT.FK_NOM_TIP_CONTRACT*/
                if (contract.getNomTipContract() != null) {
                    NomTipContract nomTipContract = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomTipContract, contract.getNomTipContract().getCod());
                    if (nomTipContract == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND, NomenclatorCodeType.NomTipContract.getLabel(), NomenclatorCodeType.NomTipContract.getCodeColumn(), contract.getNomTipContract().getCod());
                    }
                    contract.setNomTipContract(nomTipContract);
                }
                /*CONTRACT.FK_NOM_CATEGORIE_FOLOSINTA*/
                if (contract.getNomCategorieFolosinta() != null) {
                    NomCategorieFolosinta nomCategorieFolosinta = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomCategorieFolosinta, contract.getNomCategorieFolosinta().getCod(), dataRaportare);
                    if (nomCategorieFolosinta == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomenclatorCodeType.NomCategorieFolosinta.getLabel(), NomenclatorCodeType.NomCategorieFolosinta.getCodeColumn(), contract.getNomCategorieFolosinta().getCod(), dataRaportare);
                    }
                    contract.setNomCategorieFolosinta(nomCategorieFolosinta);
                }
                //PersoanaFizica
                if (contract.getPersoanaFizica() != null) {
                    //validare CNP
                    if (contract.getPersoanaFizica().getCnp() != null &&
                            !CnpValidator.isValid(contract.getPersoanaFizica().getCnp())) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.CNP_INVALID, contract.getPersoanaFizica().getCnp());
                    }
                    //reutilizare PersoanaFizica
                    PersoanaFizica dbPersoanaFizica = new PersoanaFizica();
                    dbPersoanaFizica.setIdPersoanaFizica(reutilizareService.reutilizarePersoanaFizica(contract.getPersoanaFizica(), nomUatHeader.getId()));
                    contract.setPersoanaFizica(dbPersoanaFizica);
                }
                if (contract.getPersoanaRc() != null) {
                    /*PERSOANA_RC - NOM_FORMA_ORGANIZARE_RC*/
                    ro.uti.ran.core.model.registru.NomFormaOrganizareRc nomFormaOrganizareRc = nomSrv.getNomenclatorForStringParam(NomFormaOrganizareRc, contract.getPersoanaRc().getNomFormaOrganizareRc().getCod());
                    if (nomFormaOrganizareRc == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND, NomFormaOrganizareRc.getLabel(), NomFormaOrganizareRc.getCodeColumn(), contract.getPersoanaRc().getNomFormaOrganizareRc().getCod());
                    }
                    contract.getPersoanaRc().setNomFormaOrganizareRc(nomFormaOrganizareRc);
                    /*PERSOANA_RC*/
                    PersoanaRc dbPersoanaRc = new PersoanaRc();
                    dbPersoanaRc.setIdPersoanaRc(reutilizareService.reutilizarePersoanaRc(contract.getPersoanaRc(), nomFormaOrganizareRc.getId(), nomUatHeader.getId()));
                    contract.setPersoanaRc(dbPersoanaRc);
                }
                /*fkNomJudet*/
                contract.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }

    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<Contract> oldContracts = contractRepository.findByTipContractAndGospodarieAndFkNomJudet(TipContract.CONCESIUNE.getCod(), oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getContracts() != null) {
            /*stergere*/
            if (oldContracts != null) {
                for (Contract oldContract : oldContracts) {
                    contractRepository.delete(oldContract);
                }
            }
            for (Contract xmlContract : ranDocDTO.getGospodarie().getContracts()) {
                /*adaugare*/
                xmlContract.setGospodarie(oldGospodarie);
                contractRepository.save(xmlContract);
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
        List<Contract> oldContracts = contractRepository.findByTipContractAndGospodarieAndFkNomJudet(TipContract.CONCESIUNE.getCod(), oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldContracts != null) {
            for (Contract oldContract : oldContracts) {
                contractRepository.delete(oldContract);
            }
        }

    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*CONTRACT - Concesiune*/
        gospodarieDTO.setContracts(contractRepository.findByTipContractAndGospodarieAndFkNomJudet(TipContract.CONCESIUNE.getCod(), gospodarieDTO.getGospodarie().getIdGospodarie(), gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_15b.class;
    }
}
