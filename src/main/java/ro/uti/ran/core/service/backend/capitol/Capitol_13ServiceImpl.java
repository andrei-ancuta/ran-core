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
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.MentiuneCerereSucHelper;
import ro.uti.ran.core.utils.CnpValidator;
import ro.uti.ran.core.xml.model.capitol.Capitol_13;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.*;

/**
 * Mențiuni cu privire la sesizările/cererile pentru deschiderea procedurilor succesorale înaintate notarilor publici
 * Created by Dan on 09-Nov-15.
 */
@Service("capitol_13Service")
@Transactional(value = "registruTransactionManager", rollbackFor = RanBusinessException.class)
public class Capitol_13ServiceImpl extends AbstractCapitolFara0XXService {


    @Override
    public void valideazaDateRegistru(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
            valideazaDateRegistruLaEditare(ranDocDTO);
        } else {
            validareExistentaGospodarieNomUatHeader(ranDocDTO);
        }
    }

    /**
     * - unicitate dupa: MENTIUNE_CERERE_SUC.NR_INREGISTRARE + MENTIUNE_CERERE_SUC.DATA_INREGISTRARE
     * - unicitate dupa SUCCESIBIL.NUME + SUCCESIBIL.PRENUME
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
        NomUat nomUatHeader = nomSrv.getNomenclatorForStringParam(NomUat, ranDocDTO.getSirutaUAT(), dataRaportare);
        Gospodarie gospodarie = ranDocDTO.getGospodarie();
        if (gospodarie.getMentiuneCerereSucs() != null) {
            List<MentiuneCerereSucHelper> xmlRands = new ArrayList<>();
            for (MentiuneCerereSuc mentiuneCerereSuc : gospodarie.getMentiuneCerereSucs()) {
                MentiuneCerereSucHelper rand = new MentiuneCerereSucHelper(mentiuneCerereSuc);
                /*unicitate la MENTIUNE_CERERE_SUC*/
                if (xmlRands.contains(rand)) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.MENTIUNE_CERERE_SUC_DUBLICAT, mentiuneCerereSuc.getDataInregistrare(), mentiuneCerereSuc.getNrInregistrare());
                } else {
                    xmlRands.add(rand);
                }
                /*succesibil*/
                if (mentiuneCerereSuc.getSuccesibils() != null) {
                    for (Succesibil succesibil : mentiuneCerereSuc.getSuccesibils()) {
                        /*SUCCESIBIL.FK_NOM_TARA*/
                        NomTara nomTara = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomTara, succesibil.getAdresa().getNomTara().getCodNumeric(), dataRaportare);
                        if (nomTara == null) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomenclatorCodeType.NomTara.getLabel(), NomenclatorCodeType.NomTara.getCodeColumn(), succesibil.getAdresa().getNomTara().getCodNumeric(), dataRaportare);
                        }
                        succesibil.getAdresa().setNomTara(nomTara);
                        /*SUCCESIBIL.FK_NOM_JUDET*/
                        if (succesibil.getAdresa().getNomJudet() != null) {
                            NomJudet nomJudet = nomSrv.getNomenclatorForStringParam(NomJudet, succesibil.getAdresa().getNomJudet().getCodSiruta(), dataRaportare);
                            if (nomJudet == null) {
                                throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomJudet.getLabel(), NomJudet.getCodeColumn(), succesibil.getAdresa().getNomJudet().getCodSiruta(), dataRaportare);
                            }
                            succesibil.getAdresa().setNomJudet(nomJudet);
                        }
                        /*SUCCESIBIL.FK_NOM_UAT*/
                        if (succesibil.getAdresa().getNomUat() != null) {
                            NomUat nomUatAdresa = nomSrv.getNomenclatorForStringParam(NomUat, succesibil.getAdresa().getNomUat().getCodSiruta(), dataRaportare);
                            if (nomUatAdresa == null) {
                                throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomUat.getLabel(), NomUat.getCodeColumn(), succesibil.getAdresa().getNomUat().getCodSiruta(), dataRaportare);
                            }
                            succesibil.getAdresa().setNomUat(nomUatAdresa);
                        }
                        /*SUCCESIBIL.FK_NOM_LOCALITATE*/
                        if (succesibil.getAdresa().getNomLocalitate() != null) {
                            NomLocalitate nomLocalitate = nomSrv.getNomenclatorForStringParam(NomLocalitate, succesibil.getAdresa().getNomLocalitate().getCodSiruta(), dataRaportare);
                            if (nomLocalitate == null) {
                                throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomLocalitate.getLabel(), NomLocalitate.getCodeColumn(), succesibil.getAdresa().getNomLocalitate().getCodSiruta(), dataRaportare);
                            }
                            succesibil.getAdresa().setNomLocalitate(nomLocalitate);
                        }
                        //
                        validareCoduriSiruta(succesibil.getAdresa().getNomUat(), succesibil.getAdresa().getNomJudet(), succesibil.getAdresa().getNomLocalitate());

                        //
                        validareAdresaRenns(succesibil.getAdresa(), "succesibil");
                        //
                        validareAdresaCUAandReferintaGeoXml(succesibil.getAdresa(), "succesibil");
                        //PersoanaFizica
                        //validare CNP
                        if (succesibil.getPersoanaFizica().getCnp() != null &&
                                !CnpValidator.isValid(succesibil.getPersoanaFizica().getCnp())) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.CNP_INVALID, succesibil.getPersoanaFizica().getCnp());
                        }
                        //reutilizare PersoanaFizica
                        PersoanaFizica dbPersoanaFizica = new PersoanaFizica();
                        dbPersoanaFizica.setIdPersoanaFizica(reutilizareService.reutilizarePersoanaFizica(succesibil.getPersoanaFizica(), nomUatHeader.getId()));
                        succesibil.setPersoanaFizica(dbPersoanaFizica);
                    }
                }
                /*fkNomJudet*/
                mentiuneCerereSuc.setFkNomJudet(oldGospodarie.getNomUat().getNomJudet().getId());
                //PersoanaFizica
                //validare CNP
                if (mentiuneCerereSuc.getPersoanaFizica().getCnp() != null &&
                        !CnpValidator.isValid(mentiuneCerereSuc.getPersoanaFizica().getCnp())) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.CNP_INVALID, mentiuneCerereSuc.getPersoanaFizica().getCnp());
                }
                //reutilizare PersoanaFizica
                PersoanaFizica dbPersoanaFizica = new PersoanaFizica();
                dbPersoanaFizica.setIdPersoanaFizica(reutilizareService.reutilizarePersoanaFizica(mentiuneCerereSuc.getPersoanaFizica(), nomUatHeader.getId()));
                mentiuneCerereSuc.setPersoanaFizica(dbPersoanaFizica);
            }
        }
    }


    /**
     * @param ranDocDTO info trimse prin xml
     */
    public void addUpdateDateRegistru(RanDocDTO ranDocDTO) {
        Gospodarie oldGospodarie = gospodarieRepository.findByUatAndIdentificator(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie());
        List<MentiuneCerereSuc> oldMentiuneCerereSucs = mentiuneCerereSucRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        /*stergere intreg  capitol old*/
        if (oldMentiuneCerereSucs != null) {
            for (MentiuneCerereSuc oldMentiuneCerereSuc : oldMentiuneCerereSucs) {
                mentiuneCerereSucRepository.delete(oldMentiuneCerereSuc);
            }
        }
        /*adaugare intreg  capitol new*/
        if (ranDocDTO.getGospodarie().getMentiuneCerereSucs() != null) {
            for (MentiuneCerereSuc xmlMentiuneCerereSuc : ranDocDTO.getGospodarie().getMentiuneCerereSucs()) {
                if (xmlMentiuneCerereSuc.getSuccesibils() != null) {
                    for (Succesibil succesibil : xmlMentiuneCerereSuc.getSuccesibils()) {
                        succesibil.getAdresa().setDataStart(new Date());
                        adresaRepository.save(succesibil.getAdresa());
                    }
                }
                xmlMentiuneCerereSuc.setGospodarie(oldGospodarie);
                mentiuneCerereSucRepository.save(xmlMentiuneCerereSuc);
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
        List<MentiuneCerereSuc> oldMentiuneCerereSucs = mentiuneCerereSucRepository.findByGospodarieAndFkNomJudet(oldGospodarie.getIdGospodarie(), oldGospodarie.getNomUat().getNomJudet().getId());
        /*anulare intreg capitol*/
        if (oldMentiuneCerereSucs != null) {
            for (MentiuneCerereSuc oldMentiuneCerereSuc : oldMentiuneCerereSucs) {
                mentiuneCerereSucRepository.delete(oldMentiuneCerereSuc);
            }
        }
    }

    @Override
    public void populeazaDateCapitol(GospodarieDTO gospodarieDTO, Integer an, Byte semestru) {
        /*MENTIUNE_CERERE_SUC*/
        gospodarieDTO.setMentiuneCerereSucs(mentiuneCerereSucRepository.findByGospodarieAndFkNomJudet(gospodarieDTO.getGospodarie().getIdGospodarie(), gospodarieDTO.getGospodarie().getNomUat().getNomJudet().getId()));
    }

    @Override
    public Class getCapitolClass() {
        return Capitol_13.class;
    }
}
