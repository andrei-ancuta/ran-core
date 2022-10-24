package ro.uti.ran.core.service.backend.capitol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.InterogareDateRegistruException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.dto.AnulareDTO;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12b1DTO;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.service.backend.utils.TipSpatiuProtejat;
import ro.uti.ran.core.utils.Tuple;
import ro.uti.ran.core.xml.model.capitol.Capitol_12b1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.model.utils.RanConstants.NOM_VALOARE_DA;
import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapCulturaProd;

/**
 * Created by smash on 17/11/15.
 */

@Service("capitol_12b1Service")
@Transactional(value = "registruTransactionManager")
public class Capitol_12b1ServiceImpl extends AbstractCapitolCentralizatorCuTotaluriService {

    @Override
    public void populeazaDateCapitol(CapitolCentralizatorDTO capitolCentralizatorDTO) throws RanBusinessException {
        NomCapitol nomCapitol = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomCapitol, TipCapitol.CAP12b1.name(), new DataRaportareValabilitate());
        if (nomCapitol == null) {
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.CAPITOL_CENTRALIZATOR_NOT_FOUND, TipCapitol.CAP12b1.name());
        }

        NomUat nomUat = nomUatRepository.findByCodSiruta(capitolCentralizatorDTO.getCodSiruta());

        capitolCentralizatorDTO.setDenumire(nomCapitol.getDenumire());
        List<CulturaProd> culturaProdList = culturaProdRepository.findAllByTipSpatiuAndYearForSirutaAndFkNomJudet(capitolCentralizatorDTO.getAn(), TipSpatiuProtejat.SE.name(), capitolCentralizatorDTO.getCodSiruta(), TipCapitol.CAP12b1, nomUat.getNomJudet().getId());
        if (culturaProdList != null && !culturaProdList.isEmpty()) {
            List<RandCapitol_12b1DTO> randuri = new ArrayList<RandCapitol_12b1DTO>();
            for (CulturaProd culturaProt : culturaProdList) {

                RandCapitol_12b1DTO rand = new RandCapitol_12b1DTO();
                rand.setSuprafata(culturaProt.getSuprafata());
                rand.setProductieMedie(NOM_VALOARE_DA.equals(culturaProt.getCapCulturaProd().getIsProdMedie()) ? culturaProt.getProdMedie() : null);
                rand.setProductieTotala(culturaProt.getProdTotal());
                rand.setDenumire(culturaProt.getCapCulturaProd().getDenumire());
                rand.setCod(culturaProt.getCapCulturaProd().getCod());
                rand.setCodRand(culturaProt.getCapCulturaProd().getCodRand());
                randuri.add(rand);
            }
            capitolCentralizatorDTO.setRandCapitolList(randuri);
        } else {
            //Capitolul este gol, arunc exceptie
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.CAPITOL_CENTRALIZATOR_EMPTY, capitolCentralizatorDTO.getAn(), TipCapitol.CAP12b1.name(), capitolCentralizatorDTO.getCodSiruta());
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_12b1.class;
    }


    @SuppressWarnings(value = "unchecked")
    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        valideazaDateRegistru(ranDocDTO, TipCapitol.CAP12b1, NomenclatorCodeType.CapCulturaProd, new Tuple[]{
                new Tuple("isProdMedie", "productieMedie", "prodMedieKgPerHa"),
        });
    }

    @Transactional(rollbackFor = RanBusinessException.class)
    protected void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        anuleazaDateRegistru(ranDocDTO);

        List<RandCapitol_12b1DTO> randuriCapitolCentralizator = (List<RandCapitol_12b1DTO>) ranDocDTO.getRanduriCapitolCentralizator();
        NomUat nomUat = nomUatRepository.findByCodSiruta(ranDocDTO.getSirutaUAT());
        Set<Long> childrenIds = new HashSet<Long>();

        for (RandCapitol_12b1DTO rand : randuriCapitolCentralizator) {
            CapCulturaProd capCulturaProd = nomSrv.getNomenlatorForId(NomenclatorCodeType.CapCulturaProd, rand.getNomId());
            NomTipSpatiuProt nomTipSpatiuProt = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomTipSpatiuProt, TipSpatiuProtejat.SE.name(), new DataRaportareValabilitate());

            Integer prodMedieCalculata = null;
            //validare productie medie
            if (rand.getProductieTotala() != null) {
                prodMedieCalculata = calculeazaProdMedie(rand.getProductieTotala(), rand.getSuprafata(), capCulturaProd.getNomUnitateMasura().getCod(),"nrHA", rand.getCodRand(), rand.getDenumire());
            }
            if (rand.getProductieTotala() != null && rand.getProductieMedie() != null) {
                valideazaProdMedie(prodMedieCalculata, rand.getProductieMedie(), capCulturaProd.getNomUnitateMasura().getCod(), "HA", "prodMedieKgPerHa", rand.getCodRand(), rand.getDenumire());
            }

            //Adaug daca este inregistrare noua
            CulturaProd culturaProd = new CulturaProd();
            culturaProd.setNomUat(nomUat);
            culturaProd.setCapCulturaProd(capCulturaProd);
            culturaProd.setNomTipSpatiuProt(nomTipSpatiuProt);
            culturaProd.setAn(rand.getAn());
            culturaProd.setFkNomJudet(nomUat.getNomJudet().getId());
            culturaProd.setSuprafata(rand.getSuprafata());
            culturaProd.setProdTotal(rand.getProductieTotala());
            culturaProd.setProdMedie(rand.getProductieMedie());
            culturaProdRepository.save(culturaProd);

            if (RanConstants.NOM_IS_FORMULA_NU.equals(capCulturaProd.getIsFormula())) {
                childrenIds.add(culturaProd.getCapCulturaProd().getId());
            }
        }
        /*daca am valori care se modifica regenrez totalurile*/
        if (childrenIds.isEmpty()) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.SECTIUNE_IS_FORMULA_0_NU_EXISTA);
        }
