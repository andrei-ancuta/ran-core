package ro.uti.ran.core.service.backend.capitol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.InterogareDateRegistruException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes;
import ro.uti.ran.core.model.registru.CapPlantatieProd;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.model.registru.PlantatieProd;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.dto.AnulareDTO;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12eDTO;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.utils.Tuple;
import ro.uti.ran.core.xml.model.capitol.Capitol_12e;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.model.utils.RanConstants.NOM_VALOARE_DA;
import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapPlantatieProd;

/**
 * Created by smash on 19/11/15.
 */

@Service("capitol_12eService")
@Transactional(value = "registruTransactionManager")
public class Capitol_12eServiceImpl extends AbstractCapitolCentralizatorCuTotaluriService {


    @Override
    public void populeazaDateCapitol(CapitolCentralizatorDTO capitolCentralizatorDTO) throws RanBusinessException {
        NomCapitol nomCapitol = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomCapitol, TipCapitol.CAP12e.name(), new DataRaportareValabilitate());
        if (nomCapitol == null) {
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.CAPITOL_CENTRALIZATOR_NOT_FOUND, TipCapitol.CAP12e.name());
        }

        NomUat nomUat = nomUatRepository.findByCodSiruta(capitolCentralizatorDTO.getCodSiruta());

        capitolCentralizatorDTO.setDenumire(nomCapitol.getDenumire());
        List<PlantatieProd> plantatieProds = plantatieProdRepository.findAllByAnAndCodSirutaAndFkNomJudet(capitolCentralizatorDTO.getAn(), capitolCentralizatorDTO.getCodSiruta(), TipCapitol.CAP12e, nomUat.getNomJudet().getId());
        if (plantatieProds != null && !plantatieProds.isEmpty()) {
            List<RandCapitol_12eDTO> randuri = new ArrayList<RandCapitol_12eDTO>();
            for (PlantatieProd pomProd : plantatieProds) {

                RandCapitol_12eDTO rand = new RandCapitol_12eDTO();
                rand.setSuprafataLivezi(pomProd.getSuprafata());
                rand.setProdMedieLivezi(NOM_VALOARE_DA.equals(pomProd.getCapPlantatieProd().getIsProdMedie()) ? pomProd.getProdMedie() : null);
                rand.setProdTotalaLivezi(pomProd.getProdTotal());
                rand.setDenumire(pomProd.getCapPlantatieProd().getDenumire());
                rand.setCod(pomProd.getCapPlantatieProd().getCod());
                rand.setCodRand(pomProd.getCapPlantatieProd().getCodRand());
                randuri.add(rand);
            }
            capitolCentralizatorDTO.setRandCapitolList(randuri);
        } else {
            //Capitolul este gol, arunc exceptie
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.CAPITOL_CENTRALIZATOR_EMPTY, capitolCentralizatorDTO.getAn(), TipCapitol.CAP12e.name(), capitolCentralizatorDTO.getCodSiruta());
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_12e.class;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        valideazaDateRegistru(ranDocDTO, TipCapitol.CAP12e, CapPlantatieProd, new Tuple[]{
                new Tuple<>("isProdMedie", "prodMedieLivezi", "productieMedieLivezi"),
        });
    }

    @Transactional(rollbackFor = RanBusinessException.class)
    protected void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        anuleazaDateRegistru(ranDocDTO);

        List<RandCapitol_12eDTO> randuriCapitolCentralizator = (List<RandCapitol_12eDTO>) ranDocDTO.getRanduriCapitolCentralizator();
        NomUat nomUat = nomUatRepository.findByCodSiruta(ranDocDTO.getSirutaUAT());
        Set<Long> childrenIds = new HashSet<Long>();

        for (RandCapitol_12eDTO rand : randuriCapitolCentralizator) {
            CapPlantatieProd capPlantatieProd = nomSrv.getNomenlatorForId(CapPlantatieProd, rand.getNomId());

            Integer prodMedieCalculata = null;
            //validare productie medie
            if (rand.getProdTotalaLivezi() != null) {
                prodMedieCalculata = calculeazaProdMedie(rand.getProdTotalaLivezi(), rand.getSuprafataLivezi(),"T","arieLiveziHa", rand.getCodRand(), rand.getDenumire());
            }
            if (rand.getProdTotalaLivezi() != null && rand.getProdMedieLivezi() != null) {
                valideazaProdMedie(prodMedieCalculata, rand.getProdMedieLivezi(), "T", "HA", "productieMedieLivezi", rand.getCodRand(), rand.getDenumire());
            }

            //Adaug daca este inregistrare noua
            PlantatieProd plantatieProd = new PlantatieProd();
            plantatieProd.setNomUat(nomUat);
            plantatieProd.setCapPlantatieProd(capPlantatieProd);
            plantatieProd.setAn(rand.getAn());
            plantatieProd.setFkNomJudet(nomUat.getNomJudet().getId());

            plantatieProd.setSuprafata(rand.getSuprafataLivezi());
            plantatieProd.setProdMedie(rand.getProdMedieLivezi());
            plantatieProd.setProdTotal(rand.getProdTotalaLivezi());

            plantatieProdRepository.save(plantatieProd);

            if (RanConstants.NOM_IS_FORMULA_NU.equals(capPlantatieProd.getIsFormula())) {
                childrenIds.add(plantatieProd.getCapPlantatieProd().getId());
            }
        }
        /*daca am valori care se modifica regenrez totalurile*/
        if (childrenIds.isEmpty()) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_IS_FORMULA_0_NU_EXISTA);
        }

