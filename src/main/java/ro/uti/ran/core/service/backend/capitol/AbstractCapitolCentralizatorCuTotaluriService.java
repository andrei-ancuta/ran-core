package ro.uti.ran.core.service.backend.capitol;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.service.backend.TotaluriService;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.TipCapitol;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.exception.codes.DateRegistruValidationCodes.*;
import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapAnimalProd;
import static ro.uti.ran.core.service.backend.utils.TipCapitol.CAP13cent;

/**
 * pas 1: salvez toate randurile/sectiunile din xml: isFormula = 0, subtotaluri, totaluri, etc
 * - validare: trebuie sa am cel putin un rand cu isFormula = 0.
 * pas 2: incarc toti parintii pentru randurile cu isFormula = 0.
 * pas 3: pentru fiecare parinte calculez totalul cu ajutorul copiilor si formulelor;
 * pas 4: daca totalul calculat nu coincide cu valoarea din parinte EROARE
 * pas 5: revin la pas 2. acum pe post de copii o sa am randuri cu isFormula = 1; si asa mai departe merg pe ierarhie in sus.
 * <p/>
 * Created by Dan on 27-Jan-16.
 */
public abstract class AbstractCapitolCentralizatorCuTotaluriService extends AbstractCapitolCentralizatorService {
    @Autowired
    protected TotaluriService totaluriService;

    /**
     * Total intr-un semestru sau an
     *
     * @param nomChildrenIds             id-uri copii (isFormula=0) in cadrul structurii ierarhice a nomenclatorului; valorile initiale care se modifica
     * @param an                         anul raportarii
     * @param semestru                   semestrul raportarii; poate fi si null
     * @param nomEntityName              ex: CapModUtilizare, CapCultura, CapCategorieFolosinta utilizat in totaluriService.getParentsForChildrenIds
     * @param nomFieldParentName_1       ex: capCategorieFolosintas, capModUtilizares, capCulturas utilizat in totaluriService.getParentsForChildrenIds
     * @param nomenclatorCodeType        din enum ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType
     * @param randEntityName             ex: SuprafataCategorie, SuprafataUtilizare, Cultura utilizat in totaluriService.getTotalForParent
     * @param randEntityFieldValoareName ex: suprafata, nrPomRod, nrPomTanar utilizat in totaluriService.getTotalForParent; ordinea logica trebuie sa fie identica cu ordinea din valoareLabel
     * @param randEntityFieldNomName     ex: capCategorieFolosinta, capModUtilizare  utilizat in totaluriService.getTotalForParent
     * @param nomFieldParentName_2       ex: capCategorieFolosinta, capModUtilizare utilizat in totaluriService.getTotalForParent
     * @param valoareLabel               ex: altelocARI/altelocHA, suprafata, nrARI/nrHA utilizat in mesajele de eroare; ordinea logica trebuie sa fie identica cu ordinea din randEntityFieldValoareName
     * @param nomenclatorCodeTypeLabel   ex: mod_utilizare_suprafete_agricole, cultura_in_camp in general NomenclatorCodeType.getLabel() utilizat in mesajele de eroare
     * @throws DateRegistruValidationException daca nu sunt indeplinite conditiile de validare
     */
    protected void regenerareTotaluri(Integer codSiruta,
                                      Set<Long> nomChildrenIds,
                                      Integer an,
                                      Integer semestru,
                                      String nomEntityName,
                                      String nomFieldParentName_1,
                                      TipCapitol tipCapitol,
                                      NomenclatorCodeType nomenclatorCodeType,
                                      String randEntityName,
                                      String[] randEntityFieldValoareName,
                                      String randEntityFieldNomName,
                                      String nomFieldParentName_2,
                                      String[] valoareLabel,
                                      String nomenclatorCodeTypeLabel) throws DateRegistruValidationException {
        while (!nomChildrenIds.isEmpty()) {
            /*totaluri care se modifica*/
            Set<Long> nomParentIds = new HashSet<>();
            List nomParents = totaluriService.getParentsForChildrenIds(nomChildrenIds, nomEntityName, nomFieldParentName_1);
            if (nomParents == null || nomParents.isEmpty()) {
                //nu mai am parinti ies din while / metoda
                break;
            }
            for (Object nomObj : nomParents) {
                Nomenclator nomParent = (Nomenclator) nomObj;
                /*rand din capitol-(gospodarie,nomParent) - citesc din baza*/
                Object oldRandTotal = getOldRandTotal(codSiruta, an, semestru, tipCapitol, nomParent.getId());
                /*total gospodarie pentru nomParent*/
                Number[] total = new Number[randEntityFieldValoareName.length];
                for (int i = 0; i < randEntityFieldValoareName.length; i++) {
                    total[i] = totaluriService.getTotalCentralizatorForParent(codSiruta, an, nomParent.getId(), randEntityName, randEntityFieldValoareName[i], randEntityFieldNomName, nomFieldParentName_2);
                }
                if (oldRandTotal != null) {
                    //verific totaluri calculate vs totaluri din baza
                    Number[] dbTotal = getDbTotal(codSiruta, an, semestru, tipCapitol, nomParent.getId());
                    for (int i = 0; i < randEntityFieldValoareName.length; i++) {
                        // unele campuri de totale nu se aplica si le stim ca sunt NULL in getDbTotal
                        if (null == dbTotal[i]) {
                            total[i] = null;
                        }
                        validareTotal(dbTotal[i], total[i] != null ? total[i].intValue() : null, valoareLabel[i], nomenclatorCodeTypeLabel, nomenclatorCodeType, nomParent);
                    }
                } else {
                    //subtotalul/totalul nu exista in baza
                    throw new DateRegistruValidationException(SECTIUNE_NU_EXISTA, nomenclatorCodeTypeLabel, nomenclatorCodeType.getCodeColumn(), getProperty(nomParent, "codRand"), getProperty(nomParent, "denumire"));
                }
                nomParentIds.add(nomParent.getId());
            }
            /*totalurile care se modifica acum sunt la randul lor copii si o iau de la capat*/
            nomChildrenIds = nomParentIds;
        }
    }

