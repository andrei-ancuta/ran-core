package ro.uti.ran.core.ws.internal.interogare;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.exception.InterogareDateRegistruException;
import ro.uti.ran.core.exception.RequestValidationException;
import ro.uti.ran.core.exception.base.RanBusinessException;
import ro.uti.ran.core.model.portal.Institutie;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.repository.portal.InstitutieRepository;
import ro.uti.ran.core.repository.registru.NomUatRepository;
import ro.uti.ran.core.service.backend.DateRegistruXmlParser;
import ro.uti.ran.core.service.backend.InterogareDateService;
import ro.uti.ran.core.service.backend.dto.ParametriiInterogare;
import ro.uti.ran.core.utils.CnpValidator;
import ro.uti.ran.core.ws.fault.RanException;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.model.interogare.IdentificatorGospodarie;
import ro.uti.ran.core.ws.model.interogare.IdentificatorPF;
import ro.uti.ran.core.ws.utils.ExceptionUtil;
import ro.uti.ran.core.xml.model.RanDoc;
import ro.uti.ran.core.xml.model.types.CNP;

import java.util.ArrayList;
import java.util.List;

import static ro.uti.ran.core.exception.codes.InterogareDateRegistruCodes.INSUFICIENT_PRIVELEGES;
import static ro.uti.ran.core.exception.codes.RequestCodes.INVALID_ELEMENT;
import static ro.uti.ran.core.exception.codes.RequestCodes.MISSING_ELEMENT;
import static ro.uti.ran.core.service.backend.utils.TipCapitol.checkTipCapitol;
import static ro.uti.ran.core.ws.WsUtils.*;

/**
 * Created by Anastasia cea micuta on 10/11/2015.
 */
@Service("interogareDateService")
public class InterogareDateImpl implements InterogareDate {
    @Autowired
    private InterogareDateService interogareDateService;

    @Autowired
    private DateRegistruXmlParser dateRegistruXmlParser;

    @Autowired
    private NomUatRepository nomUatRepository;

    @Autowired
    private ExceptionUtil exceptionUtil;

    @Autowired
    private InstitutieRepository institutieRepository;

