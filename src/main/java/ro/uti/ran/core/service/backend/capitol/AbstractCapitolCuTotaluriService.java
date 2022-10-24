package ro.uti.ran.core.service.backend.capitol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.Gospodarie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.service.backend.TotaluriService;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public abstract class AbstractCapitolCuTotaluriService extends AbstractCapitolFara0XXService {
    @Autowired
    protected TotaluriService totaluriService;

    /**
     * Total intr-un semestru sau an
     *
     * @param oldGospodarie              gospodaria din baza de date
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
     * @param umLabel                    ex: mp, kg, ha, nr. capete  utilizat in mesajele de eroare; ordinea logica trebuie sa fie identica cu ordinea din randEntityFieldValoareName
     * @param nomenclatorCodeTypeLabel   ex: mod_utilizare_suprafete_agricole, cultura_in_camp in general NomenclatorCodeType.getLabel() utilizat in mesajele de eroare
     * @throws DateRegistruValidationException daca nu sunt indeplinite conditiile de validare
     */
    @Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
    protected void regenerareTotaluri(Gospodarie oldGospodarie,
                                      Set<Long> nomChildrenIds,
                                      Integer an,
                                      Integer semestru,
                                      String nomEntityName,
                                      String nomFieldParentName_1,
                                      NomenclatorCodeType nomenclatorCodeType,
                                      String randEntityName,
                                      String[] randEntityFieldValoareName,
                                      String randEntityFieldNomName,
                                      String nomFieldParentName_2,
                                      String[] valoareLabel,
                                      String[] umLabel,
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
                Object oldRandTotal = getOldRandTotal(an, semestru, oldGospodarie.getIdGospodarie(), nomParent.getId());
                /*total gospodarie pentru nomParent*/
                Number[] total = new Number[randEntityFieldValoareName.length];
                for (int i = 0; i < randEntityFieldValoareName.length; i++) {
                    if (semestru != null) {
                        total[i] = totaluriService.getTotalForParent(oldGospodarie.getIdGospodarie(), an, semestru, nomParent.getId(), randEntityName, randEntityFieldValoareName[i], randEntityFieldNomName, nomFieldParentName_2);
                    } else {
                        total[i] = totaluriService.getTotalForParent(oldGospodarie.getIdGospodarie(), an, nomParent.getId(), randEntityName, randEntityFieldValoareName[i], randEntityFieldNomName, nomFieldParentName_2);
                    }
                }
                if (oldRandTotal != null) {
                    //verific totaluri calculate vs totaluri din baza
                    Integer[] dbTotal = getDbTotal(an, semestru, oldGospodarie.getIdGospodarie(), nomParent.getId());
                    for (int i = 0; i < randEntityFieldValoareName.length; i++) {
                        validareTotal(dbTotal[i],
                                total[i] != null ? total[i].intValue() : null,
                                valoareLabel[i],
                                umLabel[i],
                                nomenclatorCodeTypeLabel,
                                nomenclatorCodeType,
                                nomParent);
                    }
                } else {
                    //subtotalul/totalul nu exista in baza
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_NU_EXISTA, nomenclatorCodeTypeLabel, nomenclatorCodeType.getCodeColumn(), getValoriCodRandAndDenumire(nomParent)[0], getValoriCodRandAndDenumire(nomParent)[1]);
                }
                nomParentIds.add(nomParent.getId());
            }
            /*totalurile care se modifica acum sunt la randul lor copii si o iau de la capat*/
            nomChildrenIds = nomParentIds;
        }
    }

    /**
     * Total intr-un an
     *
     * @param oldGospodarie              gospodaria din baza de date
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
     * @param umLabel                    ex: mp, kg, ha, nr. capete  utilizat in mesajele de eroare; ordinea logica trebuie sa fie identica cu ordinea din randEntityFieldValoareName
     * @param nomenclatorCodeTypeLabel   ex: mod_utilizare_suprafete_agricole, cultura_in_camp in general NomenclatorCodeType.getLabel() utilizat in mesajele de eroare
     * @throws DateRegistruValidationException daca nu sunt indeplinite conditiile de validare
     */
    @Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
    protected void regenerareTotaluri(Gospodarie oldGospodarie,
                                      Set<Long> nomChildrenIds,
                                      Integer an,
                                      String nomEntityName,
                                      String nomFieldParentName_1,
                                      NomenclatorCodeType nomenclatorCodeType,
                                      String randEntityName,
                                      String[] randEntityFieldValoareName,
                                      String randEntityFieldNomName,
                                      String nomFieldParentName_2,
                                      String[] valoareLabel,
                                      String[] umLabel,
                                      String nomenclatorCodeTypeLabel) throws DateRegistruValidationException {
        regenerareTotaluri(oldGospodarie,
                nomChildrenIds,
                an,
                null,
                nomEntityName,
                nomFieldParentName_1,
                nomenclatorCodeType,
                randEntityName,
                randEntityFieldValoareName,
                randEntityFieldNomName,
                nomFieldParentName_2,
                valoareLabel,
                umLabel,
                nomenclatorCodeTypeLabel);
    }

    /**
     * @param dbTotal                  totalul din baza
     * @param calculatTotal            totalul calculat
     * @param valoareLabel             vezi descriere in regenerareTotaluri
     * @param umLabel                  vezi descriere in regenerareTotaluri
     * @param nomenclatorCodeTypeLabel vezi descriere in regenerareTotaluri
     * @param nomenclatorCodeType      vezi descriere in regenerareTotaluri
     * @param nomParent                vezi descriere in regenerareTotaluri
     * @throws DateRegistruValidationException
     */
    private void validareTotal(Integer dbTotal,
                               Integer calculatTotal,
                               String valoareLabel,
                               String umLabel,
                               String nomenclatorCodeTypeLabel,
                               NomenclatorCodeType nomenclatorCodeType,
                               Nomenclator nomParent) throws DateRegistruValidationException {
        if (dbTotal != null && !dbTotal.equals(calculatTotal)) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.TOTAL_CAMP_RAND_NECORESPUNZATOR, dbTotal, umLabel, calculatTotal != null ? calculatTotal : "N/A", valoareLabel, nomenclatorCodeTypeLabel, nomenclatorCodeType.getCodeColumn(), getValoriCodRandAndDenumire(nomParent)[0], getValoriCodRandAndDenumire(nomParent)[1]);
        }
        if (calculatTotal != null && !calculatTotal.equals(dbTotal)) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.TOTAL_CAMP_RAND_NECORESPUNZATOR, dbTotal != null ? dbTotal : "N/A", umLabel, calculatTotal, valoareLabel, nomenclatorCodeTypeLabel, nomenclatorCodeType.getCodeColumn(), getValoriCodRandAndDenumire(nomParent)[0], getValoriCodRandAndDenumire(nomParent)[1]);
        }
    }

    /**
     * Atentie ordinea in vector sa fie identica cu ordinea din regenerareTotaluri randEntityFieldValoareName si valoareLabel
     *
     * @param an           anul raportarii
     * @param semestru     semestrul raportarii; poate fi si null
     * @param idGospodarie id gospodarie
     * @param idNomParent  id nomenclator cu IS_FORMULA = 1
     * @return daca sunt inregistrari in baza se returneaza un vector de dimensiuni variabile ce contine totaluri pentru campurile la care se calculeza totaluri
     */
    protected abstract Integer[] getDbTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent);

    /**
     * @param an           anul raportarii
     * @param semestru     semestrul raportarii; poate fi si null
     * @param idGospodarie id gospodarie
     * @param idNomParent  id nomenclator cu IS_FORMULA = 1
     * @return un rand de capitol din perioada de raportare corespunzator nomenclatorului; poate fi si null
     */
    protected abstract Object getOldRandTotal(Integer an, Integer semestru, Long idGospodarie, Long idNomParent);

    /**
     * se utilizeaza in mesajul de eroare
     *
     * @param nomParent o inregistrare din nomenclator cu IS_FORMULA = 1
     * @return [0] = valoarea nomParent.getCodRand() sau nomParent).getCod() depinde de tipul de nom; [1] = nomParent.getDenumire()
     */
    protected abstract Object[] getValoriCodRandAndDenumire(Nomenclator nomParent);
}
