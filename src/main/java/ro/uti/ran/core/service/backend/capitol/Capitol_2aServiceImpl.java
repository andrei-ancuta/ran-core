package ro.uti.ran.core.service.backend.capitol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.SuprafataCategorie;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.capitol.Capitol_2a;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapCategorieFolosinta;

/**
 * Terenuri aflate în proprietate
 * Created by Dan on 26-Oct-15.
 */
@Service("capitol_2aService")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_2aServiceImpl extends AbstractCapitolCuTotaluriService {

    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }


    /**
     * - unicitate dupa an + CAP_CATEGORIE_FOLOSINTA.COD_RAND
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
        if (gospodarie.getSuprafataCategories() != null && !gospodarie.getSuprafataCategories().isEmpty()) {
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
            List<Integer> xmlNomCategoriesCodValue = new ArrayList<Integer>();
            for (SuprafataCategorie suprafataCategorie : gospodarie.getSuprafataCategories()) {
                /*unicitate*/
                Integer codValue = suprafataCategorie.getCapCategorieFolosinta().getCodRand();
                if (xmlNomCategoriesCodValue.contains(codValue)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, CapCategorieFolosinta.getLabel(), CapCategorieFolosinta.getCodeColumn(), codValue);
                } else {
                    xmlNomCategoriesCodValue.add(codValue);
                }
                /*SUPRAFATA_CATEGORIE - CAP_CATEGORIE_FOLOSINTA*/
                ro.uti.ran.core.model.registru.CapCategorieFolosinta capCategorieFolosinta = nomSrv.getNomenclatorForStringParam(CapCategorieFolosinta, codValue, dataRaportare, TipCapitol.CAP2a.name());
                if (capCategorieFolosinta == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, CapCategorieFolosinta.getLabel(), CapCategorieFolosinta.getCodeColumn(), codValue, dataRaportare);
                }
                /*codNomenclator*/
                if (!suprafataCategorie.getCapCategorieFolosinta().getCod().equals(capCategorieFolosinta.getCod())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_NOMENCLATOR_INVALID, suprafataCategorie.getCapCategorieFolosinta().getCod(), CapCategorieFolosinta.getLabel(), codValue);
                }
                suprafataCategorie.setCapCategorieFolosinta(capCategorieFolosinta);
                /*fkNomJudet*/
                suprafataCategorie.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                /*total Ari/HA sa fie egal cu altelocARI/HA+localARI/HA*/
                validareTotalPeRand(suprafataCategorie);
            }
        }
    }


    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<SuprafataCategorie> oldSuprafataCategoriesDinAn = suprafataCategorieRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP2a, oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getSuprafataCategories() != null) {
            /*stergere*/
            if (oldSuprafataCategoriesDinAn != null) {
                for (SuprafataCategorie oldSuprafataCategorie : oldSuprafataCategoriesDinAn) {
                    suprafataCategorieRepository.delete(oldSuprafataCategorie);
                }
            }
            /*valorile isFormula=0 care se modifica*/
            Set<Long> childrenIds = new HashSet<Long>();
            for (SuprafataCategorie xmlSuprafataCategorie : ranDocDTO.getGospodarie().getSuprafataCategories()) {
                /*adaugare*/
                xmlSuprafataCategorie.setGospodarie(oldGospodarie);
                suprafataCategorieRepository.save(xmlSuprafataCategorie);
                //
                if (RanConstants.NOM_IS_FORMULA_NU.equals(xmlSuprafataCategorie.getCapCategorieFolosinta().getIsFormula())) {
                    childrenIds.add(xmlSuprafataCategorie.getCapCategorieFolosinta().getId());
                }
            }
            if (childrenIds.isEmpty()) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_IS_FORMULA_0_NU_EXISTA);
            }
            regenerareTotaluri(oldGospodarie,
                    childrenIds,
                    ranDocDTO.getAnRaportare(),
                    "CapCategorieFolosinta",
                    "capCategorieFolosintas",
                    CapCategorieFolosinta,
                    "SuprafataCategorie",
                    new String[]{"suprafataAlt", "suprafataLocal", "suprafataTotal"},
                    "capCategorieFolosinta",
                    "capCategorieFolosinta",
                    new String[]{"altelocARI/altelocHA", "localARI/localHA", "totalARI/totalHA"},
                    new String[]{RanConstants.UM_MP, RanConstants.UM_MP, RanConstants.UM_MP},
                    CapCategorieFolosinta.getLabel());
        }
    }


    /**
     * - unicitate dupa an + NOM_CATEGORIE_FOLOSINTA.COD_RAND
     * Identific info din XML cu cele din baza dupa anumite chei simple sau compuse (cnp, an, semestru, cod nomenclator, codRand, etc).
     * Info din XML care apare si in DB - delete
     *
     * @param ranDocDTO info trimse prin xml
     */
    public void anuleazaDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<SuprafataCategorie> oldSuprafataCategoriesDinAn = suprafataCategorieRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP2a, oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldSuprafataCategoriesDinAn != null) {
            for (SuprafataCategorie oldSuprafataCategorie : oldSuprafataCategoriesDinAn) {
                suprafataCategorieRepository.delete(oldSuprafataCategorie);
            }
        }

    }

    /**
     * Pentru codurile de rand 1-10 exista validare ca total Ari/HA sa fie egal cu altelocARI/HA+localARI/HA Pentru codurile de rand 11-16 aceasta validare nu exista
     *
     * @param suprafataCategorie info trimse prin xml
     * @throws DateRegistruValidationException
     */
    private void validareTotalPeRand(SuprafataCategorie suprafataCategorie) throws DateRegistruValidationException {
        if (1 <= suprafataCategorie.getCapCategorieFolosinta().getCodRand() &&
                suprafataCategorie.getCapCategorieFolosinta().getCodRand() <= 10) {
            int totalAriHa = suprafataCategorie.getSuprafataTotal() != null ? suprafataCategorie.getSuprafataTotal() : 0;
            int altelocPlusLocalAriHa = (suprafataCategorie.getSuprafataAlt() != null ? suprafataCategorie.getSuprafataAlt() : 0) +
                    (suprafataCategorie.getSuprafataLocal() != null ? suprafataCategorie.getSuprafataLocal() : 0);
            if (totalAriHa != altelocPlusLocalAriHa) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.TOTAL_2a_RAND_NECORESPUNZATOR, RanConstants.UM_MP, totalAriHa, altelocPlusLocalAriHa, suprafataCategorie.getCapCategorieFolosinta().getCodRand(), suprafataCategorie.getCapCategorieFolosinta().getDenumire());
            }
        }
    }

    @Override
    protected Integer[] getDbTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        SuprafataCategorie oldRandTotal = (SuprafataCategorie) getOldRandTotal(an, semestru, idGospodarie, idNomParent);
        if (oldRandTotal != null) {
            return new Integer[]{oldRandTotal.getSuprafataAlt(), oldRandTotal.getSuprafataLocal(), oldRandTotal.getSuprafataTotal()};
        }
        return null;
    }

    @Override
    protected Object getOldRandTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        return suprafataCategorieRepository.findByAnAndGospodarieAndCapCategorieFolosinta(an, idGospodarie, idNomParent);
    }

    @Override
    protected Object[] getValoriCodRandAndDenumire(Nomenclator nomParent) {
        return new Object[]{((ro.uti.ran.core.model.registru.CapCategorieFolosinta) nomParent).getCodRand(), ((ro.uti.ran.core.model.registru.CapCategorieFolosinta) nomParent).getDenumire()};
    }


    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*SUPRAFATA_CATEGORIE - din an*/
        gospodarieDTO.setSuprafataCategories(suprafataCategorieRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(an, gospodarieDTO.getGospodarie().getIdGospodarie(), TipCapitol.CAP2a, gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_2a.class;
    }
}
