package ro.uti.ran.core.service.registru;

import org.apache.cxf.common.i18n.Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.portal.UAT;
import ro.uti.ran.core.model.registru.NomUat;
import ro.uti.ran.core.repository.registru.NomUatRepository;
import ro.uti.ran.core.repository.registru.NumarSecventaUatRepository;
import ro.uti.ran.core.service.parametru.ParametruService;
import ro.uti.ran.core.ws.WsUtils;
import ro.uti.ran.core.ws.WsUtilsService;
import ro.uti.ran.core.ws.fault.RanRuntimeException;
import ro.uti.ran.core.ws.internal.RanAuthorization;
import ro.uti.ran.core.ws.internal.UatRanAuthorization;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Service
public class NumarSecventaUatServiceImpl implements NumarSecventaUatService {

    private static final Logger log = LoggerFactory.getLogger(NumarSecventaUatServiceImpl.class);

    @Autowired
    private NomUatRepository nomUatRepository;

    @Autowired
    private NumarSecventaUatRepository numarSecventaUatRepository;

    @Autowired
    private WsUtilsService wsUtilsService;

    @Autowired
    private ParametruService parametruService;


    @Override
    @Transactional("registruTransactionManager")
    public Long vizualizeazaNumarCerere(UatRanAuthorization ranAuthorization) throws NumarSecventaNotFoundException, RanRuntimeException {
        NomUat nomUat = getNomUat(ranAuthorization);
        try {
//            return numarSecventaUatRepository.viewMaxNrCerereByUat(nomUat.getId());
            return numarSecventaUatRepository.viewSecventaByBaseId(nomUat.getBaseId()).longValue();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new NumarSecventaNotFoundException("Nu s-a putut prelua un numar pentru UAT " + ranAuthorization.getIdEntity() + ": " + e.getMessage());
        }
    }

    @Override
    @Transactional("registruTransactionManager")
    public Long getNumarCerere(UatRanAuthorization ranAuthorization) throws NumarSecventaNotFoundException, RanRuntimeException {

        try {
            Long baseId = getBaseId(ranAuthorization);
            return numarSecventaUatRepository.getSecventaByBaseId(baseId).longValue();
        } catch (RanRuntimeException re) {
            throw re;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new NumarSecventaNotFoundException("Nu s-a putut prelua un numar pentru UAT " + ranAuthorization.getIdEntity() + ": " + e.getMessage());
        }

    }

    @Override
    @Transactional("registruTransactionManager")
    public void setSecventa(UatRanAuthorization ranAuthorization, BigDecimal value) throws NumarSecventaNotFoundException, RanRuntimeException {
        _setSecventa(ranAuthorization, value, true);
    }

    private void _setSecventa(UatRanAuthorization ranAuthorization, BigDecimal value, boolean testLimit) throws NumarSecventaNotFoundException, RanRuntimeException {
        try {
            NomUat nomUat = getNomUat(ranAuthorization);

            if (testLimit) {
//                Long maxNrCerereByUat = numarSecventaUatRepository.viewMaxNrCerereByUat(nomUat.getBaseId());
                Long maxNrCerereByUat = numarSecventaUatRepository.viewSecventaByBaseId(nomUat.getBaseId()).longValue();
                if (maxNrCerereByUat - value.longValue() >= -1) {
                    throw new RanRuntimeException("Secventa trebuie setata la o valoare mai mare decat cea existenta");
                }
            }

            _setSecventa(ranAuthorization, nomUat.getBaseId(), value);
        } catch (RanRuntimeException re) {
            throw re;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new NumarSecventaNotFoundException("Nu s-a putut seta secventa pentru UAT " + ranAuthorization.getIdEntity() + ": " + e.getMessage());
        }
    }

    private void _setSecventa(RanAuthorization ranAuthorization, Long baseId, BigDecimal value) throws NumarSecventaNotFoundException, RanRuntimeException {
        try {
            numarSecventaUatRepository.resetSecventa(baseId, value);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new NumarSecventaNotFoundException("Nu s-a putut seta secventa pentru UAT " + ranAuthorization.getIdEntity() + ": " + e.getMessage());
        }
    }


    protected Long getBaseId(UatRanAuthorization ranAuthorization) throws NumarSecventaNotFoundException, RanRuntimeException {
        NomUat nomUat = getNomUat(ranAuthorization);
        return nomUat.getBaseId();
    }

    protected NomUat getNomUat(UatRanAuthorization ranAuthorization) throws NumarSecventaNotFoundException, RanRuntimeException {

        if (ranAuthorization.getIdEntity() == null || 0 == ranAuthorization.getIdEntity()) {
            ranAuthorization.setIdEntity(nomUatRepository.findByCodSiruta(ranAuthorization.getCodSiruta()).getId());
        }

        WsUtils.checkRanAuthorization(ranAuthorization);

        if (!wsUtilsService.isUat(ranAuthorization)) {
            throw new IllegalArgumentException("Informatii autorizare incorecte");
        }

        UAT uat = wsUtilsService.getUatFrom(ranAuthorization);
        if (uat == null) {
            throw new NumarSecventaNotFoundException("Nu a fost gasit un UAT pentru id " + ranAuthorization.getIdEntity());
        }

        NomUat nomUat = nomUatRepository.findOne(uat.getId());
        if (nomUat == null) {
            throw new NumarSecventaNotFoundException("Nu a fost gasit un UAT pentru id " + uat.getId());
        }

        return nomUat;
    }

    @Override
    @Transactional("registruTransactionManager")
    public void resetSecventa(UatRanAuthorization ranAuthorization)
            throws NumarSecventaNotFoundException, RanRuntimeException, NumarSecventaInvalidException {

        int interval = 15;

        try {
            interval = Integer.valueOf(parametruService.getParametru(ParametruService.PARAM_CONFIG_RESET_SECV).getValoare());
        } catch (Throwable t) {
            log.debug("Nu a putut fi preluat numarul de zile in care se poate face resetarea secvenetei", t);
        }

        if (!testResetDate(interval)) {
            throw new NumarSecventaInvalidException("A fost depasita perioada de " + interval + " zile in care se putea reseta secventa");
        }

        _setSecventa(ranAuthorization, BigDecimal.valueOf(1L), false);
    }

    private boolean testResetDate(final int interval) {
        int year = new GregorianCalendar().get(Calendar.YEAR);

        int previousYear = (new GregorianCalendar() {
            private static final long serialVersionUID = 1L;

            {
                add(Calendar.DATE, -interval);
            }
        }).get(Calendar.YEAR);

        int nextYear = (new GregorianCalendar() {
            private static final long serialVersionUID = 1L;

            {
                add(Calendar.DATE, interval);
            }
        }).get(Calendar.YEAR);

        if (year != previousYear || year != nextYear) {
            return true;
        }

        return false;
    }

}
