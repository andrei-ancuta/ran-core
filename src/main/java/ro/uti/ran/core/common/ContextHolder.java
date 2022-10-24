package ro.uti.ran.core.common;

/**
 * 
 * @author mihai.plavichianu
 *
 */
public final class ContextHolder {
    // hold context for operation
    private static ThreadLocal<OperationContext> operationContextHolder = new ThreadLocal<OperationContext>();
    private static ThreadLocal<HeadersContext> headersContextHolder = new ThreadLocal<HeadersContext>();

    // singleton instance
    private static ContextHolder INSTANCE = null;

    private ContextHolder() {
        // initialize the context holder
    }

    public static ContextHolder getInstance() {
        if (null == INSTANCE) {
            synchronized (ContextHolder.class) {
                INSTANCE = new ContextHolder();
            }
        }

        return INSTANCE;
    }


    public void setOperationContext(OperationContext operationContext) {
        operationContextHolder.set(operationContext);
    }

    public OperationContext getOperationContext() {
        return operationContextHolder.get();
    }

    public HeadersContext getHeadersContext() {
        return headersContextHolder.get();
    }

    public void setHeadersContext(HeadersContext headersContext) {
        headersContextHolder.set(headersContext);
    }

    public void disposeOperationContext() {
        operationContextHolder.set(null);
    }

    public void disposeHeadersContext() {
        headersContextHolder.set(null);
    }

    public void disposeAll() {
        disposeOperationContext();
        disposeHeadersContext();
    }
}
