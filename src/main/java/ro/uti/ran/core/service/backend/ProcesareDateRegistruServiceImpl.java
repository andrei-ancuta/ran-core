package ro.uti.ran.core.service.backend;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.business.scheduler.annotations.Cluster;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.StructuralValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.exception.codes.DateRegistruValidationCodes;
import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.model.registru.*;
import ro.uti.ran.core.model.registru.view.ViewUatGospodarie;
import ro.uti.ran.core.model.utils.IndicativXml;
import ro.uti.ran.core.service.backend.capitol.CapitolBaseService;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.exception.SyntaxXmlException;
import ro.uti.ran.core.service.backend.jaxb.RanDocConversionHelper;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorService;
import ro.uti.ran.core.service.backend.renns.AsyncUpdateFromRenns;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.service.backend.view.ViewUatGospodarieService;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.service.registru.RegistruService;
import ro.uti.ran.core.xml.model.RanDoc;
import ro.uti.ran.core.xml.model.capitol.Capitol_0_12;
import ro.uti.ran.core.xml.model.capitol.Capitol_0_34;
import ro.uti.ran.core.xml.model.types.TipIndicativ;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import static ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType.NomUat;

/**
 * Acest back-end este responsabil de procesarea mesajelor din coada de procesare.
 */
@Service("procesareDateRegistruService")
public class ProcesareDateRegistruServiceImpl implements ProcesareDateRegistruService {

    private static final Logger logger = LoggerFactory.getLogger(ProcesareDateRegistruServiceImpl.class);
    @Autowired
    private Environment env;

    @Autowired
    private NomenclatorService nomSrv;

    @Autowired
    private DateRegistruXmlParser dateRegistruXmlParser;

    @Autowired
    private RegistruService registruService;

    @Autowired
    private ParametruService parametruService;

    @Autowired
    private ViewUatGospodarieService viewUatGospodarieService;

    @Autowired
    private AsyncUpdateFromRenns asyncUpdateFromRenns;

    @Autowired
    private GospodarieService gospodarieService;

    private Map<String, CapitolBaseService> capitolServiceMap = new HashMap<String, CapitolBaseService>();


    @Autowired
    public ProcesareDateRegistruServiceImpl(List<? extends CapitolBaseService> capitolServices) {
        for (CapitolBaseService capitolService : capitolServices) {
            this.capitolServiceMap.put(capitolService.getCapitolClass().getName(), capitolService);
        }
    }

    @Cluster
    @Scheduled(cron = "0 0 22 ? * MON-FRI")
    @Async("reProcesareDateRegistru")
    public void reProcesareDateRegistru() throws InterruptedException {
        logger.info("Rulare job reProcesareDateRegistru");
        List<Registru> registruData = registruService.getAllUnprocessedErrorData();
        for(Registru registru : registruData) {

            Thread.sleep(2000);

            try {
                procesareDateRegistru(registru.getIdRegistru());
            }catch(Exception e){
                logger.error("eroare reProcesareDateRegistru procesareDateRegistru",e);
            }
        }
    }


