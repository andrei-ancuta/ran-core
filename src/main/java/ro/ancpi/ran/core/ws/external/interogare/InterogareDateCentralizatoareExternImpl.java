package ro.ancpi.ran.core.ws.external.interogare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ancpi.ran.core.ws.ExceptionUtilExternal;
import ro.uti.ran.core.common.ContextHolder;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.service.backend.DateRegistruXmlParser;
import ro.uti.ran.core.service.backend.InterogareDateCentralizatorService;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.service.security.SecurityWsService;
import ro.uti.ran.core.utils.ZipUtil;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.ancpi.ran.core.ws.fault.RanException;
import ro.ancpi.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.utils.ExceptionUtil;
import ro.uti.ran.core.xml.model.RanDoc;

import javax.jws.WebService;
import java.nio.charset.Charset;

import static ro.uti.ran.core.exception.codes.RequestCodes.INVALID_ELEMENT;
import static ro.uti.ran.core.exception.codes.RequestCodes.MISSING_ELEMENT;
import static ro.ancpi.ran.core.ws.WsUtilsExternal.checkRanAuthentication;

/**
 * Created by smash on 24/11/15.
 */
@WebService(
        serviceName = "InterogareDateCentralizatoareService",
        endpointInterface = "ro.ancpi.ran.core.ws.external.interogare.InterogareDateCentralizatoareExtern",
        targetNamespace = "http://interogare.external.ws.core.ran.ancpi.ro",
        portName = "InterogareDateExternServicePort")
@Service("interogareDateCentralizatoareExternService")
public class InterogareDateCentralizatoareExternImpl implements InterogareDateCentralizatoareExtern {

    @Autowired
    private InterogareDateCentralizatorService interogareDateCentralizatorService;

    @Autowired
    private DateRegistruXmlParser dateRegistruXmlParser;

    @Autowired
    private SecurityWsService securityWsService;

    @Autowired
    private ExceptionUtilExternal exceptionUtil;

    @Override
    public byte[] getDateCapitolCentralizator(Integer an, String codCapitol, Integer sirutaUAT, RanAuthentication ranAuthentication) throws RanRuntimeException, RanException {
        RanAuthorization ranAuthorization = checkRanAuthenticationEntity(ranAuthentication);

        if (an == null) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "an")));
        }

        if (null == sirutaUAT || sirutaUAT < 1) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(INVALID_ELEMENT, "sirutaUAT")));
        }

        if (null == codCapitol || codCapitol.isEmpty()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "codCapitol")));
        }

        try {
            interogareDateCentralizatorService.checkRight(sirutaUAT, ranAuthorization);
            //
            TipCapitol tipCapitol = TipCapitol.checkTipCapitol(codCapitol);
            RanDoc randoc = interogareDateCentralizatorService.getDateCapitol(sirutaUAT, an, tipCapitol);
            String xml = dateRegistruXmlParser.getXMLFromPojo(randoc);
            byte[] xmlCompresat = ZipUtil.compress(xml.getBytes(Charset.forName("UTF-8")));
            return xmlCompresat;
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable t) {
            throw exceptionUtil.buildException(new RanRuntimeException(t));
        }
    }

    private RanAuthorization checkRanAuthenticationEntity(RanAuthentication ranAuthentication) throws RanRuntimeException, RanException {
        RanAuthentication tempAuthentication = checkRanAuthentication(ranAuthentication, ContextHolder.getInstance().getHeadersContext());

        try {
            return securityWsService.authenticate(tempAuthentication);
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable t) {
            throw exceptionUtil.buildException(new RanRuntimeException(t));
        }

    }
}
