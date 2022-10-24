package ro.uti.ran.core.audit;

import ro.uti.ran.core.ws.utils.AuditInfo;

/**
 * Created by Stanciu Neculai on 04.Jan.2016.
 */

public interface AuditService {
    void audit(Audit auditMeta, Object[] joinPointArgs, Object result, AuditInfo auditInfo);

    void audit(Audit auditMeta, Object[] joinPointArgs, Throwable t, AuditInfo auditInfo);
}
