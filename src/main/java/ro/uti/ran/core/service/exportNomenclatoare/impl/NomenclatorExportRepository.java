package ro.uti.ran.core.service.exportNomenclatoare.impl;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.model.registru.NomCapitol;
import ro.uti.ran.core.service.backend.nomenclator.ExportableParamsEnum;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.backend.nomenclator.type.SqlConditionType;
import ro.uti.ran.core.service.exportNomenclatoare.ComplexExportableNomServicesFactory;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportStatus;
import ro.uti.ran.core.service.exportNomenclatoare.data.summary.NomenclatorMetadata;
import ro.uti.ran.core.service.exportNomenclatoare.data.summary.NomenclatorsSummary;
import ro.uti.ran.core.service.exportNomenclatoare.exception.NomenclatorExportCodes;
import ro.uti.ran.core.service.exportNomenclatoare.exception.NomenclatorExportException;
import ro.uti.ran.core.service.parametru.ParametruService;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created with IntelliJ IDEA. User: mala, horia
 */
@Component
public class NomenclatorExportRepository {

    // NOMENCLATORS EXPORT parameters
    public static final String NOMENCLATORS_EXPORT_CLUSTER_DIR_PATH = "nomenclators.export.cluster.dir.path";
    public static final String NOMENCLATORS_EXPORT_PATH = "nomenclators.export.path";
    public static final String NOMENCLATORS_SUMMARY_EXPORT_PATH = "nomenclators.summary.export.path";
    public static final String NOMENCLATORS_ZIP_EXPORT_PATH = "nomenclators.zip.export.path";
    public static final String NOMENCLATORS_STORE_LOCAL = "nomenclators.store.local";
    public static final String NOT_APP_UPDATABLE_PATTERN = "appUpdate";
    private static final String EXPORT_DATE_PATTERN = "yyyyMMddHHmm";
    // SELECT to_date('2014-01-01 12:00:30', 'YYYY-MM-dd HH24:MI:SS') from dual
    public static String VALID_FROM_QUERY = "SELECT max(DATA_START) FROM {0} WHERE "
            + "NVL(DATA_STOP, to_date(''{1}'', ''YYYY-MM-dd HH24:MI:SS'')) <= to_date(''{1}'', ''YYYY-MM-dd HH24:MI:SS'')";
    public static String SIMPLE_UPDATE_DATE_QUERY = "SELECT max(LAST_MODIFIED_DATE) FROM {0} ";
    private static Log LOGGER = LogFactory.getLog(NomenclatorExportRepository.class);
    private static String nomenclatorsDirPath;
    private static String nomenclatorsRepositoryPath;
    private static String nomenclatorsSummaryPath;
    private static String nomenclatorsZipPath;
    private static String nomenclatorsZipExportFileName = "RAN_Nomenclators";
    private static String nomenclatorsSummaryName = "nomenclators_items_summary_";

    @Autowired
    private ParametruService parametruService;
    @PersistenceContext(unitName = "ran-registru")
    private EntityManager entityManager;
    @Autowired
    private ComplexExportableNomServicesFactory nomServicesFactory;

    public static File zipAll(String filename, String summaryPath, NomenclatorExportStatus... exportedNomenclators) throws IOException {
        List<NomenclatorExportStatus> listOfUniqueNom = getUniqueNom(Arrays.asList(exportedNomenclators));
        File zipfile = new File(filename);
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        // create the ZIP file
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
        // add summary
        if (!StringUtils.isEmpty(summaryPath)) {
            FileInputStream in = new FileInputStream(summaryPath);
            // add ZIP entry to output stream
            out.putNextEntry(new ZipEntry(nomenclatorsSummaryName.substring(0, nomenclatorsSummaryName.length() - 1).concat(".xml")));
            // transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // complete the entry
            out.closeEntry();
            in.close();
        }

        // compress the files
        for (NomenclatorExportStatus exportStatus : listOfUniqueNom) {
            FileInputStream in = new FileInputStream(exportStatus.getFilePath());
            // add ZIP entry to output stream
            out.putNextEntry(new ZipEntry(exportStatus.getNomenclatorName().concat(".xml")));
            // transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // complete the entry
            out.closeEntry();
            in.close();
        }
        // complete the ZIP file
        out.flush();
        out.close();
        return zipfile;
    }

