package ro.uti.ran.core.exception.base;

public abstract class RanBusinessException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private MessageHolder provider;

    public <E extends MessageProvider> RanBusinessException(E e) {
        super(e == null || e.getMessageHolder() == null ? null : e.getMessageHolder().getMessage());
        if (e == null)
            return;
        provider = e.getMessageHolder();
    }

    public <E extends MessageProvider> RanBusinessException(E e, Object... args) {
        super(e == null || e.getMessageHolder() == null ? null : e.getMessageHolder().format(args));
        if (e == null)
            return;
        provider = e.getMessageHolder();

    }

    public <E extends MessageProvider> RanBusinessException(E e, Throwable cause) {
        super(e == null || e.getMessageHolder() == null ? null : e.getMessageHolder().getMessage(), cause);
        if (e == null)
            return;
        provider = e.getMessageHolder();
    }

    public <E extends MessageProvider> RanBusinessException(E e, Throwable cause, Object... args) {
        super(e == null || e.getMessageHolder() == null ? null : e.getMessageHolder().format(args), cause);
        if (e == null)
            return;
        provider = e.getMessageHolder();

    }

    public RanBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public RanBusinessException(String message) {
        super(message);
    }

    public RanBusinessException(Throwable cause) {
        super(cause);
    }

    public final String getCode() {
        return getBaseCode().getCode() + "-" + (provider == null ? 0 : provider.getSecondaryCode());
    }

    protected abstract RanExceptionBaseCodes getBaseCode();

    public final String getHint() {
        return provider == null ? null : provider.getHint();
    }

    public final String getResourceKey() {
        return provider == null ? null : provider.getResourceKey();
    }

    public final Integer getSecondaryCode() {
        return provider == null ? null : provider.getSecondaryCode();
    }

}
