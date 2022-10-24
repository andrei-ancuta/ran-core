package ro.uti.ran.core.audit.descriereOperatii;

import org.springframework.stereotype.Component;
import ro.uti.ran.core.audit.Audit;

import java.text.MessageFormat;

/**
 * Created by Anastasia cea micuta on 1/18/2016.
 */
@Component
public class DescarcaNomenclatorOperatieSesiuneGenericBuilder extends DescriereOperatieSesiuneGenericBuilder {
    @Override
    public String getDescriereOpSesiuneFrom(Audit auditMeta, Object[] args, Throwable t) {
        if (null == t) {
            return MessageFormat.format(env.getProperty(auditMeta.opType().getCodDescriereProperties()), args[0]);
        } else {
            return "exceptie: " + t.getMessage();
        }
    }

    @Override
    public String getDescriereCompletaOpSesiuneFrom(Audit auditMeta, Object[] args, Throwable t) {
        if (null == t) {
            return MessageFormat.format(env.getProperty(auditMeta.opType().getCodDescriereProperties()), args[0]);
        } else {
            return "exceptie: " + t.getMessage();
        }
    }
}
