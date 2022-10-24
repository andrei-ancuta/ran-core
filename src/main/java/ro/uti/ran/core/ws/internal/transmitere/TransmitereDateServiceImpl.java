package ro.uti.ran.core.ws.internal.transmitere;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.StructuralValidationException;
import ro.uti.ran.core.exception.TipTransmitereDateException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.model.portal.UATConfig;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.registru.view.ViewRegistruNomStare;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.repository.portal.UatConfigRepository;
import ro.uti.ran.core.repository.registru.GospodarieRepository;
import ro.uti.ran.core.service.backend.DateRegistruXmlParser;
import ro.uti.ran.core.service.backend.exception.SyntaxXmlException;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorService;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.service.registru.FluxRegistruService;
import ro.uti.ran.core.service.registru.RegistruService;
import ro.uti.ran.core.ws.WsUtilsService;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.model.transmitere.InformatiiTransmisie;
import ro.uti.ran.core.ws.model.transmitere.ModalitateProcesareDate;
import ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere;
import ro.uti.ran.core.ws.model.transmitere.StareTransmisie;
import ro.uti.ran.core.xml.model.AnRaportare;
import ro.uti.ran.core.xml.model.AnRaportareCentralizator;
import ro.uti.ran.core.xml.model.AnulareCapitol;
import ro.uti.ran.core.xml.model.RanDoc;
import ro.uti.ran.core.xml.model.capitol.*;
import ro.uti.ran.core.xml.model.capitol.nested.DateIdentificareGospodarie;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomTipDetinator;
import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomUat;

/**
 * Created by Stanciu Neculai on 13.Nov.2015.
 */
@Service
public class TransmitereDateServiceImpl implements TransmitereDateService {
    private static final Logger log = LoggerFactory.getLogger(TransmitereDateServiceImpl.class);

    @Autowired
    protected NomenclatorService nomSrv;
    @Autowired
    private RegistruService registruService;
    @Autowired
    private DateRegistruXmlParser dateRegistruXmlParser;
    @Autowired
    private GospodarieRepository gospodarieRepository;
    @Autowired
    private ParametruService parametruService;

    @Autowired
    @Qualifier("jmsMessageSender")
    private ProcesareDateRegistruAsync procesareDateRegistruAsync;

    @Autowired
    private FluxRegistruService fluxRegistruService;
    @Autowired
    private WsUtilsService wsUtilsService;
    @Autowired
    private UatConfigRepository uatConfigRepository;

    private ThreadLocal<Set<String>> gospodarii = new ThreadLocal<Set<String>>();

