package ro.uti.ran.core.service.backend.capitol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.GeometrieSuprafataUtiliz;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.SuprafataUtilizare;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.GeometrieService;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.capitol.Capitol_3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapModUtilizare;

/**
 * Modul de utilizare a suprafețelor agricole situate pe raza localității
 * Created by Dan on 28-Oct-15.
 */
@Service("capitol_3Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_3ServiceImpl extends AbstractCapitolCuTotaluriService {

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
     * - unicitate dupa an + CAP_MOD_UTILIZARE.COD_RAND
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
        if (gospodarie.getSuprafataUtilizares() != null) {
            List<Integer> xmlCodValues = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
            for (SuprafataUtilizare suprafataUtilizare : gospodarie.getSuprafataUtilizares()) {
                //Validare gml in perimetru UAT
                List<GeometrieSuprafataUtiliz> geometrieGml = suprafataUtilizare.getGeometrieSuprafataUtiliz();
                if (geometrieGml != null) {
                    for (GeometrieSuprafataUtiliz gmls : geometrieGml) {
                        String terenGml = gmls.getGeometrieGML();
                        if (terenGml != null) {
                            geometrieService.validateGeometrie(terenGml);
                            geometrieService.validateGeometriePoligonUatLimit(ranDocDTO.getSirutaUAT(), terenGml);
                        }
                    }
                }

                Integer codValue = suprafataUtilizare.getCapModUtilizare().getCodRand();
                /*unicitate*/
                if (xmlCodValues.contains(codValue)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, CapModUtilizare.getLabel(), CapModUtilizare.getCodeColumn(), codValue);
                } else {
                    xmlCodValues.add(codValue);
                }
                /*SUPRAFATA_UTILIZARE - CAP_MOD_UTILIZARE*/
                ro.uti.ran.core.model.registru.CapModUtilizare capModUtilizare = nomSrv.getNomenclatorForStringParam(CapModUtilizare, codValue, dataRaportare, TipCapitol.CAP3.name());
                if (capModUtilizare == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, CapModUtilizare.getLabel(), CapModUtilizare.getCodeColumn(), codValue, dataRaportare);
                }
                /*codNomenclator*/
                if (!suprafataUtilizare.getCapModUtilizare().getCod().equals(capModUtilizare.getCod())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_NOMENCLATOR_INVALID, suprafataUtilizare.getCapModUtilizare().getCod(), CapModUtilizare.getLabel(), codValue);
                }
                suprafataUtilizare.setCapModUtilizare(capModUtilizare);
                //verific daca am geometrie pt totaluri
                if (suprafataUtilizare.getGeometrieSuprafataUtiliz() != null && !suprafataUtilizare.getGeometrieSuprafataUtiliz().isEmpty()) {
                    if (RanConstants.NOM_IS_FORMULA_DA.equals(suprafataUtilizare.getCapModUtilizare().getIsFormula())
                            || suprafataUtilizare.getCapModUtilizare().getCapModUtilizare().getCodRand() != 2) {//hardcodare: 2 = Suprafața agricolă primită
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.GEOMETRIE_CAP3, suprafataUtilizare.getCapModUtilizare().getCod(), suprafataUtilizare.getCapModUtilizare().getCodRand());
                    }
                }
                /*fkNomJudet*/
                suprafataUtilizare.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }

    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<SuprafataUtilizare> oldSuprafataUtilizaresDinAn = suprafataUtilizareRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP3, oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getSuprafataUtilizares() != null) {
             /*stergere*/
            if (oldSuprafataUtilizaresDinAn != null) {
                for (SuprafataUtilizare oldSuprafataUtilizare : oldSuprafataUtilizaresDinAn) {
                    suprafataUtilizareRepository.delete(oldSuprafataUtilizare);
                }
            }
            /*valorile isFormula=0 care se modifica*/
            Set<Long> childrenIds = new HashSet<>();
            for (SuprafataUtilizare xmlSuprafataUtilizare : ranDocDTO.getGospodarie().getSuprafataUtilizares()) {
                /*adaugare*/
                List<GeometrieSuprafataUtiliz> xmlGeometrie = xmlSuprafataUtilizare.getGeometrieSuprafataUtiliz();
                xmlSuprafataUtilizare.setGospodarie(oldGospodarie);
                xmlSuprafataUtilizare.setGeometrieSuprafataUtiliz(null);
                suprafataUtilizareRepository.save(xmlSuprafataUtilizare);
                //
                if (RanConstants.NOM_IS_FORMULA_NU.equals(xmlSuprafataUtilizare.getCapModUtilizare().getIsFormula())) {
                    childrenIds.add(xmlSuprafataUtilizare.getCapModUtilizare().getId());
                    /*Geometria*/
                    if (xmlGeometrie != null) {
                        for (GeometrieSuprafataUtiliz geometrieSuprafataUtiliz : xmlGeometrie) {
                            geometrieService.insertSuprafataUtilizGIS(xmlSuprafataUtilizare.getIdSuprafataUtilizare(), geometrieSuprafataUtiliz.getGeometrieGML(), xmlSuprafataUtilizare.getFkNomJudet());
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
                    "CapModUtilizare",
                    "capModUtilizares",
                    CapModUtilizare,
                    "SuprafataUtilizare",
                    new String[]{"suprafata"},
                    "capModUtilizare",
                    "capModUtilizare",
                    new String[]{"nrARI/nrHA"},
                    new String[]{RanConstants.UM_MP},
                    CapModUtilizare.getLabel());
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
        List<SuprafataUtilizare> oldSuprafataUtilizaresDinAn = suprafataUtilizareRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP3, oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldSuprafataUtilizaresDinAn != null) {
            for (SuprafataUtilizare oldSuprafataUtilizare : oldSuprafataUtilizaresDinAn) {
                suprafataUtilizareRepository.delete(oldSuprafataUtilizare);
            }
        }

    }

    @Override
    protected Integer[] getDbTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        SuprafataUtilizare oldRandTotal = (SuprafataUtilizare) getOldRandTotal(an, semestru, idGospodarie, idNomParent);
        if (oldRandTotal != null) {
            return new Integer[]{oldRandTotal.getSuprafata()};
        }
        return null;
    }

    @Override
    protected Object getOldRandTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        return suprafataUtilizareRepository.findByAnAndGospodarieAndNomModUtilizare(an, idGospodarie, idNomParent);
    }

    @Override
    protected Object[] getValoriCodRandAndDenumire(Nomenclator nomParent) {
        return new Object[]{((ro.uti.ran.core.model.registru.CapModUtilizare) nomParent).getCodRand(), ((ro.uti.ran.core.model.registru.CapModUtilizare) nomParent).getDenumire()};
    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*SUPRAFATA_UTILIZARE - din an*/
        gospodarieDTO.setSuprafataUtilizares(suprafataUtilizareRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(an, gospodarieDTO.getGospodarie().getIdGospodarie(), TipCapitol.CAP3, gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
        /*Geometrie*/
        if (gospodarieDTO.getSuprafataUtilizares() != null) {
            for (SuprafataUtilizare suprafataUtilizare : gospodarieDTO.getSuprafataUtilizares()) {
                List<GeometrieSuprafataUtiliz> geometrii = geometrieSuprafataUtilizRepository.findBySuprafataUtilizareIdSuprafataUtilizare(suprafataUtilizare.getIdSuprafataUtilizare());
                if (geometrii != null) {
                    for (GeometrieSuprafataUtiliz geometrie : geometrii) {
                        geometrie.setGeometrieGML(geometrieService.getSuprafataUtilizGIS(geometrie.getIdGeometrieSuprafataUtiliz()));
                    }
                }
                suprafataUtilizare.setGeometrieSuprafataUtiliz(geometrii);
            }
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_3.class;
    }
}
