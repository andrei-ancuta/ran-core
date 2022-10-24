package ro.ancpi.ran.core.ws.external.interogare;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ro.ancpi.ran.core.ws.ExceptionUtilExternal;
import ro.ancpi.ran.core.ws.fault.RanException;
import ro.ancpi.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.common.ContextHolder;
import ro.uti.ran.core.exception.InterogareDateRegistruException;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.service.backend.DateRegistruXmlParser;
import ro.uti.ran.core.service.backend.InterogareDateService;
import ro.uti.ran.core.service.backend.dto.ParametriiInterogare;
import ro.uti.ran.core.service.security.SecurityWsService;
import ro.uti.ran.core.utils.CnpValidator;
import ro.uti.ran.core.utils.ZipUtil;
import ro.uti.ran.core.ws.external.RanAuthentication;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.model.interogare.IdentificatorGospodarie;
import ro.uti.ran.core.ws.model.interogare.IdentificatorPF;
import ro.uti.ran.core.xml.model.RanDoc;
import ro.uti.ran.core.xml.model.types.CNP;

import javax.jws.WebService;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static ro.ancpi.ran.core.ws.WsUtilsExternal.checkRanAuthentication;
import static ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes.INSUFICIENT_PRIVELEGES;
import static ro.uti.ran.core.exception.codes.RequestCodes.INVALID_ELEMENT;
import static ro.uti.ran.core.exception.codes.RequestCodes.MISSING_ELEMENT;
import static ro.uti.ran.core.service.backend.utils.TipCapitol.checkTipCapitol;

/**
 * Created by Anastasia cea micuta on 10/11/2015.
 */
@WebService(
        serviceName = "InterogareDateService",
        endpointInterface = "ro.ancpi.ran.core.ws.external.interogare.InterogareDateExtern",
        targetNamespace = "http://interogare.external.ws.core.ran.ancpi.ro",
        portName = "InterogareDateExternServicePort")
@Service("interogareDateExternService")
public class InterogareDateExternImpl implements InterogareDateExtern {
    @Autowired
    private InterogareDateService interogareDateService;

    @Autowired
    private DateRegistruXmlParser dateRegistruXmlParser;

    @Autowired
    private SecurityWsService securityWsService;

    @Autowired
    private ExceptionUtilExternal exceptionUtil;