//        regenerareTotaluri(ranDocDTO.getSirutaUAT(), childrenIds, ranDocDTO.getAnRaportare());

        regenerareTotaluri(
                nomUat.getCodSiruta(),
                childrenIds,
                ranDocDTO.getAnRaportare(),
                "CapPlantatieProd",
                "capPlantatieProds",
                TipCapitol.CAP12e,
                CapPlantatieProd,
                "PlantatieProd",
                new String[]{"suprafata",  "prodTotal"},
                "capPlantatieProd",
                "capPlantatieProd",
                new String[]{"arieLiveziHa",  "productieTotalaLivezi"},
                "productie_fructe_in_teren_agricol");
    }

    @Transactional(rollbackFor = RanBusinessException.class)
    protected void anuleazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        AnulareDTO anulareDTO = ranDocDTO.getAnulareDTO();
        NomUat nomUat = nomUatRepository.findByCodSiruta(ranDocDTO.getSirutaUAT());
        List<PlantatieProd> plantatieProdList = plantatieProdRepository.findAllByAnAndCodSirutaAndFkNomJudet(ranDocDTO.getAnRaportare(), ranDocDTO.getSirutaUAT(), TipCapitol.CAP12e, nomUat.getNomJudet().getId());


        if (plantatieProdList == null || plantatieProdList.isEmpty()) {
            //Nu am ce anula, nu exista niciun rand in baza
            return;
        }


        //Sterg tot capitolul
        for (PlantatieProd oldPomAltPomProd : plantatieProdList) {
            plantatieProdRepository.delete(oldPomAltPomProd);
        }

    }


    @Override
    protected Integer[] getDbTotal(Integer codSiruta, Integer an, Integer semestru, TipCapitol tipCapitol, Long idNomParent) {
        PlantatieProd oldRandTotal = (PlantatieProd) getOldRandTotal(codSiruta, an, semestru, tipCapitol, idNomParent);
        if (oldRandTotal != null) {
            CapPlantatieProd capPlantatieProd = oldRandTotal.getCapPlantatieProd();
            return new Integer[]{
                    oldRandTotal.getSuprafata(),
                    oldRandTotal.getProdTotal()
            };
        }
        return null;
    }

    @Override
    protected Object getOldRandTotal(Integer codSiruta, Integer an, Integer semestru, TipCapitol tipCapitol, Long idNomParent) {
        NomUat nomUat = nomUatRepository.findByCodSiruta(codSiruta);
        return plantatieProdRepository.findByAnAndUatAndCapitolAndCapPlantatieProdAndFkNomJudet(an, codSiruta, tipCapitol, idNomParent, nomUat.getNomJudet().getId());
    }

}