    /**
     * UAT din XML sa coincida cu UAT din autorizare
     * @param uatAuthorization uat extras din RanAuthorization
     * @param nomUatHeader uat extras din XML header
     * @throws DateRegistruValidationException
     */
    private void validareUatAuthorizationVsUatXml(UAT uatAuthorization, NomUat nomUatHeader) throws DateRegistruValidationException {
        if (uatAuthorization == null || !nomUatHeader.getCodSiruta().equals(uatAuthorization.getCodSiruta())) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.UAT_DIFERIT, uatAuthorization != null ? uatAuthorization.getCodSiruta() : "", nomUatHeader.getCodSiruta());
        }
    }

    public Long transmitere(String xmlCDATA, ModalitateTransmitere modalitateTransmitere, RanAuthorization ranAuthorization) throws RanBusinessException {

        try {
            if (ranAuthorization == null) {
                throw new IllegalArgumentException("Informatii autorizare nespecificate");
            }
            if (!wsUtilsService.checkIfRanAuthorizationValid(ranAuthorization)) {
                throw new IllegalArgumentException("Informatii autorizare incorecte");
            }
            UAT uat = wsUtilsService.getUatFrom(ranAuthorization);
            if (wsUtilsService.isUat(ranAuthorization)) {
                if (uat != null) {
                    UATConfig uatConfig = uatConfigRepository.findByUat_Id(uat.getId());
                    if (null != uatConfig && uatConfig.isTransmitereManuala() && !modalitateTransmitere.equals(ModalitateTransmitere.MANUAL)) {
                        throw new TipTransmitereDateException("Modalitatea de tranmistere trebuie sa fie MANUAL cand este selectata optiunea de " +
                                "transmitere manuala.");
                    }
                }
            }

            String xml = xmlCDATA;
            RanDoc ranDoc = dateRegistruXmlParser.getPojoFromXML(xml);
            Object gospodarieSauRaportare = ranDoc.getBody().getGospodarieSauRaportare();

            //Creez un registru nou si populez atributele comune atat pentru gospodarie cat si pentru rapoartele centralizate
            Registru registru = new Registru();
            registru.setDataRegistru(new Date());
            registru.setDataExport(ranDoc.getHeader().getDataExport());
            registru.setContinut(xml);
            IndicativXml indicativXml;
            switch (ranDoc.getHeader().getIndicativ()) {
                case ADAUGA_SI_INLOCUIESTE:
                    indicativXml = IndicativXml.ADAUGA_SI_INLOCUIESTE;
                    break;
                case ANULEAZA:
                    indicativXml = IndicativXml.ANULEAZA;
                    break;
                case DEZACTIVARE_GOSPODARIE:
                    indicativXml = IndicativXml.DEZACTIVARE;
                    break;
                case REACTIVARE_GOSPODARIE:
                    indicativXml = IndicativXml.REACTIVARE;
                    break;
                default:
                    throw new DateRegistruValidationException("TipIndicativ la transmitere date poate fi doar 'ADAUGA_SI_INLOCUIESTE', 'ANULEAZA', 'DEZACTIVARE_GOSPODARIE' sau 'REACTIVARE_GOSPODARIE'.");
            }

            registru.setNomIndicativXml(
                    nomSrv.<NomIndicativXml>getNomenclatorForStringParam(
                            NomenclatorCodeType.NomIndicativXml,
                            indicativXml.getCod()
                    )
            );

            registru.setNomStareRegistru(
                    nomSrv.<NomStareRegistru>getNomenclatorForStringParam(
                            NomenclatorCodeType.NomStareRegistru,
                            "R"
                    )
            );

            registru.setNomSursaRegistru(
                    nomSrv.<NomSursaRegistru>getNomenclatorForStringParam(
                            NomenclatorCodeType.NomSursaRegistru,
                            "UAT"
                    )
            );

            registru.setModalitateTransmitere(modalitateTransmitere);
            registru.setIsRecipisaSemnata(false);

            if (indexExists(ranDoc.getHeader().getCodXml().getValue().toLowerCase())) {
                throw new DateRegistruValidationException("Indexul asociat acestui fisier xml exista deja.");
            } else {
                registru.setIndexRegistru(ranDoc.getHeader().getCodXml().getValue().toLowerCase());
            }


            Integer an = null;
            String codCapitol = null;
            Integer semestru = null;

            if (gospodarieSauRaportare instanceof AnRaportareCentralizator) {
                Object capitolCentralizatorCuAnRaportare = ((AnRaportareCentralizator) gospodarieSauRaportare).getCapitolCentralizatorCuAnRaportare();
                codCapitol = getCodCapitolFromCapitolSauAnulare(capitolCentralizatorCuAnRaportare);

                an = ((AnRaportareCentralizator) gospodarieSauRaportare).getAn();

                /*Header - NOM_UAT*/
                NomUat nomUatHeader = nomSrv.getNomenclatorForStringParam(NomUat, ranDoc.getHeader().getSirutaUAT(), new DataRaportareValabilitate(an));
                if (nomUatHeader == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomUat.getLabel(), NomUat.getCodeColumn(), ranDoc.getHeader().getSirutaUAT(), ranDoc.getHeader().getDataExport());
                }
                validareUatAuthorizationVsUatXml(uat, nomUatHeader);
                registru.setNomUat(nomUatHeader);

            } else if (gospodarieSauRaportare instanceof ro.uti.ran.core.xml.model.Gospodarie) {

                ro.uti.ran.core.xml.model.Gospodarie gospodarieElement = (ro.uti.ran.core.xml.model.Gospodarie) gospodarieSauRaportare;
                Object unitateGospodarie = gospodarieElement.getUnitateGospodarie();
                if (unitateGospodarie instanceof AnRaportare) {
                    codCapitol = getCodCapitolFromCapitolSauAnulare(((AnRaportare) unitateGospodarie).getCapitolCuAnRaportare());
                    an = ((AnRaportare) unitateGospodarie).getAn();
                    semestru = getSemestruFromCapitolSauAnulare(((AnRaportare) unitateGospodarie).getCapitolCuAnRaportare());
                } else {
                    codCapitol = getCodCapitolFromCapitolSauAnulare(unitateGospodarie);
                }
                DataRaportareValabilitate dataRaportareValabilitate = new DataRaportareValabilitate();
                if (an != null && semestru != null) {
                    dataRaportareValabilitate = new DataRaportareValabilitate(an, semestru);
                }
                if (an != null && semestru == null) {
                    dataRaportareValabilitate = new DataRaportareValabilitate(an);
                }

                boolean isFirstChapter = unitateGospodarie instanceof Capitol_0_12 || unitateGospodarie instanceof Capitol_0_34;

                String identificatorGospodarie = gospodarieElement.getIdentificator();

                String gospodarieKey = ranDoc.getHeader().getSirutaUAT() + "_" + identificatorGospodarie;
                Gospodarie gospodarie = null;
                if (isFirstChapter) {
                    if (gospodarii.get() == null) {
                        gospodarii.set(new HashSet<String>());
                    }

                    gospodarii.get().add(gospodarieKey);

                } else {

                    if (gospodarii.get() == null || !gospodarii.get().contains(gospodarieKey)) {
                        gospodarie = gospodarieRepository.findByUatAndIdentificator(ranDoc.getHeader().getSirutaUAT(), identificatorGospodarie);
                        if (null == gospodarie) {
                            throw new DateRegistruValidationException("Gospodarie inexistent&#259; cu identificator " + identificatorGospodarie + " &#351;i cod sirut&#259; UAT " + ranDoc.getHeader().getSirutaUAT() + "." +
                                    "Trebuie sa se transmit&#259; anterior unul dintre capitolele: " +
                                    "Capitol_0_12 sau Capitol_0_34 pentru a transmite apoi alte capitole RAN.");
                        }
                        registru.setGospodarie(gospodarie);
                    }

                }

                // TODO: conventional indentificatorul de gospodarie este ZERO
                registru.setIdentificatorGospodarie(
                        ranDoc.getBody().getGospodarieSauRaportare() instanceof ro.uti.ran.core.xml.model.Gospodarie ?
                                ((ro.uti.ran.core.xml.model.Gospodarie) ranDoc.getBody().getGospodarieSauRaportare()).getIdentificator() : "0"
                );

                if (isFirstChapter) {
                    DateIdentificareGospodarie dateIdentificareGospodarie =
                            unitateGospodarie instanceof Capitol_0_12 ?
                                    ((Capitol_0_12) unitateGospodarie).getRandCapitol() :
                                    ((Capitol_0_34) unitateGospodarie).getRandCapitol();

                    NomTipDetinator nomTipDetinator = nomSrv.getNomenclatorForStringParam(NomTipDetinator, dateIdentificareGospodarie.getTipDetinator().toString(), dataRaportareValabilitate);
                    if (nomTipDetinator == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomTipDetinator.getLabel(), NomTipDetinator.getCodeColumn(), dateIdentificareGospodarie.getTipDetinator(), ranDoc.getHeader().getDataExport());
                    }

                    NomUat nomUatHeader = nomSrv.getNomenclatorForStringParam(NomUat, ranDoc.getHeader().getSirutaUAT(), dataRaportareValabilitate);
                    if (nomUatHeader == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomUat.getLabel(), NomUat.getCodeColumn(), ranDoc.getHeader().getSirutaUAT(), ranDoc.getHeader().getDataExport());
                    }
                    validareUatAuthorizationVsUatXml(uat, nomUatHeader);
                    registru.setNomUat(nomUatHeader);

                } else {

                    if (gospodarie != null) {
                        registru.setNomUat(gospodarie.getNomUat());
                    } else {

                        NomUat nomUatHeader = nomSrv.getNomenclatorForStringParam(NomUat, ranDoc.getHeader().getSirutaUAT(), dataRaportareValabilitate);
                        if (nomUatHeader == null) {
                            throw new DateRegistruValidationException(DateRegistruValidationCodes.NOMENCLATOR_RECORD_NOT_FOUND_AT_DATE, NomUat.getLabel(), NomUat.getCodeColumn(), ranDoc.getHeader().getSirutaUAT(), ranDoc.getHeader().getDataExport());
                        }
                        validareUatAuthorizationVsUatXml(uat, nomUatHeader);
                        registru.setNomUat(nomUatHeader);
                    }

                }

            }


            if (null == codCapitol) {
                throw new IllegalArgumentException("codCapitol nelocalizat in fisierul xml transmis");
            }
            NomCapitol nomCapitol = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomCapitol, codCapitol, new DataRaportareValabilitate());
            if (nomCapitol == null) {
                throw new DateRegistruValidationException("Nu exista Capitol cu codul: " + codCapitol);
            }
            registru.setNomCapitol(nomCapitol);

            // AN raportare
            registru.setAn(an);
            //SEMESTRU raportare
            if (TipCapitol.CAP7.equals(registru.getNomCapitol().getCod()) || TipCapitol.CAP8.equals(registru.getNomCapitol().getCod())) {
                registru.setSemestru(semestru);
            }
            //
            // Salvare registru

            //
            registru.setFkNomJudet(registru.getNomUat().getNomJudet().getId());
            //
            registru = registruService.saveRegistru(registru);

            actualizareRegistru(registru, RanConstants.STARE_REGISTRU_RECEPTIONATA_COD, null);
            // numai daca tipul de transmitere este asynch facem operatia tranzactional (adica punerea in coada este tranzactionala cu transmiterea -> punerea in coada este garantata)
            // totusi punerea in coada nu este chiar obligatorie pentru ca oricum job-urile de procesare vor fi preluate de catre un scheduler care le va procesa la fiecare final de zi
            ModalitateProcesareDate modalitateProcesareDate = getTipTransmitereFromParamConfig();
            if (ModalitateProcesareDate.ASYNCHRONOUS == modalitateProcesareDate) {
                procesareDateRegistruAsync.procesareDateRegistru(registru.getIdRegistru());
            }

            return registru.getIdRegistru();
        } catch (SyntaxXmlException e) {
            throw new StructuralValidationException(e);
        } catch (RanBusinessException e) {
            throw e;
        } catch (Throwable e) {
            // TODO: aici nu inteleg ce trebuie sa se intample?...daca este nevalid XML-ul structural nu returnam RanBusinessException?
            throw new DateRegistruValidationException(e);
        }
    }


    private void actualizareRegistru(Registru registru, String stareRegistruCod, Throwable ex) {
        /*FK_NOM_STARE_REGISTRU*/
        NomStareRegistru nomStareRegistru = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomStareRegistru, stareRegistruCod);
        registru.setNomStareRegistru(nomStareRegistru);
        //
        Registru savedRegistru = registruService.saveRegistru(registru);
        if (ex != null) {
            if (ex instanceof RanBusinessException) {
                fluxRegistruService.saveFluxRegistru(savedRegistru, nomStareRegistru, fluxRegistruService.extractRespinsMesaj((RanBusinessException) ex));
            } else {
                fluxRegistruService.saveFluxRegistru(savedRegistru, nomStareRegistru, ex.getMessage());
            }
        } else {
            fluxRegistruService.saveFluxRegistru(savedRegistru, nomStareRegistru, null);
        }
    }

    private boolean indexExists(String codXml) {
        ViewRegistruNomStare registru = registruService.getByIndexRegistruOrNull(codXml);
        if (registru != null) {
            return true;
        } else {
            return false;
        }
    }

    private String getCodCapitolFromCapitolSauAnulare(Object capitolSauAnulare) {
        if (capitolSauAnulare instanceof Capitol) {
            Capitol capitol = (Capitol) capitolSauAnulare;
            return capitol.getCodCapitol();
        } else if (capitolSauAnulare instanceof AnulareCapitol) {
            AnulareCapitol anumare = (AnulareCapitol) capitolSauAnulare;
            return anumare.getCodCapitol();
        } else {
            return null;
        }
    }

    private Integer getSemestruFromCapitolSauAnulare(Object capitolSauAnulare) {
        if (capitolSauAnulare instanceof Capitol_7) {
            Capitol_7 capitol = (Capitol_7) capitolSauAnulare;
            return capitol.getSemestru();
        } else if (capitolSauAnulare instanceof Capitol_8) {
            Capitol_8 capitol = (Capitol_8) capitolSauAnulare;
            return capitol.getSemestru();
        } else if (capitolSauAnulare instanceof AnulareCapitol) {
            AnulareCapitol anumare = (AnulareCapitol) capitolSauAnulare;
            return anumare.getSemestru();
        } else {
            return null;
        }
    }

    @Override
    public String getTransmitereXsdSchema(RanAuthorization ranAuthorization) throws RanBusinessException {
        try {
            Resource resource = dateRegistruXmlParser.getRanDocXsdSchema();
            String xsd = new String(IOUtils.toByteArray(resource.getInputStream()), "UTF-8");
            return xsd;
        } catch (Throwable t) {
            throw new DateRegistruValidationException(t);
        }
    }

    @Override
    public InformatiiTransmisie getStatusTransmisie(String uuidTransmisie, RanAuthorization ranAuthorization) throws
            RanBusinessException {
        ViewRegistruNomStare registru = registruService.getByIndexRegistruOrThrow(uuidTransmisie);
        InformatiiTransmisie informatiiTransmisie = new InformatiiTransmisie();

        informatiiTransmisie.setStareTransmisie(StareTransmisie.getByStareRegistru(registru.getCod()));
        informatiiTransmisie.setRecipisa(registru.getRecipisa());
        informatiiTransmisie.setIsRecipisaSemnata(registru.getIsRecipisaSemnata());

        return informatiiTransmisie;
    }

    private ModalitateProcesareDate getTipTransmitereFromParamConfig() {
        Parametru parametru = parametruService.getParametru(ParametruService.PARAM_CONFIG_TIP_TRANS_COD);
        Boolean isAsync = Boolean.valueOf(parametru.getValoare());
        if (parametru.getValoare() != null && !parametru.getValoare().isEmpty() && isAsync != null) {
            if (isAsync) {
                return ModalitateProcesareDate.ASYNCHRONOUS;
            } else {
                return ModalitateProcesareDate.SYNCHRONOUS;
            }
        } else {
            //valoarea implicita ASYNCHRONOUS
            return ModalitateProcesareDate.ASYNCHRONOUS;
        }
    }
}