    private Object getProperty(Nomenclator nomenclator, String fieldName) {
        try {
            return PropertyUtils.getProperty(nomenclator, fieldName);
        } catch (Throwable e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private Object getProperty(Nomenclator nomenclator, String fieldName, Object defaultValue) {
        try {
            return PropertyUtils.getProperty(nomenclator, fieldName);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    /**
     * Total intr-un an
     *
     * @param nomChildrenIds             id-uri copii (isFormula=0) in cadrul structurii ierarhice a nomenclatorului; valorile initiale care se modifica
     * @param an                         anul raportarii
     * @param nomEntityName              ex: CapModUtilizare, CapCultura, CapCategorieFolosinta utilizat in totaluriService.getParentsForChildrenIds
     * @param nomFieldParentName_1       ex: capCategorieFolosintas, capModUtilizares, capCulturas utilizat in totaluriService.getParentsForChildrenIds
     * @param nomenclatorCodeType        din enum ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType
     * @param randEntityName             ex: SuprafataCategorie, SuprafataUtilizare, Cultura utilizat in totaluriService.getTotalForParent
     * @param randEntityFieldValoareName ex: suprafata, nrPomRod, nrPomTanar utilizat in totaluriService.getTotalForParent; ordinea logica trebuie sa fie identica cu ordinea din valoareLabel
     * @param randEntityFieldNomName     ex: capCategorieFolosinta, capModUtilizare  utilizat in totaluriService.getTotalForParent
     * @param nomFieldParentName_2       ex: capCategorieFolosinta, capModUtilizare utilizat in totaluriService.getTotalForParent
     * @param valoareLabel               ex: altelocARI/altelocHA, suprafata, nrARI/nrHA utilizat in mesajele de eroare; ordinea logica trebuie sa fie identica cu ordinea din randEntityFieldValoareName
     * @param nomenclatorCodeTypeLabel   ex: mod_utilizare_suprafete_agricole, cultura_in_camp in general NomenclatorCodeType.getLabel() utilizat in mesajele de eroare
     * @throws DateRegistruValidationException daca nu sunt indeplinite conditiile de validare
     */
    protected void regenerareTotaluri(Integer codSiruta,
                                      Set<Long> nomChildrenIds,
                                      Integer an,
                                      String nomEntityName,
                                      String nomFieldParentName_1,
                                      TipCapitol tipCapitol,
                                      NomenclatorCodeType nomenclatorCodeType,
                                      String randEntityName,
                                      String[] randEntityFieldValoareName,
                                      String randEntityFieldNomName,
                                      String nomFieldParentName_2,
                                      String[] valoareLabel,
                                      String nomenclatorCodeTypeLabel) throws DateRegistruValidationException {
        regenerareTotaluri(codSiruta,
                nomChildrenIds,
                an,
                null,
                nomEntityName,
                nomFieldParentName_1,
                tipCapitol,
                nomenclatorCodeType,
                randEntityName,
                randEntityFieldValoareName,
                randEntityFieldNomName,
                nomFieldParentName_2,
                valoareLabel,
                nomenclatorCodeTypeLabel);
    }

    protected void regenerareTotaluri_CAP13cent(Integer codSiruta, Set<Long> nomChildrenIds, Integer an) throws DateRegistruValidationException {
        /*daca am valori care se modifica regenrez totalurile*/
        if (nomChildrenIds.isEmpty()) {
            throw new DateRegistruValidationException(SECTIUNE_IS_FORMULA_0_NU_EXISTA);
        }

        NomUat nomUat = nomUatRepository.findByCodSiruta(codSiruta);

        List<CapAnimalProd> nomParents = totaluriService.getParentsForChildrenIds(nomChildrenIds, "CapAnimalProd", "capAnimalProds");
        if (nomParents != null && !nomParents.isEmpty()) {
            //nu mai am parinti ies din while / metoda
            for (CapAnimalProd nomParent : nomParents) {
                List<AnimalProd> children = animalProdRepository.findAllByAnAndUatAndCapitolAndFkCapAnimalProdAndFkNomJudet(an, codSiruta, CAP13cent, nomParent.getId(), nomUat.getNomJudet().getId());
                AnimalProd impartitor = null, deimpartit = null;
                for (AnimalProd child : children) {
                    if (OpType.IMPARTITOR.getValue() == child.getCapAnimalProd().getTipOperandRelatie()) {
                        impartitor = child;
                    } else if (OpType.DEIMPARTIT.getValue() == child.getCapAnimalProd().getTipOperandRelatie()) {
                        deimpartit = child;
                    }
                }

                if (null == impartitor || null == deimpartit) {
                    throw new IllegalArgumentException("CAP13cent (AnimalProd) is in invalid state!");
                }

                double calculatTotal = calculeazaValoare(deimpartit, impartitor);
                AnimalProd oldRandTotal = (AnimalProd) getOldRandTotal(codSiruta, an, null, CAP13cent, nomParent.getId());
                if (null != oldRandTotal) {
                    validareTotal(oldRandTotal.getValoare().doubleValue(), calculatTotal, "valoareProductie", CapAnimalProd.getLabel(), CapAnimalProd, nomParent);
                } else {
                    //subtotalul/totalul nu exista in baza
                    throw new DateRegistruValidationException(SECTIUNE_NU_EXISTA, CapAnimalProd.getLabel(), CapAnimalProd.getCodeColumn(), getProperty(nomParent, "codRand"), getProperty(nomParent, "denumire"));
                }
            }
        }
    }

    /**
     * se vor face calcule cu 5 zecimale exacte apoi rezultatul se va rotunji la 2 zecimale exacte
     *
     * @param deimpartit deimpartit din formula
     * @param impartitor impartitor din formula
     * @return valoarea calculata
     */
    private double calculeazaValoare(AnimalProd deimpartit, AnimalProd impartitor) {
        /**/
        BigDecimal deimpartitBigDecimal = deimpartit.getValoare().setScale(5, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(deimpartit.getCapAnimalProd().getOrdinMultiplicare()));
        BigDecimal impartitorBigDecimal = impartitor.getValoare().setScale(5, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(impartitor.getCapAnimalProd().getOrdinMultiplicare()));
        BigDecimal rezultat = deimpartitBigDecimal.divide(impartitorBigDecimal, 5, BigDecimal.ROUND_HALF_UP);
        return rezultat.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * @param dbTotal                  totalul din baza
     * @param calculatTotal            totalul calculat
     * @param valoareLabel             vezi descriere in regenerareTotaluri
     * @param nomenclatorCodeTypeLabel vezi descriere in regenerareTotaluri
     * @param nomenclatorCodeType      vezi descriere in regenerareTotaluri
     * @param nomParent                vezi descriere in regenerareTotaluri
     * @throws DateRegistruValidationException
     */
    protected void validareTotal(Number dbTotal, Number calculatTotal, String valoareLabel, String nomenclatorCodeTypeLabel, NomenclatorCodeType nomenclatorCodeType, Nomenclator nomParent) throws DateRegistruValidationException {
        // verificare copii sa aiba cel putin un 'is...'
        if (dbTotal != null && !dbTotal.equals(calculatTotal) && !isException(nomenclatorCodeType, nomParent)) {
            throw new DateRegistruValidationException(TOTAL_CAMP_RAND_NECORESPUNZATOR, dbTotal, getProperty(nomParent, "nomUnitateMasura.cod", "unitati"), calculatTotal != null ? calculatTotal : "N/A", valoareLabel, nomenclatorCodeTypeLabel, nomenclatorCodeType.getCodeColumn(), getProperty(nomParent, "codRand"), getProperty(nomParent, "denumire"));
        }
        if (calculatTotal != null && !calculatTotal.equals(dbTotal)) {
            if (null != dbTotal) {
                // altfel nu se transmite conf. normelor legislative acest total si nu mai trebuie calculat
                // TODO: trebuie verificat isField
                throw new DateRegistruValidationException(TOTAL_CAMP_RAND_NECORESPUNZATOR, dbTotal, getProperty(nomParent, "nomUnitateMasura.cod", "unitati"), calculatTotal, valoareLabel, nomenclatorCodeTypeLabel, nomenclatorCodeType.getCodeColumn(), getProperty(nomParent, "codRand"), getProperty(nomParent, "denumire"));
            }
        }

    }


    private boolean isException(NomenclatorCodeType nomenclatorCodeType, Nomenclator nomParent) {
        if (NomenclatorCodeType.CapFructProd == nomenclatorCodeType) {
            Integer codRand = ((CapFructProd) nomParent).getCodRand();
            if (codRand == 2 || codRand == 8) {
                return true;
            }
        }

        return false;
    }

    /**
     * Atentie ordinea in vector sa fie identica cu ordinea din regenerareTotaluri randEntityFieldValoareName si valoareLabel
     *
     * @param an          anul raportarii
     * @param semestru    semestrul raportarii; poate fi si null
     * @param idNomParent id nomenclator cu IS_FORMULA = 1
     * @return daca sunt inregistrari in baza se returneaza un vector de dimensiuni variabile ce contine totaluri pentru campurile la care se calculeza totaluri
     */
    protected abstract Number[] getDbTotal(Integer codSiruta, Integer an, Integer semestru, TipCapitol tipCapitol, Long idNomParent);

    /**
     * @param an          anul raportarii
     * @param semestru    semestrul raportarii; poate fi si null
     * @param idNomParent id nomenclator cu IS_FORMULA = 1
     * @return un rand de capitol din perioada de raportare corespunzator nomenclatorului; poate fi si null
     */
    protected abstract Object getOldRandTotal(Integer codSiruta, Integer an, Integer semestru, TipCapitol tipCapitol, Long idNomParent);

    private enum OpType {
        IMPARTITOR(2), DEIMPARTIT(1);

        private final int value;

        OpType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