    @Override
    public ArrayList<IdentificatorGospodarie> getListaGospodariiPF(IdentificatorPF identificatorPF, Boolean activ, RanAuthorization ranAuthorization) throws RanException, RanRuntimeException {
        checkRanAuthorizationEntity(ranAuthorization, null);

        if (null == identificatorPF || null == identificatorPF.getIdentificator()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "identificatorPJ")));
        }

        try {
            switchOnRAL(ranAuthorization);

            return interogareDateService.getListaGospodariiPF(identificatorPF, activ, ranAuthorization);
        } catch (Throwable t) {
            throw exceptionUtil.buildException(new RanRuntimeException(t));
        } finally {
            switchOffRAL(ranAuthorization);
        }
    }

    @Override
    public List<IdentificatorGospodarie> getListaGospodariiPJ(String cui, Boolean activ, RanAuthorization ranAuthorization) throws RanException, RanRuntimeException {
        checkRanAuthorizationEntity(ranAuthorization, null);

        if (null == cui || cui.isEmpty()) {
            throw exceptionUtil.buildException(new RanException(new RequestValidationException(MISSING_ELEMENT, "cui")));
        }

        try {
            switchOnRAL(ranAuthorization);

            return interogareDateService.getListaGospodariiPJ(cui, activ, ranAuthorization);
        } catch (Throwable t) {
            throw exceptionUtil.buildException(new RanRuntimeException(t));
        } finally {
            switchOffRAL(ranAuthorization);
        }

    }

    private String getDateCapitol(IdentificatorGospodarie identificatorGospodarie, Integer sirutaUAT, String codCapitol, Integer an, Integer semestru, RanAuthorization ranAuthorization) throws RanException, RanRuntimeException {
        if (identificatorGospodarie != null) {
            return getDateCapitol(identificatorGospodarie.getId(), sirutaUAT, codCapitol, an, semestru, ranAuthorization);
        }
        return null;
    }


    @Override
    public String getDateCapitol(String idGospodarie, Integer sirutaUAT, String codCapitol, Integer an, Integer semestru, RanAuthorization ranAuthorization) throws RanException, RanRuntimeException {

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
            switchOnRAL(ranAuthorization);

            checkRanAuthorizationEntity(ranAuthorization, sirutaUAT);

            try {
                interogareDateService.checkGospodarieRight(sirutaUAT, idGospodarie, ranAuthorization);
            } catch (InterogareDateRegistruException e) {
                throw exceptionUtil.buildException(new RanException(new InterogareDateRegistruException(INSUFICIENT_PRIVELEGES, sirutaUAT)));
            }

            ParametriiInterogare parametriiInterogare =
                    new ParametriiInterogare
                            .ParametriiInterogareBuilder(sirutaUAT, idGospodarie, checkTipCapitol(codCapitol))
                            .an(an)
                            .semestru(null != semestru ? semestru.byteValue() : null)
                            .build();
            RanDoc randoc = interogareDateService.getDateCapitol(parametriiInterogare);
            String xml = dateRegistruXmlParser.getXMLFromPojo(randoc);
            return xml;
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } catch (Throwable t) {
            throw exceptionUtil.buildException(new RanRuntimeException(t));
        } finally {
            switchOffRAL(ranAuthorization);
        }
    }

    @Override
    public String getDateCapitolPF(IdentificatorPF identificatorPF, Integer sirutaUAT, String codCapitol, Integer an, Integer semestru, RanAuthorization ranAuthorization) throws RanException, RanRuntimeException {

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
            switchOnRAL(ranAuthorization);

            checkRanAuthorizationEntity(ranAuthorization, sirutaUAT);

            identificatorGospodarie = interogareDateService.getGospodariePF(identificatorPF, sirutaUAT, ranAuthorization);
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } finally {
            switchOffRAL(ranAuthorization);
        }

        return getDateCapitol(identificatorGospodarie, sirutaUAT, codCapitol, an, semestru, ranAuthorization);
    }

    @Override
    public String getDateCapitolPJ(String cui, Integer sirutaUAT, String codCapitol, Integer an, Integer semestru, RanAuthorization ranAuthorization) throws RanException, RanRuntimeException {

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
            switchOnRAL(ranAuthorization);

            checkRanAuthorizationEntity(ranAuthorization, sirutaUAT);

            identificatorGospodarie = interogareDateService.getGospodariePJ(cui, sirutaUAT, ranAuthorization);
        } catch (RanBusinessException e) {
            throw exceptionUtil.buildException(new RanException(e));
        } finally {
            switchOffRAL(ranAuthorization);
        }
        return getDateCapitol(identificatorGospodarie, sirutaUAT, codCapitol, an, semestru, ranAuthorization);
    }


    @Override
    public String getInterogareXsdSchema(RanAuthorization ranAuthorization) throws RanException, RanRuntimeException {
        checkRanAuthorizationEntity(ranAuthorization, null);

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

    private void checkRanAuthorizationEntity(RanAuthorization ranAuthorization, Integer codSirutaUAT) throws RanRuntimeException {
        checkRanAuthorization(ranAuthorization);

        if ("UAT".equals(ranAuthorization.getContext())) {
            NomUat nomUat = nomUatRepository.findOne(ranAuthorization.getIdEntity());
            if (null == nomUat) {
                throw exceptionUtil.buildException(new RanRuntimeException("Nu a fost identificat niciun UAT (id=" + ranAuthorization.getIdEntity() + ") in <RanAuthorization> SOAP header"));
            } else if (codSirutaUAT != null && !codSirutaUAT.equals(nomUat.getCodSiruta())) {
                throw exceptionUtil.buildException(new RanRuntimeException("Valoarea 'sirutaUAT' = '" + codSirutaUAT + "' nu coincide cu '" + nomUat.getCodSiruta() + "' corespunzatoare  UAT (id=" + ranAuthorization.getIdEntity() + ") in <RanAuthorization> SOAP header"));
            }
        } else {
            Institutie institutie = institutieRepository.findOne(ranAuthorization.getIdEntity());
            if (institutie == null || !(institutie.getCod().compareToIgnoreCase(ranAuthorization.getContext()) == 0)) {
                throw exceptionUtil.buildException(new RanRuntimeException("Nu a fost identificata institutia externa (id=" + ranAuthorization.getIdEntity() + ", cod=" + ranAuthorization.getContext() + ") in <RanAuthorization> SOAP header"));
            }
        }
    }
}
