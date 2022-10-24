package ro.uti.ran.core.ws.internal.transmitere;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Parametru;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.repository.registru.NomUatRepository;
import ro.uti.ran.core.service.backend.ProcesareDateRegistruService;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.model.transmitere.InformatiiTransmisie;
import ro.uti.ran.core.ws.model.transmitere.ModalitateProcesareDate;
import ro.uti.ran.core.ws.model.transmitere.ModalitateTransmitere;
import ro.uti.ran.core.ws.utils.ExceptionUtil;

import java.util.Arrays;
import java.util.UUID;

import static ro.uti.ran.core.exception.codes.RequestCodes.*;
import static ro.uti.ran.core.ws.WsUtils.checkRanAuthorization;
import static ro.uti.ran.core.ws.WsUtils.switchOffRAL;
import static ro.uti.ran.core.ws.WsUtils.switchOnRAL;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-29 17:16
 */

@Service("transmitereDateService")
public class TransmitereDateImpl implements TransmitereDate {

    @Autowired
    private TransmitereDateService transmitereDateService;

    @Autowired
    private ParametruService parametruService;

    @Autowired
    @Qualifier("procesareDateRegistruService")
    private ProcesareDateRegistruService procesareDateRegistruService;

    @Autowired
    private NomUatRepository nomUatRepository;

    @Autowired
    private ExceptionUtil exceptionUtil;

    @Override
    public void transmitere(String xmlCDATA, ModalitateTransmitere modalitateTransmitere, RanAuthorization ranAuthorization) throws RanException, RanRuntimeException {
        checkRanAuthorizationUAT(ranAuthorization);

        try {
            switchOnRAL(ranAuthorization);

            if (null == xmlCDATA) {
                throw new RequestValidationException(MISSING_XML_CDATA);
            }

            if (null == modalitateTransmitere) {
                throw new RequestValidationException(MISSING_ELEMENT, "modalitateTransmitere: " + Arrays.toString(ModalitateTransmitere.values()));
            }

            if(Boolean.TRUE.equals(ranAuthorization.getLocal())) {
                procesareDateRegistruService.procesareDateXml(xmlCDATA, ranAuthorization.getIdEntity(), true);
            } else {
                Long idRegistru = transmitereDateService.transmitere(xmlCDATA, modalitateTransmitere, ranAuthorization);

                // numai daca tipul de transmitere este asynch facem operatia tranzactional (adica punerea in coada este tranzactionala cu transmiterea -> punerea in coada este garantata)
                // totusi punerea in coada nu este chiar obligatorie pentru ca oricum job-urile de procesare vor fi preluate de catre un scheduler care le va procesa la fiecare final de zi
                ModalitateProcesareDate modalitateProcesareDate = getTipTransmitereFromParamConfig();
                if (ModalitateProcesareDate.SYNCHRONOUS == modalitateProcesareDate) {
                    procesareDateRegistruService.procesareDateRegistru(idRegistru);
                }
            }


        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        } finally {
            switchOffRAL(ranAuthorization);
        }
    }

    @Override
    public String getTransmitereXsdSchema(RanAuthorization ranAuthorization) throws RanException, RanRuntimeException {
        checkRanAuthorizationUAT(ranAuthorization);

        try {
            return transmitereDateService.getTransmitereXsdSchema(ranAuthorization);
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }
    }

    @Override
    public InformatiiTransmisie getStatusTransmisie(String uuidTransmisie, RanAuthorization ranAuthorization) throws RanException, RanRuntimeException {
        checkRanAuthorizationUAT(ranAuthorization);

        if (null == uuidTransmisie || uuidTransmisie.isEmpty()) {
            throw new RuntimeException(new RequestValidationException(MISSING_UUID_TRANSMISIE));
        }

        try {
            UUID uuid = UUID.fromString(uuidTransmisie);
            return transmitereDateService.getStatusTransmisie(uuid.toString().toLowerCase(), ranAuthorization);
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable e) {
            throw exceptionUtil.buildException(new RanRuntimeException(e));
        }
    }

    private ModalitateProcesareDate getTipTransmitereFromParamConfig() {
        Parametru parametru = parametruService.getParametru( ParametruService.PARAM_CONFIG_TIP_TRANS_COD);
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

    private void checkRanAuthorizationUAT(RanAuthorization ranAuthorization) throws RanRuntimeException {
        checkRanAuthorization(ranAuthorization);

        if ("UAT".equals(ranAuthorization.getContext())) {
            NomUat nomUat = nomUatRepository.findOne(ranAuthorization.getIdEntity());
            if (null == nomUat) {
                throw exceptionUtil.buildException(new RanRuntimeException("Nu a fost identificat niciun UAT (id=" + ranAuthorization.getIdEntity() + ") in RanAuthorization SOAP header"));
            }
        } else {
            throw exceptionUtil.buildException(new RanRuntimeException("In headerul SOAP RanAuthorization pe serviciul de transmitere date se permite apelul doar de catre context UAT"));
        }
    }
}
