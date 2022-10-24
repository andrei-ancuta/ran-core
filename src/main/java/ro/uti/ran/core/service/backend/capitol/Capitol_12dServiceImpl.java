package ro.uti.ran.core.service.backend.capitol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.InterogareDateRegistruException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes;
import ro.uti.ran.core.model.registru.CapFructProd;
import ro.uti.ran.core.model.registru.FructProd;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.service.backend.dto.AnulareDTO;
import ro.uti.ran.core.service.backend.dto.CapitolCentralizatorDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.dto.RandCapitol_12dDTO;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.utils.Tuple;
import ro.uti.ran.core.xml.model.capitol.Capitol_12d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ro.uti.ran.core.exception.codes.DateRegistruValidationCodes.SECTIUNE_IS_FORMULA_0_NU_EXISTA;
import static ro.uti.ran.core.model.utils.RanConstants.NOM_VALOARE_DA;
import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.CapFructProd;
import static ro.uti.ran.core.service.backend.utils.TipCapitol.CAP12d;

/**
 * Created by smash on 18/11/15.
 */

@Service("capitol_12dService")
@Transactional(value = "registruTransactionManager")
public class Capitol_12dServiceImpl extends AbstractCapitolCentralizatorCuTotaluriService {


    @Override
    public void populeazaDateCapitol(CapitolCentralizatorDTO capitolCentralizatorDTO) throws RanBusinessException {
        NomCapitol nomCapitol = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomCapitol, CAP12d.name(), new DataRaportareValabilitate());
        if (nomCapitol == null) {
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.CAPITOL_CENTRALIZATOR_NOT_FOUND, CAP12d.name());
        }

        NomUat nomUat = nomUatRepository.findByCodSiruta(capitolCentralizatorDTO.getCodSiruta());

        capitolCentralizatorDTO.setDenumire(nomCapitol.getDenumire());
        List<FructProd> fructProdList = fructProdRepository.findAllByYearForSirutaAndFkNomJudet(capitolCentralizatorDTO.getAn(), capitolCentralizatorDTO.getCodSiruta(), CAP12d, nomUat.getNomJudet().getId());
        if (fructProdList != null && !fructProdList.isEmpty()) {
            List<RandCapitol_12dDTO> randuri = new ArrayList<RandCapitol_12dDTO>();
            for (FructProd fructProd : fructProdList) {

                RandCapitol_12dDTO rand = new RandCapitol_12dDTO();
                rand.setNrPomiRazleti(NOM_VALOARE_DA.equals(fructProd.getCapFructProd().getIsNrPomRazlet()) ? fructProd.getNrPomRazletRod() : null);
                rand.setProdMediePomiRazleti(NOM_VALOARE_DA.equals(fructProd.getCapFructProd().getIsProdMediePomRazlet()) ? fructProd.getProdMediePomRazletRod() : null);
                rand.setProdTotalaPomiRazleti(NOM_VALOARE_DA.equals(fructProd.getCapFructProd().getIsProdTotalPomRazlet()) ? fructProd.getProdTotalPomRazletRod() : null);
                rand.setSuprafataLivezi(NOM_VALOARE_DA.equals(fructProd.getCapFructProd().getIsSuprafataLivada()) ? fructProd.getSuprafataLivada() : null);
                rand.setProdMedieLivezi(NOM_VALOARE_DA.equals(fructProd.getCapFructProd().getIsProdMedieLivada()) ? fructProd.getProdMedieLivada() : null);
                rand.setProdTotalaLivezi(NOM_VALOARE_DA.equals(fructProd.getCapFructProd().getIsProdTotalLivada()) ? fructProd.getProdTotalLivada() : null);

                rand.setDenumire(fructProd.getCapFructProd().getDenumire());
                rand.setCod(fructProd.getCapFructProd().getCod());
                rand.setCodRand(fructProd.getCapFructProd().getCodRand());
                randuri.add(rand);
            }
            capitolCentralizatorDTO.setRandCapitolList(randuri);
        } else {
            //Capitolul este gol, arunc exceptie
            throw new InterogareDateRegistruException(InterogareDateRegistruCodes.CAPITOL_CENTRALIZATOR_EMPTY, capitolCentralizatorDTO.getAn(), CAP12d.name(), capitolCentralizatorDTO.getCodSiruta());
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_12d.class;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        valideazaDateRegistru(ranDocDTO, TipCapitol.CAP12d, NomenclatorCodeType.CapFructProd, new Tuple[]{
                new Tuple<>("isNrPomRazlet", "nrPomiRazleti", "nrPomiRazleti"),
                new Tuple<>("isProdMedieLivada", "prodMedieLivezi", "productieMedieLivezi"),
                new Tuple<>("isProdMediePomRazlet", "prodMediePomiRazleti", "productieMediePomiRazleti"),
                new Tuple<>("isProdTotalLivada", "prodTotalaLivezi", "productieTotalaLivezi"),
                new Tuple<>("isProdTotalPomRazlet", "prodTotalaPomiRazleti", "productieTotalaPomiRazleti"),
                new Tuple<>("isSuprafataLivada", "suprafataLivezi", "arieLiveziHa"),

        });
    }

    @Transactional(rollbackFor = RanBusinessException.class)
    protected void addUpdateDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        anuleazaDateRegistru(ranDocDTO);

        List<RandCapitol_12dDTO> randuriCapitolCentralizator = (List<RandCapitol_12dDTO>) ranDocDTO.getRanduriCapitolCentralizator();
        NomUat nomUat = nomUatRepository.findByCodSiruta(ranDocDTO.getSirutaUAT());
        Set<Long> childrenIds = new HashSet<Long>();

        for (RandCapitol_12dDTO rand : randuriCapitolCentralizator) {
            CapFructProd capFructProd = nomSrv.getNomenlatorForId(CapFructProd, rand.getNomId());

            Integer prodMedieCalculata = null;
            //validare productie medie livezi
            if (rand.getProdTotalaLivezi() != null) {
                prodMedieCalculata = calculeazaProdMedie(rand.getProdTotalaLivezi(), rand.getSuprafataLivezi(), "T", "arieLiveziHa", rand.getCodRand(), rand.getDenumire());
            }
            if (rand.getProdTotalaLivezi() != null && rand.getProdMedieLivezi() != null) {
                valideazaProdMedie(prodMedieCalculata, rand.getProdMedieLivezi(), "T", "HA", "productieMedieLivezi", rand.getCodRand(), rand.getDenumire());
            }
            //validare productie medie pom razlet
            if (rand.getProdTotalaPomiRazleti() != null) {
                prodMedieCalculata = calculeazaProdMedie(rand.getProdTotalaPomiRazleti(), rand.getNrPomiRazleti(), "T", "nrPomiRazleti", rand.getCodRand(), rand.getDenumire());
            }
            if (rand.getProdTotalaPomiRazleti() != null && rand.getProdMediePomiRazleti() != null) {
                valideazaProdMedie(prodMedieCalculata, rand.getProdMediePomiRazleti(), "T", "pom", "productieMediePomiRazleti", rand.getCodRand(), rand.getDenumire());
            }

            //Adaug daca este inregistrare noua
            FructProd fructProd = new FructProd();
            fructProd.setNomUat(nomUat);
            fructProd.setCapFructProd(capFructProd);
            fructProd.setAn(rand.getAn());
            fructProd.setFkNomJudet(nomUat.getNomJudet().getId());

            fructProd.setNrPomRazletRod(rand.getNrPomiRazleti());
            fructProd.setProdMediePomRazletRod(rand.getProdMediePomiRazleti());
            fructProd.setProdTotalPomRazletRod(rand.getProdTotalaPomiRazleti());
            //
            fructProd.setSuprafataLivada(rand.getSuprafataLivezi());
            fructProd.setProdMedieLivada(rand.getProdMedieLivezi());
            fructProd.setProdTotalLivada(rand.getProdTotalaLivezi());

            fructProdRepository.save(fructProd);

            if (RanConstants.NOM_IS_FORMULA_NU.equals(capFructProd.getIsFormula())) {
                childrenIds.add(fructProd.getCapFructProd().getId());
            }
        }
        /*daca am valori care se modifica regenrez totalurile*/
        if (childrenIds.isEmpty()) {
            throw new DateRegistruValidationException(SECTIUNE_IS_FORMULA_0_NU_EXISTA);
        }

        regenerareTotaluri(
                nomUat.getCodSiruta(),
                childrenIds,
                ranDocDTO.getAnRaportare(),
                "CapFructProd",
                "capFructProds",
                TipCapitol.CAP12d,
                CapFructProd,
                "FructProd",
                new String[]{"suprafataLivada", "nrPomRazletRod", "prodTotalLivada", "prodTotalPomRazletRod"},
                "capFructProd",
                "capFructProd",
                new String[]{"arieLiveziHa", "nrPomiRazleti", "productieTotalaLivezi", "productieTotalaPomiRazleti"},
                "productie_fructe");
    }

    @Transactional(rollbackFor = RanBusinessException.class)
    protected void anuleazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        AnulareDTO anulareDTO = ranDocDTO.getAnulareDTO();
        NomUat nomUat = nomUatRepository.findByCodSiruta(ranDocDTO.getSirutaUAT());
        List<FructProd> fructProdList = fructProdRepository.findAllByYearForSirutaAndFkNomJudet(ranDocDTO.getAnRaportare(), ranDocDTO.getSirutaUAT(), CAP12d, nomUat.getNomJudet().getId());


        if (fructProdList == null || fructProdList.isEmpty()) {
            //Nu am ce anula, nu exista niciun rand in baza
            return;
        }


        //Sterg tot capitolul
        for (FructProd oldFructProd : fructProdList) {
            fructProdRepository.delete(oldFructProd);
        }

    }


    @Override
    protected Integer[] getDbTotal(Integer codSiruta, Integer an, Integer semestru, TipCapitol tipCapitol, Long idNomParent) {
        FructProd oldRandTotal = (FructProd) getOldRandTotal(codSiruta, an, semestru, tipCapitol, idNomParent);
        if (oldRandTotal != null) {
            CapFructProd capFructProd = oldRandTotal.getCapFructProd();
            return NOM_VALOARE_DA.equals(capFructProd.getIsFormula()) ?
                    new Integer[]{
                            NOM_VALOARE_DA.equals(capFructProd.getIsSuprafataLivada()) ? oldRandTotal.getSuprafataLivada() : null,
                            NOM_VALOARE_DA.equals(capFructProd.getIsNrPomRazlet()) ? oldRandTotal.getNrPomRazletRod() : null,
                            NOM_VALOARE_DA.equals(capFructProd.getIsProdTotalLivada()) ? oldRandTotal.getProdTotalLivada() : null,
                            NOM_VALOARE_DA.equals(capFructProd.getIsProdTotalPomRazlet()) ? oldRandTotal.getProdTotalPomRazletRod() : null
                    } :
                    new Integer[]{
                            oldRandTotal.getSuprafataLivada(),
                            oldRandTotal.getNrPomRazletRod(),
                            oldRandTotal.getProdTotalLivada(),
                            oldRandTotal.getProdTotalPomRazletRod()

                    };
        }
        return null;
    }

    @Override
    protected Object getOldRandTotal(Integer codSiruta, Integer an, Integer semestru, TipCapitol tipCapitol, Long idNomParent) {
        NomUat nomUat = nomUatRepository.findByCodSiruta(codSiruta);
        return fructProdRepository.findByAnAndUatAndNomFructAndFkNomJudet(an, codSiruta, idNomParent, tipCapitol, nomUat.getNomJudet().getId());
    }
}
