package ro.uti.ran.core.service.exportNomenclatoare.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.repository.registru.NomCapitolRepository;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.service.exportNomenclatoare.NomenclatorClassificationService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Stanciu Neculai on 11.Feb.2016.
 */
@Service
public class NomenclatorClassificationServiceImpl implements NomenclatorClassificationService {

    @Autowired
    private NomCapitolRepository nomCapitolRepository;

    public List<NomCapitol> availableNonCapitolListFor(TipNomenclator tipNomenclator) {
        if (tipNomenclator.equals(TipNomenclator.CapAnimalProd)) {
            return getNomCapitolListForCapAnimalProd();
        } else if (tipNomenclator.equals(TipNomenclator.CapCultura)) {
            return getNomCapitolListForCapCultura();
        } else if (tipNomenclator.equals(TipNomenclator.CapCulturaProd)) {
            return getNomCapitolListForCapCulturaProd();
        } else if (tipNomenclator.equals(TipNomenclator.CapPlantatie)) {
            return getNomCapitolListForCapPlantatie();
        } else if (tipNomenclator.equals(TipNomenclator.CapPlantatieProd)) {
            return getNomCapitolListForCapPlantatieProd();
        } else if (tipNomenclator.equals(TipNomenclator.CapSistemTehnic)) {
            return getNomCapitolListForCapSistemTehnic();
        } else if (tipNomenclator.equals(TipNomenclator.CapModUtilizare)) {
            return getNomCapitolListForCapModUtilizare();
        } else if (tipNomenclator.equals(TipNomenclator.CapFructProd)) {
            return getNomCapitolListForCapFructProd();
        } else if (tipNomenclator.equals(TipNomenclator.CapCategorieAnimal)) {
            return getNomCapitolListForCapCategorieAnimal();
        } else if (tipNomenclator.equals(TipNomenclator.CapCategorieFolosinta)) {
            return getNomCapitolListForCapCategorieFolosinta();
        } else if (tipNomenclator.equals(TipNomenclator.CapPomRazlet)) {
            return getNomCapitolListForCapPomRazlet();
        } else if (tipNomenclator.equals(TipNomenclator.CapSubstantaChimica)) {
            return getNomCapitolListForCapSubstantaChimica();
        } else if (tipNomenclator.equals(TipNomenclator.CapAplicareIngrasamant)) {
            return getNomCapitolListForCapAplicareIngrasamant();
        } else if (tipNomenclator.equals(TipNomenclator.CapTerenIrigat)) {
            return getNomCapitolListForCapTerenIrigat();
        } else {
            throw new RuntimeException("" + tipNomenclator.getClazz().getSimpleName() + " not implemented in NomenclatorClassificationServiceImpl");
        }
    }

    /**
     * Structura capitole cu terenul irigat.
     * Partea 1 - Capitolul 6
     */
    private List<NomCapitol> getNomCapitolListForCapTerenIrigat() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(TipCapitol.CAP6);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }

    /**
     * Structura capitole cu aplicarea ingrasamintelor.
     * Partea 1 - Capitolul 10a
     * @return
     */
    private List<NomCapitol> getNomCapitolListForCapAplicareIngrasamant() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(TipCapitol.CAP10a);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }

    /**
     * Structura capitole cu substantele chimice agricole utilizate la culturi
     * Partea 1 - Capitolul 10b
     * @return
     */
    private List<NomCapitol> getNomCapitolListForCapSubstantaChimica() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(TipCapitol.CAP10b);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }

    /**
     * Structura capitole cu pomii razleti.
     * Partea 1 - Capitolul 5a
     * @return
     */
    private List<NomCapitol> getNomCapitolListForCapPomRazlet() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(TipCapitol.CAP5a);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }

    /**
     * Structura capitole cu categoriile de folosinta ale terenului.
     * Partea 1 - Capitolul 2a
     * @return
     */
    private List<NomCapitol> getNomCapitolListForCapCategorieFolosinta() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(TipCapitol.CAP2a);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }

    /**
     * Structura capitole cu categoriile de animale.
     * Partea 1 - Capitolul 7 + Capitolul 8
     * @return
     */
    private List<NomCapitol> getNomCapitolListForCapCategorieAnimal() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(TipCapitol.CAP7,TipCapitol.CAP8);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }


    /**
     * Comment db:
     * Structura capitole cu productia animala.
     * Partea 2 - Capitolul 13
     */
    private List<NomCapitol> getNomCapitolListForCapAnimalProd() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(TipCapitol.CAP13);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }

    /**
     * Comment db:
     * Structura capitole cu culturile.
     * Partea 1 - Capitolul 4a + Capitolul 4a1 + Capitolul 4b + Capitolul 4c
     */
    private List<NomCapitol> getNomCapitolListForCapCultura() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(
                TipCapitol.CAP4a,
                TipCapitol.CAP4a1,
                TipCapitol.CAP4b1,
                TipCapitol.CAP4c);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }

    /**
     * Structura capitole cu culturile pe zona de productie
     * Partea 2 - Capitolul 12a + Capitolul 12a1 + Capitolul 12b1 + Capitolul 12b2 + Capitolul 12c
     *
     * @return
     */
    private List<NomCapitol> getNomCapitolListForCapCulturaProd() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(
                TipCapitol.CAP12a,
                TipCapitol.CAP12a1,
                TipCapitol.CAP12b1,
                TipCapitol.CAP12b2,
                TipCapitol.CAP12c);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }

    /**
     * Structura capitole cu plantatiile pomicole si viticole
     * Partea 1 - Capitolul 5b + Capitolul 5c + Capitolul 5d
     *
     * @return
     */
    private List<NomCapitol> getNomCapitolListForCapPlantatie() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(
                TipCapitol.CAP5b,
                TipCapitol.CAP5c,
                TipCapitol.CAP5d);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }

    /**
     * Structura capitole cu productia de pe plantatii pomicole si viticole
     * Partea 2 - Capitolul 12e + Capitolul 12f
     *
     * @return
     */
    private List<NomCapitol> getNomCapitolListForCapPlantatieProd() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(
                TipCapitol.CAP12e,
                TipCapitol.CAP12f);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }

    /**
     * Structura capitole cu sistemele tehnice agricole.
     * Partea 1 - Capitolul 9
     *
     * @return
     */
    private List<NomCapitol> getNomCapitolListForCapSistemTehnic() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(
                TipCapitol.CAP9);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }

    /**
     * Structura capitole cu modurile de utilizare ale suprafetelor agricole.
     * Partea 1 - Capitolul 3
     *
     * @return
     */
    private List<NomCapitol> getNomCapitolListForCapModUtilizare() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(
                TipCapitol.CAP3);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }

    /**
     * Structura capitole cu productia de fructe
     * Partea 2 - Capitolul 12d
     *
     * @return
     */
    private List<NomCapitol> getNomCapitolListForCapFructProd() {
        final List<TipCapitol> nomCapitolCodes = Arrays.asList(
                TipCapitol.CAP12d);
        return nomCapitolRepository.findByCodesList(nomCapitolCodes);
    }
}
