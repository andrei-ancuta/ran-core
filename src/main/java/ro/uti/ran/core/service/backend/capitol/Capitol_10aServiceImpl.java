package ro.uti.ran.core.service.backend.capitol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.AplicareIngrasamant;
import ro.uti.ran.core.model.registru.GeometrieAplicareIngras;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.GeometrieService;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.capitol.Capitol_10a;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapAplicareIngrasamant;

/**
 * Aplicarea îngrașamintelor, amendamentelor si pesticidelor pe suprafețe situate pe raza localității
 * Created by Dan on 30-Oct-15.
 */
@Service("capitol_10aService")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_10aServiceImpl extends AbstractCapitolCuTotaluriService {

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
     * - unicitate dupa an + NOM_UTILIZARE_INGRASAMANT.COD_RAND
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
        if (gospodarie.getAplicareIngrasamants() != null) {
            List<Integer> xmlCodValues = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
            for (AplicareIngrasamant utilizareIngrasamant : gospodarie.getAplicareIngrasamants()) {
                //validare GML la apartinenta UAT

                for (GeometrieAplicareIngras gmls : utilizareIngrasamant.getGeometrieAplicareIngrases()) {
                    String terenGml = gmls.getGeometrieGML();
                    if (terenGml != null) {
                        geometrieService.validateGeometrie(terenGml);
                        geometrieService.validateGeometriePoligonUatLimit(ranDocDTO.getSirutaUAT(), terenGml);
                    }
                }

                Integer codValue = utilizareIngrasamant.getCapAplicareIngrasamant().getCodRand();
                /*unicitate*/
                if (xmlCodValues.contains(codValue)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, CapAplicareIngrasamant.getLabel(), CapAplicareIngrasamant.getCodeColumn(), codValue);
                } else {
                    xmlCodValues.add(codValue);
                }
                /*APLICARE_INGRASAMANT - CAP_APLICARE_INGRASAMANT*/
                ro.uti.ran.core.model.registru.CapAplicareIngrasamant capAplicareIngrasamant = nomSrv.getNomenclatorForStringParam(CapAplicareIngrasamant, codValue, dataRaportare, TipCapitol.CAP10a.name());
                if (capAplicareIngrasamant == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, CapAplicareIngrasamant.getLabel(), CapAplicareIngrasamant.getCodeColumn(), codValue, dataRaportare);
                }
                 /*codNomenclator*/
                if (!utilizareIngrasamant.getCapAplicareIngrasamant().getCod().equals(capAplicareIngrasamant.getCod())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_NOMENCLATOR_INVALID, utilizareIngrasamant.getCapAplicareIngrasamant().getCod(), CapAplicareIngrasamant.getLabel(), codValue);
                }
                utilizareIngrasamant.setCapAplicareIngrasamant(capAplicareIngrasamant);
                /*fkNomJudet*/
                utilizareIngrasamant.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }

    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<AplicareIngrasamant> oldAplicareIngrasamantsDinAn = aplicareIngrasamantRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP10a, oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getAplicareIngrasamants() != null) {
             /*stergere*/
            if (oldAplicareIngrasamantsDinAn != null) {
                for (AplicareIngrasamant oldAplicareIngrasamant : oldAplicareIngrasamantsDinAn) {
                    aplicareIngrasamantRepository.delete(oldAplicareIngrasamant);
                }
            }
            /*valorile isFormula=0 care se modifica*/
            Set<Long> childrenIds = new HashSet<>();
            for (AplicareIngrasamant xmlAplicareIngrasamant : ranDocDTO.getGospodarie().getAplicareIngrasamants()) {
                /*adaugare*/
                List<String> geoPoligons = new ArrayList<String>();
                if (xmlAplicareIngrasamant.getGeometrieAplicareIngrases() != null) {
                    for (GeometrieAplicareIngras geoItem : xmlAplicareIngrasamant.getGeometrieAplicareIngrases()) {
                        geoPoligons.add(geoItem.getGeometrieGML());
                    }
                    xmlAplicareIngrasamant.setGeometrieAplicareIngrases(new ArrayList<GeometrieAplicareIngras>());
                }
                xmlAplicareIngrasamant.setGospodarie(oldGospodarie);
                aplicareIngrasamantRepository.save(xmlAplicareIngrasamant);
                //
                if (RanConstants.NOM_IS_FORMULA_NU.equals(xmlAplicareIngrasamant.getCapAplicareIngrasamant().getIsFormula())) {
                    childrenIds.add(xmlAplicareIngrasamant.getCapAplicareIngrasamant().getId());
                    for (String geoPoligon : geoPoligons) {
                        geometrieService.insertAplicareIngrasamantGIS(xmlAplicareIngrasamant.getIdAplicareIngrasamant(), geoPoligon, xmlAplicareIngrasamant.getFkNomJudet());
                    }
                }
            }
            if (childrenIds.isEmpty()) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_IS_FORMULA_0_NU_EXISTA);
            }
            regenerareTotaluri(oldGospodarie,
                    childrenIds,
                    ranDocDTO.getAnRaportare(),
                    "CapAplicareIngrasamant",
                    "capAplicareIngrasamants",
                    CapAplicareIngrasamant,
                    "AplicareIngrasamant",
                    new String[]{"suprafata", "cantitate"},
                    "capAplicareIngrasamant",
                    "capAplicareIngrasamant",
                    new String[]{"nrHA", "nrKG"},
                    new String[]{RanConstants.UM_HA, RanConstants.UM_KG},
                    CapAplicareIngrasamant.getLabel());
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
        List<AplicareIngrasamant> oldAplicareIngrasamantsDinAn = aplicareIngrasamantRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP10a, oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldAplicareIngrasamantsDinAn != null) {
            for (AplicareIngrasamant oldAplicareIngrasamant : oldAplicareIngrasamantsDinAn) {
                aplicareIngrasamantRepository.delete(oldAplicareIngrasamant);
            }
        }

    }

    @Override
    protected Integer[] getDbTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        AplicareIngrasamant oldRandTotal = (AplicareIngrasamant) getOldRandTotal(an, semestru, idGospodarie, idNomParent);
        if (oldRandTotal != null) {
            return new Integer[]{oldRandTotal.getSuprafata(), oldRandTotal.getCantitate()};
        }
        return null;
    }

    @Override
    protected Object getOldRandTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        return aplicareIngrasamantRepository.findByAnAndGospodarieAndCapAplicareIngrasamant(an, idGospodarie, idNomParent);
    }

    @Override
    protected Object[] getValoriCodRandAndDenumire(Nomenclator nomParent) {
        return new Object[]{((ro.uti.ran.core.model.registru.CapAplicareIngrasamant) nomParent).getCodRand(), ((ro.uti.ran.core.model.registru.CapAplicareIngrasamant) nomParent).getDenumire()};
    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*UTILIZARE_INGRASAMANT - din an*/
        gospodarieDTO.setAplicareIngrasamants(aplicareIngrasamantRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(an, gospodarieDTO.getGospodarie().getIdGospodarie(), TipCapitol.CAP10a, gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
        for (AplicareIngrasamant aplicareIngrasamant : gospodarieDTO.getAplicareIngrasamants()) {
            List<GeometrieAplicareIngras> geometrii = geometrieAplicareIngrasRepository.findByFkAplicareIngrasamant(aplicareIngrasamant.getIdAplicareIngrasamant());
            for (GeometrieAplicareIngras geometrie : geometrii) {
                geometrie.setGeometrieGML(geometrieService.getAplicareIngrasamantGIS(geometrie.getIdGeometrieAplicareIngras()));
            }
            aplicareIngrasamant.setGeometrieAplicareIngrases(geometrii);
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_10a.class;
    }
}