    /**
     * In timpul procesarii pot avea 2 tipuri mari de erori:
     * <p>Erori datorate bug-urilor care pot aparea la runtime; nu fac nimic rollback daca am pus ceva in baza;</p>
     * <p>Erori deoarece xml-ul nu respecta xsd-ul sau erori de validare business; marchez recipisa de raspuns cu "invalid, mesaj neprocesat" plus erori</p>
     *
     * @param idRegistru id inregistrare din baza de date
     */
    @Override
    public void procesareDateRegistru(Long idRegistru) throws RanBusinessException {
        if (idRegistru == null) {
            throw new IllegalArgumentException("ID Registru nedefinit!");
        }
        Registru registru = null;
        RanDoc ranDoc = null;
        RanDocDTO ranDocDTO = new RanDocDTO();
        try {
            registru = registruService.getRegistruById(idRegistru);
            if (registru == null) {
                throw new IllegalArgumentException("Nu exista Registru pentru ID = " + idRegistru);
            }
            /*Extragerea datelor din XML*/
            ranDoc = dateRegistruXmlParser.getPojoFromXML(registru.getContinut());
            RanDocConversionHelper.populeazaFromSchemaType(ranDoc, ranDocDTO);
            /*Dezactivare/Reactivare*/
            if (ranDocDTO.getIsDezactivare() || ranDocDTO.getIsReactivare()) {
                TipCapitol tipCapitol = TipCapitol.valueOf(ranDocDTO.getCodCapitol());
                switch (tipCapitol) {
                    case CAP0_12:
                        ranDocDTO.setClazz(Capitol_0_12.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP0_12);
                        break;
                    case CAP0_34:
                        ranDocDTO.setClazz(Capitol_0_34.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP0_34);
                        break;
                    default:
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.DEZ_RE_ACTIVARE_CODCAPITOL_NECORESPUNZATOR, ranDocDTO.getIsDezactivare() ? "dezactivareGospodarie" : "reactivareGospodarie");
                }
            }
            CapitolBaseService capitolService = getCapitolService(ranDocDTO);
            /*Aplicarea regulilor de validare*/
            valideaza(ranDocDTO, registru.getNomUat(), registru.getDataRegistru(), capitolService, false);
            /*Salvarea datelor din mesajele valide în baza de date relațională RAN*/
            capitolService.salveazaDateRegistru(ranDocDTO);
            /*Actualizare asincrona, daca este cazul, RENNS*/
            actualizareFromRenns(ranDocDTO);
            /*Actualizarea registrului de transmisii cu "recipisa de răspuns" care are semnificația de "valid, mesaj procesat"*/
            registruService.actualizareRegistruCazSucces(idRegistru, ranDocDTO, getSuccesAsHtml(ranDocDTO.getCodXml().toString()));
        } catch (SyntaxXmlException e) {
            /* la interogare nu se valideaza xsd caz exceptional pentru date istorice
               nu ar trebui sa apara din motive ca xml transmis se valideaza la transmitre
               si in caz de erori nu se inscrie in registru. Comlectat pentru evitarea NullPointer
             */
            if (ranDoc == null) {
                String message = "";
                for (String errorText : e.getLstErrorDescription()) {
                    message += errorText + "\n";
                }
                throw new IllegalArgumentException("Eroare validare registru ID- " + idRegistru + " ( \n" + message + " )");
            }
            /*Actualizarea registrului de transmisii cu "recipisa de răspuns" care are semnificația de "invalid, mesaj neprocesat". Aceasta conține și erorile.*/
            registruService.actualizareRegistruCazEroare(idRegistru, ranDocDTO, e, getErrorAsHtml(e, ranDocDTO.getCodXml().toString(), false));
            logErrorAsHtml(idRegistru, registru.getIdentificatorGospodarie(), e, registru.getNomCapitol() != null ? registru.getNomCapitol().getCod().name() : "unknown", ranDocDTO.getCodXml().toString());
            throw new StructuralValidationException(e);
        } catch (DateRegistruValidationException e) {
            /*Actualizarea registrului de transmisii cu "recipisa de răspuns" care are semnificația de "invalid, mesaj neprocesat". Aceasta conține și erorile.*/
            registruService.actualizareRegistruCazEroare(idRegistru, ranDocDTO, e, getErrorAsHtml(e, ranDocDTO.getCodXml().toString(), false));
            logErrorAsHtml(idRegistru, registru.getIdentificatorGospodarie(), e, registru.getNomCapitol() != null ? registru.getNomCapitol().getCod().name() : "unknown", ranDocDTO.getCodXml().toString());
            throw e;
        } catch (Throwable t) {
            Throwable e = ExceptionUtils.getRootCause(t);
            /*Actualizarea registrului de transmisii cu "recipisa de răspuns" care are semnificația de "invalid, mesaj neprocesat". Aceasta conține și erorile.*/
            if (registru != null) {
                registruService.actualizareRegistruCazEroare(idRegistru, ranDocDTO, e, getErrorAsHtml(e, ranDocDTO.getCodXml().toString(), false));
            }
            logErrorAsHtml(
                    idRegistru,
                    registru != null ? registru.getIdentificatorGospodarie() : "unknown",
                    e,
                    (null != registru && registru.getNomCapitol() != null) ? registru.getNomCapitol().getCod().name() : "unknown", ranDocDTO.getCodXml().toString()
            );

            if (e instanceof RanBusinessException) {
                throw (RanBusinessException) e;
            } else {
                throw new DateRegistruValidationException(e);
            }
        }
    }

    @Override
    public void procesareDateXml(String xml, Long idNomUat, boolean local) throws RanBusinessException {
        RanDoc ranDoc = null;
        RanDocDTO ranDocDTO = new RanDocDTO();
        try {
            NomUat nomUat = nomSrv.getNomenlatorForId(NomUat, idNomUat);

            /*Extragerea datelor din XML*/
            ranDoc = dateRegistruXmlParser.getPojoFromXML(xml);
            RanDocConversionHelper.populeazaFromSchemaType(ranDoc, ranDocDTO);
            /*Dezactivare/Reactivare*/
            if (ranDocDTO.getIsDezactivare() || ranDocDTO.getIsReactivare()) {
                TipCapitol tipCapitol = TipCapitol.valueOf(ranDocDTO.getCodCapitol());
                switch (tipCapitol) {
                    case CAP0_12:
                        ranDocDTO.setClazz(Capitol_0_12.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP0_12);
                        break;
                    case CAP0_34:
                        ranDocDTO.setClazz(Capitol_0_34.class.getName());
                        ranDocDTO.setTipCapitol(TipCapitol.CAP0_34);
                        break;
                    default:
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.DEZ_RE_ACTIVARE_CODCAPITOL_NECORESPUNZATOR, ranDocDTO.getIsDezactivare() ? "dezactivareGospodarie" : "reactivareGospodarie");
                }
            }
            CapitolBaseService capitolService = getCapitolService(ranDocDTO);
            /*Aplicarea regulilor de validare*/
            valideaza(ranDocDTO, nomUat, new Date(), capitolService, local);
            /*Salvarea datelor din mesajele valide în baza de date relațională RAN*/
            capitolService.salveazaDateRegistru(ranDocDTO);
            /*Actualizarea registrului de transmisii cu "recipisa de răspuns" care are semnificația de "valid, mesaj procesat"*/
        } catch (SyntaxXmlException e) {
            /* la interogare nu se valideaza xsd caz exceptional pentru date istorice
               nu ar trebui sa apara din motive ca xml transmis se valideaza la transmitre
               si in caz de erori nu se inscrie in registru. Comlectat pentru evitarea NullPointer
             */
            if (ranDoc == null) {
                String message = "";
                for (String errorText : e.getLstErrorDescription()) {
                    message += errorText + "\n";
                }
                throw new IllegalArgumentException("Eroare validare la transmitere date ( \n" + message + " )");
            }
            throw new StructuralValidationException(e);
        } catch (Throwable e) {
            if (e instanceof RanBusinessException) {
                throw (RanBusinessException) e;
            } else {
                throw new DateRegistruValidationException(e);
            }
        }
    }

    /**
     * Validari generale; altele fata de cele specifice fiecarui capitol (vezi si  capitolService.valideazaDateRegistru)
     *
     * @param ranDocDTO      dto
     * @param dataTransmisie dataTransmisie
     * @param capitolService service
     * @throws DateRegistruValidationException
     */
    private void valideaza(RanDocDTO ranDocDTO, NomUat nomUat, Date dataTransmisie, CapitolBaseService capitolService, boolean local) throws DateRegistruValidationException {

        validareAnRaportare(ranDocDTO);
        logger.info("S-a facut validarea de an pentru registrul cu indexul: " + ranDocDTO.getCodXml());
        /*codCapitol corespunzator cu XML-ul trimis*/
        if (!ranDocDTO.getTipCapitol().name().equalsIgnoreCase(ranDocDTO.getCodCapitol())) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.COD_CAPITOL_INVALID, ranDocDTO.getCodCapitol(), ranDocDTO.getTipCapitol().name());
        }
        logger.info("S-a facut validarea de cod capitol pentru registrul cu indexul: " + ranDocDTO.getCodXml());
        /*UAT care trimite datele corespunzator la UAT din XML*/
        if (nomUat != null && !nomUat.getCodSiruta().equals(ranDocDTO.getSirutaUAT())) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.UAT_DIFERIT, nomUat.getCodSiruta(), ranDocDTO.getSirutaUAT());
        }
        logger.info("S-a facut validarea de codSiruta pentru registrul cu indexul: " + ranDocDTO.getCodXml());
        /*validare HEADER.indicativ vs continut BODY*/
        if (IndicativXml.ANULEAZA.equals(ranDocDTO.getIndicativ()) &&
                ranDocDTO.getAnulareDTO() == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.INDICATIV_HEADER_BODY_NECORESPUNZATOR, TipIndicativ.ANULEAZA.name());
        }
        if (IndicativXml.ANULEAZA.equals(ranDocDTO.getIndicativ())) {
            validareAnSemestruLaAnulare(ranDocDTO);
        }
        if (IndicativXml.DEZACTIVARE.equals(ranDocDTO.getIndicativ()) &&
                !ranDocDTO.getIsDezactivare()) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.INDICATIV_HEADER_BODY_NECORESPUNZATOR, TipIndicativ.DEZACTIVARE_GOSPODARIE.name());
        }
        if (IndicativXml.REACTIVARE.equals(ranDocDTO.getIndicativ()) &&
                !ranDocDTO.getIsReactivare()) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.INDICATIV_HEADER_BODY_NECORESPUNZATOR, TipIndicativ.REACTIVARE_GOSPODARIE.name());
        }
        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ()) &&
                (ranDocDTO.getAnulareDTO() != null || ranDocDTO.getIsDezactivare() || ranDocDTO.getIsReactivare())) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.INDICATIV_HEADER_BODY_NECORESPUNZATOR, TipIndicativ.ADAUGA_SI_INLOCUIESTE.name());
        }
        logger.info("S-a facut validarea de indicativ xml pentru registrul cu indexul: " + ranDocDTO.getCodXml());
        /*validare nu se pot transmite capitole pentru o gospodarie dezactivata*/
        validareActivInactivGospodarie(ranDocDTO);
        // if (!local) {
        /*validare Numarul total de declaratii pe gospodarie depuse la nivel de UAT in anul NNNN*/
        //blocarea validareNrTotalGospodarii temporar
        //validareNrTotalGospodarii(ranDocDTO, nomUat);
        //}
        /*validare ADAUGA_SI_MODIFICA: limitarea posibilitatii transmiterii de date aferente anului anterior de la UAT-uri pana la o anumita data din anul curent*/
