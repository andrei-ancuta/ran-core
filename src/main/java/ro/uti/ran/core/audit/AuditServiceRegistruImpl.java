package ro.uti.ran.core.audit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ro.uti.ran.core.model.registru.Nomenclator;
import ro.uti.ran.core.model.registru.OperatieSesiuneSin;
import ro.uti.ran.core.model.registru.SesiuneSin;
import ro.uti.ran.core.model.registru.TipOperatieSin;
import ro.uti.ran.core.repository.registru.OperatieSesiuneRepositoryRegistru;
import ro.uti.ran.core.repository.registru.SesiuneSinRepository;
import ro.uti.ran.core.repository.registru.TipOperatieRegistruRepository;
import ro.uti.ran.core.ws.utils.AuditInfo;
import ro.uti.ran.core.ws.utils.AuditInfoThreadLocal;

import java.util.Date;

/**
 * Created by Stanciu Neculai on 07.Jan.2016.
 */
@Service("auditServiceRegistru")
public class AuditServiceRegistruImpl implements AuditService {
    public static final Logger log = LoggerFactory.getLogger(AuditServiceRegistruImpl.class);

    @Autowired
    private TipOperatieRegistruRepository tipOperatieRepository;
    @Autowired
    private OperatieSesiuneRepositoryRegistru operatieSesiuneRepo;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SesiuneSinRepository sesiuneSinRepository;

    @Override
    public void audit(Audit auditMeta, Object[] joinPointArgs, Object result,AuditInfo auditInfo)  {
        if (auditInfo != null) {
            SesiuneSin sesiuneaCurenta = getSessionSin(auditInfo);
            TipOperatieSin operatieCurenta = getTipOperatieFrom(auditMeta.opType());
            if (sesiuneaCurenta != null && operatieCurenta != null) {
                OperatieSesiuneSin operatieSesiune = new OperatieSesiuneSin();
                operatieSesiune.setDataOperatie(new Date());
                operatieSesiune.setDescriere(
                        getDescriereOperatieSesiuneBean(auditMeta).getDescriereOpSesiuneFrom(auditMeta, joinPointArgs, result)
                );
                if(joinPointArgs[0].getClass().getSuperclass().isInstance(Nomenclator.class)){
                     operatieSesiune.setDescriereComplet(
                             getDescriereOperatieSesiuneBean(auditMeta).getNomenclatorDescriereOpSesiuneFrom(auditMeta,joinPointArgs,result)
                     );
                }else {
                    operatieSesiune.setDescriereComplet(
                            getDescriereOperatieSesiuneBean(auditMeta).getDescriereCompletaOpSesiuneFrom(auditMeta, joinPointArgs, result)
                    );
                }
                operatieSesiune.setSesiuneSin(sesiuneaCurenta);
                operatieSesiune.setTipOperatieSin(operatieCurenta);
                operatieSesiuneRepo.save(operatieSesiune);
            } else {
                log.error("Sesiune sau tipOperatie din auditInfo este null");
            }
        } else {
            log.error("AuditInfo este null => AuditInfoThreadLocal.get() este null");
        }
    }

    @Override
    public void audit(Audit auditMeta, Object[] joinPointArgs, Throwable t,AuditInfo auditInfo)  {

            audit(auditMeta, joinPointArgs, (Object) t,auditInfo);

    }

    private TipOperatieSin getTipOperatieFrom(AuditOpType auditOpType) {
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

    private SesiuneSin getSessionSin(AuditInfo auditInfo) {
        return sesiuneSinRepository.findByToken(auditInfo.getToken());
    }

}
