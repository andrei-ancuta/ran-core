package ro.uti.ran.core.service.backend.capitol;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.service.backend.dto.GospodarieDTO;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.utils.CertificatComHelper;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.xml.model.capitol.Capitol_12;

import java.util.ArrayList;
import java.util.List;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomTipAct;

/**
 * Atestatele de producător si carnetele de comercializare eliberate/vizate
 * Created by Dan on 09-Nov-15.
 */
@Service("capitol_12Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_12ServiceImpl extends AbstractCapitolFara0XXService {


    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }


    /**
     * - unicitate dupa: ATESTAT.SERIE_NUMAR_ATESTAT
     * - unicitate dupa ATESTAT_PRODUS.DENUMIRE_PRODUS
     * - unicitate dupa ATESTAT_VIZA.NUMAR_VIZA
     * - unicitate dupa CERTIFICAT_COM.SERIE + DATA_ELIBERARE 00:00:00 000
     *
     * @param ranDocDTO datele ce trebuiesc validate (din mesajul XML)
     * @throws DateRegistruValidationException semnaleaza date invalide
     */

    public void valideazaDateRegistruLaEditare(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        /*gospodaria trebuie sa existe in baza*/
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        if (oldGospodarie == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.GOSPODARIE_NOT_FOUND, ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        }
        DataRaportareValabilitate dataRaportare = new DataRaportareValabilitate();
        Gospodarie gospodarie = ranDocDTO.getGospodarie();
        if (gospodarie.getAtestats() != null) {
            List<String> xmlRands = new ArrayList<>();
            for (Atestat atestat : gospodarie.getAtestats()) {
                String rand = atestat.getSerieNumarAtestat().toLowerCase();
                /*unicitate la ATESTAT*/
                if (xmlRands.contains(rand)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.ATESTAT_DUBLICAT, atestat.getSerieNumarAtestat());
                } else {
                    xmlRands.add(rand);
                }
                /*produs*/
                if (atestat.getAtestatProduses() != null && !atestat.getAtestatProduses().isEmpty()) {
                    List<String> xmlSubRand = new ArrayList<>();
                    for (AtestatProdus atestatProdus : atestat.getAtestatProduses()) {
                        String subRand = atestatProdus.getDenumireProdus().toLowerCase();
                        /*unicitate la ATESTAT_PRODUS*/
                        if (xmlSubRand.contains(subRand)) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.ATESTAT_ATESTAT_PRODUS_DUBLICATE,
                                    atestat.getDataEliberareAtestat(),
                                    atestat.getSerieNumarAtestat(),
                                    atestatProdus.getDenumireProdus());
                        } else {
                            xmlSubRand.add(subRand);
                        }
                    }
                }
                /*viza*/
                if (atestat.getAtestatVizas() != null && !atestat.getAtestatVizas().isEmpty()) {
                    List<Integer> xmlSubRand = new ArrayList<>();
                    for (AtestatViza atestatViza : atestat.getAtestatVizas()) {
                        Integer subRand = atestatViza.getNumarViza();
                        /*unicitate la ATESTAT_VIZA*/
                        if (xmlSubRand.contains(subRand)) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.ATESTAT_ATESTAT_VIZA_DUBLICATE,
                                    atestat.getDataEliberareAtestat(),
                                    atestat.getSerieNumarAtestat(),
                                    atestatViza.getNumarViza());
                        } else {
                            xmlSubRand.add(subRand);
                        }
                    }
                }
                /*certificatComercializare*/
                if (atestat.getCertificatComs() != null && !atestat.getCertificatComs().isEmpty()) {
                    List<CertificatComHelper> xmlSubRand = new ArrayList<>();
                    for (CertificatCom certificatCom : atestat.getCertificatComs()) {
                        CertificatComHelper subRand = new CertificatComHelper(certificatCom);
                        /*unicitate la CERTIFICAT_COM*/
                        if (xmlSubRand.contains(subRand)) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.ATESTAT_CERTIFICAT_COM_DUBLICATE,
                                    atestat.getDataEliberareAtestat(),
                                    atestat.getSerieNumarAtestat(),
                                    certificatCom.getSerie(),
                                    certificatCom.getDataEliberare());
                        } else {
                            xmlSubRand.add(subRand);
                        }
                    }
                }
                 /*act - NOM_TIP_ACT*/
                if (atestat.getAct() != null) {
                    String codValue = atestat.getAct().getNomTipAct().getCod();
                    ro.uti.ran.core.model.registru.NomTipAct nomTipAct = nomSrv.getNomenclatorForStringParam(NomTipAct, codValue, dataRaportare);
                    if (nomTipAct == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, "TIP_ACT_AVIZ_CONSULTATIV", NomTipAct.getCodeColumn(), codValue, dataRaportare);
                    }
                    atestat.getAct().setNomTipAct(nomTipAct);
                     /*fkNomJudet*/
                    atestat.getAct().setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                }
                /*fkNomJudet*/
                atestat.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
            }
        }
    }

    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<Atestat> oldAtestats = atestatRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        if (ranDocDTO.getGospodarie().getAtestats() != null) {
            /*stergere*/
            if (oldAtestats != null) {
                for (Atestat oldAtestat : oldAtestats) {
                    atestatRepository.delete(oldAtestat);
                }
            }
            for (Atestat xmlAtestat : ranDocDTO.getGospodarie().getAtestats()) {
                /*adaugare*/
                if (xmlAtestat.getAct() != null) {
                    actRepository.save(xmlAtestat.getAct());
                }
                xmlAtestat.setGospodarie(oldGospodarie);
                atestatRepository.save(xmlAtestat);
            }
        }
    }

    /**
     * Identific info din XML cu cele din baza dupa anumite chei simple sau compuse (cnp, an, semestru, cod nomenclator, codRand, etc).
     * Info din XML care apare si in DB - delete
     *
     * @param ranDocDTO info trimse prin xml
     */
    public void anuleazaDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<Atestat> oldAtestats = atestatRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldAtestats != null) {
            for (Atestat oldAtestat : oldAtestats) {
                atestatRepository.delete(oldAtestat);
            }
        }

    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*ATESTAT*/
        gospodarieDTO.setAtestats(atestatRepository.findByGospodarieAndFkNomJudet(gospodarieDTO.getGospodarie().getIdGospodarie(), gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_12.class;
    }
}
