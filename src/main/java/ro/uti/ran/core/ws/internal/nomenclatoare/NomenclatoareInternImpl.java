package ro.uti.ran.core.ws.internal.nomenclatoare;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.service.exportNomenclatoare.IExportNomenclators;
import ro.uti.ran.core.service.exportNomenclatoare.data.summary.NomenclatorsSummary;
import ro.uti.ran.core.service.exportNomenclatoare.exception.NomenclatorExportException;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import javax.jws.WebService;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static ro.uti.ran.core.exception.codes.RequestCodes.MISSING_ELEMENT;
import static ro.uti.ran.core.model.registru.TipNomenclator.checkCodNomenclator;

/**
 * Created by Stanciu Neculai on 03.Dec.2015.
 */
@WebService(
        serviceName = "NomenclatorInternService",
        endpointInterface = "ro.uti.ran.core.ws.internal.nomenclatoare.NomenclatoareIntern",
        targetNamespace = "http://nomenclatoare.internal.ws.core.ran.uti.ro",
        portName = "NomenclatorInternServicePort")
@Service("nomenclatorInternService")
public class NomenclatoareInternImpl implements NomenclatoareIntern {
    private static final Log log = LogFactory.getLog(NomenclatoareInternImpl.class);
    @Autowired
    private IExportNomenclators nomenclatorExportService;

    @Autowired
    private ExceptionUtil exceptionUtil;

    @Override
    public NomenclatorsSummary getListaNomenclatoare() throws RanException, RanRuntimeException {
        try {
            return nomenclatorExportService.getNomenclatorsSummary();
        } catch (NomenclatorExportException e) {
            throw new RanException(e);
        }
    }

    @Override
    public byte[] getNomenclator(String codNomenclator) throws RanException, RanRuntimeException {
        if (null == codNomenclator || codNomenclator.isEmpty()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "codNomenclator")));
        }
        try {
            String tipNomenclator = codNomenclator;
            int pos = codNomenclator.indexOf('-');
            if (pos > 0) {
                tipNomenclator = tipNomenclator.substring(0, pos);
            }
            File listaNomenclatoareFile = nomenclatorExportService.exportNomenclatorContentAsFile(checkCodNomenclator(tipNomenclator), codNomenclator);
            byte[] listaNomenclatoareCompressBytes = zipFile(listaNomenclatoareFile,codNomenclator);
            return listaNomenclatoareCompressBytes;
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }
    }

    private byte[] zipFile(File listaNomenclatoareFile, String codNomenclator) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream);
        try {
            ZipEntry entry = new ZipEntry(listaNomenclatoareFile.getName());
            zos.putNextEntry(entry);
            zos.write(IOUtils.toByteArray(new FileInputStream(listaNomenclatoareFile)));
            zos.closeEntry();
            zos.finish();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("Error",e);
            return null;
        } finally {
            IOUtils.closeQuietly(zos);
        }
    }
}
