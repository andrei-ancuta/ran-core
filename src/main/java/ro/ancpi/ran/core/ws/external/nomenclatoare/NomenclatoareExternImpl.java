package ro.ancpi.ran.core.ws.external.nomenclatoare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ancpi.ran.core.ws.ExceptionUtilExternal;
import ro.uti.ran.core.common.ContextHolder;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.service.exportNomenclatoare.IExportNomenclators;
import ro.uti.ran.core.service.exportNomenclatoare.NomenclatorSchemaGen;
import ro.uti.ran.core.service.security.SecurityWsService;
import ro.uti.ran.core.utils.ZipUtil;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.ancpi.ran.core.ws.fault.RanException;
import ro.ancpi.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;

import javax.jws.WebService;
import java.io.ByteArrayOutputStream;

import static ro.uti.ran.core.exception.codes.RequestCodes.MISSING_ELEMENT;
import static ro.uti.ran.core.model.registru.TipNomenclator.checkCodNomenclator;
import static ro.ancpi.ran.core.ws.WsUtilsExternal.checkRanAuthentication;

/**
 * Created by Anastasia cea micuta on 11/30/2015.
 */
@WebService(
        serviceName = "NomenclatorExternService",
        endpointInterface = "ro.ancpi.ran.core.ws.external.nomenclatoare.NomenclatoareExtern",
        targetNamespace = "http://nomenclatoare.external.ws.core.ran.ancpi.ro",
        portName = "NomenclatorExternServicePort")
@Service("nomenclatorExternService")
public class NomenclatoareExternImpl implements NomenclatoareExtern {

    @Autowired
    private IExportNomenclators nomenclatorExportService;

    @Autowired
    private SecurityWsService securityWsService;

    @Autowired
    private ExceptionUtilExternal exceptionUtil;

    @Autowired
    private NomenclatorSchemaGen nomenclatorSchemaGen;

    @Override
    public byte[] getListaNomenclatoare(RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthentication tempAuthentication = checkRanAuthentication(ranAuthentication, ContextHolder.getInstance().getHeadersContext());

        try {
            RanAuthorization ranAuthorization = securityWsService.authenticate(tempAuthentication);

            byte[] listaNomenclatoareBytes = nomenclatorExportService.getNomenclatorsSummaryContent();
            byte[] listaNomenclatoareCompressBytes = ZipUtil.compress(listaNomenclatoareBytes);
            return listaNomenclatoareCompressBytes;
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }
    }

    @Override
    public byte[] getNomenclator(String codNomenclator, RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthentication tempAuthentication = checkRanAuthentication(ranAuthentication, ContextHolder.getInstance().getHeadersContext());

        if (null == codNomenclator || codNomenclator.isEmpty()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "codNomenclator")));
        }

        try {
            RanAuthorization ranAuthorization = securityWsService.authenticate(tempAuthentication);

            String tipNomenclator = codNomenclator;
            int pos = codNomenclator.indexOf('-');
            if (pos > 0) {
                tipNomenclator = tipNomenclator.substring(0, pos);
            }

            byte[] listaNomenclatoareBytes = nomenclatorExportService.exportNomenclatorContent(checkCodNomenclator(tipNomenclator), codNomenclator);
            byte[] listaNomenclatoareCompressBytes = ZipUtil.compress(listaNomenclatoareBytes);
            return listaNomenclatoareCompressBytes;
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }
    }

    @Override
    public String getListaNomenclatoareXsdSchema(RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthentication tempAuthentication = checkRanAuthentication(ranAuthentication, ContextHolder.getInstance().getHeadersContext());

        try {
            RanAuthorization ranAuthorization = securityWsService.authenticate(tempAuthentication);

            String xsd = nomenclatorSchemaGen.generateListaNomenclatoareXsdSchema();
            return xsd;
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }
    }

    @Override
    public String getNomenclatorXsdSchema(RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthentication tempAuthentication = checkRanAuthentication(ranAuthentication, ContextHolder.getInstance().getHeadersContext());

        try {
            RanAuthorization ranAuthorization = securityWsService.authenticate(tempAuthentication);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            String xsd = nomenclatorSchemaGen.generateNomenclatorXsdSchema();
            return xsd;
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }
    }


}
