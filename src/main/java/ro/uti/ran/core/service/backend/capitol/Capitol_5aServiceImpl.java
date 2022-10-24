package ro.uti.ran.core.service.backend.capitol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.PomRazlet;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.capitol.Capitol_5a;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapPomRazlet;

/**
 * Numărul pomilor răzleți pe raza localității
 * Created by Dan on 28-Oct-15.
 */
@Service("capitol_5aService")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_5aServiceImpl extends AbstractCapitolCuTotaluriService {

    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }


    /**
     * - unicitate dupa an + CAP_POM_RAZLET.COD_RAND
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
        if (gospodarie.getPomRazlets() != null) {
            List<Integer> xmlCodValues = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
            for (PomRazlet pomRazlet : gospodarie.getPomRazlets()) {
                Integer codValue = pomRazlet.getCapPomRazlet().getCodRand();
                /*unicitate*/
                if (xmlCodValues.contains(codValue)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, CapPomRazlet.getLabel(), CapPomRazlet.getCodeColumn(), codValue);
                } else {
                    xmlCodValues.add(codValue);
                }
                /*POM_RAZLET - NOM_POM_RAZLET*/
                ro.uti.ran.core.model.registru.CapPomRazlet nomPomRazlet = nomSrv.getNomenclatorForStringParam(CapPomRazlet, codValue, dataRaportare, TipCapitol.CAP5a.name());
                if (nomPomRazlet == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, CapPomRazlet.getLabel(), CapPomRazlet.getCodeColumn(), codValue, dataRaportare);
                }
                 /*codNomenclator*/
                if (!pomRazlet.getCapPomRazlet().getCod().equals(nomPomRazlet.getCod())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_NOMENCLATOR_INVALID, pomRazlet.getCapPomRazlet().getCod(), CapPomRazlet.getLabel(), codValue);
                }
                pomRazlet.setCapPomRazlet(nomPomRazlet);
                /*fkNomJudet*/
                pomRazlet.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }

    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        Gospodarie oldGospodarieUNU = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<PomRazlet> oldPomRazletsDinAn = pomRazletRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP5a, oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getPomRazlets() != null) {
             /*stergere*/
            if (oldPomRazletsDinAn != null) {
                for (PomRazlet oldPomRazlet : oldPomRazletsDinAn) {
                    pomRazletRepository.delete(oldPomRazlet);
                }
            }
             /*valorile isFormula=0 care se modifica*/
            Set<Long> childrenIds = new HashSet<>();
            for (PomRazlet xmlPomRazlet : ranDocDTO.getGospodarie().getPomRazlets()) {
                /*adaugare*/
                xmlPomRazlet.setGospodarie(oldGospodarieUNU);
                pomRazletRepository.save(xmlPomRazlet);
                //
                if (RanConstants.NOM_IS_FORMULA_NU.equals(xmlPomRazlet.getCapPomRazlet().getIsFormula())) {
                    childrenIds.add(xmlPomRazlet.getCapPomRazlet().getId());
                }
            }
            if (childrenIds.isEmpty()) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_IS_FORMULA_0_NU_EXISTA);
            }

            regenerareTotaluri(oldGospodarie,
                    childrenIds,
                    ranDocDTO.getAnRaportare(),
                    "CapPomRazlet",
                    "capPomRazlets",
                    CapPomRazlet,
                    "PomRazlet",
                    new String[]{"nrPomRod", "nrPomTanar"},
                    "capPomRazlet",
                    "capPomRazlet",
                    new String[]{"nrPomiPeRod", "nrPomiTineri"},
                    new String[]{RanConstants.UM_NR_POMI, RanConstants.UM_NR_POMI},
                    CapPomRazlet.getLabel());
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
        List<PomRazlet> oldPomRazletsDinAn = pomRazletRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP5a, oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldPomRazletsDinAn != null) {
            for (PomRazlet oldPomRazlet : oldPomRazletsDinAn) {
                pomRazletRepository.delete(oldPomRazlet);
            }
        }

    }

    @Override
    protected Integer[] getDbTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        PomRazlet oldRandTotal = (PomRazlet) getOldRandTotal(an, semestru, idGospodarie, idNomParent);
        if (oldRandTotal != null) {
            return new Integer[]{oldRandTotal.getNrPomRod(), oldRandTotal.getNrPomTanar()};
        }
        return null;
    }

    @Override
    protected Object getOldRandTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        return pomRazletRepository.findByAnAndGospodarieAndNomPomRazlet(an, idGospodarie, idNomParent);
    }

    @Override
    protected Object[] getValoriCodRandAndDenumire(Nomenclator nomParent) {
        return new Object[]{((ro.uti.ran.core.model.registru.CapPomRazlet) nomParent).getCodRand(), ((ro.uti.ran.core.model.registru.CapPomRazlet) nomParent).getDenumire()};
    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*POM_RAZLET - din an*/
        gospodarieDTO.setPomRazlets(pomRazletRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(an, gospodarieDTO.getGospodarie().getIdGospodarie(), TipCapitol.CAP5a, gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_5a.class;
    }
}
