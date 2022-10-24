package ro.uti.ran.core.service.incarcareZip;

import static ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere.SEMIAUTOMAT;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.uti.ran.core.business.scheduler.annotations.Cluster;
import ro.uti.ran.core.exception.DateRegistruValidationException;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.StructuralValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.FluxRegistruPortal;
import ro.uti.ran.core.model.portal.Incarcare;
import ro.uti.ran.core.model.portal.NomCapitolPortal;
import ro.uti.ran.core.model.portal.RegistruPortal;
import ro.uti.ran.core.model.portal.StareIncarcare;
import ro.uti.ran.core.model.portal.StareRegistru;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.model.utils.StareIncarcareEnum;
import ro.uti.ran.core.model.utils.StareRegistruPortalEnum;
import ro.uti.ran.core.repository.portal.FluxRegistruPortalRepository;
import ro.uti.ran.core.repository.portal.IncarcareRepository;
import ro.uti.ran.core.repository.portal.NomCapitolPortalRepository;
import ro.uti.ran.core.repository.portal.RegistruPortalRepository;
import ro.uti.ran.core.repository.portal.StareIncarcareRepository;
import ro.uti.ran.core.repository.portal.StareRegistruRepository;
import ro.uti.ran.core.service.backend.DateRegistruXmlParser;
import ro.uti.ran.core.service.backend.dto.RanDocDTO;
import ro.uti.ran.core.service.backend.exception.SyntaxXmlException;
import ro.uti.ran.core.service.backend.jaxb.RanDocConversionHelper;
import ro.uti.ran.core.service.backend.nomenclator.NomenclatorService;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.backend.utils.DataRaportareValabilitate;
import ro.uti.ran.core.utils.ContextEnum;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.internal.transmitere.TransmitereDateService;
import ro.uti.ran.core.xml.model.Gospodarie;
import ro.uti.ran.core.xml.model.RanDoc;

/**
 * Created by Stanciu Neculai on 03.Nov.2015.
 */
@Service
@PropertySource(value = "classpath:ws.properties")
public class ZipExtractServiceImpl implements ZipExtractService {
    public static final Logger log = LoggerFactory.getLogger(ZipExtractServiceImpl.class);

    private static final String XML_EXTENSION = ".xml";

    private static final String XML_DEFAULT_CHARSET_KEY = "xml.charset.default";
    private static final String MESAJ_STARE_I = "Fisier incarcat cu succes, urmeaza transmiterea";
    private static final String MESAJ_STARE_T = "Fisier transmis cu succes, urmeaza procesarea";
    private static final String MESAJ_STARE_R = "Fisier respins pe serviciul de transmitere. Motiv:{0}";
    private static final String MESAJ_STARE_R_FARA_MOTIV = "Fisier respins pe serviciul de transmitere.";
    public static final String REG_PORTAL_NULL_MSG = "RegistruPortal savedRegPortal de la apel writeRegistruMetadata este null -> fisierul nu are extensia xml";
    public static final String FARA_INCARCARI_NEPROCESATE = "Nu sunt incarcari neprocesate";
    public static final String INDEX_EXISTENT = "Indexul asociat acestui fisier xml exista deja.";
    public static final String UAT_DIFERIT = "Nu puteti trmite date pentru un alt UAT";

    @Autowired
    private IncarcareRepository incarcareRepository;
    @Autowired
    private StareIncarcareRepository stareIncarcareRepository;
    @Autowired
    private StareRegistruRepository stareRegistruRepository;
    @Autowired
    private TransmitereDateService transmitereDate;
    @Autowired
    private RegistruPortalRepository registruService;

    @Autowired
    private FluxRegistruPortalRepository fluxRegistruPortalRepository;

    @Autowired
    private DateRegistruXmlParser dateRegistruXmlParser;

    @Autowired
    private NomenclatorService nomSrv;

    @Autowired
    private NomCapitolPortalRepository nomCapitolPortalRepository;

    @Autowired
    private Environment env;


    @Autowired
    private RegistruPortalRepository registruPortalRepository;


