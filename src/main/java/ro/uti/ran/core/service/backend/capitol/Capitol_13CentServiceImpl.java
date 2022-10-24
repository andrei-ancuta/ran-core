package ro.uti.ran.core.service.backend.capitol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.InterogareDateRegistruException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes;
import ro.uti.ran.core.model.registru.AnimalProd;
import ro.uti.ran.core.model.registru.CapAnimalProd;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.dto.AnulareDTO;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_13centDTO;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.utils.Tuple;
import ro.uti.ran.core.xml.model.capitol.Capitol_13_centralizator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapAnimalProd;
import static ro.uti.ran.core.service.backend.utils.TipCapitol.CAP13cent;

/**
 * Created by smash on 20/11/15.
 */

@Service("capitol_13_centralizator")
@Transactional(value = "registruTransactionManager")
public class Capitol_13CentServiceImpl extends AbstractCapitolCentralizatorCuTotaluriService {
    @Override
    public void populeazaDateCapitol(CapitolCentralizatorDTO capitolCentralizatorDTO) throws RanBusinessException {
        NomCapitol nomCapitol = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomCapitol, CAP13cent.name(), new DataRaportareValabilitate());
        if (nomCapitol == null) {
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.CAPITOL_CENTRALIZATOR_NOT_FOUND, CAP13cent.name());
        }

        NomUat nomUat = nomUatRepository.findByCodSiruta(capitolCentralizatorDTO.getCodSiruta());

        capitolCentralizatorDTO.setDenumire(nomCapitol.getDenumire());
        List<AnimalProd> animalProdList = animalProdRepository.findAllByAnAndCodSirutaAndFkNomJudet(capitolCentralizatorDTO.getAn(), capitolCentralizatorDTO.getCodSiruta(), CAP13cent, nomUat.getNomJudet().getId());
        if (animalProdList != null && !animalProdList.isEmpty()) {
            List<RandCapitol_13centDTO> randuri = new ArrayList<RandCapitol_13centDTO>();
            for (AnimalProd animalProd : animalProdList) {

                RandCapitol_13centDTO rand = new RandCapitol_13centDTO();
                rand.setValoareProductie(animalProd.getValoare());

                rand.setDenumire(animalProd.getCapAnimalProd().getDenumire());
                rand.setCod(animalProd.getCapAnimalProd().getCod());
                rand.setCodRand(animalProd.getCapAnimalProd().getCodRand());
                randuri.add(rand);
            }
            capitolCentralizatorDTO.setRandCapitolList(randuri);
        } else {
            //Capitolul este gol, arunc exceptie
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.CAPITOL_CENTRALIZATOR_EMPTY, capitolCentralizatorDTO.getAn(), CAP13cent.name(), capitolCentralizatorDTO.getCodSiruta());
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_13_centralizator.class;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        valideazaDateRegistru(ranDocDTO, CAP13cent, CapAnimalProd, new Tuple[]{
                // nothing: all field are mandatory
        });
    }

    @Transactional(rollbackFor = RanBusinessException.class)
    protected void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        anuleazaDateRegistru(ranDocDTO);

        List<RandCapitol_13centDTO> randuriCapitolCentralizator = (List<RandCapitol_13centDTO>) ranDocDTO.getRanduriCapitolCentralizator();
        NomUat nomUat = nomUatRepository.findByCodSiruta(ranDocDTO.getSirutaUAT());
        Set<Long> childrenIds = new HashSet<Long>();

        for (RandCapitol_13centDTO rand : randuriCapitolCentralizator) {
            CapAnimalProd nomAnimalProd = nomSrv.getNomenlatorForId(CapAnimalProd, rand.getNomId());

            //Adaug daca este inregistrare noua
            AnimalProd animalProd = new AnimalProd();
            animalProd.setNomUat(nomUat);
            animalProd.setCapAnimalProd(nomAnimalProd);
            animalProd.setAn(rand.getAn());
            animalProd.setFkNomJudet(nomUat.getNomJudet().getId());

            animalProd.setValoare(rand.getValoareProductie());

            animalProdRepository.save(animalProd);

            if (RanConstants.NOM_IS_FORMULA_NU.equals(nomAnimalProd.getIsFormula())) {
                childrenIds.add(animalProd.getCapAnimalProd().getId());
            }
        }

        regenerareTotaluri_CAP13cent(nomUat.getCodSiruta(), childrenIds, ranDocDTO.getAnRaportare());
    }

    @Transactional(rollbackFor = RanBusinessException.class)
    protected void anuleazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {

        AnulareDTO anulareDTO = ranDocDTO.getAnulareDTO();
        NomUat nomUat = nomUatRepository.findByCodSiruta(ranDocDTO.getSirutaUAT());
        List<AnimalProd> animalProdList = animalProdRepository.findAllByAnAndCodSirutaAndFkNomJudet(ranDocDTO.getAnRaportare(), ranDocDTO.getSirutaUAT(), CAP13cent, nomUat.getNomJudet().getId());


        if (animalProdList == null || animalProdList.isEmpty()) {
            //Nu am ce anula, nu exista niciun rand in baza
            return;
        }


        //Sterg tot capitolul
        for (AnimalProd oldAnimalProd : animalProdList) {
            animalProdRepository.delete(oldAnimalProd);
        }


    }

    @Override
    protected Number[] getDbTotal(Integer codSiruta, Integer an, Integer semestru, TipCapitol tipCapitol, Long idNomParent) {
        AnimalProd oldRandTotal = (AnimalProd) getOldRandTotal(codSiruta, an, semestru, tipCapitol, idNomParent);
        if (oldRandTotal != null) {
            return new Number[]{
                    oldRandTotal.getValoare()
            };
        }
        return null;
    }

    @Override
    protected Object getOldRandTotal(Integer codSiruta, Integer an, Integer semestru, TipCapitol tipCapitol, Long idNomParent) {
        NomUat nomUat = nomUatRepository.findByCodSiruta(codSiruta);
        return animalProdRepository.findByAnAndUatAndCapitolAndCapAnimalProdAndFkNomJudet(an, codSiruta, tipCapitol, idNomParent, nomUat.getNomJudet().getId());
    }

}
