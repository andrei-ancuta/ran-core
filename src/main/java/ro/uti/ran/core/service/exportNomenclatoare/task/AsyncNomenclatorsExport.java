package ro.uti.ran.core.service.exportNomenclatoare.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.service.backend.nomenclator.ExportableParamsEnum;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.backend.nomenclator.type.ExportStatusType;
import ro.uti.ran.core.service.backend.nomenclator.type.SqlConditionType;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNom;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNomServicesFactory;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportMetadata;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportStatus;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportValue;
import ro.uti.ran.core.service.exportNomenclatoare.exception.NomenclatorExportCodes;
import ro.uti.ran.core.service.exportNomenclatoare.exception.NomenclatorExportException;
import ro.uti.ran.core.service.exportNomenclatoare.impl.NomenclatorExportRepository;
import ro.uti.ran.core.service.exportNomenclatoare.impl.NomenclatorExportServiceImpl;
import ro.uti.ran.core.service.exportNomenclatoare.impl.StreamingMarshaller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Async support for nomenclator export service User: mala, horia
 */
public class AsyncNomenclatorsExport {

    private static final Log LOGGER = LogFactory.getLog(AsyncNomenclatorsExport.class);

    public static final String COUNT_QUERY = "SELECT COUNT(*) FROM {0}";
    public static final String NOMENCLATOR_VALUE_QUERY = "SELECT {0} AS CODE, {1} AS VALUE, null as validFrom, null as validTo  FROM {2}";
    public static final String NOMENCLATOR_VALUE_QUERY_WITH_VALIDITY = "SELECT {0} AS CODE, {1} AS VALUE, {2} as validFrom, {3} as validTo FROM {4}";
    public static final String NOMENCLATOR_VALUE_QUERY_WITH_PARENT = "SELECT t1.{0} AS CODE, t1.{1} AS VALUE, null as validFrom, null as validTo," +
            " t2.{0} as PARENT FROM {2} t1 inner join {2} t2 on t1.{3} = t2.{4}";
    public static final String NOMENCLATOR_VALUE_QUERY_WITH_VALIDITY_AND_PARENT = "SELECT t1.{0} AS CODE, t1.{1} AS VALUE, " +
            " t1.{2} as validFrom, t1.{3} as validTo, t2.{0} as PARENT FROM {4} t1 inner join {4} t2 on t1.{5} = t2.{6}";
    public static final int WAIT_STEP_MILLIS = 1000;
    public static final int PAGE_SIZE = 1000; // chunk page size for exporting data
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PersistenceContext(unitName = "ran-registru")
    private EntityManager entityManager;

    @Autowired
    private NomenclatorExportRepository nomenclatorExportRepository;

    @Autowired
    private ComplexExportableNomServicesFactory complexServicesFactory;