    @Override
    @Scheduled(cron = "0 0 20 ? * MON-FRI")
    @Async("processUnprocessed")
    public void processUnprocessed() {
        log.info("Run job process Unprocessed");
        List<RegistruPortal> unProcessed = registruPortalRepository.findAllUnProcessed();
        log.info("A fost incarcata lista cu elemente neprocesate. Total count: "+unProcessed.size());
        for (RegistruPortal registruPortalCurent : unProcessed) {
            log.info("Procesare registru cu ID: "+registruPortalCurent.getId());
            Incarcare incarcareCurenta = incarcareRepository.getIncarcareByIdRegistru(registruPortalCurent.getId());

            RegistruPortal newRegistruPortalCurent = registruPortalRepository.findOne(registruPortalCurent.getId());

            if(incarcareCurenta == null)
                throw  new NullPointerException("Incarcarea nu a fost gasita! Id registru: "+newRegistruPortalCurent.getId());

            log.info(newRegistruPortalCurent.getId()+": A fost gasita incarcarea cu id: "+incarcareCurenta.getId());
            Boolean isSendToTransmision = sendToTransmision(newRegistruPortalCurent, incarcareCurenta);
            if(Boolean.TRUE.equals(isSendToTransmision)) {
                log.info(newRegistruPortalCurent.getId()+": Trimis cu succes!");
                saveStareRegistruPortal(newRegistruPortalCurent, StareRegistruPortalEnum.TRANSMIS);
                saveFluxRegistru(newRegistruPortalCurent, StareRegistruPortalEnum.TRANSMIS, null);
            }else {
                log.info(newRegistruPortalCurent.getId()+": Eroare transmitere!");
            }
        }
    }


    @Override
    @Scheduled(cron = "0 0 19 ? * MON-FRI")
    @Async("zipExtractor")
    public void processZipScheduled() {
        log.info("Rulare job procesare zip");
        List<Incarcare> incarcareList = incarcareRepository.findAllUnProcessedLoad();
        log.info("A fost incarcata lista cu elemente. Total count: "+incarcareList.size());
        for(Incarcare incarcare : incarcareList) {
            log.info("Procesare incarcare cu id: "+incarcare.getId());
            processZip(incarcare.getId());
        }
    }

