package ro.uti.ran.core.service.exportNomenclatoare.data;

import ro.uti.ran.core.service.backend.nomenclator.type.ExportStatusType;

import java.util.Date;

/**
 * Created with IntelliJ IDEA. User: mala
 */
public class NomenclatorExportStatus {

    private Date exportedDate;

    private ExportStatusType statusType = ExportStatusType.SUCCESS;

    private String nomenclatorName;

    private String filePath;

    private boolean needToBeExported = true;

    private Date lastUpdateDate;

    public Date getExportedDate() {
        return exportedDate;
    }

    public void setExportedDate(Date exportedDate) {
        this.exportedDate = exportedDate;
    }

    public ExportStatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(ExportStatusType statusType) {
        this.statusType = statusType;
    }

    public String getNomenclatorName() {
        return nomenclatorName;
    }

    public void setNomenclatorName(String nomenclatorName) {
        this.nomenclatorName = nomenclatorName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isNeedToBeExported() {
        return needToBeExported;
    }

    public void setNeedToBeExported(boolean needToBeExported) {
        this.needToBeExported = needToBeExported;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }


}
