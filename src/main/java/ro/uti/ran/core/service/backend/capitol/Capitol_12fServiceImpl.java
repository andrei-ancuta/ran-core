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
import ro.uti.ran.core.service.backend.dto.RandCapitol_12fDTO;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.utils.Tuple;
import ro.uti.ran.core.xml.model.capitol.Capitol_12f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by smash on 19/11/15.
 */

@Service("capitol_12fService")
@Transactional(value = "registruTransactionManager")
public class Capitol_12fServiceImpl extends AbstractCapitolCentralizatorCuTotaluriService {

    @Override
    public void populeazaDateCapitol(CapitolCentralizatorDTO capitolCentralizatorDTO) throws RanBusinessException {
        NomCapitol nomCapitol = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomCapitol, TipCapitol.CAP12f.name(), new DataRaportareValabilitate());
        if (nomCapitol == null) {
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.CAPITOL_CENTRALIZATOR_NOT_FOUND, TipCapitol.CAP12f.name());
        }

        NomUat nomUat = nomUatRepository.findByCodSiruta(capitolCentralizatorDTO.getCodSiruta());

        capitolCentralizatorDTO.setDenumire(nomCapitol.getDenumire());
        List<PlantatieProd> plantatieProdList = plantatieProdRepository.findAllByAnAndCodSirutaAndFkNomJudet(capitolCentralizatorDTO.getAn(), capitolCentralizatorDTO.getCodSiruta(), TipCapitol.CAP12f, nomUat.getNomJudet().getId());
        if (plantatieProdList != null && !plantatieProdList.isEmpty()) {
            List<RandCapitol_12fDTO> randuri = new ArrayList<RandCapitol_12fDTO>();
            for (PlantatieProd strugureProd : plantatieProdList) {

                RandCapitol_12fDTO rand = new RandCapitol_12fDTO();
                rand.setSuprafata(strugureProd.getSuprafata());
                rand.setProductieMedie(strugureProd.getProdMedie());
                rand.setProductieTotala(strugureProd.getProdTotal());

                rand.setDenumire(strugureProd.getCapPlantatieProd().getDenumire());
                rand.setCod(strugureProd.getCapPlantatieProd().getCod());
                rand.setCodRand(strugureProd.getCapPlantatieProd().getCodRand());
                randuri.add(rand);
            }
            capitolCentralizatorDTO.setRandCapitolList(randuri);
        } else {
            //Capitolul este gol, arunc exceptie
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.CAPITOL_CENTRALIZATOR_EMPTY, capitolCentralizatorDTO.getAn(), TipCapitol.CAP12f.name(), capitolCentralizatorDTO.getCodSiruta());
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_12f.class;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        valideazaDateRegistru(ranDocDTO, TipCapitol.CAP12f, NomenclatorCodeType.CapPlantatieProd, new Tuple[]{
                // nothing: all field are mandatory
        });
    }

    @Transactional(rollbackFor = RanBusinessException.class)
    protected void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        anuleazaDateRegistru(ranDocDTO);

        List<RandCapitol_12fDTO> randuriCapitolCentralizator = (List<RandCapitol_12fDTO>) ranDocDTO.getRanduriCapitolCentralizator();
        NomUat nomUat = nomUatRepository.findByCodSiruta(ranDocDTO.getSirutaUAT());
        Set<Long> childrenIds = new HashSet<Long>();

        for (RandCapitol_12fDTO rand : randuriCapitolCentralizator) {
            CapPlantatieProd capPlantatieProd = nomSrv.getNomenlatorForId(NomenclatorCodeType.CapPlantatieProd, rand.getNomId());

            Integer prodMedieCalculata = null;
            //validare productie medie
            if (rand.getProductieTotala() != null) {
                prodMedieCalculata = calculeazaProdMedie(rand.getProductieTotala(), rand.getSuprafata(),"T","suprafata", rand.getCodRand(), rand.getDenumire());
            }
            if (rand.getProductieTotala() != null && rand.getProductieMedie() != null) {
                valideazaProdMedie(prodMedieCalculata, rand.getProductieMedie(), "T", "HA", "productieMedie", rand.getCodRand(), rand.getDenumire());
            }

            //Adaug daca este inregistrare noua
            PlantatieProd strugureProd = new PlantatieProd();
            strugureProd.setNomUat(nomUat);
            strugureProd.setCapPlantatieProd(capPlantatieProd);
            strugureProd.setAn(rand.getAn());
            strugureProd.setFkNomJudet(nomUat.getNomJudet().getId());


            strugureProd.setSuprafata(rand.getSuprafata());
            strugureProd.setProdMedie(rand.getProductieMedie());
            strugureProd.setProdTotal(rand.getProductieTotala());

            plantatieProdRepository.save(strugureProd);

            if (RanConstants.NOM_IS_FORMULA_NU.equals(capPlantatieProd.getIsFormula())) {
                childrenIds.add(strugureProd.getCapPlantatieProd().getId());
            }
        }

        /*daca am valori care se modifica regenrez totalurile*/
        if (childrenIds.isEmpty()) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_IS_FORMULA_0_NU_EXISTA);
        }

        regenerareTotaluri(
                nomUat.getCodSiruta(),
                childrenIds,
                ranDocDTO.getAnRaportare(),
                "CapPlantatieProd",
                "capPlantatieProds",
                TipCapitol.CAP12f,
                NomenclatorCodeType.CapPlantatieProd,
                "PlantatieProd",
                new String[]{"suprafata", "prodTotal"},
                "capPlantatieProd",
                "capPlantatieProd",
                new String[]{"suprafata",  "productieTotala"},
                "productie_struguri");
    }

    @Transactional(rollbackFor = RanBusinessException.class)
    protected void anuleazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        AnulareDTO anulareDTO = ranDocDTO.getAnulareDTO();
        NomUat nomUat = nomUatRepository.findByCodSiruta(ranDocDTO.getSirutaUAT());
        List<PlantatieProd> strugureProdList = plantatieProdRepository.findAllByAnAndCodSirutaAndFkNomJudet(ranDocDTO.getAnRaportare(), ranDocDTO.getSirutaUAT(), TipCapitol.CAP12f, nomUat.getNomJudet().getId());


        if (strugureProdList == null || strugureProdList.isEmpty()) {
            //Nu am ce anula, nu exista niciun rand in baza
            return;
        }


        //Sterg tot capitolul
        for (PlantatieProd oldStrugureProd : strugureProdList) {
            plantatieProdRepository.delete(oldStrugureProd);
        }

    }


    @Override
    protected Integer[] getDbTotal(Integer codSiruta, Integer an, Integer semestru, TipCapitol tipCapitol, Long idNomParent) {
        PlantatieProd oldRandTotal = (PlantatieProd) getOldRandTotal(codSiruta, an, semestru, tipCapitol, idNomParent);
        if (oldRandTotal != null) {
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
