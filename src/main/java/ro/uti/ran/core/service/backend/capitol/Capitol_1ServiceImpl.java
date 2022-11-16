package ro.uti.ran.core.service.backend.capitol;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.LegaturaRudenie;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.utils.CnpValidator;
import ro.uti.ran.core.xml.model.capitol.Capitol_1;

import java.util.ArrayList;
import java.util.List;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomLegaturaRudenie;
import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomUat;

/**
 * Componența gospodăriei/exploatației agricole fără personalitate juridică
 * Created by Dan on 27-Oct-15.
 */
@Service("capitol_1Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_1ServiceImpl extends AbstractCapitolFara0XXService {

    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }


    /**
     * - unicitate dupa MEMBRU_PF.CNP
     * - sa fie un singur Cap Gospodarie (codRand=1)
     *
     * @param ranDocDTO datele ce trebuiesc validate (din mesajul XML)
     * @throws DateRegistruValidationException
     */
    public void valideazaDateRegistruLaEditare(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        /*gospodaria trebuie sa existe in baza*/
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate();
        NomUat nomUatHeader = nomSrv.getNomenclatorForStringParam(NomUat, ranDocDTO.getSirutaUAT(), dataRaportare);
        if (oldGospodarie == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.GOSPODARIE_NOT_FOUND, ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        }
        /*gospodaria trebuie sa aiba un DetinatorPf*/
        if (oldGospodarie.getDetinatorPfs() == null || oldGospodarie.getDetinatorPfs().isEmpty()) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.DETINATOR_PF_NOT_FOUND, ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        }
        /*populare cu nomenclatoare: identific dupa cod/dataExport linia din nomenclatoare corespunzatoare; daca nu gasesc ... eroare*/
        Gospodarie gospodarie = ranDocDTO.getGospodarie();

        if (gospodarie.getDetinatorPfs() == null || gospodarie.getDetinatorPfs().isEmpty()) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.DETINATOR_PF_NOT_FOUND, ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        }

        DetinatorPf detinatorPf = gospodarie.getDetinatorPfs().get(0);
        if (detinatorPf.getMembruPfs() == null || detinatorPf.getMembruPfs().size() == 0) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.DETINATOR_PF_NOT_FOUND, ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        }
        //MEMBRU PF
        valideazaDateRegistruLaEditare_MembruPF(oldGospodarie, nomUatHeader, ranDocDTO, dataRaportare, detinatorPf);

    }

    private void valideazaDateRegistruLaEditare_MembruPF(Gospodarie oldGospodarie, NomUat nomUatHeader, RanDocDTO ranDocDTO, DataRaportareValabilitate dataRaportare, DetinatorPf detinatorPf) throws DateRegistruValidationException {
        List<String> xmlCnps = new ArrayList<>();
        boolean isCapGospodarie = false;
        for (MembruPf membruPf : detinatorPf.getMembruPfs()) {
            /* Validari pentru campurile care au constrangere in baza de date, dar nu si in schema xml si genereaza erori ORA-12899*/
            if (null != membruPf.getPersoanaFizica().getNume() && membruPf.getPersoanaFizica().getNume().length() > 60) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CONSTRANGERE_LUNGIME_CAMP_DEPASITA, "nume", membruPf.getPersoanaFizica().getNume().length(), 60);
            }
            if (null != membruPf.getPersoanaFizica().getPrenume() && membruPf.getPersoanaFizica().getPrenume().length() > 60) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CONSTRANGERE_LUNGIME_CAMP_DEPASITA, "prenume", membruPf.getPersoanaFizica().getPrenume().length(), 60);
            }
            if (null != membruPf.getPersoanaFizica().getInitialaTata() && membruPf.getPersoanaFizica().getInitialaTata().length() > 10) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CONSTRANGERE_LUNGIME_CAMP_DEPASITA, "initialaTata", membruPf.getPersoanaFizica().getInitialaTata().length(), 10);
            }
            if (null != membruPf.getMentiune() && membruPf.getMentiune().length() > 500) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.CONSTRANGERE_LUNGIME_CAMP_DEPASITA, "mentiuni", membruPf.getMentiune().length(), 500);
            }

            String cnp = (membruPf.getPersoanaFizica().getCnp() != null ? membruPf
                    .getPersoanaFizica().getCnp().toUpperCase()
                    : null);
            String nif = (membruPf.getPersoanaFizica().getNif() != null ? membruPf
                    .getPersoanaFizica().getNif().toUpperCase()
                    : null);
            /* unicitate la MEMBRU_PF dupa CNP */
            if (cnp != null && xmlCnps.contains(cnp)) {
                throw new DateRegistruValidationException(
                        DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA,
                        "membru_gospodarie", "cnp", membruPf
                        .getPersoanaFizica().getCnp());
            } else if (cnp != null) {
                xmlCnps.add(cnp);
            }
            /* Cap Gospodarie */
            if (LegaturaRudenie.CAP_GOSPODARIE.getCod().equals(
                    membruPf.getNomLegaturaRudenie().getCod())) {
                /* unicitate cap Gospodarie */
                if (isCapGospodarie) {
                    throw new DateRegistruValidationException(
                            DateRegistruValidationCodes.SECTIUNE_CAPITOL_DUPLICATA,
                            "membru_gospodarie", "codLegaturaRudenie",
                            LegaturaRudenie.CAP_GOSPODARIE.getCod());
                } else {
                    isCapGospodarie = true;
                }
                /* cnp_cap1 = cnp_cap012 */
                if (StringUtils.isNotEmpty(oldGospodarie.getDetinatorPfs()
                                                        .get(0).getPersoanaFizica().getCnp())
                        && !oldGospodarie.getDetinatorPfs().get(0)
                                         .getPersoanaFizica().getCnp()
                                         .equalsIgnoreCase(cnp != null ? cnp : "")) {
                    throw new DateRegistruValidationException(
                            DateRegistruValidationCodes.CNP_CAP012_CAP1,
                            oldGospodarie.getDetinatorPfs().get(0)
                                         .getPersoanaFizica().getCnp(),
                            cnp != null ? cnp : "");
                }
                if (StringUtils.isNotEmpty(oldGospodarie.getDetinatorPfs()
                                                        .get(0).getPersoanaFizica().getNif())
                        && !oldGospodarie.getDetinatorPfs().get(0)
                                         .getPersoanaFizica().getNif()
                                         .equalsIgnoreCase(nif != null ? nif : "")) {
                    throw new DateRegistruValidationException(
                            DateRegistruValidationCodes.NIF_CAP012_CAP1,
                            oldGospodarie.getDetinatorPfs().get(0)
                                         .getPersoanaFizica().getNif(),
                            nif != null ? nif : "");
                }

            }
            //Validare pentru lungimea codului de rand
            if(membruPf.getCodRand().toString().length() > 2) {
                throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_RAND_ERONAT, membruPf.getCodRand().toString(), 2);
            }
            /* MEMBRU_PF - NOM_LEGATURA_RUDENIE */
            ro.uti.ran.core.model.registru.NomLegaturaRudenie nomLegaturaRudenie = nomSrv
                    .getNomenclatorForStringParam(NomLegaturaRudenie, membruPf
                            .getNomLegaturaRudenie().getCod(), dataRaportare);
            if (nomLegaturaRudenie == null) {
                throw new DateRegistruValidationException(
                        DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE,
                        NomLegaturaRudenie.getLabel(), NomLegaturaRudenie
                        .getCodeColumn(), membruPf
                        .getNomLegaturaRudenie().getCod(),
                        dataRaportare);
            }
            membruPf.setNomLegaturaRudenie(nomLegaturaRudenie);
            /* fkNomJudet */
            membruPf.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet()
                                                .getId());
            // PersoanaFizica
            // validare CNP
            if (membruPf.getPersoanaFizica().getCnp() != null
                    && !CnpValidator.isValid(membruPf.getPersoanaFizica()
                                                     .getCnp())) {
                throw new DateRegistruValidationException(
                        DateRegistruValidationCodes.CNP_INVALID, membruPf
                        .getPersoanaFizica().getCnp());
            }
            //
            validareUnicitateMembruPF(oldGospodarie,ranDocDTO.getSirutaUAT(),membruPf);
            // reutilizare PersoanaFizica
            PersoanaFizica dbPersoanaFizica = new PersoanaFizica();
            dbPersoanaFizica.setIdPersoanaFizica(reutilizareService
                    .reutilizarePersoanaFizica(membruPf.getPersoanaFizica(), nomUatHeader.getId()));
            membruPf.setPersoanaFizica(dbPersoanaFizica);
        }
        /* existenta cap Gospodarie */
        if (!isCapGospodarie) {
            throw new DateRegistruValidationException(
                    DateRegistruValidationCodes.CAP_GOSPODARIE_OBLIGATORIU);
        }
    }

    /**
     * Identific info din XML cu cele din baza dupa anumite chei simple sau compuse (cnp, an, semestru, cod nomenclator, codRand, etc).
     * Info din XML care apare si in DB - inlocuire (stergere + adaugare)
     * Info din XML care NU apare si in DB - adaugare
     *
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        DetinatorPf oldDetinatorPf = oldGospodarie.getDetinatorPfs().get(0);
        if (ranDocDTO.getGospodarie().getDetinatorPfs() != null && !ranDocDTO.getGospodarie().getDetinatorPfs().isEmpty()) {
            DetinatorPf xmlDetinatorPf = ranDocDTO.getGospodarie().getDetinatorPfs().get(0);
            if (xmlDetinatorPf.getMembruPfs() != null) {
                /*stergere*/
                if (oldDetinatorPf.getMembruPfs() != null) {
                    for (MembruPf dbMembruPf : oldDetinatorPf.getMembruPfs()) {
                        membruPfRepository.delete(dbMembruPf);
                    }
                }
                /*adaugare*/
                for (MembruPf xmlMembruPf : xmlDetinatorPf.getMembruPfs()) {
                    xmlMembruPf.setDetinatorPf(oldDetinatorPf);
                    membruPfRepository.save(xmlMembruPf);
                }
            }
        }
    }

    /**
     * - unicitate dupa MEMBRU_PF.CNP
     * Identific info din XML cu cele din baza dupa anumite chei simple sau compuse (cnp, an, semestru, cod nomenclator, codRand, etc).
     * Info din XML care apare si in DB - delete
     *
     * @param ranDocDTO info trimse prin xml
     */
    public void anuleazaDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<DetinatorPf> lstDetinatorPf = detinatorPfRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        if (lstDetinatorPf != null && !lstDetinatorPf.isEmpty()) {
            /*o gospodarie are un singur DetinatorPf*/
            DetinatorPf oldDetinatorPf = lstDetinatorPf.get(0);

            /*anulare intreg capitol*/
            if (oldDetinatorPf.getMembruPfs() != null) {
                for (MembruPf oldMembruPf : oldDetinatorPf.getMembruPfs()) {
                    membruPfRepository.delete(oldMembruPf);
                }
            }

        }
    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*DETINATOR_PF*/
        List<DetinatorPf> lstDetinatorPf = detinatorPfRepository.findByGospodarieAndFkNomJudet(gospodarieDTO.getGospodarie().getIdGospodarie(), gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId());
        if (lstDetinatorPf != null && !lstDetinatorPf.isEmpty()) {
            /*o gospodarie are un singur DetinatorPf*/
            DetinatorPf detinatorPf = lstDetinatorPf.get(0);
            if (detinatorPf.getMembruPfs() != null && !detinatorPf.getMembruPfs().isEmpty()) {
                gospodarieDTO.setMembruPfs(detinatorPf.getMembruPfs());
            }
        }
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_1.class;
    }
}
