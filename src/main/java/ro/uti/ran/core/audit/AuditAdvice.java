package ro.uti.ran.core.audit;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.model.portal.Context;
import ro.uti.ran.core.model.portal.Sesiune;
import ro.uti.ran.core.model.portal.Utilizator;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.repository.portal.ContextRepository;
import ro.uti.ran.core.repository.portal.SesiuneRepository;
import ro.uti.ran.core.repository.portal.UtilizatorRepository;
import ro.uti.ran.core.ws.utils.AuditInfo;
import ro.uti.ran.core.ws.utils.AuditInfoThreadLocal;

import java.util.Date;

/**
 * Created by Stanciu Neculai on 04.Jan.2016.
 */
@Aspect
@Order(value = RanConstants.AUDIT_ORDER)
@Component
public class AuditAdvice {
    public static final Logger log = LoggerFactory.getLogger(AuditAdvice.class);

    @Autowired
    @Qualifier("auditServicePortal")
    private AuditService auditServicePortal;

    @Autowired
    @Qualifier("auditServiceRegistru")
    private AuditService auditServiceRegistru;

    @Autowired
    private UtilizatorRepository utilizatorRepository;

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    private SesiuneRepository sesiuneRepository;


    @Around("@annotation(audit)")
    public Object afterOperation(ProceedingJoinPoint joinPoint, Audit audit) throws Throwable {
        //before
        log.debug("Start audit ****************************");
        try {
            Object result = joinPoint.proceed();
            setArgUserNameforToken(joinPoint, audit);
            auditAfterOperation(joinPoint, audit, result);
            return result;
        } catch (Throwable t) {
            auditAfterOperation(joinPoint, audit, t);
            throw t;
        }


    }




    private void auditAfterOperation(ProceedingJoinPoint joinPoint, Audit audit, Throwable t) {
        //after
        AuditInfo auditInfo = getAuditInfo(audit);
        if (audit.opType().getTransactionUnit().equals(TipTranzactieEnum.TRANSACTION_UNIT_PORTAL)) {
                auditServicePortal.audit(audit, joinPoint.getArgs(), t, auditInfo);
        } else if (audit.opType().getTransactionUnit().equals(TipTranzactieEnum.TRANSACTION_UNIT_REGISTRU)) {
                auditServiceRegistru.audit(audit, joinPoint.getArgs(), t, auditInfo);
        } else {
            log.debug("End audit****************************");
        }
    }

    private void auditAfterOperation(ProceedingJoinPoint joinPoint, Audit audit, Object result) {
        //after
        AuditInfo auditInfo = getAuditInfo(audit);
        Object[] args = joinPoint.getArgs();
        filterArgs(joinPoint.getSignature(), args);

        if (audit.opType().getTransactionUnit().equals(TipTranzactieEnum.TRANSACTION_UNIT_PORTAL)) {
                            auditServicePortal.audit(audit, args, result, auditInfo);
        } else if (audit.opType().getTransactionUnit().equals(TipTranzactieEnum.TRANSACTION_UNIT_REGISTRU)) {
                auditServiceRegistru.audit(audit, args, result, auditInfo);
        } else {
            log.debug("End audit****************************");
        }
    }

    private void filterArgs(Signature signature, Object[] args) {
        if (null != args) {
            if (signature.getName().equals("login")) {
                censor(args, 1);
            } else if (signature.getName().equals("changeMyPassword")) {
                censor(args, 1, 2);
            } else if (signature.getName().equals("salveazaToken")) {
                censor(args, 1, 2);
            } else if(signature.getName().equals("resetMyPassword")){
                censor(args, 1);
            }
        }
    }



    private void censor(Object[] args, int... pos) {
        for (int i : pos) {
            args[i] = "XXXXXXX";
        }
    }

    private AuditInfo getAuditInfo(Audit audit) {
        AuditInfo auditInfo = AuditInfoThreadLocal.get();
        if (auditInfo != null) {
            Sesiune sesiuneaCurenta = auditInfo.getSesiune();
            if (sesiuneaCurenta == null) {
//          Cazul 1 - Cand exista X-Username + X-CONTEXT si sesiunea este nula
                if (auditInfo.getUsername() != null && !auditInfo.getUsername().isEmpty() && auditInfo.getContext() != null && !auditInfo.getContext().isEmpty()) {
                    Sesiune sesiuneaNoua = createSesiuneAndSave(auditInfo);
                    if (sesiuneaNoua != null) {
                        auditInfo.setSesiune(sesiuneaNoua);
                    }
                }
//          Cazul 2 - Cand OpType = Auth se utilizeaza sesiunea cu id 0, adica ses. anonima
                if (AuditOpType.LOGIN_USERNAME_PASSWORD.equals(audit.opType()) || AuditOpType.LOGIN_CERTIFICATE.equals(audit.opType()) || AuditOpType.RESETEAZA_PAROLA_DE_CATRE_UTILIZATOR.equals(audit.opType())) {
                    Sesiune sesiuneAnonima = sesiuneRepository.findOne(0L);
                    if (sesiuneAnonima != null) {
                        auditInfo.setSesiune(sesiuneAnonima);
                    }
                }
            }

        }

        return auditInfo;
    }

    private Sesiune createSesiuneAndSave(AuditInfo auditInfo) {
        Utilizator utilizator = null;
        Context context = null;
        if (!StringUtils.isEmpty(auditInfo.getUsername())) {
            utilizator = utilizatorRepository.findByNumeUtilizatorIgnoreCase(auditInfo.getUsername());
        }
        if(!StringUtils.isEmpty(auditInfo.getContext())) {
            context = contextRepository.findByCod(auditInfo.getContext());
        }
        if (utilizator != null && context != null && !StringUtils.isEmpty(auditInfo.getRemoteIp()) && !StringUtils.isEmpty(auditInfo.getToken())) {
            Sesiune sesiune = new Sesiune();
            sesiune.setUtilizator(utilizator);
            sesiune.setContext(context);
            sesiune.setAdresaIp(auditInfo.getRemoteIp());
            sesiune.setDataStart(new Date());
            sesiune.setUidSesiuneHttp(auditInfo.getToken());
            Sesiune savedSesiune = sesiuneRepository.save(sesiune);
            return savedSesiune;
        } else {
            return null;
        }
    }


    private void setArgUserNameforToken(ProceedingJoinPoint joinPoint,Audit audit){

        Signature  signature=  joinPoint.getSignature();

        if(signature !=null){
            if("salveazaToken".equals(signature.getName())){
                AuditInfo auditInfo = getAuditInfo(audit);
                auditInfo.getUsername();
                Object[] args = joinPoint.getArgs();
                if(auditInfo.getUsername()!=null){
                    args[0] =auditInfo.getUsername();
                }
            }
        }
    }

}