    private static List<NomenclatorExportStatus> getUniqueNom(List<NomenclatorExportStatus> exportedNomenclators) {
        List<NomenclatorExportStatus> res = new ArrayList<NomenclatorExportStatus>();
        boolean isAdded = false;
        for (NomenclatorExportStatus nomExportStatus : exportedNomenclators) {
            for (NomenclatorExportStatus status : res) {
                if (status.getNomenclatorName().equalsIgnoreCase(nomExportStatus.getNomenclatorName())) {
                    isAdded = true;
                }
            }
            if (!isAdded) {
                res.add(nomExportStatus);
            }
        }
        return res;
    }

    @PostConstruct
    private void init() throws RanBusinessException {
        // verify if nomenclators should be exported locally
        Parametru nomenclatorLocal = parametruService.getParametru(NOMENCLATORS_STORE_LOCAL);
        Boolean storeNomenclatorsLocal = null == nomenclatorLocal ? false : Boolean.valueOf(nomenclatorLocal.getValoare());

        // Calea catre user home este independenta de sistemul de operare si este folosita doar daca mesajele sunt salvate local
        // pe UNIX e /Users/username
        // pe WINDOWS e C:Users\\username
        String userDir = System.getProperty("user.home");

        // make sure that repository path is created
        Parametru dirPath = parametruService.getParametru(NOMENCLATORS_EXPORT_CLUSTER_DIR_PATH);
        Parametru nomenclatorsPath = parametruService.getParametru(NOMENCLATORS_EXPORT_PATH);
        Parametru summaryPath = parametruService.getParametru(NOMENCLATORS_SUMMARY_EXPORT_PATH);
        Parametru zipPath = parametruService.getParametru(NOMENCLATORS_ZIP_EXPORT_PATH);

        nomenclatorsRepositoryPath = null == nomenclatorsPath ? "nomenclators" : nomenclatorsPath.getValoare();
        nomenclatorsSummaryPath = null == summaryPath ? "summary" : summaryPath.getValoare();
        nomenclatorsZipPath = null == zipPath ? "archive" : zipPath.getValoare();
        nomenclatorsDirPath = null == dirPath || storeNomenclatorsLocal ? userDir : dirPath.getValoare();

        // create structure dir for all available nomenclators
        for (ExportableNomenclator exportableCatalog : NomenclatorExportServiceImpl.exportablesCatalogs) {
            try {
                if (exportableCatalog.getConditionType().equals(SqlConditionType.COMPLEX) &&
                        nomServicesFactory.getComplexExportableNomenclator(exportableCatalog).getSubcatNameCodes(exportableCatalog) != null) {
                    Map<ExportableParamsEnum, Object> categories = nomServicesFactory.getComplexExportableNomenclator(exportableCatalog).getSubcatNameCodes(exportableCatalog);
                    for (Map.Entry<ExportableParamsEnum, Object> catEntry : categories.entrySet()) {
                        if (catEntry.getKey().equals(ExportableParamsEnum.CAPITOL) || catEntry.getKey().equals(ExportableParamsEnum.CAPITOL_PROD)) {
                            List<NomCapitol> catValues = (List<NomCapitol>) catEntry.getValue();
                            if (catValues != null) {
                                for (NomCapitol nomCapitol : catValues) {
                                    FileUtils.forceMkdir(new File(getNomenclatorBasePath(exportableCatalog, exportableCatalog.getNumeNomenclator(nomCapitol))));
                                }
                            }
                        }
                    }
                } else if(!exportableCatalog.getConditionType().equals(SqlConditionType.COMPLEX)){
                    FileUtils.forceMkdir(new File(getNomenclatorBasePath(exportableCatalog, null)));
                }
            } catch (IOException e) {
                LOGGER.error("Exception during nomenclators export! ", e);
                throw new NomenclatorExportException(NomenclatorExportCodes.PATH_NOT_FOUND, e);
            }
        }


        // create dir for nomenclator summary and archive
        try {
            FileUtils.forceMkdir(new File(geNomenclatorsSummaryFolderPath()));
            FileUtils.forceMkdir(new File(getZipFolderPath()));
        } catch (IOException e) {
            LOGGER.error("Exception during nomenclators export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.PATH_NOT_FOUND, e);
        }
    }

    public String addSummary(Date exportDate, NomenclatorExportStatus... exportedNomenclators) throws NomenclatorExportException {
        String summaryName = getNomenclatorsSummaryPath(exportDate);
        JAXBContext context;
        XMLStreamWriter xmlOut = null;
        try {
            xmlOut = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileOutputStream(summaryName));
            try {
                xmlOut = new IndentingXMLStreamWriter(xmlOut);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            xmlOut.writeStartDocument();
            context = JAXBContext.newInstance(NomenclatorsSummary.class, NomenclatorMetadata.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            NomenclatorsSummary summary = new NomenclatorsSummary();
            summary.setSummaryDate(DateFormatUtils.format(exportDate, "yyyy-MM-dd HH:mm:ss"));
            List<NomenclatorMetadata> nomenclators = new ArrayList<NomenclatorMetadata>();
            for (NomenclatorExportStatus status : exportedNomenclators) {
                NomenclatorMetadata cm = new NomenclatorMetadata();
                if (status.getLastUpdateDate() != null) {
                    cm.setLastModifyDate(DateFormatUtils.format(status.getLastUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
                }
                cm.setNomenclatorName(status.getNomenclatorName());
                nomenclators.add(cm);
            }
            summary.setNomenclators(nomenclators);
            marshaller.marshal(summary, xmlOut);
            xmlOut.writeEndDocument();
            return summaryName;
        } catch (JAXBException e) {
            LOGGER.error("Exception during nomenclators export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.JAXB_MARSHALLER_NOT_LOADED, e);
        } catch (FileNotFoundException e) {
            LOGGER.error("Exception during nomenclators export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.EXPORT_ERROR, e);
        } catch (XMLStreamException e) {
            LOGGER.error("Exception during nomenclators export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.EXPORT_ERROR, e);
        } catch (FactoryConfigurationError e) {
            LOGGER.error("Exception during nomenclators export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.EXPORT_ERROR, e);
        } finally {
            if (xmlOut != null) {
                try {
                    xmlOut.flush();
                    xmlOut.close();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //     try to delete files if error
    protected void deleteAddedFilesAtError(Date exportDate) {
        try {
            // delete nomenclators
            for (ExportableNomenclator expCatalog : NomenclatorExportServiceImpl.exportablesCatalogs) {
                if (expCatalog.getConditionType().equals(SqlConditionType.COMPLEX) &&
                        nomServicesFactory.getComplexExportableNomenclator(expCatalog).getSubcatNameCodes(expCatalog) != null) {
                    Map<ExportableParamsEnum, Object> categories = nomServicesFactory.getComplexExportableNomenclator(expCatalog).getSubcatNameCodes(expCatalog);
                    for (Map.Entry<ExportableParamsEnum, Object> catEntry : categories.entrySet()) {
                        if (catEntry.getKey().equals(ExportableParamsEnum.CAPITOL) || catEntry.getKey().equals(ExportableParamsEnum.CAPITOL_PROD)) {
                            List<NomCapitol> catValues = (List<NomCapitol>) catEntry.getValue();
                            for (NomCapitol nomCapitol : catValues) {
                                File latestCatalog = getCatalogForExportDate(expCatalog, exportDate, expCatalog.getNumeNomenclator(nomCapitol));
                                if (latestCatalog != null && latestCatalog.exists()) {
                                    try {
                                        FileUtils.deleteQuietly(latestCatalog);
                                    } catch (Exception e) {
                                        LOGGER.error("Error: ", e);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    File latestCatalog = getCatalogForExportDate(expCatalog, exportDate, null);
                    if (latestCatalog != null && latestCatalog.exists()) {
                        try {
                            FileUtils.deleteQuietly(latestCatalog);
                        } catch (Exception e) {
                            LOGGER.error("Error: ", e);
                        }
                    }
                }
            }

            // delete summary
            String summaryPath = getNomenclatorsSummaryPath(exportDate);
            File summary = new File(summaryPath);
            if (summary != null && summary.exists()) {
                try {
                    FileUtils.deleteQuietly(summary);
                } catch (Exception e) {
                    LOGGER.error("Error: ", e);
                }
            }

            // delete zip
            String zipPath = getZipPath(exportDate);
            File zip = new File(zipPath);
            if (zip != null && zip.exists()) {
                try {
                    FileUtils.deleteQuietly(zip);
                } catch (Exception e) {
                    LOGGER.error("Error: ", e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error: ", e);
        }
    }

    protected void deleteOldCatalogFiles(String filePath) {
        try {
            File nomenclatorFile = FileUtils.getFile(filePath);
            if (nomenclatorFile.exists() && nomenclatorFile.isFile()) {
                final File folder = nomenclatorFile.getParentFile();
                if (folder != null && folder.isDirectory()) {
                    for (final File file : folder.listFiles()) {
                        if (file.isFile() && !file.getName().equalsIgnoreCase(nomenclatorFile.getName())) {
                            FileUtils.deleteQuietly(file);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // do not throw exception
            LOGGER.error("Error: ", e);
        }
    }

    public NomenclatorExportStatus getNomenclatorExportStatus(ExportableNomenclator exportableNomenclator, Date exportDate, String nomName) throws NomenclatorExportException {
        NomenclatorExportStatus exportStatus = new NomenclatorExportStatus();
        String numeNomenclator = exportableNomenclator.getName();
        if (nomName != null && !nomName.isEmpty()) {
            numeNomenclator = nomName;
        }
        exportStatus.setNomenclatorName(numeNomenclator);
        exportStatus.setExportedDate(exportDate);
        String formattedDate = DateFormatUtils.format(exportDate, "yyyy-MM-dd HH:mm:ss");
        if (exportableNomenclator.isAppUpdatable()) {
            Timestamp lastUpd = (Timestamp) entityManager.createNativeQuery(
                    MessageFormat.format(SIMPLE_UPDATE_DATE_QUERY, exportableNomenclator.getTableName())).getSingleResult();
            exportStatus.setLastUpdateDate(new Date(lastUpd.getTime()));
        }

        // check to see if last update date is also exported
        if (isNomenclatorExported(exportableNomenclator, exportStatus.getLastUpdateDate(), nomName)) {
            exportStatus.setNeedToBeExported(false);
            // set path with last modifying date and last exported date
            exportStatus.setFilePath(getLatestNomenclator(exportableNomenclator, nomName).getPath());
        } else {
            // set path with last modifying date and current export date
            exportStatus.setFilePath(getNomenclatorPath(exportableNomenclator, exportStatus.getLastUpdateDate(), exportDate, nomName));
        }
        return exportStatus;

    }

    public boolean isNomenclatorExported(ExportableNomenclator nomenclator, Date lastUpdateDate, String nomName) throws NomenclatorExportException {
        String numeNomenclator = nomenclator.getName();
        if (nomName != null && !nomName.isEmpty()) {
            numeNomenclator = nomName;
        }
        String basePath = getNomenclatorBasePath(nomenclator, numeNomenclator);
        final File folder = new File(basePath);
        if (!folder.isDirectory() || folder.listFiles() == null || folder.listFiles().length == 0)
            return false;
        String updateDate = "";
        if (nomenclator.isAppUpdatable()) {
            updateDate = DateFormatUtils.format(lastUpdateDate, EXPORT_DATE_PATTERN);
        }

        File[] xmlFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        });

        for (final File file : xmlFiles) {
            if (file.isFile() && file.getName().startsWith(numeNomenclator)) {
                String whereToSearch = file.getName().substring(numeNomenclator.length());
                if (StringUtils.countMatches(whereToSearch, "_") == 2) {
                    String strDate = file.getName().substring(file.getName().indexOf("_") + 1, file.getName().lastIndexOf("_"));
                    if (!nomenclator.isAppUpdatable() && strDate.equals(NOT_APP_UPDATABLE_PATTERN)) {
                        //todo aici adaug parametru care va forta scrierea nom care nu se modifica din aplicatie
                        return true;
                    }
                    if (nomenclator.isAppUpdatable() && updateDate.equalsIgnoreCase(strDate)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public File getLatestSummaryFile() throws NomenclatorExportException {
        File latestSummary = getLatestFileFromFolder(geNomenclatorsSummaryFolderPath());
        return latestSummary;
    }

    public NomenclatorsSummary getLatestSummary() throws NomenclatorExportException {
        File latestSummary = getLatestFileFromFolder(geNomenclatorsSummaryFolderPath());
        if (latestSummary == null)
            return null;
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(NomenclatorsSummary.class, NomenclatorMetadata.class);
            Unmarshaller um = context.createUnmarshaller();
            NomenclatorsSummary summary = (NomenclatorsSummary) um.unmarshal(latestSummary);
            return summary;
        } catch (JAXBException e) {
            LOGGER.error("Exception during nomenclators export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.JAXB_MARSHALLER_NOT_LOADED, e);
        }
    }

    public File getLatestNomenclator(ExportableNomenclator nomenclator, String nomName) throws NomenclatorExportException {
        return getLatestFileFromFolder(getNomenclatorBasePath(nomenclator, nomName));
    }

    public File getCatalogForExportDate(ExportableNomenclator nomenclator, Date exportDate, String nomName) throws NomenclatorExportException {
        // compare only to dd mm yyyy hh:mm
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(exportDate);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        Date refDate = calendar.getTime();

        String basePath = getNomenclatorBasePath(nomenclator, nomName);
        final File folder = new File(basePath);
        if (!folder.isDirectory() || folder.listFiles() == null || folder.listFiles().length == 0)
            return null;
        SimpleDateFormat df = new SimpleDateFormat(EXPORT_DATE_PATTERN);
        File[] xmlFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        });
        for (final File file : xmlFiles) {
            if (file.isFile()) {
                String strDate = file.getName().substring(file.getName().lastIndexOf("_") + 1, file.getName().indexOf("."));
                Date date;
                try {
                    date = df.parse(strDate);
                } catch (ParseException e) {
                    LOGGER.error("Exception during nomenclators export! ", e);
                    throw new NomenclatorExportException(NomenclatorExportCodes.NOMENCLATOR_PARSE_DATE, strDate, EXPORT_DATE_PATTERN);
                }
                if (date.compareTo(refDate) == 0) {
                    return file;
                }
            }
        }
        return null;
    }

    public File getLatestFileFromFolder(String folderPath) throws NomenclatorExportException {
        final File folder = new File(folderPath);
        if (!folder.isDirectory() || folder.listFiles() == null || folder.listFiles().length == 0)
            return null;
        SimpleDateFormat df = new SimpleDateFormat(EXPORT_DATE_PATTERN);
        File latestFile = null;
        boolean first = true;
        Date refDate = null;

        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.toLowerCase().endsWith(".xml") || name.toLowerCase().endsWith(".zip"));
            }
        });

        if (null != files) {
            for (final File file : files) {
                if (file.isFile()) {
                    String strDate = file.getName().substring(file.getName().lastIndexOf("_") + 1, file.getName().indexOf("."));
                    Date date;
                    try {
                        date = df.parse(strDate);
                    } catch (ParseException e) {
                        LOGGER.error("Exception during nomenclators export! ", e);
                        throw new NomenclatorExportException(NomenclatorExportCodes.NOMENCLATOR_PARSE_DATE, strDate, EXPORT_DATE_PATTERN);
                    }
                    if (first) {
                        refDate = date;
                        latestFile = file;
                        first = false;
                    } else if (date.after(refDate)) {
                        refDate = date;
                        latestFile = file;
                    }
                }
            }
        }
        return latestFile;
    }

    public String getNomenclatorBasePath(ExportableNomenclator exportableNomenclator, String nomName) throws NomenclatorExportException {
        String numeNomenclator = exportableNomenclator.getName();
        if (nomName != null && !nomName.isEmpty()) {
            numeNomenclator = nomName;
        }
        String path = new StringBuilder(nomenclatorsDirPath).append(File.separatorChar).append(nomenclatorsRepositoryPath)
                .append(File.separatorChar).append(numeNomenclator).toString();
        try {
            FileUtils.forceMkdir(new File(path));
        } catch (IOException e) {
            LOGGER.error("Exception during nomenclators export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.PATH_NOT_FOUND, e);
        }
        return path;
    }

    public String getNomenclatorPath(ExportableNomenclator exportableNomenclator, Date lastModifyDate, Date exportDate, String nomName) throws NomenclatorExportException {
        String numeNomenclator = exportableNomenclator.getName();
        if (nomName != null && !nomName.isEmpty()) {
            numeNomenclator = nomName;
        }

        String lastModifiedDateStr;
        if (lastModifyDate == null) {
            lastModifiedDateStr = NOT_APP_UPDATABLE_PATTERN;
        } else {
            lastModifiedDateStr = DateFormatUtils.format(lastModifyDate, EXPORT_DATE_PATTERN);
        }
        return new StringBuilder(getNomenclatorBasePath(exportableNomenclator, nomName)).append(File.separatorChar)
                .append(numeNomenclator).append("_")
                .append(lastModifiedDateStr).append("_")
                .append(DateFormatUtils.format(exportDate, EXPORT_DATE_PATTERN)).append(".xml").toString();
    }

    public String getNomenclatorsSummaryPath(Date lastGeneratedDate) throws NomenclatorExportException {
        return new StringBuilder(geNomenclatorsSummaryFolderPath()).append(File.separatorChar).append(nomenclatorsSummaryName)
                .append(DateFormatUtils.format(lastGeneratedDate, EXPORT_DATE_PATTERN)).append(".xml").toString();
    }

    public String geNomenclatorsSummaryFolderPath() throws NomenclatorExportException {
        String path = new StringBuilder(nomenclatorsDirPath).append(File.separatorChar).append(nomenclatorsRepositoryPath)
                .append(File.separatorChar).append(nomenclatorsSummaryPath).toString();
        try {
            FileUtils.forceMkdir(new File(path));
        } catch (IOException e) {
            LOGGER.error("Exception during nomenclators export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.PATH_NOT_FOUND, e);
        }
        return path;

    }

    public String getZipPath(Date lastGeneratedDate) throws NomenclatorExportException {
        return new StringBuilder(getZipFolderPath()).append(File.separatorChar).append(nomenclatorsZipExportFileName)
                .append(new SimpleDateFormat(EXPORT_DATE_PATTERN).format(lastGeneratedDate)).append(".zip").toString();
    }

    public String getZipFolderPath() throws NomenclatorExportException {
        String path = new StringBuilder(nomenclatorsDirPath).append(File.separatorChar).append(nomenclatorsRepositoryPath)
                .append(File.separatorChar).append(nomenclatorsZipPath).toString();
        try {
            FileUtils.forceMkdir(new File(path));
        } catch (IOException e) {
            LOGGER.error("Exception during nomenclators export! ", e);
            throw new NomenclatorExportException(NomenclatorExportCodes.PATH_NOT_FOUND, e);
        }
        return path;
    }

    public String zip(Date exportDate, String summaryPath, NomenclatorExportStatus... exportedNomenclators) throws IOException,
            NomenclatorExportException {
        String zipName = getZipPath(exportDate);
        // zip all
        zipAll(zipName, summaryPath, exportedNomenclators);
        return zipName;
    }

}
