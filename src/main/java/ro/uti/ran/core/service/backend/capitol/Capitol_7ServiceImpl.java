package ro.uti.ran.core.service.backend.capitol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.CategorieAnimal;
import ro.uti.ran.core.model.registru.Crotalie;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.xml.model.capitol.Capitol_7;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapCategorieAnimal;

/**
 * Animale domestice si/sau animale sălbatice crescute in captivitate, in condițiile legii – Situația la începutul semestrului
 * Created by Dan on 29-Oct-15.
 */
@Service("capitol_7Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_7ServiceImpl extends AbstractCapitolCuTotaluriService {

    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }


    /**
     * - unicitate dupa an + semestru + NOM_CATEGORIE_ANIMAL.COD_RAND
     * - unicitate dupa CROTALIE.COD_IDENTIFICARE
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
        if (gospodarie.getCategorieAnimals() != null) {
            List<Integer> xmlCodValues = new ArrayList<>();
            DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare(), ranDocDTO.getSemestruRaportare());
            for (CategorieAnimal categorieAnimal : gospodarie.getCategorieAnimals()) {
                Integer codValue = categorieAnimal.getCapCategorieAnimal().getCodRand();
                /*unicitate*/
                if (xmlCodValues.contains(codValue)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA, "categorie_animale", CapCategorieAnimal.getCodeColumn(), codValue);
                } else {
                    xmlCodValues.add(codValue);
                }
                /*CATEGORIE_ANIMAL - CAP_CATEGORIE_ANIMAL*/
                ro.uti.ran.core.model.registru.CapCategorieAnimal capCategorieAnimal = nomSrv.getNomenclatorForStringParam(CapCategorieAnimal, codValue, dataRaportare, TipCapitol.CAP7.name());
                if (capCategorieAnimal == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, "categorie_animale", CapCategorieAnimal.getCodeColumn(), codValue, dataRaportare);
                }
                 /*codNomenclator*/
                if (!categorieAnimal.getCapCategorieAnimal().getCod().equals(capCategorieAnimal.getCod())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_NOMENCLATOR_INVALID, categorieAnimal.getCapCategorieAnimal().getCod(), "categorie_animale", codValue);
                }
                categorieAnimal.setCapCategorieAnimal(capCategorieAnimal);
                /*CROTALIE*/
                if (categorieAnimal.getCrotalies() != null && !categorieAnimal.getCrotalies().isEmpty()) {
                    List<String> xmlRands = new ArrayList<>();
                    for (Crotalie crotalie : categorieAnimal.getCrotalies()) {
                        String rand = crotalie.getCodIdentificare().toLowerCase();
                        /*unicitate la CROTALIE*/
                        if (xmlRands.contains(rand)) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.CATEGORIE_ANIMAL_CROTALIE_DUBLICATE, categorieAnimal.getCapCategorieAnimal().getCod(), crotalie.getCodIdentificare());
                        } else {
                            xmlRands.add(rand);
                        }
                    }
                }
                /*fkNomJudet*/
                categorieAnimal.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }


    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<CategorieAnimal> oldCategorieAnimalsDinAn = categorieAnimalRepository.findByAnAndSemestruAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), ranDocDTO.getSemestruRaportare().byteValue(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP7, oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getCategorieAnimals() != null) {
             /*stergere*/
            if (oldCategorieAnimalsDinAn != null) {
                for (CategorieAnimal oldCategorieAnimal : oldCategorieAnimalsDinAn) {
                    categorieAnimalRepository.delete(oldCategorieAnimal);
                }
            }
            /*valorile isFormula=0 care se modifica*/
            Set<Long> childrenIds = new HashSet<>();
            for (CategorieAnimal xmlCategorieAnimal : ranDocDTO.getGospodarie().getCategorieAnimals()) {
                /*adaugare*/
                xmlCategorieAnimal.setGospodarie(oldGospodarie);
                categorieAnimalRepository.save(xmlCategorieAnimal);
                //
                if (RanConstants.NOM_IS_FORMULA_NU.equals(xmlCategorieAnimal.getCapCategorieAnimal().getIsFormula())) {
                    childrenIds.add(xmlCategorieAnimal.getCapCategorieAnimal().getId());
                }
            }
            if (childrenIds.isEmpty()) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_IS_FORMULA_0_NU_EXISTA);
            }

            regenerareTotaluri(oldGospodarie,
                    childrenIds,
                    ranDocDTO.getAnRaportare(),
                    ranDocDTO.getSemestruRaportare(),
                    "CapCategorieAnimal",
                    "capCategorieAnimals",
                    CapCategorieAnimal,
                    "CategorieAnimal",
                    new String[]{"nrCap"},
                    "capCategorieAnimal",
                    "capCategorieAnimal",
                    new String[]{"nrCapete"},
                    new String[]{RanConstants.UM_NR_CAPETE},
                    "categorie_animale");
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
        List<CategorieAnimal> oldCategorieAnimalsDinAn = categorieAnimalRepository.findByAnAndSemestruAndGospodarieAndCapitolAndFkNomJudet(ranDocDTO.getAnRaportare(), ranDocDTO.getSemestruRaportare().byteValue(), oldGospodarie.getIdGospodarie(), TipCapitol.CAP7, oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldCategorieAnimalsDinAn != null) {
            for (CategorieAnimal oldCategorieAnimal : oldCategorieAnimalsDinAn) {
                categorieAnimalRepository.delete(oldCategorieAnimal);
            }
        }

    }

    @Override
    protected Integer[] getDbTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        CategorieAnimal oldRandTotal = (CategorieAnimal) getOldRandTotal(an, semestru, idGospodarie, idNomParent);
        if (oldRandTotal != null) {
            return new Integer[]{oldRandTotal.getNrCap()};
        }
        return null;
    }

    @Override
    protected Object getOldRandTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent) {
        return categorieAnimalRepository.findByAnAndSemestruAndGospodarieAndNomCategorieAnimal(an, semestru, idGospodarie, idNomParent);
    }

    @Override
    protected Object[] getValoriCodRandAndDenumire(Nomenclator nomParent) {
        return new Object[]{((ro.uti.ran.core.model.registru.CapCategorieAnimal) nomParent).getCodRand(), ((ro.uti.ran.core.model.registru.CapCategorieAnimal) nomParent).getDenumire()};
    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*CATEGORIE_ANIMAL - din an / semestru */
        gospodarieDTO.setCategorieAnimals(categorieAnimalRepository.findByAnAndSemestruAndGospodarieAndCapitolAndFkNomJudet(an, semestru, gospodarieDTO.getGospodarie().getIdGospodarie(), TipCapitol.CAP7, gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_7.class;
    }
}
