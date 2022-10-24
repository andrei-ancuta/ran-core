package ro.uti.ran.core.ws.internal.interogare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.repository.portal.InstitutieRepository;
import ro.uti.ran.core.repository.registru.NomUatRepository;
import ro.uti.ran.core.service.backend.DateRegistruXmlParser;
import ro.uti.ran.core.service.backend.InterogareDateCentralizatorService;
import ro.uti.ran.core.service.backend.utils.TipCapitol;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.utils.ExceptionUtil;
import ro.uti.ran.core.xml.model.RanDoc;

import static ro.uti.ran.core.exception.codes.RequestCodes.INVALID_ELEMENT;
import static ro.uti.ran.core.exception.codes.RequestCodes.MISSING_ELEMENT;
import static ro.uti.ran.core.ws.WsUtils.*;

/**
 * Created by smash on 24/11/15.
 */

@Service("interogareDateCentralizatoare")
public class InterogareDateCentralizatoareImpl implements InterogareDateCentralizatoare {

    @Autowired
    private InterogareDateCentralizatorService interogareDateCentralizatorService;

    @Autowired
    private DateRegistruXmlParser dateRegistruXmlParser;

    @Autowired
    private NomUatRepository nomUatRepository;

    @Autowired
    private ExceptionUtil exceptionUtil;

    @Autowired
    private InstitutieRepository institutieRepository;

    @Override
    public String getDateCapitolCentralizator(Integer an, String codCapitol, Integer sirutaUAT, RanAuthorization ranAuthorization) throws RanRuntimeException, RanException {
        checkRanAuthorizationEntity(ranAuthorization);

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
            switchOnRAL(ranAuthorization);

            TipCapitol tipCapitol = TipCapitol.checkTipCapitol(codCapitol);
            RanDoc randoc = interogareDateCentralizatorService.getDateCapitol(sirutaUAT, an, tipCapitol);
            return dateRegistruXmlParser.getXMLFromPojo(randoc);
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable t) {
            throw exceptionUtil.buildException(new RanRuntimeException(t));
        } finally {
            switchOffRAL(ranAuthorization);
        }
    }

    private void checkRanAuthorizationEntity(RanAuthorization ranAuthorization) throws RanRuntimeException {
        checkRanAuthorization(ranAuthorization);

        if ("UAT".equals(ranAuthorization.getContext())) {
            NomUat nomUat = nomUatRepository.findOne(ranAuthorization.getIdEntity());
            if (null == nomUat) {
                throw exceptionUtil.buildException(new RanRuntimeException("Nu a fost identificat niciun UAT (id=" + ranAuthorization.getIdEntity() + ") in <RanAuthorization> SOAP header"));
            }
        } else {
            Institutie institutie = institutieRepository.findOne(ranAuthorization.getIdEntity());
            if (institutie == null || !(institutie.getCod().compareToIgnoreCase(ranAuthorization.getContext()) == 0)) {
                throw exceptionUtil.buildException(new RanRuntimeException("Nu a fost identificata institutia externa (id=" + ranAuthorization.getIdEntity() + ", cod=" + ranAuthorization.getContext() + ") in <RanAuthorization> SOAP header"));
            }
        }
    }
}