    @Async("zipExtractor")
    private void processZip(Long idIncarcare) {
        Map<String, List<String>> savedXmlFiles = new HashMap<>();
        Incarcare incarcareCurenta;
        List<ZipFileEntry> entries = new ArrayList<>();
        if (idIncarcare != null) {
            incarcareCurenta = incarcareRepository.findOne(idIncarcare);
        } else {
            log.error("Ups! Eroare la cautarea incarcarii curente");
            throw new IllegalArgumentException("idIncarcare este null");
        }
        try {
            if (incarcareCurenta != null) {
                saveStareIncarcare(incarcareCurenta, StareIncarcareEnum.PROCESARE_IN_CURS);
                InputStream zipFile = incarcareCurenta.getContinutFisier();
                if (zipFile != null) {
                    // trebuie obtinuta o noua instanta pentru ca se pierde InputStream-ul de la continut fisier
                    ZipInputStream zipInputStream = new ZipInputStream(incarcareRepository.findOne(idIncarcare).getContinutFisier());
                    //get the zipped file list entry
                    log.info(incarcareCurenta.getId()+": Am obtinut o noua instanta");
                    try {
                        ZipEntry ze = zipInputStream.getNextEntry();
                        while (ze != null) {
                        	entries.add(new ZipFileEntry(ze, extractXmlFile(zipInputStream)));
                        	ze = zipInputStream.getNextEntry();
                        }
                        log.info(incarcareCurenta.getId()+": Au fost extrase xml-urile din zip. ");

                        Collections.sort(entries);

                        zipInputStream.closeEntry();
                        IOUtils.closeQuietly(zipInputStream);
                        log.info(incarcareCurenta.getId()+": Incepe procesarea XML-urilor.");
                        for(ZipFileEntry entry : entries) {
                            if (entry.getXmlFile() != null) {
                                try {
                                    RegistruPortal registruPortalCurent = validateAndSaveRegistru(entry.getZipEntry(), entry.getXmlFile(), incarcareCurenta, entry.getRanDoc());
                                    log.info(incarcareCurenta.getId()+": S-a salvat in tabela registru portal cu succes. ID: "+registruPortalCurent.getId());
                                    //adaug xml salavat in istoric
                                    if (isGospodarie(entry.getRanDoc()) == true) {
                                        addNewXmlFile(savedXmlFiles, entry.getRanDoc(), registruPortalCurent);
                                    } else {
                                        Boolean isSendToTransmision = sendToTransmision(registruPortalCurent, incarcareCurenta);
                                        if(isSendToTransmision) {
                                            saveStareRegistruPortal(registruPortalCurent, StareRegistruPortalEnum.TRANSMIS);
                                            saveFluxRegistru(registruPortalCurent, StareRegistruPortalEnum.TRANSMIS, null);
                                        }
                                    }
                                } catch (RanBusinessException e) {
                                    RegistruPortal savedRegPortal = writeRegistruMetadata(entry.getZipEntry(), entry.getXmlFile(), incarcareCurenta, null, null, true);
                                    if (savedRegPortal != null) {
                                        saveStareRegistruPortal(savedRegPortal, StareRegistruPortalEnum.RESPINS);
                                        saveFluxRegistru(savedRegPortal, StareRegistruPortalEnum.RESPINS, extractRespinsMesaj(e));
                                    } else {
                                        log.error(REG_PORTAL_NULL_MSG);
                                    }
                                    log.error(e.getMessage(), e);
                                }  catch (Exception e){
                                  RegistruPortal savedRegPortal = writeRegistruMetadata(entry.getZipEntry(), entry.getXmlFile(), incarcareCurenta, null, null, true);
                                  if(savedRegPortal != null) {
                                    saveStareRegistruPortal(savedRegPortal,
                                                            StareRegistruPortalEnum.RESPINS);
                                      if (e instanceof SyntaxXmlException) {
                                          StringBuilder sb = new StringBuilder();
                                          for (String error : ((SyntaxXmlException) e).getLstErrorDescription()) {
                                              sb.append(error).append("\n");
                                          }
                                          saveFluxRegistru(savedRegPortal, StareRegistruPortalEnum.RESPINS, sb.toString());
                                      } else if (e instanceof DateRegistruValidationException) {
                                          StringBuilder sb = new StringBuilder();
                                          DateRegistruValidationException drve = (DateRegistruValidationException) e;
                                          sb.append("Invalid, mesaj neprocesat. Erori - validari de business");
                                          sb.append(drve.getCode()).append(": ").append(drve.getMessage());
                                          if (StringUtils.isNotEmpty(drve.getHint())) {
                                              sb.append("Indicatii:").append(drve.getHint());
                                          }
                                          saveFluxRegistru(savedRegPortal, StareRegistruPortalEnum.RESPINS, sb.toString());
                                      } else {
                                          saveFluxRegistru(savedRegPortal, StareRegistruPortalEnum.RESPINS, e.getMessage());
                                      }
                                  } else {
                                    log.error(REG_PORTAL_NULL_MSG);
                                  }
                                  log.error(e.getMessage(), e);
                                }

                            }
                        }
                  
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                
                trimiteGospodariile(savedXmlFiles, incarcareCurenta);
                saveStareIncarcare(incarcareCurenta, StareIncarcareEnum.PROCESARE_FINALIZATA);
            } else {
                log.debug(FARA_INCARCARI_NEPROCESATE);
            }
        } catch (Exception e) {
            if (incarcareCurenta != null) {
                saveStareIncarcare(incarcareCurenta, StareIncarcareEnum.PROCESARE_FINALIZATA);
            }
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @Async("zipExtractor")
    public void processZipAsync(Long idIncarcare) {
        processZip(idIncarcare);
    }

    private void trimiteGospodariile(Map<String, List<String>> savedXmlFiles, Incarcare incarcareCurenta) throws RanBusinessException {
        for (Map.Entry<String, List<String>> gospodarieXmlFiles : savedXmlFiles.entrySet()) {
            List<String> registruIndexesByGospodarie = gospodarieXmlFiles.getValue();
            List<RegistruPortal> listaRegistruOrdonata = registruPortalRepository.findAllByRegistruIndices(registruIndexesByGospodarie);
            for (RegistruPortal registruPortalCurent : listaRegistruOrdonata) {
                Boolean isSendToTransmision = sendToTransmision(registruPortalCurent, incarcareCurenta);
                if(isSendToTransmision) {
                    saveStareRegistruPortal(registruPortalCurent, StareRegistruPortalEnum.TRANSMIS);
                    saveFluxRegistru(registruPortalCurent, StareRegistruPortalEnum.TRANSMIS, null);
                }
            }

        }

    }

    private void addNewXmlFile(Map<String, List<String>> savedXmlFilesRef, RanDoc ranDoc, RegistruPortal registruPortalCurent) {
        String idGospodarie = getIndetificatorGospodarie(ranDoc);
        if (idGospodarie != null && savedXmlFilesRef != null) {
            if (savedXmlFilesRef.containsKey(idGospodarie)) {
                List<String> xmlUuids = savedXmlFilesRef.get(idGospodarie);
                xmlUuids.add(registruPortalCurent.getIndexRegistru().toLowerCase());
            } else {
                List<String> xmlUuids = new ArrayList<String>();
                xmlUuids.add(registruPortalCurent.getIndexRegistru().toLowerCase());
                savedXmlFilesRef.put(idGospodarie, xmlUuids);
            }
        }
    }

    @Transactional(value = "portalTransactionManager", readOnly = true)
    public Incarcare getIncarcareCurenta() {
        try {
            StareIncarcare stareIncarcare = stareIncarcareRepository.findByCod(StareIncarcareEnum.RECEPTIONAT.getCod());
            return incarcareRepository.findFirstByStareIncarcare(stareIncarcare);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional("portalTransactionManager")
    public RegistruPortal writeRegistruMetadata(ZipEntry ze, String xmlFile, Incarcare incarcareCurenta, NomCapitolPortal nomCapitolPortal, RanDoc ranDoc, boolean isOnException) throws RanBusinessException {
        if (ze != null && isXmlExtension(ze.getName())) {
            RegistruPortal registruPortal = new RegistruPortal();
            registruPortal.setIncarcare(incarcareCurenta);
            setStareRegistruPortal(registruPortal, StareRegistruPortalEnum.INCARCAT);
            registruPortal.setDataRegistru(new Date());
            registruPortal.setDenumireFisier(ze.getName());
            registruPortal.setContinutFisier(xmlFile);
            registruPortal.setIsRecipisaSemnata(Boolean.FALSE);
            if (ranDoc != null) {
            	if(incarcareCurenta.getUat().getCodSiruta() != ranDoc.getHeader().getSirutaUAT()) {
            		 throw new DateRegistruValidationException(UAT_DIFERIT);
            	}
            	
                if (indexExists(ranDoc.getHeader().getCodXml().getValue().toLowerCase())) {
                    throw new DateRegistruValidationException(INDEX_EXISTENT);
                } else {
                    registruPortal.setIndexRegistru(ranDoc.getHeader().getCodXml().getValue().toLowerCase());
                }
            }
            if (nomCapitolPortal != null) {
                registruPortal.setNomCapitolPortal(nomCapitolPortal);
            }

            RegistruPortal savedRegPortal = registruPortalRepository.save(registruPortal);
            if (!isOnException) {
                saveFluxRegistru(savedRegPortal, StareRegistruPortalEnum.INCARCAT, null);
            }
            return savedRegPortal;
        } else {
            return null;
        }

    }

    private Long getNomCapitol(RanDoc ranDoc) throws RequestValidationException {
        RanDocDTO ranDocDTO = getRanDocDTO(ranDoc);
        DataRaportareValabilitate  dataRaportareValabilitate = new DataRaportareValabilitate();
        if(ranDocDTO.getAnRaportare() != null && ranDocDTO.getSemestruRaportare() != null){
            dataRaportareValabilitate = new DataRaportareValabilitate(ranDocDTO.getAnRaportare(),ranDocDTO.getSemestruRaportare());
        }
        if(ranDocDTO.getAnRaportare() != null && ranDocDTO.getSemestruRaportare() == null){
            dataRaportareValabilitate = new DataRaportareValabilitate(ranDocDTO.getAnRaportare());
        }
        String codCapitol = ranDocDTO.getCodCapitol();
        NomCapitol tmp  = nomSrv.getNomenclatorForStringParam(NomenclatorCodeType.NomCapitol, codCapitol, dataRaportareValabilitate);
        if (tmp != null) {
            return tmp.getId();
        } else {
            return null;
        }
    }

    private String getIndetificatorGospodarie(RanDoc ranDoc) {
        Object gospodarieSauRaportare = ranDoc.getBody().getGospodarieSauRaportare();
        if (gospodarieSauRaportare instanceof ro.uti.ran.core.xml.model.Gospodarie) {
            Gospodarie gospodarie = (Gospodarie) gospodarieSauRaportare;
            return gospodarie.getIdentificator();
        } else {
            return null;
        }
    }

    private boolean isGospodarie(RanDoc ranDoc) {
        Object gospodarieSauRaportare = ranDoc.getBody().getGospodarieSauRaportare();
        if (gospodarieSauRaportare instanceof ro.uti.ran.core.xml.model.Gospodarie) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * fara tranzactie pentru a nu avea conflict intre cele 2 contexte
     */
    public RegistruPortal validateAndSaveRegistru(ZipEntry ze, String xmlFile, Incarcare incarcareCurenta, RanDoc ranDoc) throws RanBusinessException {
        try {
            NomCapitolPortal nomCapitolPortal = null;
            Long idNomCapitol = getNomCapitol(ranDoc);
            if (idNomCapitol != null) {
                nomCapitolPortal = getNomCapitolFromPortal(idNomCapitol);
            }
            if (nomCapitolPortal != null) {
                return writeRegistruMetadata(ze, xmlFile, incarcareCurenta, nomCapitolPortal, ranDoc, false);
            } else {
                throw new DateRegistruValidationException("Nu s-a putut identifica un capitol pentru aceast fisier!");
            }
        } catch (RanBusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new DateRegistruValidationException(e);
        }

    }

    @Transactional(value = "portalTransactionManager", readOnly = true)
    private NomCapitolPortal getNomCapitolFromPortal(Long idNomCapitol) {
        return nomCapitolPortalRepository.findOne(idNomCapitol);
    }

    private RanDocDTO getRanDocDTO(RanDoc ranDoc) throws RequestValidationException {
        RanDocDTO ranDocDTO = new RanDocDTO();
        RanDocConversionHelper.populeazaFromSchemaType(ranDoc, ranDocDTO);
        return ranDocDTO;
    }

    private boolean indexExists(String indexRegistru) {
        RegistruPortal registru = registruService.findByIndexRegistru(indexRegistru);
        if (registru != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * transmitereDate.transmitere se executa cu  @Transactional("registruTransactionManager")
     *
     * @param registruPortalCurent
     * @param incarcareCurenta
     */
    public Boolean sendToTransmision(RegistruPortal registruPortalCurent, Incarcare incarcareCurenta) {
        if (registruPortalCurent != null) {
            log.debug(registruPortalCurent.toString());
            RanAuthorization ranAuthorization = new RanAuthorization();
            ranAuthorization.setContext(ContextEnum.UAT.getValue());
            ranAuthorization.setIdEntity(incarcareCurenta.getUat().getId());
            try {
                transmitereDate.transmitere(registruPortalCurent.getContinutFisier(), SEMIAUTOMAT, ranAuthorization);
                return Boolean.TRUE;
            } catch (RanBusinessException e) {
                log.error(e.getMessage(), e);
                saveStareRegistruPortal(registruPortalCurent, StareRegistruPortalEnum.RESPINS);
                String respinsMesaj = extractRespinsMesaj(e);
                saveFluxRegistru(registruPortalCurent, StareRegistruPortalEnum.RESPINS, respinsMesaj);
                return Boolean.FALSE;
            }
        } else {
            log.debug(" *** registruPortalCurent is null");
            return Boolean.FALSE;
        }
    }

    private String extractRespinsMesaj(RanBusinessException e) {
        StringBuilder sb = new StringBuilder();
        if(e.getCode() != null) {
            sb.append(" Cod:");
            sb.append(e.getCode());
        }
        if(e.getMessage() != null && !"null".equals(e.getMessage())) {
            sb.append(" Mesaj:");
            sb.append(e.getMessage());
        }
        if(e.getResourceKey() != null && !"null".equals(e.getResourceKey())) {
            sb.append(" Detalii:");
            sb.append(e.getResourceKey());
            if(e.getSecondaryCode() != null && !"null".equals(e.getSecondaryCode())) {
                sb.append(", ");
                sb.append(e.getSecondaryCode());
            }
        } else if(e.getSecondaryCode() != null && !"null".equals(e.getSecondaryCode())) {
            sb.append(" Detalii:");
            sb.append(e.getSecondaryCode());
        }
        if(e.getHint() != null && !"null".equals(e.getHint())) {
            sb.append(" Hint:");
            sb.append(e.getHint());
        }
        return sb.toString();
    }

    @Transactional(value = "portalTransactionManager", readOnly = true)
    private void setStareRegistruPortal(RegistruPortal registruPortalRef, StareRegistruPortalEnum stareRegistruPortalEnum) {
        StareRegistru stareRegistru = stareRegistruRepository.findByCod(stareRegistruPortalEnum.getCod());
        registruPortalRef.setStareRegistru(stareRegistru);
    }

    @Transactional("portalTransactionManager")
    private void saveStareRegistruPortal(RegistruPortal registruPortal, StareRegistruPortalEnum stareRegistruPortalEnum) {
        StareRegistru stareRegistru = stareRegistruRepository.findByCod(stareRegistruPortalEnum.getCod());
        registruPortal.setStareRegistru(stareRegistru);
        registruPortalRepository.save(registruPortal);
    }

    @Transactional("portalTransactionManager")
    private void saveStareIncarcare(Incarcare incarcareCurenta, StareIncarcareEnum stareIncarcareEnum) {
        StareIncarcare stareIncarcareInCurs = stareIncarcareRepository.findOne(stareIncarcareEnum.getId());
        incarcareCurenta.setStareIncarcare(stareIncarcareInCurs);
        incarcareRepository.save(incarcareCurenta);
    }

    @Transactional("portalTransactionManager")
    private void saveFluxRegistru(RegistruPortal registru, StareRegistruPortalEnum stareRegistruEnum, String respinsParam) {
        FluxRegistruPortal fluxRegistru = new FluxRegistruPortal();
        fluxRegistru.setDataStare(new Date());
        StareRegistru stareRegistru = stareRegistruRepository.findByCod(stareRegistruEnum.getCod());

        String stareMesaj =getStareMesaj(stareRegistruEnum, respinsParam);

        if(stareMesaj.length() > 4000)
            stareMesaj = stareMesaj.substring(0, 3900);
        fluxRegistru.setMesajStare(stareMesaj);

        fluxRegistru.setStareRegistru(stareRegistru);
        fluxRegistru.setRegistru(registru);
        fluxRegistruPortalRepository.save(fluxRegistru);
    }

    private String getStareMesaj(StareRegistruPortalEnum stareRegistruEnum, String respinsParam) {
        if (stareRegistruEnum.equals(StareRegistruPortalEnum.INCARCAT)) {
            return MESAJ_STARE_I;
        } else if (stareRegistruEnum.equals(StareRegistruPortalEnum.TRANSMIS)) {
            return MESAJ_STARE_T;
        } else {
            if(respinsParam == null || respinsParam.isEmpty()){
                return MESAJ_STARE_R_FARA_MOTIV;
            } else {
                return MessageFormat.format(MESAJ_STARE_R, respinsParam);
            }
        }
    }

    private String extractXmlFile(ZipInputStream zis) {
        String defaultCharset = env.getProperty(XML_DEFAULT_CHARSET_KEY);
        try {
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
            int len;
            while ((len = zis.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }
            return new String(zipOutputStream.toByteArray(), defaultCharset);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private boolean isXmlExtension(String name) {
        return name.toLowerCase().endsWith(XML_EXTENSION);
    }

    
    private class ZipFileEntry implements Comparable<ZipFileEntry>{
    	
    	private ZipEntry zipEntry;
    	private String xmlFile;
    	
    	private RanDoc ranDoc;
    	private Long prioritate;
    	
    	private ZipFileEntry(ZipEntry zipEntry, String xmlFile) {
    		this.zipEntry = zipEntry;
    		this.xmlFile = xmlFile;
    	}
    	
		private ZipEntry getZipEntry() {
			return zipEntry;
		}
	
		private String getXmlFile() {
			return xmlFile;
		}
		
		private RanDoc getRanDoc() throws StructuralValidationException, SyntaxXmlException {
			if(ranDoc != null) {
				return ranDoc;
			}
			
			ranDoc = dateRegistruXmlParser.getPojoFromXML(getXmlFile());
			return ranDoc;
		}
		
		private Long getPrioritate() throws StructuralValidationException, SyntaxXmlException, RequestValidationException {
			if(prioritate != null) {
				return prioritate;
			}

            Long nomCapitol = getNomCapitol( getRanDoc());
            Set<Long> maxPriority = RanConstants.MAX_PRIORIRTY_CHAPTERS_INT;

            if(maxPriority.contains(nomCapitol)) {
                return 0L;
            }

			prioritate = nomCapitol;
			return prioritate;
		}

		@Override
		public int compareTo(ZipFileEntry o) {
			try {
				return this.getPrioritate().compareTo(o.getPrioritate());
			} catch (Exception e) {
				e.printStackTrace();
				return 1;
			}
		}
    }

}