    @Async("catalogsExportExecutor")
    public Future<List<NomenclatorExportStatus>> exportAllNomenclators(Date exportDate) throws NomenclatorExportException {
        boolean generateNewZipAndSummary = false;
        List<NomenclatorExportStatus> exportStatuses = new ArrayList<NomenclatorExportStatus>();
        try {
            for (ExportableNomenclator nomenclator : NomenclatorExportServiceImpl.exportablesCatalogs) {
                if (nomenclator.getConditionType().equals(SqlConditionType.COMPLEX) &&
                        complexServicesFactory.getComplexExportableNomenclator(nomenclator).getSubcatNameCodes(nomenclator) != null) {
                    Map<ExportableParamsEnum, Object> categories = complexServicesFactory.getComplexExportableNomenclator(nomenclator).getSubcatNameCodes(nomenclator);
                    for (Map.Entry<ExportableParamsEnum, Object> catEntry : categories.entrySet()) {
                        if (catEntry.getKey().equals(ExportableParamsEnum.CAPITOL) || catEntry.getKey().equals(ExportableParamsEnum.CAPITOL_PROD)) {
                            List<NomCapitol> catValues = (List<NomCapitol>) catEntry.getValue();
                            for (NomCapitol nomCapitol : catValues) {
                                Map<ExportableParamsEnum,Object> additionalParams = new HashMap<ExportableParamsEnum, Object>();
                                additionalParams.put(catEntry.getKey(),nomCapitol);
                                Future<NomenclatorExportStatus> futureExport = exportNomenclator(nomenclator, exportDate, nomenclator.getNumeNomenclator(nomCapitol),additionalParams);
                                // sleep and release CPU
                                while (!futureExport.isDone()) {
                                    Thread.sleep(WAIT_STEP_MILLIS);
                                }
                                NomenclatorExportStatus status = futureExport.get();
                                if (status.isNeedToBeExported()) {
                                    generateNewZipAndSummary = true;
                                }
                                exportStatuses.add(status);
                            }
                        }
                    }

                } else if(!nomenclator.getConditionType().equals(SqlConditionType.COMPLEX)) {
                    Future<NomenclatorExportStatus> futureExport = exportNomenclator(nomenclator, exportDate, null, null);
                    // sleep and release CPU
                    while (!futureExport.isDone()) {
                        Thread.sleep(WAIT_STEP_MILLIS);
                    }
                    NomenclatorExportStatus status = futureExport.get();
                    if (status.isNeedToBeExported()) {
                        generateNewZipAndSummary = true;
                    }
                    exportStatuses.add(status);
                }
            }

            // try to zip exported catalogs and generate new summary if at least one catalog was modified
            if (generateNewZipAndSummary) {
                String summaryName = nomenclatorExportRepository.addSummary(exportDate,
                        exportStatuses.toArray(new NomenclatorExportStatus[exportStatuses.size()]));
                nomenclatorExportRepository
                        .zip(exportDate, summaryName, exportStatuses.toArray(new NomenclatorExportStatus[exportStatuses.size()]));
            }
            return new AsyncResult<List<NomenclatorExportStatus>>(exportStatuses);
        } catch (NomenclatorExportException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exception during all catalogs export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.EXPORT_ERROR, e);
        }
    }

    @Async("catalogsExportExecutor")
    public Future<NomenclatorExportStatus> exportNomenclator(ExportableNomenclator exportableNomenclator, Date exportDate, String nomName, Map<ExportableParamsEnum, Object> additionalParams) throws NomenclatorExportException {
        NomenclatorExportStatus exportStatus = nomenclatorExportRepository.getNomenclatorExportStatus(exportableNomenclator, exportDate, nomName);
        // no need to be exported
        if (!exportStatus.isNeedToBeExported())
            return new AsyncResult<NomenclatorExportStatus>(exportStatus);

        long maxResults = ((BigDecimal) entityManager
                .createNativeQuery(MessageFormat.format(COUNT_QUERY, exportableNomenclator.getTableName())).getSingleResult()).longValue();

        // prepare metadata
        NomenclatorExportMetadata catalogExportMetadata = buildExportNomenclatorMetadata(exportableNomenclator,nomName);
        catalogExportMetadata.setDate(dateFormatter.format(exportDate));
        if (exportableNomenclator.isAppUpdatable()) {
            catalogExportMetadata.setLastModifyDate(dateFormatter.format(exportStatus.getLastUpdateDate()));
        }
        // create streaming marshaller
        StreamingMarshaller<NomenclatorExportValue> marshaller;
        try {
            marshaller = new StreamingMarshaller<NomenclatorExportValue>(NomenclatorExportValue.class, catalogExportMetadata);
            // touch file and put metadata information
            marshaller.open(exportStatus.getFilePath());
        } catch (JAXBException e) {
            LOGGER.error("Exception during catalog export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.JAXB_MARSHALLER_NOT_LOADED, e);
        } catch (IOException e) {
            LOGGER.error("Exception during catalog export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.PATH_NOT_FOUND, e);
        } catch (XMLStreamException e) {
            LOGGER.error("Exception during catalog export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.JAXB_MARSHALLER_NOT_LOADED, e);
        }

        // iterate all over the chunks
        for (int chunk = 0; chunk <= maxResults / PAGE_SIZE; chunk++) {
            // TODO condition when maxResult % PAGE_SIZE == 0
            if(exportableNomenclator.getConditionType().equals(SqlConditionType.COMPLEX) &&
                    complexServicesFactory.getComplexExportableNomenclator(exportableNomenclator).getSubcatNameCodes(exportableNomenclator) != null) {
                        Future<List<NomenclatorExportValue>> promiseValues = exportNomenclatorChunk(chunk, exportableNomenclator, additionalParams);
                        try {
                            while (!promiseValues.isDone())
                                Thread.sleep(100);
                            List<NomenclatorExportValue> values = promiseValues.get();
                            // write values
                            for (NomenclatorExportValue value : values)
                                marshaller.write(value);
                        } catch (InterruptedException e) {
                            LOGGER.error("Exception during catalog export! ", e);
                            throw new NomenclatorExportException(NomenclatorExportCodes.EXPORT_ERROR, e);
                        } catch (ExecutionException e) {
                            LOGGER.error("Exception during catalog export! ", e);
                            throw new NomenclatorExportException(NomenclatorExportCodes.EXPORT_ERROR, e);
                        } catch (JAXBException e) {
                            LOGGER.error("Exception during catalog export! ", e);
                            throw new NomenclatorExportException(NomenclatorExportCodes.JAXB_MARSHALLER_NOT_LOADED, e);
                        }

            } else {
                Future<List<NomenclatorExportValue>> promiseValues = exportNomenclatorChunk(chunk, exportableNomenclator,null);
                try {
                    while (!promiseValues.isDone())
                        Thread.sleep(100);
                    List<NomenclatorExportValue> values = promiseValues.get();
                    // write values
                    for (NomenclatorExportValue value : values)
                        marshaller.write(value);
                } catch (InterruptedException e) {
                    LOGGER.error("Exception during catalog export! ", e);
                    throw new NomenclatorExportException(NomenclatorExportCodes.EXPORT_ERROR, e);
                } catch (ExecutionException e) {
                    LOGGER.error("Exception during catalog export! ", e);
                    throw new NomenclatorExportException(NomenclatorExportCodes.EXPORT_ERROR, e);
                } catch (JAXBException e) {
                    LOGGER.error("Exception during catalog export! ", e);
                    throw new NomenclatorExportException(NomenclatorExportCodes.JAXB_MARSHALLER_NOT_LOADED, e);
                }
            }
        }

        // finish export
        try {
            marshaller.close();
            exportStatus.setStatusType(ExportStatusType.SUCCESS);
        } catch (XMLStreamException e) {
            LOGGER.error("Exception during catalog export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.JAXB_MARSHALLER_NOT_LOADED, e);
        }
        return new AsyncResult<NomenclatorExportStatus>(exportStatus);
    }

    @SuppressWarnings("unchecked")
    @Async("catalogsExportExecutor")
    public Future<List<NomenclatorExportValue>> exportNomenclatorChunk(int chunk, ExportableNomenclator exportableNomenclator, Map<ExportableParamsEnum, Object> additionalParams) {
        String sql = null;
        List<NomenclatorExportValue> complexExportedValues = null;
        if (exportableNomenclator.getConditionType().compareTo(SqlConditionType.SIMPLE) == 0) {
            sql = MessageFormat.format(NOMENCLATOR_VALUE_QUERY, exportableNomenclator.getCodeColumn(), exportableNomenclator.getDescriptionColumn(),
                    exportableNomenclator.getTableName());
        } else if (exportableNomenclator.getConditionType().equals(SqlConditionType.WITH_DATE_BETWEEN)) {
            sql = MessageFormat.format(NOMENCLATOR_VALUE_QUERY_WITH_VALIDITY, exportableNomenclator.getCodeColumn(),
                    exportableNomenclator.getDescriptionColumn(), "DATA_START", "DATA_STOP", exportableNomenclator.getTableName());
        } else if (exportableNomenclator.getConditionType().equals(SqlConditionType.WITH_PARENT)) {
            sql = MessageFormat.format(NOMENCLATOR_VALUE_QUERY_WITH_PARENT,
                    exportableNomenclator.getCodeColumn(),
                    exportableNomenclator.getDescriptionColumn(),
                    exportableNomenclator.getTableName(),
                    "ID_" + exportableNomenclator.getTableName(),
                    "FK_" + exportableNomenclator.getTableName());
        } else if (exportableNomenclator.getConditionType().equals(SqlConditionType.WITH_PARENT_AND_DATE_BETWEEN)) {
            sql = MessageFormat.format(NOMENCLATOR_VALUE_QUERY_WITH_VALIDITY_AND_PARENT,
                    exportableNomenclator.getCodeColumn(),
                    exportableNomenclator.getDescriptionColumn(),
                    "DATA_START",
                    "DATA_STOP",
                    exportableNomenclator.getTableName(),
                    "ID_" + exportableNomenclator.getTableName(),
                    "FK_" + exportableNomenclator.getTableName());
        } else {
            complexExportedValues = extractExportedValuesFromComplexNomenclators(chunk, exportableNomenclator, additionalParams);
        }

        List<NomenclatorExportValue> exportValues = new ArrayList<NomenclatorExportValue>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!exportableNomenclator.getConditionType().equals(SqlConditionType.COMPLEX)) {
            if (sql != null) {
                List<Object> values = entityManager.createNativeQuery(sql).setFirstResult(chunk * PAGE_SIZE).setMaxResults(PAGE_SIZE)
                        .getResultList();
                for (Object value : values) {
                    Object[] objs = (Object[]) value;
                    NomenclatorExportValue v = new NomenclatorExportValue();
                    v.setCode(objs[0] == null ? null : String.valueOf(objs[0]));
                    v.setDescription(objs[1] == null ? null : (String) objs[1]);
                    v.setValidFrom(objs[2] == null ? null : df.format(objs[2]));
                    v.setValidTo(objs[3] == null ? null : df.format(objs[3]));
                    if (exportableNomenclator.getConditionType().equals(SqlConditionType.WITH_PARENT_AND_DATE_BETWEEN) ||
                            exportableNomenclator.getConditionType().equals(SqlConditionType.WITH_PARENT) ||
                            exportableNomenclator.getConditionType().equals(SqlConditionType.COMPLEX)) {
                        v.setParentCode(objs[4] == null ? null : String.valueOf(objs[4]));
                    }
                    exportValues.add(v);
                }
            }
        } else {
            if (complexExportedValues != null) {
                exportValues = complexExportedValues;
            }
        }

        return new AsyncResult<List<NomenclatorExportValue>>(exportValues);
    }

    private List<NomenclatorExportValue> extractExportedValuesFromComplexNomenclators(int chunk,
                                                                                      ExportableNomenclator exportableNomenclator, Map<ExportableParamsEnum, Object> additionalParams) {
        ComplexExportableNom complexExportableNom = complexServicesFactory.getComplexExportableNomenclator(exportableNomenclator);
        complexExportableNom.getSubcatNameCodes(exportableNomenclator);
        List<NomenclatorExportValue> nomenclatorExportValueList = complexExportableNom.getNomValues(chunk, PAGE_SIZE, exportableNomenclator, additionalParams);
        if (nomenclatorExportValueList != null) {
            return nomenclatorExportValueList;
        } else {
            LOGGER.error("NULL on exportableNomenclator type:" + exportableNomenclator.getNomType() + "");
            throw new NullPointerException("NULL on exportableNomenclator type:" + exportableNomenclator.getNomType() + "");
        }

    }


    private NomenclatorExportMetadata buildExportNomenclatorMetadata(ExportableNomenclator exportableNomenclator,String nomName) {
        String numeNomenclator = exportableNomenclator.getName();
        if(nomName != null && !nomName.isEmpty()){
            numeNomenclator = nomName;
        }
        NomenclatorExportMetadata metadata = new NomenclatorExportMetadata();
        metadata.setDisplayName(numeNomenclator);
        return metadata;
    }
}
