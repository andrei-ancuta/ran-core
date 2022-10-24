package ro.uti.ran.core.service.backend.capitol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.MentiuneSpeciala;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.xml.model.capitol.Capitol_16;

import java.util.List;

/**
 * Mențiuni speciale
 * Created by Dan on 02-Nov-15.
 */
@Service("capitol_16Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_16ServiceImpl extends AbstractCapitolFara0XXService {

    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }


    public void valideazaDateRegistruLaEditare(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        /*gospodaria trebuie sa existe in baza*/
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        if (oldGospodarie == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.GOSPODARIE_NOT_FOUND, ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        }
    }


    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        /*sterg ce este vechi*/
        List<MentiuneSpeciala> oldMentiuneSpecialas = mentiuneSpecialaRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        if (oldMentiuneSpecialas != null) {
            for (MentiuneSpeciala mentiuneSpeciala : oldMentiuneSpecialas) {
                mentiuneSpecialaRepository.delete(mentiuneSpeciala);
            }
        }
        /*adaug ce este nou*/
        if (ranDocDTO.getGospodarie().getMentiuneSpecialas() != null) {
            for (MentiuneSpeciala mentiuneSpeciala : ranDocDTO.getGospodarie().getMentiuneSpecialas()) {
                mentiuneSpeciala.setGospodarie(oldGospodarie);
                /*fkNomJudet*/
                mentiuneSpeciala.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                //
                mentiuneSpecialaRepository.save(mentiuneSpeciala);
            }
        }
    }

    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void anuleazaDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<MentiuneSpeciala> oldMentiuneSpecialas = mentiuneSpecialaRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        if (oldMentiuneSpecialas != null) {
            for (MentiuneSpeciala mentiuneSpeciala : oldMentiuneSpecialas) {
                mentiuneSpecialaRepository.delete(mentiuneSpeciala);
            }
        }
    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
       /*MENTIUNE_SPECIALA*/
        gospodarieDTO.setMentiuneSpecialas(mentiuneSpecialaRepository.findByGospodarieAndFkNomJudet(gospodarieDTO.getGospodarie().getIdGospodarie(), gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_16.class;
    }
}