    @Override
    public ArrayList<IdentificatorGospodarie> getListaGospodariiPF(IdentificatorPF identificatorPF, Boolean activ, RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthorization ranAuthorization = checkRanAuthenticationEntity(ranAuthentication);

        if (null == identificatorPF && null == identificatorPF.getIdentificator()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "identificatorPJ")));
        }

        try {
            return interogareDateService.getListaGospodariiPF(identificatorPF, activ, ranAuthorization);
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }

    }

    @Override
    public List<IdentificatorGospodarie> getListaGospodariiPJ(String cui, Boolean activ, RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthorization ranAuthorization = checkRanAuthenticationEntity(ranAuthentication);

        if (null == cui || cui.isEmpty()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "cui")));
        }

        try {
            return interogareDateService.getListaGospodariiPJ(cui, activ, ranAuthorization);
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }

    }

    private byte[] getDateCapitol(IdentificatorGospodarie identificatorGospodarie, Integer sirutaUAT, String codCapitol, Integer an, Integer semestru, RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        if (identificatorGospodarie != null) {
            return getDateCapitol(identificatorGospodarie.getId(), sirutaUAT, codCapitol, an, semestru, ranAuthentication);
        }
        return null;
    }


    @Override
    public byte[] getDateCapitol(String idGospodarie, Integer sirutaUAT, String codCapitol, Integer an, Integer semestru, RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthorization ranAuthorization = checkRanAuthenticationEntity(ranAuthentication);


        if (null == idGospodarie || idGospodarie.isEmpty()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "idGospodarie")));
        }

        if (null == sirutaUAT || sirutaUAT < 1) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(INVALID_ELEMENT, "sirutaUAT")));
        }

        if (null == codCapitol || codCapitol.isEmpty()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "codCapitol")));
        }

        try {
            interogareDateService.checkGospodarieRight(sirutaUAT, idGospodarie, ranAuthorization);
        } catch (InterogareDateRegistruException e) {
            throw exceptionUtil.buildException(new RanException(new InterogareDateRegistruException(INSUFICIENT_PRIVELEGES, sirutaUAT)));
        }

        try {
            ParametriiInterogare parametriiInterogare =
                    new ParametriiInterogare
                            .ParametriiInterogareBuilder(sirutaUAT, idGospodarie, checkTipCapitol(codCapitol))
                            .an(an)
                            .semestru(null != semestru ? semestru.byteValue() : null)
                            .build();
            RanDoc randoc = interogareDateService.getDateCapitol(parametriiInterogare);
            String xml = dateRegistruXmlParser.getXMLFromPojo(randoc);
            byte[] xmlCompresat = ZipUtil.compress(xml.getBytes(Charset.forName("UTF-8")));
            return xmlCompresat;
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable t) {
            throw exceptionUtil.buildException(new RanRuntimeException(t));
        }
    }

    @Override
    public byte[] getDateCapitolPF(IdentificatorPF identificatorPF, Integer sirutaUAT, String codCapitol, Integer an, Integer semestru, RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthorization ranAuthorization = checkRanAuthenticationEntity(ranAuthentication);

        if (null == identificatorPF || null == identificatorPF.getIdentificator()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "identificatorPJ")));
        } else {
            if (identificatorPF.getIdentificator() instanceof CNP && !CnpValidator.isValid(((CNP) identificatorPF.getIdentificator()).getValue())) {
                throw exceptionUtil.buildException(new RanException(new RequestValidationException(INVALID_ELEMENT, "cnp")));
            }
        }

        if (null == sirutaUAT || sirutaUAT < 1) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(INVALID_ELEMENT, "sirutaUAT")));
        }

        if (null == codCapitol || codCapitol.isEmpty()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "codCapitol")));
        }


        IdentificatorGospodarie identificatorGospodarie;
        try {
            identificatorGospodarie = interogareDateService.getGospodariePF(identificatorPF, sirutaUAT, ranAuthorization);
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        }
        return getDateCapitol(identificatorGospodarie, sirutaUAT, codCapitol, an, semestru, ranAuthentication);
    }

    @Override
    public byte[] getDateCapitolPJ(String cui, Integer sirutaUAT, String codCapitol, Integer an, Integer semestru, RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthorization ranAuthorization = checkRanAuthenticationEntity(ranAuthentication);

        if (null == cui || cui.isEmpty()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "cui")));
        }

        if (null == sirutaUAT || sirutaUAT < 1) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(INVALID_ELEMENT, "sirutaUAT")));
        }

        if (null == codCapitol || codCapitol.isEmpty()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "codCapitol")));
        }

        IdentificatorGospodarie identificatorGospodarie = null;
        try {
            identificatorGospodarie = interogareDateService.getGospodariePJ(cui, sirutaUAT, ranAuthorization);
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        }
        return getDateCapitol(identificatorGospodarie, sirutaUAT, codCapitol, an, semestru, ranAuthentication);
    }


    @Override
    public String getInterogareXsdSchema(RanAuthentication ranAuthentication) throws RanException, RanRuntimeException {
        RanAuthorization ranAuthorization = checkRanAuthenticationEntity(ranAuthentication);

        try {
            Resource resource = dateRegistruXmlParser.getRanDocXsdSchema();
            String xsd = new String(IOUtils.toByteArray(resource.getInputStream()), "UTF-8");
            return xsd;
//        } catch (RanBusinessException e) {
//            e.printStackTrace();
//            throw new RanException(e);
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
