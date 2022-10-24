package ro.ancpi.ran.core.ws.external.transmitere;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ro.ancpi.ran.core.ws.ExceptionUtilExternal;
import ro.ancpi.ran.core.ws.fault.RanException;
import ro.ancpi.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.common.ContextHolder;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.WsAuthenticationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.service.backend.ProcesareDateRegistruService;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.service.security.SecurityWsService;
import ro.uti.ran.core.utils.ZipUtil;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.internal.transmitere.TransmitereDateService;
import ro.uti.ran.core.ws.model.transmitere.InformatiiTransmisie;
import ro.uti.ran.core.ws.model.transmitere.ModalitateProcesareDate;

import javax.jws.WebService;
import java.nio.charset.Charset;
import java.util.UUID;

import static ro.ancpi.ran.core.ws.WsUtilsExternal.checkRanAuthentication;
import static ro.uti.ran.core.exception.codes.RequestCodes.MISSING_ELEMENT;
import static ro.uti.ran.core.exception.codes.RequestCodes.MISSING_UUID_TRANSMISIE;
import static ro.uti.ran.core.exception.codes.WsAutenticationCodes.TRANSMITERE_UAT_LOCAL;
import static ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere.AUTOMAT;


/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-29 17:16
 */
@WebService(
        serviceName = "TransmitereDateCentralizatoareService",
        endpointInterface = "ro.ancpi.ran.core.ws.external.transmitere.TransmitereDateExtern",
        targetNamespace = "http://transmitere.external.ws.core.ran.ancpi.ro",
        portName = "TransmitereDateExternServicePort")
@Service("transmitereDateExternService")
public class TransmitereDateExternImpl implements TransmitereDateExtern {

    @Autowired
    private TransmitereDateService transmitereDateService;

    @Autowired
    private ParametruService parametruService;

    @Autowired
    @Qualifier("procesareDateRegistruService")
    private ProcesareDateRegistruService procesareDateRegistruService;

    @Autowired
    private SecurityWsService securityWsService;

    @Autowired
    private ExceptionUtilExternal exceptionUtil;

    @Override
    public void transmitere(byte[] xmlCompresat, RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthentication tempAuthentication = checkRanAuthentication(ranAuthentication, ContextHolder.getInstance().getHeadersContext());

        if (null == xmlCompresat) {
            throw new RuntimeException(new RequestValidationException(MISSING_ELEMENT, "xmlCompresat"));
        }

        try {
            RanAuthorization ranAuthorization = securityWsService.authenticate(tempAuthentication);

            //Pentru transmitere sa fie autentificate doar conturile de UAT locale
            if (!Boolean.TRUE.equals(ranAuthorization.getUATLocal())) {
                throw new WsAuthenticationException(TRANSMITERE_UAT_LOCAL, ranAuthentication.getUsername());
            }

            byte[] xmlBinary = ZipUtil.decompress(xmlCompresat);
            String xml = new String(xmlBinary, Charset.forName("UTF-8"));

            Long idRegistru = transmitereDateService.transmitere(xml, AUTOMAT, ranAuthorization);

            // numai daca tipul de transmitere este asynch facem operatia tranzactional (adica punerea in coada este tranzactionala cu transmiterea -> punerea in coada este garantata)
            // totusi punerea in coada nu este chiar obligatorie pentru ca oricum job-urile de procesare vor fi preluate de catre un scheduler care le va procesa la fiecare final de zi
            ModalitateProcesareDate modalitateProcesareDate = getTipTransmitereFromParamConfig();
            if (ModalitateProcesareDate.SYNCHRONOUS == modalitateProcesareDate) {
                procesareDateRegistruService.procesareDateRegistru(idRegistru);
            }
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }
    }

    @Override
    public String getTransmitereXsdSchema(RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthentication tempAuthentication = checkRanAuthentication(ranAuthentication, ContextHolder.getInstance().getHeadersContext());

        try {
            RanAuthorization ranAuthorization = securityWsService.authenticate(tempAuthentication);

            return transmitereDateService.getTransmitereXsdSchema(ranAuthorization);
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }
    }

    @Override
    public InformatiiTransmisie getStatusTransmisie(String uuidTransmisie, RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthentication tempAuthentication = checkRanAuthentication(ranAuthentication, ContextHolder.getInstance().getHeadersContext());

        if (null == uuidTransmisie || uuidTransmisie.isEmpty()) {
            throw new RuntimeException(new RequestValidationException(MISSING_UUID_TRANSMISIE));
        }

        try {
            RanAuthorization ranAuthorization = securityWsService.authenticate(tempAuthentication);

            UUID uuid = UUID.fromString(uuidTransmisie);
            return transmitereDateService.getStatusTransmisie(uuid.toString().toLowerCase(), ranAuthorization);
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }
    }

    private ModalitateProcesareDate getTipTransmitereFromParamConfig() {
        Parametru parametru = parametruService.getParametru(ParametruService.PARAM_CONFIG_TIP_TRANS_COD);
        Boolean isAsync = Boolean.valueOf(parametru.getValoare());
        if (parametru.getValoare() != null && !parametru.getValoare().isEmpty() && isAsync != null) {
            if (isAsync) {
                return ModalitateProcesareDate.ASYNCHRONOUS;
            } else {
                return ModalitateProcesareDate.SYNCHRONOUS;
            }
        } else {
            //valoarea implicita ASYNCHRONOUS
            return ModalitateProcesareDate.ASYNCHRONOUS;
        }
    }
}
