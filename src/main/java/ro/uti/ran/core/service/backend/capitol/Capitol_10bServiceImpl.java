package ro.uti.ran.core.service.backend.capitol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.SubstantaChimica;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.capitol.Capitol_10b;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapSubstantaChimica;

/**
 * Utilizarea îngrasamintelor chimice (în echivalent substanța chimica) la principalele culturi
 * Created by Dan on 30-Oct-15.
 */
@Service("capitol_10bService")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_10bServiceImpl extends AbstractCapitolCuTotaluriService {

    private static final Logger logger = LoggerFactory.getLogger(Capitol_10bServiceImpl.class);

    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }


    /**
     * - unicitate dupa an + NOM_SUBSTANTA_CHIMICA.COD_RAND
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
        if (gospodarie.getSubstantaChimicas() != null) {
            List<Integer> xmlCodValues = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
            for (SubstantaChimica substantaChimica : gospodarie.getSubstantaChimicas()) {
                Integer codValue = substantaChimica.getCapSubstantaChimica().getCodRand();
                /*unicitate*/
                if (xmlCodValues.contains(codValue)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, CapSubstantaChimica.getLabel(), CapSubstantaChimica.getCodeColumn(), codValue);
                } else {
                    xmlCodValues.add(codValue);
                }
                /*SUBSTANTA_CHIMICA - CAP_SUBSTANTA_CHIMICA*/
                ro.uti.ran.core.model.registru.CapSubstantaChimica capSubstantaChimica = nomSrv.getNomenclatorForStringParam(CapSubstantaChimica, codValue, dataRaportare, TipCapitol.CAP10b.name());
                if (capSubstantaChimica == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, CapSubstantaChimica.getLabel(), CapSubstantaChimica.getCodeColumn(), codValue, dataRaportare);
                }
                 /*codNomenclator*/
                if (!substantaChimica.getCapSubstantaChimica().getCod().equals(capSubstantaChimica.getCod())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_NOMENCLATOR_INVALID, substantaChimica.getCapSubstantaChimica().getCod(), CapSubstantaChimica.getLabel(), codValue);
                }
                substantaChimica.setCapSubstantaChimica(capSubstantaChimica);
                /*fkNomJudet*/
                substantaChimica.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                /*cantitateTotal si suprafataTotal*/
                validareTotalPeRand(substantaChimica);
            }
        }
    }


    /**
     * "cantitateAzotoase" + "cantitateFosfatice" + "cantitatePotasice"  = "cantitateTotal"
     * "suprafataAzotoase" + "suprafataFosfatice" + "suprafataPotasice"  = "suprafataTotal"
     *
     * @param substantaChimica info trimse prin xml
     * @throws DateRegistruValidationException
     */
    private void validareTotalPeRand(SubstantaChimica substantaChimica) throws DateRegistruValidationException {
        //HA
        int totalSuprafataTransmis = substantaChimica.getSuprafataTotal() != null ? substantaChimica.getSuprafataTotal() : 0;
        int totalSuprafataCalculat = (substantaChimica.getSuprafataAzotoase() != null ? substantaChimica.getSuprafataAzotoase() : 0) +
                (substantaChimica.getSuprafataFosfatice() != null ? substantaChimica.getSuprafataFosfatice() : 0) +
                (substantaChimica.getSuprafataPotasice() != null ? substantaChimica.getSuprafataPotasice() : 0);
        if (totalSuprafataTransmis != totalSuprafataCalculat) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.TOTAL_10b_HA_RAND_NECORESPUNZATOR, totalSuprafataCalculat, totalSuprafataTransmis, substantaChimica.getCapSubstantaChimica().getCodRand(), substantaChimica.getCapSubstantaChimica().getDenumire());
        }
        //KG
        int totalCantitateTransmis = substantaChimica.getCantitateTotal() != null ? substantaChimica.getCantitateTotal() : 0;
        int totalCantitateCalculat = (substantaChimica.getCantitateAzotoase() != null ? substantaChimica.getCantitateAzotoase() : 0) +
                (substantaChimica.getCantitateFosfatice() != null ? substantaChimica.getCantitateFosfatice() : 0) +
                (substantaChimica.getCantitatePotasice() != null ? substantaChimica.getCantitatePotasice() : 0);
        if (totalCantitateTransmis != totalCantitateCalculat) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.TOTAL_10b_KG_RAND_NECORESPUNZATOR, totalCantitateCalculat, totalCantitateTransmis, substantaChimica.getCapSubstantaChimica().getCodRand(), substantaChimica.getCapSubstantaChimica().getDenumire());
        }
    }

    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<SubstantaChimica> oldSubstantaChimicasDinAn = substantaChimicaRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP10b, oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getSubstantaChimicas() != null) {
             /*stergere*/
            if (oldSubstantaChimicasDinAn != null) {
                for (SubstantaChimica oldSubstantaChimica : oldSubstantaChimicasDinAn) {
                    substantaChimicaRepository.delete(oldSubstantaChimica);
                }
            }
            /*valorile isFormula=0 care se modifica*/
            Set<Long> childrenIds = new HashSet<>();
            for (SubstantaChimica xmlSubstantaChimica : ranDocDTO.getGospodarie().getSubstantaChimicas()) {
                /*adaugare*/
                xmlSubstantaChimica.setGospodarie(oldGospodarie);
                substantaChimicaRepository.save(xmlSubstantaChimica);
                //
                if (RanConstants.NOM_IS_FORMULA_NU.equals(xmlSubstantaChimica.getCapSubstantaChimica().getIsFormula())) {
                    childrenIds.add(xmlSubstantaChimica.getCapSubstantaChimica().getId());
                }
            }
            if (childrenIds.isEmpty()) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_IS_FORMULA_0_NU_EXISTA);
            }
            regenerareTotaluri(oldGospodarie,
                    childrenIds,
                    ranDocDTO.getAnRaportare(),
                    "CapSubstantaChimica",
                    "capSubstantaChimicas",
                    CapSubstantaChimica,
                    "SubstantaChimica",
                    new String[]{"cantitateAzotoase", "cantitateFosfatice", "cantitatePotasice", "cantitateTotal", "suprafataAzotoase", "suprafataFosfatice", "suprafataPotasice", "suprafataTotal"},
                    "capSubstantaChimica",
                    "capSubstantaChimica",
                    new String[]{"nrKGazotoase", "nrKGfosfatice", "nrKGpotasice", "totalKG", "nrHAazotoase", "nrHAfosfatice", "nrHApotasice", "totalHA"},
                    new String[]{RanConstants.UM_KG, RanConstants.UM_KG, RanConstants.UM_KG, RanConstants.UM_KG, RanConstants.UM_HA, RanConstants.UM_HA, RanConstants.UM_HA, RanConstants.UM_HA},
                    CapSubstantaChimica.getLabel());
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
        List<SubstantaChimica> oldSubstantaChimicasDinAn = substantaChimicaRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP10b, oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldSubstantaChimicasDinAn != null) {
            for (SubstantaChimica oldSubstantaChimica : oldSubstantaChimicasDinAn) {
                substantaChimicaRepository.delete(oldSubstantaChimica);
            }
        }

    }

    @Override
    protected Integer[] getDbTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        SubstantaChimica oldRandTotal = (SubstantaChimica) getOldRandTotal(an, semestru, idGospodarie, idNomParent);
        if (oldRandTotal != null) {
            return new Integer[]{oldRandTotal.getCantitateAzotoase(), oldRandTotal.getCantitateFosfatice(), oldRandTotal.getCantitatePotasice(), oldRandTotal.getCantitateTotal(),
                    oldRandTotal.getSuprafataAzotoase(), oldRandTotal.getSuprafataFosfatice(), oldRandTotal.getSuprafataPotasice(), oldRandTotal.getSuprafataTotal()};
        }
        return null;
    }

    @Override
    protected Object getOldRandTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        return substantaChimicaRepository.findByAnAndGospodarieAndNomSubstantaChimica(an, idGospodarie, idNomParent);
    }

    @Override
    protected Object[] getValoriCodRandAndDenumire(Nomenclator nomParent) {
        return new Object[]{((ro.uti.ran.core.model.registru.CapSubstantaChimica) nomParent).getCodRand(), ((ro.uti.ran.core.model.registru.CapSubstantaChimica) nomParent).getDenumire()};
    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*SUBSTANTA_CHIMICA - din an*/
        gospodarieDTO.setSubstantaChimicas(substantaChimicaRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(an, gospodarieDTO.getGospodarie().getIdGospodarie(), TipCapitol.CAP10b, gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_10b.class;
    }
}
