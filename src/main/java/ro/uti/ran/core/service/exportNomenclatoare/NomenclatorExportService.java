package ro.uti.ran.core.service.exportNomenclatoare;

import ro.uti.ran.core.service.exportNomenclatoare.exception.NomenclatorExportException;

/**
 * Created with IntelliJ IDEA. User: mala
 */
public interface NomenclatorExportService {

    /**
     * @return
     */
    void exportAllNomenclators() throws NomenclatorExportException;

}
