package ro.uti.ran.core.service.backend.capitol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.GeometrieTerenIrigat;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.TerenIrigat;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.GeometrieService;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.capitol.Capitol_6;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapTerenIrigat;

/**
 * Suprafețele efectiv irigate in câmp, situate pe raza localității
 * Created by Dan on 29-Oct-15.
 */
@Service("capitol_6Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_6ServiceImpl extends AbstractCapitolCuTotaluriService {

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
     * - unicitate dupa an + NOM_TEREN_IRIGAT.COD_RAND
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
        if (gospodarie.getTerenIrigats() != null) {
            List<Integer> xmlCodValues = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
            for (TerenIrigat terenIrigat : gospodarie.getTerenIrigats()) {
                //validare GML la apartinenta UAT

                for (GeometrieTerenIrigat gmls : terenIrigat.getGeometrieTerenIrigats()) {
                    String terenGml = gmls.getGeometrieGML();
                    if (terenGml != null) {
                        geometrieService.validateGeometrie(terenGml);
                        geometrieService.validateGeometriePoligonUatLimit(ranDocDTO.getSirutaUAT(), terenGml);
                    }
                }

                Integer codValue = terenIrigat.getCapTerenIrigat().getCodRand();
                /*unicitate*/
                if (xmlCodValues.contains(codValue)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, CapTerenIrigat.getLabel(), CapTerenIrigat.getCodeColumn(), codValue);
                } else {
                    xmlCodValues.add(codValue);
                }
                /*TEREN_IRIGAT - NOM_TEREN_IRIGAT*/
                ro.uti.ran.core.model.registru.CapTerenIrigat capTerenIrigat = nomSrv.getNomenclatorForStringParam(CapTerenIrigat, codValue, dataRaportare, TipCapitol.CAP6.name());
                if (capTerenIrigat == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, CapTerenIrigat.getLabel(), CapTerenIrigat.getCodeColumn(), codValue, dataRaportare);
                }
                 /*codNomenclator*/
                if (!terenIrigat.getCapTerenIrigat().getCod().equals(capTerenIrigat.getCod())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_NOMENCLATOR_INVALID, terenIrigat.getCapTerenIrigat().getCod(), CapTerenIrigat.getLabel(), codValue);
                }
                terenIrigat.setCapTerenIrigat(capTerenIrigat);
                /*fkNomJudet*/
                terenIrigat.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }


    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<TerenIrigat> oldTerenIrigatsDinAn = terenIrigatRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP6, oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getTerenIrigats() != null) {
             /*stergere*/
            if (oldTerenIrigatsDinAn != null) {
                for (TerenIrigat oldTerenIrigat : oldTerenIrigatsDinAn) {
                    terenIrigatRepository.delete(oldTerenIrigat);
                }
            }
            /*valorile isFormula=0 care se modifica*/
            Set<Long> childrenIds = new HashSet<>();
            for (TerenIrigat xmlTerenIrigat : ranDocDTO.getGospodarie().getTerenIrigats()) {
                /*adaugare*/
                //lista geo
                List<String> geoPoligons = new ArrayList<String>();
                if (xmlTerenIrigat.getGeometrieTerenIrigats() != null) {
                    for (GeometrieTerenIrigat geoItem : xmlTerenIrigat.getGeometrieTerenIrigats()) {
                        geoPoligons.add(geoItem.getGeometrieGML());
                    }
                    xmlTerenIrigat.setGeometrieTerenIrigats(new ArrayList<GeometrieTerenIrigat>());
                }
                xmlTerenIrigat.setGospodarie(oldGospodarie);
                terenIrigatRepository.save(xmlTerenIrigat);
                //
                if (RanConstants.NOM_IS_FORMULA_NU.equals(xmlTerenIrigat.getCapTerenIrigat().getIsFormula())) {
                    childrenIds.add(xmlTerenIrigat.getCapTerenIrigat().getId());
                    for (String geoPoligon : geoPoligons) {
                        geometrieService.insertTerenIrigatGIS(xmlTerenIrigat.getIdTerenIrigat(), geoPoligon, xmlTerenIrigat.getFkNomJudet());
                    }
                }
            }
            if (childrenIds.isEmpty()) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_IS_FORMULA_0_NU_EXISTA);
            }

            regenerareTotaluri(oldGospodarie,
                    childrenIds,
                    ranDocDTO.getAnRaportare(),
                    "CapTerenIrigat",
                    "capTerenIrigats",
                    CapTerenIrigat,
                    "TerenIrigat",
                    new String[]{"suprafata"},
                    "capTerenIrigat",
                    "capTerenIrigat",
                    new String[]{"nrARI/nrHA"},
                    new String[]{RanConstants.UM_MP},
                    CapTerenIrigat.getLabel());
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
        List<TerenIrigat> oldTerenIrigatsDinAn = terenIrigatRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP6, oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldTerenIrigatsDinAn != null) {
            for (TerenIrigat oldTerenIrigat : oldTerenIrigatsDinAn) {
                terenIrigatRepository.delete(oldTerenIrigat);
            }
        }

    }

    @Override
    protected Integer[] getDbTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        TerenIrigat oldRandTotal = (TerenIrigat) getOldRandTotal(an, semestru, idGospodarie, idNomParent);
        if (oldRandTotal != null) {
            return new Integer[]{oldRandTotal.getSuprafata()};
        }
        return null;
    }

    @Override
    protected Object getOldRandTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        return terenIrigatRepository.findByAnAndGospodarieAndNomTerenIrigat(an, idGospodarie, idNomParent);
    }

    @Override
    protected Object[] getValoriCodRandAndDenumire(Nomenclator nomParent) {
        return new Object[]{((ro.uti.ran.core.model.registru.CapTerenIrigat) nomParent).getCodRand(), ((ro.uti.ran.core.model.registru.CapTerenIrigat) nomParent).getDenumire()};
    }


    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*TEREN_IRIGAT - din an*/
        gospodarieDTO.setTerenIrigats(terenIrigatRepository.findByAnAndGospodarieAndCapitolAndFkNomJudet(an, gospodarieDTO.getGospodarie().getIdGospodarie(), TipCapitol.CAP6, gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
        for (TerenIrigat terenIrigat : gospodarieDTO.getTerenIrigats()) {
            List<GeometrieTerenIrigat> geometrii = geometrieTerenIrigatRepository.findByFkTerenIrigat(terenIrigat.getIdTerenIrigat());
            for (GeometrieTerenIrigat geometrie : geometrii) {
                geometrie.setGeometrieGML(geometrieService.getTerenIrigatGIS(geometrie.getIdGeometrieTerenIrigat()));
            }
            terenIrigat.setGeometrieTerenIrigats(geometrii);
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_6.class;
    }
}
