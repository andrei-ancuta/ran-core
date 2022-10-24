package ro.uti.ran.core.ws.internal.incarcare;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.*;
import ro.uti.ran.core.model.utils.StareRegistruPortalEnum;
import ro.uti.ran.core.repository.portal.RegistruCoreRepository;
import ro.uti.ran.core.repository.portal.RegistruPortalRepository;
import ro.uti.ran.core.repository.portal.UatRepository;
import ro.uti.ran.core.repository.portal.UtilizatorRepository;
import ro.uti.ran.core.service.incarcareZip.AsyncZipExtractorHandler;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.service.registru.IncarcareService;
import ro.uti.ran.core.service.registru.IncarcariSearchFilter;
import ro.uti.ran.core.service.utilizator.UatConfigService;
import ro.uti.ran.core.utils.*;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.utilizator.UatConfig;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jws.WebService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-17 15:53
 */
@WebService(
        serviceName = "IncarcareDateService",
        endpointInterface = "ro.uti.ran.core.ws.internal.incarcare.IncarcareDate",
        targetNamespace = "http://portal.registru.internal.ws.core.ran.uti.ro",
        portName = "IncarcareDateServicePort")
@Service("incarcareDateService")
public class IncarcareDateImpl implements IncarcareDate {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncarcareDateImpl.class);
    public static final String PARAM_UPLOAD_XML_MAX_FILES_KEY = "upload.xml.max.files";
    public static final String PARAM_UPLOAD_XML_MAX_SIZE_KEY = "upload.xml.max.size";

    @Autowired
    private IncarcareService incarcareService;

    @Autowired
    RegistruPortalRepository registruPortalRepository;

    @Autowired
    private UtilizatorRepository utilizatorRepository;

    @Autowired
    private UatRepository uatRepository;
    
    @Autowired
    private ExceptionUtil exceptionUtil;

    @Autowired
    private AsyncZipExtractorHandler zipExtractor;

    @Autowired
    private UatConfigService uatConfigService;

    @Autowired
    private RegistruCoreRepository registruCoreRepository;

    @Autowired
    private ParametruService parametruService;

    /*
    http://openjpa.apache.org/builds/2.0.1/apache-openjpa-2.0.1/docs/manual/ref_guide_mapping_jpa.html#ref_guide_streamsupport
    http://stackoverflow.com/questions/9898739/save-blob-using-a-stream-from-ejb-to-database-in-a-memory-efficient-way

     */
    @Override
    public RezultatIncarcare incarca( String denumireFisier,
                                      DataHandler continutFisier,
                                      String numeUtilizator,
                                      Long idUat
    ) throws RanException, RanRuntimeException {

        try {
            if(StringUtils.isEmpty(denumireFisier)){
                throw new IllegalArgumentException("Parametru denumire fisier nedefinit");
            }

            if(!denumireFisier.toLowerCase().endsWith(".zip")){
                throw new IllegalArgumentException("Fisierul nu este in format zip");
            }

            if(continutFisier == null){
                throw new IllegalArgumentException("Parametru continutFisier nedefinit");
            }

            if(StringUtils.isEmpty(numeUtilizator)){
                throw new IllegalArgumentException("Parametru numeUtilizator nedefinit");
            }

            if(idUat == null){
                throw new IllegalArgumentException("Parametru idUat nedefinit");
            }

            if( !ZipUtil.isZipFile(continutFisier.getInputStream())){
                throw new IllegalArgumentException("Fisierul nu este in format zip");
            }
            try {
                if (!ZipUtil.checkZipFileContentForXml(continutFisier.getInputStream())) {
                    throw new IllegalArgumentException("Arhiva zip nu contine nici un fisier xml");
                }
            } catch (IOException e){
                LOGGER.error(e.getMessage(),e);
                throw new IllegalArgumentException("Fisierul zip nu poate fi citit");
            }
            try {
                Parametru upMaxFilesParam = parametruService.getParametru(PARAM_UPLOAD_XML_MAX_FILES_KEY);
                int nrMaxFisiereXml;
                nrMaxFisiereXml = extractIntValueFromParam(upMaxFilesParam);
                if(nrMaxFisiereXml > 0 ) {
                    if (nrMaxFisiereXml < ZipUtil.countZipFileContentForXml(continutFisier.getInputStream())) {
                        throw new IllegalArgumentException("Arhiva zip contine mai multe fisiere xml decat maxiumul admis de: "+nrMaxFisiereXml);
                    }
                }
            } catch (IOException e){
                LOGGER.error(e.getMessage(),e);
                throw new IllegalArgumentException("Fisierul zip nu poate fi citit");
            }



            Utilizator utilizator = utilizatorRepository.findByNumeUtilizatorIgnoreCase(numeUtilizator);
            if (utilizator == null) {
                throw new IllegalArgumentException("Nu exista utilizator cu numeUtilizator " + numeUtilizator);
            }

            UAT uat = uatRepository.findOne(idUat);
            if (uat == null) {
                throw new IllegalArgumentException("Nu exista UAT cu ID " + idUat);
            }

            Incarcare incarcare = new Incarcare();

            incarcare.setContinutFisier(continutFisier.getInputStream());
            incarcare.setDenumireFisier(denumireFisier);

            incarcare.setUtilizator(utilizator);
            incarcare.setUat(uat);

            incarcare = incarcareService.saveIncarcare(incarcare);
            zipExtractor.processZipAsync(incarcare.getId());


            RezultatIncarcare rezultatIncarcare = new RezultatIncarcare();
            rezultatIncarcare.setDataPrimire(incarcare.getDataIncarcare());
            rezultatIncarcare.setIdentificatorTransmisie(incarcare.getIndexIncarcare());
            rezultatIncarcare.setMesaj("Pachet primit, urmeaza validarea datelor");

            return rezultatIncarcare;
        }catch (RanBusinessException rbe){
        	 throw exceptionUtil.buildException(new RanException(rbe));
        }catch(Throwable th){
            LOGGER.error("Eroare la preluare lista incarcari", th);
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    private int extractIntValueFromParam(Parametru intParam) {
        int intValue = 0;
        if(intParam != null) {
            if(intParam.getValoare() != null && !intParam.getValoare().isEmpty()) {
                intValue = Integer.parseInt(intParam.getValoare());
            } else if(intParam.getValoareImplicita() != null && !intParam.getValoareImplicita().isEmpty()) {
                intValue = Integer.parseInt(intParam.getValoareImplicita());
            }
        }
        return intValue;
    }

    @Override
    public IncarcariListResult getListaIncarcari(
            IncarcariSearchFilter searchFilter,
            PagingInfo pagingInfo,
            IncarcariSortInfo sortInfo
    ) throws RanException, RanRuntimeException{

        try {
            GenericListResult<Incarcare> incarcari = incarcareService.getListaIncarcari(searchFilter, pagingInfo, SortInfoHelper.buildSortInfo(sortInfo));

            return ListResultHelper.build(
                    IncarcariListResult.class,
                    incarcari,
                    new ListResultHelper.Mapper<Incarcare, DetaliiIncarcare>() {
                        @Override
                        public DetaliiIncarcare map(Incarcare source) {
                            DetaliiIncarcare incarcare = new DetaliiIncarcare();

                            BeanUtils.copyProperties(source, incarcare);

                            return incarcare;
                        }
                    }
            );
        } catch (Throwable th) {
            LOGGER.error("Eroare la preluare lista incarcari", th);
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public DetaliiIncarcare getDetaliiIncarcare(Long idIncarcare) throws RanException, RanRuntimeException {
        try {
            Incarcare incarcare = incarcareService.getIncarcareById(idIncarcare);
            if( incarcare == null){
                throw new IllegalArgumentException("Nu exista incarcare cu id " + idIncarcare);
            }

            DetaliiIncarcare detaliiIncarcare = new DetaliiIncarcare();

            BeanUtils.copyProperties(incarcare, detaliiIncarcare);

            return detaliiIncarcare;
        } catch (Throwable th) {
            LOGGER.error("Eroare la preluare incarcare", th);
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public RegistruPortalListResult getListaRegistruIncarcare(Long idIncarcare, PagingInfo pagingInfo, RegistruPortalSortInfo sortInfo) throws RanException, RanRuntimeException {
        try {
            GenericListResult<RegistruPortalDetails> registruPortal = incarcareService.getDetaliiRegistruPortal(idIncarcare, pagingInfo, SortInfoHelper.buildSortInfo(sortInfo));

            return ListResultHelper.build(RegistruPortalListResult.class, registruPortal, new ListResultHelper.Mapper<RegistruPortalDetails, DetaliiFisierXml>() {
                @Override
                public DetaliiFisierXml map(RegistruPortalDetails source) {
                    DetaliiFisierXml detaliiFisierXml = new DetaliiFisierXml();

                    detaliiFisierXml.setId(source.getId());
                    detaliiFisierXml.setIndexRegistru(source.getIndexRegistru());
                    detaliiFisierXml.setDataRegistru(source.getDataRegistru());
                    detaliiFisierXml.setDenumireFisier(source.getDenumireFisier());
                    detaliiFisierXml.setCodStare(source.getCodStareRegistruPortal());
                    detaliiFisierXml.setDenumireStare(source.getDenumireStareRegistruPortal());
                    detaliiFisierXml.setMesajStare(source.getMesajStareFluxRegistruPortal());
                    if(source.getCodStareRegistruPortal().equals(StareRegistruPortalEnum.TRANSMIS)) {
                        detaliiFisierXml.setCodStareCore(source.getCodStareRegistruCore());
                        detaliiFisierXml.setDenumireStareCore(source.getDenumireStareRegistruCore());
                    }
                    detaliiFisierXml.setIsRecipisaSemnata(source.getIsRecipisaSemnata());

                    return detaliiFisierXml;
                }
            });
        } catch (Throwable th) {
            LOGGER.error("Eroare la preluare lista registru portal dupa idIncarcare " + idIncarcare, th);
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public DataHandler descarcaIncarcare(Long idIncarcare) throws RanException, RanRuntimeException{
        try {
            if (idIncarcare == null) {
                throw new IllegalArgumentException("Parametru idIncarcare nedefinit");
            }

            final Incarcare incarcare = incarcareService.getIncarcareById(idIncarcare);
            if( incarcare == null){
                throw new IllegalArgumentException("Nu exista incarcare cu id " + idIncarcare);
            }

            return new DataHandler(new DataSource(){
                @Override
                public InputStream getInputStream() throws IOException {
                    return incarcare.getContinutFisier();
                }

                @Override
                public OutputStream getOutputStream() throws IOException {
                    return null;
                }

                @Override
                public String getContentType() {
                    return "zip";
                }

                @Override
                public String getName() {
                    return incarcare.getDenumireFisier();
                }
            });
        } catch (Throwable th) {
            LOGGER.error("Eroare descarcare Incarcare " + idIncarcare, th);
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public DataHandler descarcaFisierXml(Long idRegistru) throws RanException, RanRuntimeException {
        return null;
    }

    @Override
    public DataHandler descarcaRecipisa(Long idRegistru) throws RanException, RanRuntimeException {
        try {
            if (idRegistru == null) {
                throw new IllegalArgumentException("Parametru idRegistru nedefinit");
            }
            byte[] dateRecipisa = null;
            final RegistruPortal registruPortal = registruPortalRepository.findOne(idRegistru);
            if( registruPortal == null){
                throw new IllegalArgumentException("Nu exista registru cu id " + idRegistru);
            }
            if(registruPortal.getStareRegistru().getCod().equals(StareRegistruPortalEnum.TRANSMIS.getCod())){
                if(registruPortal.getIndexRegistru() != null && !registruPortal.getIndexRegistru().isEmpty()) {
                    final RegistruCore registruCore = registruCoreRepository.findByIndexRegistru(registruPortal.getIndexRegistru());
                    if(registruCore != null){
                        if(registruCore.getIsRecipisaSemnata()) {
                            dateRecipisa = "Recipisa este semnata.".getBytes();
                        } else if(registruCore.getRecipisa() == null) {
                            throw new IllegalStateException("Nu exista recipisa pentru id registru " + idRegistru);
                        } else {
                            dateRecipisa = registruCore.getRecipisa();
                        }
                    }
                }

            } else {
                if(registruPortal.getIsRecipisaSemnata()){
                    dateRecipisa = "Recipisa este semnata.".getBytes();
                } else {
                    if (registruPortal.getRecipisa() == null) {
                        throw new IllegalStateException("Nu exista recipisa pentru id registru " + idRegistru);
                    } else {
                        dateRecipisa = registruPortal.getRecipisa();
                    }
                }
            }
            final byte[] dateRecipisaFinal = dateRecipisa;

            return new DataHandler(new DataSource(){
                @Override
                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(dateRecipisaFinal);
                }

                @Override
                public OutputStream getOutputStream() throws IOException {
                    return null;
                }

                @Override
                public String getContentType() {
                    return null;
                }

                @Override
                public String getName() {
                    return null;
                }
            });
        } catch (Throwable th) {
            LOGGER.error("Eroare descarcare recipisa " + idRegistru, th);
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public DataHandler descarcaPachetRaspuns(Long idIncarcare) throws RanException, RanRuntimeException {
        try {
            if (idIncarcare == null) {
                throw new IllegalArgumentException("Parametru idIncarcare nedefinit");
            }

            final Incarcare incarcare = incarcareService.getIncarcareById(idIncarcare);
            if (incarcare == null) {
                throw new IllegalArgumentException("Nu exista incarcare cu id " + idIncarcare);
            }

            final InputStream zipRaspuns = incarcareService.genereazaPachetRaspuns(incarcare);

            return new DataHandler(new DataSource(){
                @Override
                public InputStream getInputStream() throws IOException {
                    return zipRaspuns;
                }

                @Override
                public OutputStream getOutputStream() throws IOException {
                    return null;
                }

                @Override
                public String getContentType() {
                    return "zip";
                }

                @Override
                public String getName() {
                    return incarcare.getDenumireFisier();
                }
            });
        } catch (Throwable th) {
            LOGGER.error("Eroare la descarcare pachet raspuns incarcare " + idIncarcare, th);
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }

    @Override
    public UatConfig getUatConfig(Long idUat) throws RanException, RanRuntimeException {
        try {
            return uatConfigService.getUatConfig(idUat);
        } catch (Throwable th) {
            throw exceptionUtil.buildException(new RanRuntimeException(th));
        }
    }
}
