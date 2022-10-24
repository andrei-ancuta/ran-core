package ro.uti.ran.core.service.backend.capitol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.InterogareDateRegistruException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes;
import ro.uti.ran.core.model.registru.CapCulturaProd;
import ro.uti.ran.core.model.registru.CulturaProd;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12aDTO;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.utils.Tuple;
import ro.uti.ran.core.xml.model.capitol.Capitol_12a;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.model.utils.RanConstants.NOM_VALOARE_DA;
import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapCulturaProd;

/**
 * Created by smash on 04/11/15.
 */

@Service("capitol_12aService")
@Transactional(value = "registruTransactionManager")
public class Capitol_12aServiceImpl extends AbstractCapitolCentralizatorCuTotaluriService {

    @SuppressWarnings(value = "unchecked")
    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        valideazaDateRegistru(ranDocDTO, TipCapitol.CAP12a, NomenclatorCodeType.CapCulturaProd, new Tuple[]{
                new Tuple<>("isProdMedie", "productieMedie", "prodMedieKgPerHa"),
                new Tuple<>("isProdTotal", "productieTotala", "prodTotalaTone")
        });
    }

    @Transactional(rollbackFor = RanBusinessException.class)
    protected void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        anuleazaDateRegistru(ranDocDTO);

        List<RandCapitol_12aDTO> randuriCapitolCentralizator = (List<RandCapitol_12aDTO>) ranDocDTO.getRanduriCapitolCentralizator();
        NomUat nomUat = nomUatRepository.findByCodSiruta(ranDocDTO.getSirutaUAT());
        Set<Long> childrenIds = new HashSet<Long>();

        for (RandCapitol_12aDTO rand : randuriCapitolCentralizator) {
            CapCulturaProd capCulturaProd = nomSrv.getNomenlatorForId(CapCulturaProd, rand.getNomId());
            Integer prodMedieCalculata = null;
            //validare productie medie
            if (rand.getProductieTotala() != null) {
                prodMedieCalculata = calculeazaProdMedie(rand.getProductieTotala(), rand.getSuprafata(), capCulturaProd.getNomUnitateMasura().getCod(),"nrHA", rand.getCodRand(), rand.getDenumire());
            }
            if (rand.getProductieTotala() != null && rand.getProductieMedie() != null) {
                valideazaProdMedie(prodMedieCalculata, rand.getProductieMedie(), capCulturaProd.getNomUnitateMasura().getCod(), "HA", "prodMedieKgPerHa", rand.getCodRand(), rand.getDenumire());
            }
            CulturaProd culturaProd = new CulturaProd();
            culturaProd.setNomUat(nomUat);
            culturaProd.setCapCulturaProd(capCulturaProd);
            culturaProd.setAn(rand.getAn());
            culturaProd.setFkNomJudet(nomUat.getNomJudet().getId());
            culturaProd.setSuprafata(rand.getSuprafata());
            culturaProd.setProdTotal(null != rand.getProductieTotala() ? rand.getProductieTotala() : null);
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

//        regenerareTotaluri_CapCulturaProd(TipCapitol.CAP12a, "cultura_in_camp", nomUat, childrenIds, ranDocDTO.getAnRaportare());

        regenerareTotaluri(
                nomUat.getCodSiruta(),
                childrenIds,
                ranDocDTO.getAnRaportare(),
                "CapCulturaProd",
                "capCulturaProds",
                TipCapitol.CAP12a,
                CapCulturaProd,
                "CulturaProd",
                new String[]{"suprafata", "prodTotal"},
                "capCulturaProd",
                "capCulturaProd",
                new String[]{"nrHA", "prodTotalaTone"},
                "cultura_in_camp");
    }

    @Transactional(rollbackFor = RanBusinessException.class)
    protected void anuleazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        NomUat nomUat = nomUatRepository.findByCodSiruta(ranDocDTO.getSirutaUAT());
        List<CulturaProd> culturaProdList = culturaProdRepository.findAllByTipCapitolAndYearForSirutaAndFkNomJudet(ranDocDTO.getAnRaportare(), TipCapitol.CAP12a, ranDocDTO.getSirutaUAT(), nomUat.getNomJudet().getId());

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
    public void populeazaDateCapitol(CapitolCentralizatorDTO capitolCentralizatorDTO) throws RanBusinessException {
        NomCapitol nomCapitol = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomCapitol, TipCapitol.CAP12a.name(), new DataRaportareValabilitate());
        if (nomCapitol == null) {
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.CAPITOL_CENTRALIZATOR_NOT_FOUND, TipCapitol.CAP12a.name());
        }

        NomUat nomUat = nomUatRepository.findByCodSiruta(capitolCentralizatorDTO.getCodSiruta());

        capitolCentralizatorDTO.setDenumire(nomCapitol.getDenumire());
        List<CulturaProd> culturaProds = culturaProdRepository.findAllByTipCapitolAndYearForSirutaAndFkNomJudet(capitolCentralizatorDTO.getAn(), capitolCentralizatorDTO.getTipCapitol(), capitolCentralizatorDTO.getCodSiruta(), nomUat.getNomJudet().getId());
        if (culturaProds != null && !culturaProds.isEmpty()) {
            List<RandCapitol_12aDTO> randuri = new ArrayList<RandCapitol_12aDTO>();
            for (CulturaProd cp : culturaProds) {

                RandCapitol_12aDTO rand = new RandCapitol_12aDTO();
                rand.setSuprafata(cp.getSuprafata());
                rand.setProductieMedie(NOM_VALOARE_DA.equals(cp.getCapCulturaProd().getIsProdMedie()) ? cp.getProdMedie() : null);
                rand.setProductieTotala(NOM_VALOARE_DA.equals(cp.getCapCulturaProd().getIsProdTotal()) ? cp.getProdTotal() : null);
                rand.setDenumire(cp.getCapCulturaProd().getDenumire());
                rand.setCod(cp.getCapCulturaProd().getCod());
                rand.setCodRand(cp.getCapCulturaProd().getCodRand());
                randuri.add(rand);
            }
            capitolCentralizatorDTO.setRandCapitolList(randuri);
        } else {
            //Capitolul este gol, arunc exceptie
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.CAPITOL_CENTRALIZATOR_EMPTY, capitolCentralizatorDTO.getAn(), TipCapitol.CAP12a.name(), capitolCentralizatorDTO.getCodSiruta());
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_12a.class;
    }

    @Override
    protected Integer[] getDbTotal(Integer codSiruta, Integer an, Integer semestru, TipCapitol tipCapitol, Long idNomParent) {
        CulturaProd oldRandTotal = (CulturaProd) getOldRandTotal(codSiruta, an, semestru, tipCapitol, idNomParent);
        if (oldRandTotal != null) {
            CapCulturaProd capCulturaProd = oldRandTotal.getCapCulturaProd();
            return new Integer[]{
                    oldRandTotal.getSuprafata(),
                    (NOM_VALOARE_DA.equals(capCulturaProd.getIsFormula()) && NOM_VALOARE_DA.equals(capCulturaProd.getIsProdTotal())) ? oldRandTotal.getProdTotal() : null,
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
