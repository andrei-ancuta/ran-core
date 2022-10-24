package ro.uti.ran.core.service.backend.capitol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.GeometriePlantatie;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.Plantatie;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.GeometrieService;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.capitol.Capitol_5c;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapPlantatie;

/**
 * Alte plantații pomicole aflate in teren agricol, pe raza localității
 * Created by Dan on 29-Oct-15.
 */
@Service("capitol_5cService")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_5cServiceImpl extends AbstractCapitolCuTotaluriService {

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
     * - unicitate dupa an + NOM_POM_ALT_PLANTATIE_POM.COD_RAND
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
        if (gospodarie.getPlantaties() != null) {
            List<Integer> xmlCodValues = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
            for (Plantatie plantatie : gospodarie.getPlantaties()) {
                //validare GML la apartinenta UAT

                for (GeometriePlantatie gmls : plantatie.getGeometriePlantatii()) {
                    String terenGml = gmls.getGeometrieGML();
                    if (terenGml != null) {
                        geometrieService.validateGeometrie(terenGml);
                        geometrieService.validateGeometriePoligonUatLimit(ranDocDTO.getSirutaUAT(), terenGml);
                    }
                }

                Integer codValue = plantatie.getCapPlantatie().getCodRand();
                /*unicitate*/
                if (xmlCodValues.contains(codValue)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, "pom_alte_plantatii_pomicole", CapPlantatie.getCodeColumn(), codValue);
                } else {
                    xmlCodValues.add(codValue);
                }
                /*PLANTATIE - CAP_PLANTATIE*/
                ro.uti.ran.core.model.registru.CapPlantatie capPlantatie = nomSrv.getNomenclatorForStringParam(CapPlantatie, codValue, dataRaportare, TipCapitol.CAP5c.name());
                if (capPlantatie == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, "pom_alte_plantatii_pomicole", CapPlantatie.getCodeColumn(), codValue, dataRaportare);
                }
                 /*codNomenclator*/
                if (!plantatie.getCapPlantatie().getCod().equals(capPlantatie.getCod())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_NOMENCLATOR_INVALID, plantatie.getCapPlantatie().getCod(), "pom_alte_plantatii_pomicole", codValue);
                }
                plantatie.setCapPlantatie(capPlantatie);
                /*fkNomJudet*/
                plantatie.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }


    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<Plantatie> oldPlantatieDinAn = plantatieRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP5c, oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getPlantaties() != null) {
            /*stergere*/
            if (oldPlantatieDinAn != null) {
                for (Plantatie oldPlantatie : oldPlantatieDinAn) {
                    plantatieRepository.delete(oldPlantatie);
                }
            }
            /*valorile isFormula=0 care se modifica*/
            Set<Long> childrenIds = new HashSet<>();
            for (Plantatie xmlPlantatie : ranDocDTO.getGospodarie().getPlantaties()) {
                /*adaugare*/
                //lista geo
                List<String> geoPoligons = new ArrayList<String>();
                if (xmlPlantatie.getGeometriePlantatii() != null) {
                    for (GeometriePlantatie geoItem : xmlPlantatie.getGeometriePlantatii()) {
                        geoPoligons.add(geoItem.getGeometrieGML());
                    }
                    xmlPlantatie.setGeometriePlantatii(new ArrayList<GeometriePlantatie>());
                }
                xmlPlantatie.setGospodarie(oldGospodarie);
                plantatieRepository.save(xmlPlantatie);
                //
                if (RanConstants.NOM_IS_FORMULA_NU.equals(xmlPlantatie.getCapPlantatie().getIsFormula())) {
                    childrenIds.add(xmlPlantatie.getCapPlantatie().getId());
                    for (String geoPoligon : geoPoligons) {
                        geometrieService.insertPlantatieGIS(xmlPlantatie.getIdPlantatie(), geoPoligon, xmlPlantatie.getFkNomJudet());
                    }
                }
            }
            if (childrenIds.isEmpty()) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_IS_FORMULA_0_NU_EXISTA);
            }

            regenerareTotaluri(oldGospodarie,
                    childrenIds,
                    ranDocDTO.getAnRaportare(),
                    "CapPlantatie",
                    "capPlantaties",
                    CapPlantatie,
                    "Plantatie",
                    new String[]{"suprafata"},
                    "capPlantatie",
                    "capPlantatie",
                    new String[]{"nrARI/nrHA"},
                    new String[]{RanConstants.UM_MP},
                    "pom_alte_plantatii_pomicole");
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
        List<Plantatie> oldPlantatieDinAn = plantatieRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP5c, oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldPlantatieDinAn != null) {
            for (Plantatie oldPlantatie : oldPlantatieDinAn) {
                plantatieRepository.delete(oldPlantatie);
            }
        }

    }

    @Override
    protected Integer[] getDbTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        Plantatie oldRandTotal = (Plantatie) getOldRandTotal(an, semestru, idGospodarie, idNomParent);
        if (oldRandTotal != null) {
            return new Integer[]{oldRandTotal.getSuprafata()};
        }
        return null;
    }

    @Override
    protected Object getOldRandTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        return plantatieRepository.findByAnAndGospodarieAndCapPlantatie(an, idGospodarie, idNomParent);
    }

    @Override
    protected Object[] getValoriCodRandAndDenumire(Nomenclator nomParent) {
        return new Object[]{((ro.uti.ran.core.model.registru.CapPlantatie) nomParent).getCodRand(), ((ro.uti.ran.core.model.registru.CapPlantatie) nomParent).getDenumire()};
    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*PLANTATIE - din an*/
        gospodarieDTO.setPlantaties(plantatieRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(an, gospodarieDTO.getGospodarie().getIdGospodarie(), TipCapitol.CAP5c, gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
        for (Plantatie plantatie : gospodarieDTO.getPlantaties()) {
            List<GeometriePlantatie> geometrii = geometriePlantatieRepository.findByFkPlantatie(plantatie.getIdPlantatie());
            for (GeometriePlantatie geometrie : geometrii) {
                geometrie.setGeometrieGML(geometrieService.getPlantatieGIS(geometrie.getIdGeometriePlantatie()));
            }
            plantatie.setGeometriePlantatii(geometrii);
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_5c.class;
    }
}
