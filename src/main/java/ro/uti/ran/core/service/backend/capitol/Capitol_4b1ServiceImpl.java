package ro.uti.ran.core.service.backend.capitol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.GeometrieService;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.xml.model.capitol.Capitol_4b1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapCultura;
import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomTipSpatiuProt;
import static ro.uti.ran.core.service.backend.utils.TipCapitol.CAP4b1;

/**
 * Suprafața cultivată in sere pe raza localității
 * Created by Dan on 28-Oct-15.
 */
@Service("capitol_4b1Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_4b1ServiceImpl extends AbstractCapitolCuTotaluriService {

    private static final Logger logger = LoggerFactory.getLogger(Capitol_4b1ServiceImpl.class);

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
     * - unicitate dupa an + CAP_CULTURA.COD_RAND
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
        if (gospodarie.getCulturas() != null) {
            List<Integer> xmlCodValues = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
            for (Cultura cultura : gospodarie.getCulturas()) {
                //Validare gml includere parametru UAT
                if (cultura.getGeometrieCulturi() != null) {
                    for (GeometrieCultura gmls : cultura.getGeometrieCulturi()) {
                        String terenGml = gmls.getGeometrieGML();
                        if (terenGml != null) {
                            geometrieService.validateGeometrie(terenGml);
                            geometrieService.validateGeometriePoligonUatLimit(ranDocDTO.getSirutaUAT(), terenGml);
                        }
                    }
                }

                Integer codValue = cultura.getCapCultura().getCodRand();
                 /*unicitate*/
                if (xmlCodValues.contains(codValue)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, "cultura_in_sere", CapCultura.getCodeColumn(), codValue);
                } else {
                    xmlCodValues.add(codValue);
                }
                /*CULTURA - CAP_CULTURA*/
                CapCultura capCultura = nomSrv.findCapByCodAndCodRandAndCapitolAndDataValabilitate(CapCultura, cultura.getCapCultura().getCod(), cultura.getCapCultura().getCodRand(), CAP4b1.name(), dataRaportare);
                if (capCultura == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, "cultura_in_sere", CapCultura.getCodeColumn(), codValue, dataRaportare);
                }
                /*codNomenclator*/
                if (!cultura.getCapCultura().getCod().equals(capCultura.getCod())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_NOMENCLATOR_INVALID, cultura.getCapCultura().getCod(), "cultura_in_sere", codValue);
                }
                cultura.setCapCultura(capCultura);
                /*CULTURA_SPATIU_PROT - NOM_TIP_SPATIU_PROT*/
                String cod = cultura.getNomTipSpatiuProt().getCod();
                ro.uti.ran.core.model.registru.NomTipSpatiuProt nomTipSpatiuProt = nomSrv.getNomenclatorForStringParam(NomTipSpatiuProt, cod);
                if (nomTipSpatiuProt == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND, NomTipSpatiuProt.getLabel(), NomTipSpatiuProt.getCodeColumn(), cod);
                }
                cultura.setNomTipSpatiuProt(nomTipSpatiuProt);
                //verific daca am geometrie pt totaluri
                if (cultura.getGeometrieCulturi() != null && !cultura.getGeometrieCulturi().isEmpty()) {
                    if (RanConstants.NOM_IS_FORMULA_DA.equals(cultura.getCapCultura().getIsFormula())) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.GEOMETRIE_CULTURA_RAND_TOTAL, "cultura_in_sere", cultura.getCapCultura().getCod(), cultura.getCapCultura().getCodRand());
                    }
                }
                /*fkNomJudet*/
                cultura.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }

    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<Cultura> oldCulturaDinAn = culturaRepository.findByAnAndGospodarieAndTipSpatiuProtAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), RanConstants.NOM_TIP_SPATIU_PROT_COD_SERA, CAP4b1, oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getCulturas() != null) {
            /*stergere*/
            if (oldCulturaDinAn != null) {
                for (Cultura oldCultura : oldCulturaDinAn) {
                    culturaRepository.delete(oldCultura);
                }
            }
            /*valorile isFormula=0 care se modifica*/
            Set<Long> childrenIds = new HashSet<>();
            for (Cultura xmlCultura : ranDocDTO.getGospodarie().getCulturas()) {
                /*adaugare*/
                List<GeometrieCultura> xmlGeometrie = xmlCultura.getGeometrieCulturi();
                //cultura
                xmlCultura.setGospodarie(oldGospodarie);
                xmlCultura.setGeometrieCulturi(null);
                culturaRepository.save(xmlCultura);
                //geometrie
                if (RanConstants.NOM_IS_FORMULA_NU.equals(xmlCultura.getCapCultura().getIsFormula())) {
                    childrenIds.add(xmlCultura.getCapCultura().getId());
                    //geometrie
                    if (xmlGeometrie != null) {
                        for (GeometrieCultura geometrieCultura : xmlGeometrie) {
                            geometrieService.insertCulturaGIS(xmlCultura.getIdCultura(), geometrieCultura.getGeometrieGML(), xmlCultura.getFkNomJudet(), geometrieCultura.getIsPrincipala());
                        }
                    }
                }
            }
            if (childrenIds.isEmpty()) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_IS_FORMULA_0_NU_EXISTA);
            }

            regenerareTotaluri(oldGospodarie,
                    childrenIds,
                    ranDocDTO.getAnRaportare(),
                    "CapCultura",
                    "capCulturas",
                    CapCultura,
                    "Cultura",
                    new String[]{"suprafata"},
                    "capCultura",
                    "capCultura",
                    new String[]{"nrMP"},
                    new String[]{RanConstants.UM_MP},
                    "cultura_in_sere");

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
        List<Cultura> oldCulturaDinAn = culturaRepository.findByAnAndGospodarieAndTipSpatiuProtAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), RanConstants.NOM_TIP_SPATIU_PROT_COD_SERA, CAP4b1, oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldCulturaDinAn != null) {
            for (Cultura oldCultura : oldCulturaDinAn) {
                culturaRepository.delete(oldCultura);
            }
        }

    }

    @Override
    protected Integer[] getDbTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        Cultura oldRandTotal = (Cultura) getOldRandTotal(an, semestru, idGospodarie, idNomParent);
        if (oldRandTotal != null) {
            return new Integer[]{oldRandTotal.getSuprafata()};
        }
        return null;
    }

    @Override
    protected Object getOldRandTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        return culturaRepository.findByAnAndGospodarieAndCapitolAndNomCultura(an, idGospodarie, CAP4b1, idNomParent);
    }

    @Override
    protected Object[] getValoriCodRandAndDenumire(Nomenclator nomParent) {
        return new Object[]{((ro.uti.ran.core.model.registru.CapCultura) nomParent).getCodRand(), ((ro.uti.ran.core.model.registru.CapCultura) nomParent).getDenumire()};
    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*CULTURA_SPATIU_PROT sera - din an*/
        gospodarieDTO.setCulturas(culturaRepository.findByAnAndGospodarieAndTipSpatiuProtAndCapitolAndFkNomJudet(an, gospodarieDTO.getGospodarie().getIdGospodarie(), RanConstants.NOM_TIP_SPATIU_PROT_COD_SERA, CAP4b1, gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
        if (gospodarieDTO.getCulturas() != null) {
            for (Cultura cultura : gospodarieDTO.getCulturas()) {
                List<GeometrieCultura> geometrii = geometrieCulturaRepository.findByFkCultura(cultura.getIdCultura());
                for (GeometrieCultura geometrie : geometrii) {
                    geometrie.setGeometrieGML(geometrieService.getCulturaGIS(geometrie.getIdGeometrieCultura()));
                }
                cultura.setGeometrieCulturi(geometrii);
            }
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_4b1.class;
    }
}
