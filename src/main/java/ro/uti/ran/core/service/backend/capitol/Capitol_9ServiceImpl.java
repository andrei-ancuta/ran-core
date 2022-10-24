package ro.uti.ran.core.service.backend.capitol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.SistemTehnic;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.capitol.Capitol_9;

import java.util.ArrayList;
import java.util.List;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapSistemTehnic;

/**
 * Utilaje, instalații pentru agricultura, mijloace de transport cu tracțiune animală si mecanică existente la începutul anului
 * Created by Dan on 30-Oct-15.
 */
@Service("capitol_9Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_9ServiceImpl extends AbstractCapitolFara0XXService {

    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }


    /**
     * - unicitate dupa an + NOM_SISTEM_TEHNIC.COD_RAND
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
        if (gospodarie.getSistemTehnics() != null) {
            List<Integer> xmlCodValues = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
            for (SistemTehnic sistemTehnic : gospodarie.getSistemTehnics()) {
                Integer codValue = sistemTehnic.getCapSistemTehnic().getCodRand();
                /*unicitate*/
                if (xmlCodValues.contains(codValue)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, CapSistemTehnic.getLabel(), CapSistemTehnic.getCodeColumn(), codValue);
                } else {
                    xmlCodValues.add(codValue);
                }
                /*SISTEM_TEHNIC - NOM_SISTEM_TEHNIC*/
                ro.uti.ran.core.model.registru.CapSistemTehnic capSistemTehnic = nomSrv.getNomenclatorForStringParam(CapSistemTehnic, codValue, dataRaportare, TipCapitol.CAP9.name());
                if (capSistemTehnic == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, CapSistemTehnic.getLabel(), CapSistemTehnic.getCodeColumn(), codValue, dataRaportare);
                }
                /*codNomenclator*/
                if (!sistemTehnic.getCapSistemTehnic().getCod().equals(capSistemTehnic.getCod())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_NOMENCLATOR_INVALID, sistemTehnic.getCapSistemTehnic().getCod(), CapSistemTehnic.getLabel(), codValue);
                }
                sistemTehnic.setCapSistemTehnic(capSistemTehnic);
                /*fkNomJudet*/
                sistemTehnic.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }

    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<SistemTehnic> oldSistemTehnicsDinAn = sistemTehnicRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP9, oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getSistemTehnics() != null) {
            /*stergere*/
            if (oldSistemTehnicsDinAn != null) {
                for (SistemTehnic oldSistemTehnic : oldSistemTehnicsDinAn) {
                    sistemTehnicRepository.delete(oldSistemTehnic);
                }
            }
            for (SistemTehnic xmlSistemTehnic : ranDocDTO.getGospodarie().getSistemTehnics()) {
                /*adaugare*/
                xmlSistemTehnic.setGospodarie(oldGospodarie);
                sistemTehnicRepository.save(xmlSistemTehnic);
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
        List<SistemTehnic> oldSistemTehnicsDinAn = sistemTehnicRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP9, oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldSistemTehnicsDinAn != null) {
            for (SistemTehnic oldSistemTehnic : oldSistemTehnicsDinAn) {
                sistemTehnicRepository.delete(oldSistemTehnic);
            }
        }

    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*SISTEM_TEHNIC - din an*/
        gospodarieDTO.setSistemTehnics(sistemTehnicRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(an, gospodarieDTO.getGospodarie().getIdGospodarie(), TipCapitol.CAP9, gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_9.class;
    }
}