//        if (IndicativXml.ADAUGA_SI_INLOCUIESTE.equals(ranDocDTO.getIndicativ())) {
//            validareAnRaportareVsDataLimitaParam(ranDocDTO, dataTransmisie);
//        }
        //



        capitolService.valideazaDateRegistru(ranDocDTO);
    }


    private void validareAnRaportare(RanDocDTO ranDocDTO) throws DateRegistruValidationException {

        List<String> capList = new ArrayList<>();
        capList.add("CAP2a");
        capList.add("CAP2b");
        capList.add("CAP3");
        capList.add("CAP4a");
        capList.add("CAP4a1");
        capList.add("CAP4b1");
        capList.add("CAP4b2");
        capList.add("CAP4c");
        capList.add("CAP5a");
        capList.add("CAP5b");
        capList.add("CAP5c");
        capList.add("CAP5d");
        capList.add("CAP6");
        capList.add("CAP7");
        capList.add("CAP8");
        capList.add("CAP9");
        capList.add("CAP10a");
        capList.add("CAP10b");
        capList.add("CAP11");

        if (capList.contains(ranDocDTO.getCodCapitol()) && ranDocDTO.getAnRaportare() == null) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.AN_RAPORTARE_NEDEFINIT);
        }

    }

    /**
     * an/luna/zi/gospodarie/capitol/idRegistru.txt
     */
    /**
     * @param idRegistru id inregistrare din baza de date
     * @param ex         exceptia
     */
    private void logErrorAsHtml(Long idRegistru, String identificatorGospodarie, Throwable ex, String codCapitol, String uuidTransmisie) {
        String htmlError = getErrorAsHtml(ex, uuidTransmisie, true);
        //an/luna/zi/gospodarie/capitol/idRegistru.txt
        Calendar now = Calendar.getInstance();
        StringBuilder path = new StringBuilder();
        path.append(env.getProperty("log.dir"));
        path.append("/").append(now.get(Calendar.YEAR));
        path.append("/").append(now.get(Calendar.MONTH) + 1);
        path.append("/").append(now.get(Calendar.DAY_OF_MONTH));
        path.append("/").append(identificatorGospodarie);
        path.append("/").append(codCapitol);
        path.append("/").append(now.get(Calendar.HOUR_OF_DAY)).append("_").append(now.get(Calendar.MINUTE));
        path.append("/").append(idRegistru).append(".html");
        try {
            File file = new File(path.toString());
            FileUtils.writeStringToFile(file, htmlError);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        logger.error(htmlError, ex);
    }

    /**
     * @param ranDocDTO dto
     */
    private void actualizareFromRenns(RanDocDTO ranDocDTO) {

        if (TipCapitol.CAP0_12.equals(ranDocDTO.getTipCapitol()) ||
                TipCapitol.CAP0_34.equals(ranDocDTO.getTipCapitol())) {
            if (ranDocDTO.getGospodarie() != null && ranDocDTO.getGospodarie().getAdresaGospodaries() != null) {
                for (AdresaGospodarie adresaGospodarie : ranDocDTO.getGospodarie().getAdresaGospodaries()) {
                    if (adresaGospodarie.getAdresa().getNomJudet() != null && StringUtils.isNotEmpty(adresaGospodarie.getAdresa().getUidRenns())) {
                        asyncUpdateFromRenns.updateFromRenns(adresaGospodarie.getAdresa().getUidRenns(), adresaGospodarie.getAdresa().getNomJudet().getDenumire());
                    }
                }
            }
        }

        if (TipCapitol.CAP11.equals(ranDocDTO.getTipCapitol())) {
            if (ranDocDTO.getGospodarie() != null && ranDocDTO.getGospodarie().getCladires() != null) {
                for (Cladire cladire : ranDocDTO.getGospodarie().getCladires()) {
                    if (cladire.getAdresa() != null && cladire.getAdresa().getNomJudet() != null && StringUtils.isNotEmpty(cladire.getAdresa().getUidRenns())) {
                        asyncUpdateFromRenns.updateFromRenns(cladire.getAdresa().getUidRenns(), cladire.getAdresa().getNomJudet().getDenumire());
                    }
                }
            }
        }

        if (TipCapitol.CAP13.equals(ranDocDTO.getTipCapitol())) {
            if (ranDocDTO.getGospodarie() != null && ranDocDTO.getGospodarie().getMentiuneCerereSucs() != null) {
                for (MentiuneCerereSuc mentiuneCerereSuc : ranDocDTO.getGospodarie().getMentiuneCerereSucs()) {
                    if (mentiuneCerereSuc.getSuccesibils() != null) {
                        for (Succesibil succesibil : mentiuneCerereSuc.getSuccesibils()) {
                            if (succesibil.getAdresa() != null && succesibil.getAdresa().getNomJudet() != null && StringUtils.isNotEmpty(succesibil.getAdresa().getUidRenns())) {
                                asyncUpdateFromRenns.updateFromRenns(succesibil.getAdresa().getUidRenns(), succesibil.getAdresa().getNomJudet().getDenumire());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Pentru o gospodarie dezactivata se poate trimite doar indicativ Reactivare
     *
     * @param ranDocDTO dto
     * @throws DateRegistruValidationException
     */
    private void validareActivInactivGospodarie(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        if (!IndicativXml.REACTIVARE.equals(ranDocDTO.getIndicativ()) &&
                !gospodarieService.isGospodarieActiva(ranDocDTO.getSirutaUAT(), ranDocDTO.getIdentificatorGospodarie())) {
            throw new DateRegistruValidationException(DateRegistruValidationCodes.GOSPODARIE_DEZACTIVATA, TipIndicativ.REACTIVARE_GOSPODARIE.name());
        }
    }

    /**
     * La anulare pentru anumite capitole trebuie transmis an/semestru raportare
     *
     * @param ranDocDTO dto
     * @throws DateRegistruValidationException
     */
    private void validareAnSemestruLaAnulare(RanDocDTO ranDocDTO) throws DateRegistruValidationException {
        switch (ranDocDTO.getTipCapitol()) {
            case CAP0_12:
            case CAP0_34:
            case CAP1:
            case CAP12:
            case CAP13:
            case CAP14:
            case CAP15a:
            case CAP15b:
            case CAP16:
                if (ranDocDTO.getAnulareDTO() == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.INDICATIV_HEADER_BODY_NECORESPUNZATOR, TipIndicativ.ANULEAZA.name());
                }
                break;
            case CAP2a:
            case CAP2b:
            case CAP3:
            case CAP4a:
            case CAP4a1:
            case CAP4b1:
            case CAP4b2:
            case CAP4c:
            case CAP5a:
            case CAP5b:
            case CAP5c:
            case CAP5d:
            case CAP6:
            case CAP9:
            case CAP10a:
            case CAP10b:
            case CAP11:
                if (ranDocDTO.getAnulareDTO().getAnRaportare() == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.ANULARE_CAP_AN, ranDocDTO.getTipCapitol().name());
                }
                break;
            case CAP7:
            case CAP8:
                if (ranDocDTO.getAnulareDTO().getAnRaportare() == null || ranDocDTO.getAnulareDTO().getSemestruRaportare() == null) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.ANULARE_CAP_SEMESTRU_AN, ranDocDTO.getTipCapitol().name());
                }
                break;
        }
    }

    /**
     * @param ranDocDTO      dto
     * @param dataTransmisie dataTransmisie
     * @throws DateRegistruValidationException
     */
    private void validareAnRaportareVsDataLimitaParam(RanDocDTO ranDocDTO, Date dataTransmisie) throws DateRegistruValidationException {
        Date dataLimitaParam = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            String tmp = getParamValue(ParametruService.DATA_LIMITARE_ANRAPORTARE);
            if (tmp != null) {
                dataLimitaParam = sdf.parse(tmp.concat(".").concat(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        switch (ranDocDTO.getTipCapitol()) {
            case CAP2a:
            case CAP2b:
            case CAP3:
            case CAP4a:
            case CAP4a1:
            case CAP4b1:
            case CAP4b2:
            case CAP4c:
            case CAP5a:
            case CAP5b:
            case CAP5c:
            case CAP5d:
            case CAP6:
            case CAP7:
            case CAP8:
            case CAP9:
            case CAP10a:
            case CAP10b:
            case CAP11:
                //Se poate transmite din anul curent si din anul anterior.
                Integer anRaportare = ranDocDTO.getAnRaportare();
                Calendar dataRegistru = Calendar.getInstance();
                dataRegistru.setTime(dataTransmisie);
                Integer anTransmisie = dataRegistru.get(Calendar.YEAR);
                if (anRaportare > anTransmisie) {
                    throw new DateRegistruValidationException(DateRegistruValidationCodes.AN_RAPORTARE_VS_AN_TRANSMISIE, ranDocDTO.getAnRaportare());
                }
                //Din anul anterior se poate transmite numai pana la data de mai sus (parametru de sistem)
                if (dataLimitaParam != null) {
                    if (anRaportare < (anTransmisie - 1)) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.AN_RAPORTARE_VS_DATA_LIMITA_PARAM, ranDocDTO.getAnRaportare(), sdf.format(dataLimitaParam));
                    }
                    if (anRaportare == (anTransmisie - 1) && dataTransmisie.after(dataLimitaParam)) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.AN_RAPORTARE_VS_DATA_LIMITA_PARAM, ranDocDTO.getAnRaportare(), sdf.format(dataLimitaParam));
                    }
                }
                break;
        }
    }

    /**
     * @param ranDocDTO ranDocDTO
     * @param nomUat    nomUat
     * @throws DateRegistruValidationException
     */
    private void validareNrTotalGospodarii(RanDocDTO ranDocDTO, NomUat nomUat) throws DateRegistruValidationException {
        boolean valParam = false;
        try {
            String tmp = getParamValue(ParametruService.RESTRICTIONARE_TRANSMISII_UAT_TOTALGOSP);
            if (tmp != null) {
                valParam = Boolean.valueOf(tmp);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        /*daca param este pe true si daca am capitol cu an*/
        if (valParam) {
            switch (ranDocDTO.getTipCapitol()) {
                case CAP2a:
                case CAP2b:
                case CAP3:
                case CAP4a:
                case CAP4a1:
                case CAP4b1:
                case CAP4b2:
                case CAP4c:
                case CAP5a:
                case CAP5b:
                case CAP5c:
                case CAP5d:
                case CAP6:
                case CAP7:
                case CAP8:
                case CAP9:
                case CAP10a:
                case CAP10b:
                case CAP11:
                    ViewUatGospodarie viewUatGospodarie = viewUatGospodarieService.findByUatAndAn(nomUat.getId(), ranDocDTO.getAnRaportare());
                    if (viewUatGospodarie == null) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.RESTRICTIE_TOTAL_UAT_GOSPODARIE, ranDocDTO.getAnRaportare());
                    }
                    if ((viewUatGospodarie.getTotalUatDeclaratie() + 1) > viewUatGospodarie.getTotalUatGospodarie()) {
                        throw new DateRegistruValidationException(DateRegistruValidationCodes.RESTRICTIE_TOTAL_UAT_DECLARATIE, ranDocDTO.getAnRaportare());
                    }
                    break;
            }

        }
    }

    /**
     * @param ranDocDTO dto pojo; datele extrase din xml
     * @return clasa de tip CapitolService corespunzatoare tipului de capitol
     */
    private CapitolBaseService getCapitolService(RanDocDTO ranDocDTO) {
        if (ranDocDTO.getClazz() != null) {
            return capitolServiceMap.get(ranDocDTO.getClazz());
        }
        throw new UnsupportedOperationException("Capitol nesuportat de sistemul RAN: " + ranDocDTO.getClazz());
    }

    /**
     * @param paramName denumire parametru
     * @return valoare parametru
     */
    private String getParamValue(String paramName) {
        Parametru parametru = parametruService.getParametru(paramName);
        if (parametru != null) {
            return parametru.getValoare();
        }
        return null;
    }

    protected String getSuccesAsHtml(String uuidTransmisie) {
        StringBuilder html = new StringBuilder();
        openHtml(html);
        openHead(html);
        addMetaCharset(html);
        closeHead(html);
        openBody(html);
        addParagraph(html, "Valid, mesaj procesat");
        addParagraph(html, "UUID Transmisie: " + uuidTransmisie);
        closeBody(html);
        closeHtml(html);
        //
        return html.toString();
    }


    protected String getErrorAsHtml(Throwable ex, String uuidTransmisie, boolean isForLogFile) {
        StringBuilder html = new StringBuilder();
        openHtml(html);
        openHead(html);
        addMetaCharset(html);
        closeHead(html);
        openBody(html);
        if (ex instanceof SyntaxXmlException) {
            SyntaxXmlException sxe = (SyntaxXmlException) ex;
            addParagraph(html, "Invalid, mesaj neprocesat. Erori - validari XSD");
            openTabel(html);
            for (String error : sxe.getLstErrorDescription()) {
                openTr(html);
                openTd(html);
                /*continut*/
                html.append(error).append("\n");
                closeTd(html);
                closeTr(html);
            }
            closeTabel(html);
        } else if (ex instanceof DateRegistruValidationException) {
            DateRegistruValidationException drve = (DateRegistruValidationException) ex;
            addParagraph(html, "Invalid, mesaj neprocesat. Erori - validari de business");
            openTabel(html);
            openTr(html);
            openTd(html);
            /*continut*/
            html.append(drve.getCode()).append(": ").append(drve.getMessage());
            if (StringUtils.isNotEmpty(drve.getHint())) {
                html.append("<br>");
                html.append("Indicatii:").append(drve.getHint());
            }
            closeTd(html);
            closeTr(html);
            closeTabel(html);
        } else {
            addParagraph(html, "Invalid, mesaj neprocesat. Erori - runtime");
            openTabel(html);
            /*Message*/
            openTr(html);
            openTd(html);
            html.append(ex.getMessage()).append("\n");
            closeTd(html);
            closeTr(html);
            /*printStackTrace*/
            if (isForLogFile) {
                openTr(html);
                openTd(html);
                StringWriter writer = new StringWriter();
                ex.printStackTrace(new PrintWriter(writer));
                html.append(writer.toString());
                closeTd(html);
                closeTr(html);
            }
            closeTabel(html);
        }
        addParagraph(html, "UUID Transmisie: " + uuidTransmisie);
        closeBody(html);
        closeHtml(html);
        return html.toString();
    }

    protected void openTabel(StringBuilder html) {
        html.append("<TABLE>").append("\n");
    }

    protected void openHead(StringBuilder html) {
        html.append("<HEAD>").append("\n");
    }


    protected void addMetaCharset(StringBuilder html) {
        html.append("<meta charset=\"UTF-8\">").append("\n");
    }

    protected void closeHead(StringBuilder html) {
        html.append("</HEAD>").append("\n");
    }

    protected void closeTabel(StringBuilder html) {
        html.append("</TABLE>").append("\n");
    }

    protected void openTr(StringBuilder html) {
        html.append("<TR>").append("\n");
    }

    protected void closeTr(StringBuilder html) {
        html.append("</TR>").append("\n");
    }

    protected void openTd(StringBuilder html) {
        html.append("<TD  style=\"border-bottom: 1px solid\">").append("\n");
    }

    protected void closeTd(StringBuilder html) {
        html.append("</TD>").append("\n");
    }

    protected void addParagraph(StringBuilder html, String msg) {
        html.append("<P>").append(msg).append("</P>").append("\n");
    }

    protected void openBody(StringBuilder html) {
        html.append("<BODY>").append("\n");
    }

    protected void closeBody(StringBuilder html) {
        html.append("</BODY>").append("\n");
    }

    protected void openHtml(StringBuilder html) {
        html.append("<HTML>").append("\n");
    }

    protected void closeHtml(StringBuilder html) {
        html.append("</HTML>").append("\n");
    }

}
