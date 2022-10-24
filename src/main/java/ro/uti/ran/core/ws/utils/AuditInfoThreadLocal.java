package ro.uti.ran.core.ws.utils;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-12-22 18:06
 */
public class AuditInfoThreadLocal {

    private static final ThreadLocal<AuditInfo> AUDIT_INFO_THREAD_LOCAL = new ThreadLocal<AuditInfo>();

    public static void set(AuditInfo auditInfo){
        AUDIT_INFO_THREAD_LOCAL.set(auditInfo);
    }

    public static AuditInfo get() {
        return AUDIT_INFO_THREAD_LOCAL.get();
    }

    public static void remove(){
        AUDIT_INFO_THREAD_LOCAL.remove();
    }
}
