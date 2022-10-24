package ro.uti.ran.core.service.exportNomenclatoare.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.business.scheduler.annotations.Cluster;
import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.service.backend.nomenclator.model.ExportableNomenclator;
import ro.uti.ran.core.service.backend.nomenclator.model.NomenclatorCodeType;
import ro.uti.ran.core.service.exportNomenclatoare.IExportNomenclators;
import ro.uti.ran.core.service.exportNomenclatoare.NomenclatorExportService;
import ro.uti.ran.core.service.exportNomenclatoare.data.NomenclatorExportStatus;
import ro.uti.ran.core.service.exportNomenclatoare.data.summary.NomenclatorsSummary;
import ro.uti.ran.core.service.exportNomenclatoare.exception.NomenclatorExportCodes;
import ro.uti.ran.core.service.exportNomenclatoare.exception.NomenclatorExportException;
import ro.uti.ran.core.service.exportNomenclatoare.task.AsyncNomenclatorsExport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA. User: Niku
 */
@Service
public class NomenclatorExportServiceImpl implements NomenclatorExportService, IExportNomenclators {

    private static final Log LOGGER = LogFactory.getLog(NomenclatorExportServiceImpl.class);

    public static List<ExportableNomenclator> exportablesCatalogs = new ArrayList<ExportableNomenclator>();

    // union all nomenclators types
    static {
        exportablesCatalogs.addAll(NomenclatorCodeType.getAllExportableCatalogCodeTypes());
    }

    @Autowired
    NomenclatorExportRepository nomenclatorExportRepository;
    @Autowired
    private AsyncNomenclatorsExport asyncExport;

    @Override
    public void exportAllNomenclators() throws NomenclatorExportException {
        Date exportDate = new Date();
        try {
            Future<List<NomenclatorExportStatus>> exportStatuses = asyncExport.exportAllNomenclators(exportDate);
            while (!exportStatuses.isDone())
                Thread.sleep(AsyncNomenclatorsExport.WAIT_STEP_MILLIS);
            List<NomenclatorExportStatus> statuses = exportStatuses.get();
            // delete the old files for newly exported nomenclators
            for (NomenclatorExportStatus nomenclatorExportStatus : statuses) {
                if (nomenclatorExportStatus.isNeedToBeExported())
                    nomenclatorExportRepository.deleteOldCatalogFiles(nomenclatorExportStatus.getFilePath());
            }
            // delete old summaries
            nomenclatorExportRepository.deleteOldCatalogFiles(nomenclatorExportRepository.getNomenclatorsSummaryPath(exportDate));
            // delete old zips
            nomenclatorExportRepository.deleteOldCatalogFiles(nomenclatorExportRepository.getZipPath(exportDate));
        } catch (Exception e) {
            nomenclatorExportRepository.deleteAddedFilesAtError(exportDate);
            if (e instanceof NomenclatorExportException) {
                throw (NomenclatorExportException) e;
            } else {
                LOGGER.error("Exception during all nomenclators export! ", e);
                throw new NomenclatorExportException(NomenclatorExportCodes.EXPORT_ERROR, e);
            }
        }
    }

    @Cluster
//    @Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0 0 22 ? * MON-FRI")
    // fires at 10PM on every week day
    public void scheduledCatalogsExport() {
        try {
            LOGGER.info("::::::::::: Scheduled nomenclators export started!");
            exportAllNomenclators();
        } catch (Exception e) {
            LOGGER.error("::::::::::: Scheduled nomenclators export error:", e);
        } finally {
            LOGGER.info("::::::::::: Scheduled nomenclators export finished!");
        }
    }

    @Override
    public NomenclatorsSummary getNomenclatorsSummary() throws NomenclatorExportException {
        // get the latest nomenclatorSummary from the repository
        NomenclatorsSummary summary = nomenclatorExportRepository.getLatestSummary();
        if (summary == null) {
            // TODO dc e null ramane de vazut dc se face export de nomenclatoare ca sa se genereze sumarul si dp trimis, acum aruncam exceptie
            // throw new NomenclatorExportException(NomenclatorExportCodes.NOMENCLATOR_SUMMARY_NOT_FOUND);
            // lasam exporte doar la inceput, dp stergem
            exportAllNomenclators();
            return getNomenclatorsSummary();
        }
        return summary;
    }

    @Override
    public byte[] getNomenclatorsSummaryContent() throws NomenclatorExportException {
        File latestSummaryFile = nomenclatorExportRepository.getLatestSummaryFile();
        if (latestSummaryFile == null) {
            exportAllNomenclators();
            latestSummaryFile = nomenclatorExportRepository.getLatestSummaryFile();
        }

        try {
            System.out.println("nomenclator summary file abs path: " + latestSummaryFile.getAbsolutePath());
            return FileUtils.readFileToByteArray(latestSummaryFile);
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public byte[] exportNomenclatorContent(TipNomenclator tipNomenclator,String nomName) throws NomenclatorExportException {
        File latestNomenclator = exportNomenclatorContentAsFile(tipNomenclator, nomName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(latestNomenclator);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error: ", e);
        }
        try {
            if(fis != null) {
                return IOUtils.toByteArray(fis);
            }
        } catch (IOException e) {
            LOGGER.error("Error: ", e);
        }
        return null;
    }

    @Override
    public File exportNomenclatorContentAsFile(TipNomenclator tipNomenclator, String nomName) throws NomenclatorExportException {
        ExportableNomenclator nomenclator = null;
        NomenclatorCodeType nomCodeType = NomenclatorCodeType.fromNomType(tipNomenclator);
        if (nomCodeType != null) {
            if (!nomCodeType.isExportable())
                throw new NomenclatorExportException(NomenclatorExportCodes.NOMENCLATOR_ISNOT_EXPORTABLE, tipNomenclator);
            nomenclator = nomCodeType;
        }
        if (nomenclator != null) {
            File latestNomenclator = nomenclatorExportRepository.getLatestNomenclator(nomenclator,nomName);
            if (latestNomenclator == null) {
                throw new NomenclatorExportException(NomenclatorExportCodes.NOMENCLATOR_NOT_EXPORTED, tipNomenclator);
            }
            return latestNomenclator;
        } else {
            throw new NomenclatorExportException(NomenclatorExportCodes.NOMENCLATOR_NOT_FOUND, tipNomenclator);
        }
    }

}