//        regenerareTotaluri_CapCulturaProd(TipCapitol.CAP12b1, "cultura_in_sere", nomUat, childrenIds, ranDocDTO.getAnRaportare());

        regenerareTotaluri(
                nomUat.getCodSiruta(),
                childrenIds,
                ranDocDTO.getAnRaportare(),
                "CapCulturaProd",
                "capCulturaProds",
                TipCapitol.CAP12b1,
                CapCulturaProd,
                "CulturaProd",
                new String[]{"suprafata", "prodTotal"},
                "capCulturaProd",
                "capCulturaProd",
                new String[]{"nrHA", "prodTotalaTone"},
                "cultura_in_sere");

    }

    @Transactional(rollbackFor = RanBusinessException.class)
    protected void anuleazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        AnulareDTO anulareDTO = ranDocDTO.getAnulareDTO();
        NomUat nomUat = nomUatRepository.findByCodSiruta(ranDocDTO.getSirutaUAT());
        List<CulturaProd> culturaProdList = culturaProdRepository.findAllByTipSpatiuAndYearForSirutaAndFkNomJudet(ranDocDTO.getAnRaportare(), TipSpatiuProtejat.SE.name(), ranDocDTO.getSirutaUAT(), TipCapitol.CAP12b1, nomUat.getNomJudet().getId());


        if (culturaProdList == null || culturaProdList.isEmpty()) {
            //Nu am ce anula, nu exista niciun rand in baza
            return;
        }


        //Sterg tot capitolul
        for (CulturaProd oldCulturaProd : culturaProdList) {
            culturaProdRepository.delete(oldCulturaProd);
        }

    }

    @Override
    protected Integer[] getDbTotal(Integer codSiruta, Integer an, Integer semestru, TipCapitol tipCapitol, Long idNomParent) {
        CulturaProd oldRandTotal = (CulturaProd) getOldRandTotal(codSiruta, an, semestru, tipCapitol, idNomParent);
        if (oldRandTotal != null) {
            CapCulturaProd capCulturaProd = oldRandTotal.getCapCulturaProd();
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
        return culturaProdRepository.findByAnAndUatAndCapitolAndNomCulturaAndFkNomJudet(an, codSiruta, tipCapitol, idNomParent, nomUat.getNomJudet().getId());
    }


}
