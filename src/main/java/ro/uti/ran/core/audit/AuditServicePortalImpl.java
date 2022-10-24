package ro.uti.ran.core.audit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.uti.ran.core.model.portal.OperatieSesiune;
import ro.uti.ran.core.model.portal.Sesiune;
import ro.uti.ran.core.model.portal.TipOperatie;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.repository.portal.OperatieSesiuneRepository;
import ro.uti.ran.core.repository.portal.SesiuneRepository;
import ro.uti.ran.core.repository.portal.TipOperatieRepository;
import ro.uti.ran.core.ws.utils.AuditInfo;
import ro.uti.ran.core.ws.utils.AuditInfoThreadLocal;

import java.util.Date;

/**
 * Created by Stanciu Neculai on 04.Jan.2016.
 */
@Service("auditServicePortal")
@Transactional
public class AuditServicePortalImpl implements AuditService {
    public static final Logger log = LoggerFactory.getLogger(AuditServicePortalImpl.class);

    @Autowired
    private TipOperatieRepository tipOperatieRepository;
    @Autowired
    private OperatieSesiuneRepository operatieSesiuneRepo;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SesiuneRepository sesiuneRepo;


    @Override
    public void audit(Audit auditMeta, Object[] joinPointArgs, Object result,AuditInfo auditInfo) {
        if (auditInfo != null) {
            Sesiune sesiuneaCurenta = auditInfo.getSesiune();
            TipOperatie operatieCurenta = getTipOperatieFrom(auditMeta.opType());
            if (sesiuneaCurenta != null && operatieCurenta != null) {
                log.debug("Audit sesiune "+sesiuneaCurenta.getId()+" operatie "+operatieCurenta.getCod());
                OperatieSesiune operatieSesiune = new OperatieSesiune();
                operatieSesiune.setDataOperatie(new Date());
                if(joinPointArgs[0].getClass().getSuperclass()==Nomenclator.class){
                    operatieSesiune.setDescriere(
                            getDescriereOperatieSesiuneBean(auditMeta).getNomenclatorDescriereOpSesiuneFrom(auditMeta,joinPointArgs,result)
                    );
                }else {
                    operatieSesiune.setDescriere(
                            getDescriereOperatieSesiuneBean(auditMeta).getDescriereOpSesiuneFrom(auditMeta, joinPointArgs, result)
                    );
                }
                    operatieSesiune.setDescriereComplet(
                            getDescriereOperatieSesiuneBean(auditMeta).getDescriereCompletaOpSesiuneFrom(auditMeta, joinPointArgs, result)
                    );

                operatieSesiune.setSesiune(sesiuneRepo.findByToken(sesiuneaCurenta.getUidSesiuneHttp()).get(0));
                operatieSesiune.setTipOperatie(operatieCurenta);
                try {
                    operatieSesiuneRepo.save(operatieSesiune);
                }
                catch (Exception ex){
                    log.debug(ex.getMessage());
                }
            } else {
                log.error("Sesiune sau tipOperatie din auditInfo este null");
            }
        } else {
            log.error("AuditInfo este null => AuditInfoThreadLocal.get() este null");
        }
    }

    @Override
    @Deprecated
    public void audit(Audit auditMeta, Object[] joinPointArgs, Throwable t,AuditInfo auditInfo) {
       // audit(auditMeta, joinPointArgs, (Object) t,auditInfo);
    }

    private TipOperatie getTipOperatieFrom(AuditOpType auditOpType) {
        return tipOperatieRepository.findByCod(auditOpType.getCod());
    }

    private DescriereOperatieSesiune getDescriereOperatieSesiuneBean(Audit audit) {
        String beanName = StringUtils.uncapitalize(audit.opType().getSpringBeanClass().getSimpleName());
        DescriereOperatieSesiune descriereOperatieSesiune = (DescriereOperatieSesiune) applicationContext.getBean(beanName);
        return descriereOperatieSesiune;
    }

    private AuditInfo getAuditInfo() {
        return AuditInfoThreadLocal.get();
    }
}
