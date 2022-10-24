package ro.uti.ran.core.service.backend.capitol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.repository.registru.*;
import ro.uti.ran.core.service.backend.TotaluriService;
import ro.uti.ran.core.service.backend.dto.AnulareDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitolCentralizator;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorService;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.utils.Tuple;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

import static ro.uti.ran.core.exception.codes.DateRegistruValidationCodes.*;

/**
 * Created by smash on 04/11/15.
 */

@Component
public abstract class AbstractCapitolCentralizatorService implements CapitolCentralizatorService {

    @Autowired
    protected NomUatRepository nomUatRepository;
    @Autowired
    protected NomenclatorService nomSrv;
    @Autowired
    protected CulturaProdRepository culturaProdRepository;
    @Autowired
    protected FructProdRepository fructProdRepository;
    @Autowired
    protected PlantatieProdRepository plantatieProdRepository;
    @Autowired
    protected AnimalProdRepository animalProdRepository;
    @Autowired
    protected TotaluriService totaluriService;
    @Autowired
    private ApplicationContext applicationContext;

    @PersistenceContext(unitName = "ran-registru")
    private EntityManager em;

    /**
     * Valoarea '%1$s', exprimata in '%2$s',  trebuie sa fie cuprinsa intre  '%3$s' si '%4$s' pentru campul '%5$s' cu 'codRand' = '%6$s' si denumire = '%7$s'!
     *
     * @param prodMedieCalculata productia medie calculata
     * @param prodMedieTransmisa productia medie transmisa
     * @param codUmProdTotala    FIRE, KFIRE, T, KG, etc
     * @param codUmRaportaLa     HA, pom
     * @param xmlTagName         ex: prodMedieKgPerHa
     * @param codRand            valoare cod rand
     * @param denumireRand       ex: Grâu comun de toamnă
     * @throws DateRegistruValidationException
     */
    protected void valideazaProdMedie(Integer prodMedieCalculata, Integer prodMedieTransmisa, String codUmProdTotala, String codUmRaportaLa, String xmlTagName, Integer codRand, String denumireRand) throws DateRegistruValidationException {
        StringBuilder umProdMedie = new StringBuilder("");
        if ("KFIRE".equalsIgnoreCase(codUmProdTotala) || "FIRE".equalsIgnoreCase(codUmProdTotala)) {
            umProdMedie.append("FIRE").append("/").append(codUmRaportaLa);
        }
        if ("T".equalsIgnoreCase(codUmProdTotala) || "KG".equalsIgnoreCase(codUmProdTotala)) {
            umProdMedie.append("KG").append("/").append(codUmRaportaLa);
        }
        if (prodMedieTransmisa > (prodMedieCalculata + 1) || prodMedieTransmisa < prodMedieCalculata) {
            throw new DateRegistruValidationException(PROD_MEDIE_RAND_NECORESPUNZATOR, prodMedieTransmisa, umProdMedie.toString(), prodMedieCalculata, prodMedieCalculata + 1, xmlTagName, codRand, denumireRand);
        }
    }

    /**
     * @param prodTotala      productia totala exprimata in codUmProdTotala
     * @param raportataLa     suprafata exprimata in HA, nr pomi, etc
     * @param codUmProdTotala FIRE, KFIRE, T, KG
     * @return valoare productie medie aproximare prin lipsa exprimata in KG/HA, FIRE/HA
     */
    protected Integer calculeazaProdMedie(Integer prodTotala, Integer raportataLa, String codUmProdTotala, String xmlTagName, Integer codRand, String denumireRand) throws DateRegistruValidationException {
        if (raportataLa == 0) {
            //"Valoarea '%1$s' nu este valida pentru elementul '%2$s'  din randul cu 'codRand' = '%3$s' si denumire = '%4$s'!"
            throw new DateRegistruValidationException(VALOARE_RAND_NECORESPUNZATOR, 0, xmlTagName, codRand, denumireRand);
        }
        BigDecimal deimpartitul = BigDecimal.valueOf(prodTotala);
        BigDecimal impartitorul = BigDecimal.valueOf(raportataLa);
        if ("KFIRE".equalsIgnoreCase(codUmProdTotala) || "T".equalsIgnoreCase(codUmProdTotala)) {
            deimpartitul = deimpartitul.multiply(BigDecimal.valueOf(1000));
        }
        BigDecimal rezultat = deimpartitul.divide(impartitorul, 2, BigDecimal.ROUND_HALF_UP);
        return rezultat.setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue();
    }

    protected <T extends Nomenclator> void valideazaDateRegistru(RanDocDTO ranDocDTO, TipCapitol tipCapitol, NomenclatorCodeType nomenclatorCodeType, Tuple<String, String, String>[] fieldsToValidate) throws DateRegistruValidationException {
        DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());

        //UAT-ul pentru care se face raportarea trebuie sa existe in baza
        NomUat nomUat = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomUat, ranDocDTO.getSirutaUAT(), dataRaportare);
        if (nomUat == null) {
            throw new DateRegistruValidationException(NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomenclatorCodeType.NomUat.getLabel(), NomenclatorCodeType.NomUat.getCodeColumn(), ranDocDTO.getSirutaUAT(), dataRaportare);
        }

        if (ranDocDTO.getRanduriCapitolCentralizator() == null || ranDocDTO.getRanduriCapitolCentralizator().isEmpty()) {
            //E vorba de o anulare...
            AnulareDTO anulare = ranDocDTO.getAnulareDTO();
            if (anulare == null) {
                throw new DateRegistruValidationException("Anularea nu a fost populata cu informatii");
            }


        } else {
            for (RandCapitolCentralizator rand : ranDocDTO.getRanduriCapitolCentralizator()) {

                NomCapitol nomCapitol = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomCapitol, ranDocDTO.getCodCapitol(), dataRaportare);
                if (nomCapitol == null) {
                    throw new DateRegistruValidationException(COD_CAPITOL_INVALID, ranDocDTO.getCodCapitol(), tipCapitol);
                }

                T nomenclatorProd = nomSrv.findCapByCodAndCodRandAndCapitolAndDataValabilitate(nomenclatorCodeType, rand.getCod(), rand.getCodRand(), ranDocDTO.getCodCapitol(), dataRaportare);
                if (nomenclatorProd == null) {
                    throw new DateRegistruValidationException(NOMENCLATOR_RECORD_COD_AND_CODRAND_NOT_FOUND_AT_DATE, nomenclatorCodeType.getTableName(), rand.getCod(), rand.getCodRand(), dataRaportare);
                }

                //Validari CAPITOL_CENTRALIZATOR
                CapitolCentralizatorValidator<T, RandCapitolCentralizator> validator = new CapitolCentralizatorValidator<>(nomenclatorProd, rand, tipCapitol);

                for (Tuple<String, String, String> field : fieldsToValidate) {
                    validator.validateField(field.getLeft(), field.getCenter(), field.getRight());
                }

                if (TipCapitol.CAP12d.equals(tipCapitol)) {
                    validator.validareRandCAP12d();
                }


                rand.setUatId(nomUat.getId());
                rand.setNomId(nomenclatorProd.getId());
            }
        }
    }

    @Override
    @Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
    public void salveazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            addUpdateDateRegistru(ranDocDTO);
        } else if (IndicativXml.ANULEAZA.equals(ranDocDTO.getIndicativ())) {
            anuleazaDateRegistru(ranDocDTO);
        }

        // Altfel nu se face rollback pe constraint-uri DB
        //em.flush();
    }

    @Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
    protected abstract void anuleazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException;

    @Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
    protected abstract void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException;

}
