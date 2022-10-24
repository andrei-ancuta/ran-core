package ro.uti.ran.core.service.exportNomenclatoare;

import ro.uti.ran.core.model.registru.TipNomenclator;
import ro.uti.ran.core.service.exportNomenclatoare.data.summary.NomenclatorsSummary;
import ro.uti.ran.core.service.exportNomenclatoare.exception.NomenclatorExportException;

import java.io.File;
import java.io.Serializable;

/**
 * @author horia
 */
public interface IExportNomenclators extends Serializable {

    NomenclatorsSummary getNomenclatorsSummary() throws NomenclatorExportException;

    byte[] getNomenclatorsSummaryContent() throws NomenclatorExportException;

    byte[] exportNomenclatorContent(TipNomenclator tipNomenclator,String nomName) throws NomenclatorExportException;

    File exportNomenclatorContentAsFile(TipNomenclator tipNomenclator,String nomName) throws NomenclatorExportException;
}
